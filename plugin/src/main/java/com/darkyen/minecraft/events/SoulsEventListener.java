package com.darkyen.minecraft.events;

import com.darkyen.minecraft.DeadSouls;
import com.darkyen.minecraft.database.SoulDatabase;
import com.darkyen.minecraft.utils.PlayerSoulInfo;
import com.darkyen.minecraft.utils.PvPBehavior;
import com.darkyen.minecraft.utils.SpigotCompat;
import com.darkyen.minecraft.utils.Util;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.astrainteractive.astralibs.events.EventListener;
import ru.astrainteractive.astralibs.events.EventManager;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import static com.darkyen.minecraft.api.DeadSoulsAPIImpl.NO_ITEM_STACKS;
import static com.darkyen.minecraft.utils.Util.getTotalExperience;

public class SoulsEventListener implements EventListener {
    final DeadSouls instance;

    public SoulsEventListener(DeadSouls instance) {
        this.instance = instance;
    }


    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onWorldLoaded(WorldLoadEvent event) {
        instance.refreshEnabledWorlds();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPlayerDeath(PlayerDeathEvent event) {
        final Player player = event.getEntity();
        if (!player.hasPermission("com.darkyen.minecraft.deadsouls.hassoul")) {
            return;
        }

        final World world = player.getWorld();
        if (!instance.enabledWorlds.contains(world.getUID())) {
            return;
        }

        final boolean pvp = player.getKiller() != null && !player.equals(player.getKiller());
        if (pvp && instance.pvpBehavior == PvPBehavior.DISABLED) {
            return;
        }

        final SoulDatabase soulDatabase = instance.soulDatabase;
        if (soulDatabase == null) {
            instance.getLogger().log(Level.WARNING, "onPlayerDeath: soulDatabase not loaded yet");
            return;
        }

        // Actually clearing the drops is deferred to the end of the method:
        // in case of any bug that causes this method to crash, we don't want to just delete the items
        boolean clearItemDrops = false;
        boolean clearXPDrops = false;

        final ItemStack[] soulItems;
        if (event.getKeepInventory() || !player.hasPermission("com.darkyen.minecraft.deadsouls.hassoul.items")) {
            // We don't modify drops for this death at all
            soulItems = NO_ITEM_STACKS;
        } else {
            final List<ItemStack> drops = event.getDrops();
            soulItems = drops.toArray(NO_ITEM_STACKS);
            clearItemDrops = true;
        }

        int soulXp;
        if (event.getKeepLevel() || !player.hasPermission("com.darkyen.minecraft.deadsouls.hassoul.xp")
                // Required because getKeepLevel is not set when world's KEEP_INVENTORY is set, but it has the same effect
                // See https://hub.spigotmc.org/jira/browse/SPIGOT-2222
                || Boolean.TRUE.equals(world.getGameRuleValue(GameRule.KEEP_INVENTORY))) {
            // We don't modify XP for this death at all
            soulXp = 0;
        } else {
            final int totalExperience = getTotalExperience(player);
            if (instance.retainedXPPercent >= 0) {
                soulXp = Math.round(totalExperience * instance.retainedXPPercent);
            } else {
                soulXp = instance.retainedXPPerLevel * player.getLevel();
            }
            soulXp = Util.clamp(soulXp, 0, totalExperience);
            clearXPDrops = true;
        }

        if (soulXp == 0 && soulItems.length == 0) {
            // Soul would be empty
            return;
        }

        Location soulLocation = null;
        try {
            if (instance.smartSoulPlacement) {
                PlayerSoulInfo info = instance.watchedPlayers.get(player);
                if (info == null) {
                    instance.getLogger().log(Level.WARNING, "Player " + player + " was not watched!");
                    info = new PlayerSoulInfo();
                    instance.watchedPlayers.put(player, info);
                }
                soulLocation = info.findSafeSoulSpawnLocation(player);
                info.lastSafeLocation.setWorld(null); // Reset it, so it isn't used twice
            } else {
                soulLocation = PlayerSoulInfo.findFallbackSoulSpawnLocation(player, player.getLocation(), false);
            }
        } catch (Exception bugException) {
            // Should never happen, but just in case!
            instance.getLogger().log(Level.SEVERE, "Failed to find soul location, defaulting to player location!", bugException);
        }
        if (soulLocation == null) {
            soulLocation = player.getLocation();
        }

        final UUID owner;
        if ((pvp && instance.pvpBehavior == PvPBehavior.FREE) || instance.soulFreeAfterMs <= 0) {
            owner = null;
        } else {
            owner = player.getUniqueId();
        }

        final int soulId = soulDatabase.addSoul(owner, world.getUID(),
                soulLocation.getX(), soulLocation.getY(), soulLocation.getZ(), soulItems, soulXp).getId();
        instance.refreshNearbySoulCache = true;

        // Show coordinates if the player has poor taste
        if (player.hasPermission("com.darkyen.minecraft.deadsouls.coordinates")) {
            final TextComponent skull = new TextComponent("☠");
            skull.setColor(ChatColor.BLACK);
            final TextComponent coords = new TextComponent(String.format(" %d / %d / %d ", Math.round(soulLocation.getX()), Math.round(soulLocation.getY()), Math.round(soulLocation.getZ())));
            coords.setColor(ChatColor.GRAY);
            player.spigot().sendMessage(skull, coords, skull);
        }

        // Do not offer to free the soul if it will be free sooner than the player can click the button
        if (owner != null && instance.soulFreeAfterMs > 1000
                && instance.soulFreeingEnabled && instance.textFreeMySoul != null && !instance.textFreeMySoul.isEmpty()
                && (player.hasPermission("com.darkyen.minecraft.deadsouls.souls.free")
                || player.hasPermission("com.darkyen.minecraft.deadsouls.souls.free.all"))) {
            final TextComponent star = new TextComponent("✦");
            star.setColor(ChatColor.YELLOW);
            final TextComponent freeMySoul = new TextComponent(" "+instance.textFreeMySoul+" ");
            freeMySoul.setColor(ChatColor.GOLD);
            freeMySoul.setBold(true);
            freeMySoul.setUnderlined(true);
            if (instance.textFreeMySoulTooltip != null && !instance.textFreeMySoulTooltip.isEmpty()) {
                SpigotCompat.textComponentSetHoverText(freeMySoul, instance.textFreeMySoulTooltip);
            }
            freeMySoul.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/souls free " + soulId));
            player.spigot().sendMessage(ChatMessageType.CHAT, star, freeMySoul, star);
        }

        if (!instance.soundSoulDropped.isEmpty()) {
            world.playSound(soulLocation, instance.soundSoulDropped, SoundCategory.MASTER, 1.1f, 1.7f);
        }

        // No need to set setKeepInventory/Level to false, because if we got here, it already is false
        if (clearItemDrops) {
            event.getDrops().clear();
        }
        if (clearXPDrops) {
            event.setNewExp(0);
            event.setNewLevel(0);
            event.setNewTotalExp(0);
            event.setDroppedExp(0);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onEntityDeath(EntityDeathEvent event) {
        final LivingEntity entity = event.getEntity();

        if (entity instanceof Player || !instance.animalsWithSouls.contains(entity.getType())) {
            return;
        }

        final ItemStack[] soulItems = event.getDrops().toArray(NO_ITEM_STACKS);
        final int soulXp = event.getDroppedExp();

        if (soulXp == 0 && soulItems.length == 0) {
            // Soul would be empty
            return;
        }

        final SoulDatabase soulDatabase = instance.soulDatabase;
        if (soulDatabase == null) {
            instance.getLogger().log(Level.WARNING, "onEntityDeath: soulDatabase not loaded yet");
            return;
        }

        final Location soulLocation = entity.getLocation();

        final World world = entity.getWorld();
        soulDatabase.addSoul(null, world.getUID(), soulLocation.getX(), soulLocation.getY(), soulLocation.getZ(), soulItems, soulXp);
        instance.refreshNearbySoulCache = true;

        if (!instance.soundSoulDropped.isEmpty()) {
            world.playSound(soulLocation, instance.soundSoulDropped, SoundCategory.MASTER, 1.1f, 1.7f);
        }

        event.getDrops().clear();
        event.setDroppedExp(0);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        instance.watchedPlayers.put(event.getPlayer(), new PlayerSoulInfo());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerLeave(PlayerQuitEvent event) {
        instance.watchedPlayers.remove(event.getPlayer());
    }


    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    @NotNull
    @Override
    public EventListener onEnable(@NotNull EventManager eventManager) {
        instance.getServer().getPluginManager().registerEvents(this, instance);
        eventManager.addHandler(this);
        return this;
    }
}
