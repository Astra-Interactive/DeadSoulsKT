package com.darkyen.minecraft;

import com.darkyen.minecraft.api.DeadSoulsAPI;
import com.darkyen.minecraft.api.DeadSoulsAPIImpl;
import com.darkyen.minecraft.commands.SoulsCommands;
import com.darkyen.minecraft.database.SoulDatabase;
import com.darkyen.minecraft.di.RootModule;
import com.darkyen.minecraft.events.SoulPickupEvent;
import com.darkyen.minecraft.events.SoulsEventListener;
import com.darkyen.minecraft.models.Soul;
import com.darkyen.minecraft.utils.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.util.NumberConversions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import static com.darkyen.minecraft.di.RootModule.getPluginConfig;
import static com.darkyen.minecraft.api.DeadSoulsAPIImpl.NO_ITEM_STACKS;
import static com.darkyen.minecraft.utils.Util.*;
import ru.astrainteractive.astralibs.events.GlobalEventListener;

/**
 *
 */
public final class DeadSouls extends JavaPlugin {
    // Need for MockBukkit testing
    public DeadSouls() {
        super();
        RootModule.getPlugin().initialize(this);
    }

    // Need for MockBukkit testing
    protected DeadSouls(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    @Nullable
    public SoulDatabase soulDatabase;


    public float retainedXPPercent;
    public int retainedXPPerLevel;


    @NotNull
    public PvPBehavior pvpBehavior = PvPBehavior.NORMAL;


    public final Set<EntityType> animalsWithSouls = new HashSet<>();

    private final ArrayList<Pattern> worldPatterns = new ArrayList<>();
    public final HashSet<UUID> enabledWorlds = new HashSet<>();

    @NotNull
    public final HashMap<Player, PlayerSoulInfo> watchedPlayers = new HashMap<>();
    public boolean refreshNearbySoulCache = false;

    public static final double COLLECTION_DISTANCE2 = NumberConversions.square(1);


    @NotNull
    private final Location processPlayers_playerLocation = new Location(null, 0, 0, 0);
    @NotNull
    private final Random processPlayers_random = new Random();
    private long processPlayers_nextFadeCheck = 0;
    private long processPlayers_nextAutoSave = 0;
    public DeadSoulsAPI api;

    private void processPlayers() {
        final SoulDatabase soulDatabase = this.soulDatabase;
        if (soulDatabase == null) {
            getLogger().log(Level.WARNING, "processPlayers: soulDatabase not loaded yet");
            return;
        }

        final PluginManager pluginManager = getServer().getPluginManager();
        final long now = System.currentTimeMillis();

        if (now > processPlayers_nextFadeCheck && getPluginConfig().getSoulFadesAfterMs().getValue() < Long.MAX_VALUE) {
            final int faded = soulDatabase.removeFadedSouls(getPluginConfig().getSoulFadesAfterMs().getValue());
            if (faded > 0) {
                this.refreshNearbySoulCache = true;
                getLogger().log(Level.FINE, "Removed " + faded + " faded soul(s)");
            }
            processPlayers_nextFadeCheck = now + 1000 * 60 * 5;// Check every 5 minutes
        }

        final boolean refreshNearbySoulCache = this.refreshNearbySoulCache;
        this.refreshNearbySoulCache = false;

        final boolean playCallingSounds = !getPluginConfig().getSoundSoulCalling().getValue().isEmpty() && getPluginConfig().getVolumeSoulCalling().getValue() > 0f && this.processPlayers_random.nextInt(12) == 0;

        boolean databaseChanged = false;

        for (Map.Entry<Player, PlayerSoulInfo> entry : watchedPlayers.entrySet()) {
            final Player player = entry.getKey();
            final GameMode playerGameMode = player.getGameMode();
            final World world = player.getWorld();
            final PlayerSoulInfo info = entry.getValue();

            boolean searchNewSouls = refreshNearbySoulCache;

            // Update location
            final Location playerLocation = player.getLocation(processPlayers_playerLocation);
            if (!isNear(playerLocation, info.lastKnownLocation, 16)) {
                set(info.lastKnownLocation, playerLocation);
                searchNewSouls = true;
            }

            if (playerGameMode != GameMode.SPECTATOR) {
                final Block underPlayer =
                        world.getBlockAt(playerLocation.getBlockX(), playerLocation.getBlockY() - 1, playerLocation.getBlockZ());
                if (underPlayer.getType().isSolid()) {
                    final Block atPlayer =
                            world.getBlockAt(playerLocation.getBlockX(), playerLocation.getBlockY(), playerLocation.getBlockZ());
                    if (atPlayer.getType() != Material.LAVA) {
                        // Do not spawn souls in air or in lava
                        set(info.lastSafeLocation, playerLocation);
                    }
                }
            }

            // Update visible souls
            final ArrayList<Soul> visibleSouls = info.visibleSouls;
            if (searchNewSouls) {
                visibleSouls.clear();
                soulDatabase.findSouls(world.getUID(), playerLocation.getBlockX(), playerLocation.getBlockZ(), 100, visibleSouls);
            }

            if (visibleSouls.isEmpty()) {
                continue;
            }

            { // Sort souls
                final ComparatorSoulDistanceTo comparator = new ComparatorSoulDistanceTo(playerLocation.getX(), playerLocation.getY(), playerLocation.getZ());
                visibleSouls.sort(comparator);
            }

            // Send particles
            final int soulCount = visibleSouls.size();
            int remainingSoulsToShow = 16;
            final boolean canSeeAllSouls = playerGameMode == GameMode.SPECTATOR && player.hasPermission("com.darkyen.minecraft.deadsouls.spectatesouls");
            for (int i = 0; i < soulCount && remainingSoulsToShow > 0; i++) {
                final Soul soul = visibleSouls.get(i);
                if (!canSeeAllSouls && !soul.isAccessibleBy(player, now, getPluginConfig().getSoulFreeAfterMs().getValue())) {
                    // Soul of somebody else, do not show nor collect
                    continue;
                }

                final Location soulLocation = soul.getLocation(player.getWorld());
                if (soulLocation == null) {
                    continue;
                }

                // Show this soul!
                if (soul.getExperiencePoints() > 0 && soul.getItems().length > 0) {
                    player.spawnParticle(Particle.REDSTONE, soulLocation, 10, 0.1, 0.1, 0.1, getPluginConfig().getSoulDustOptionsItems().getValue());
                    player.spawnParticle(Particle.REDSTONE, soulLocation, 10, 0.12, 0.12, 0.12, getPluginConfig().getSoulDustOptionsXp().getValue());
                } else if (soul.getExperiencePoints() > 0) {
                    // Only xp
                    player.spawnParticle(Particle.REDSTONE, soulLocation, 20, 0.1, 0.1, 0.1, getPluginConfig().getSoulDustOptionsXp().getValue());
                } else {
                    // Only items
                    player.spawnParticle(Particle.REDSTONE, soulLocation, 20, 0.1, 0.1, 0.1, getPluginConfig().getSoulDustOptionsItems().getValue());
                }
                remainingSoulsToShow--;
            }

            // Process collisions
            if (!player.isDead()) {
                final boolean playerCanCollectByDefault = (playerGameMode == GameMode.SURVIVAL || playerGameMode == GameMode.ADVENTURE);

                //noinspection ForLoopReplaceableByForEach
                for (int soulI = 0; soulI < visibleSouls.size(); soulI++) {
                    final Soul closestSoul = visibleSouls.get(soulI);
                    if (!closestSoul.isAccessibleBy(player, now, getPluginConfig().getSoulFreeAfterMs().getValue())) {
                        // Soul of somebody else, do not collect
                        continue;
                    }

                    final double dst2 = distance2(closestSoul, playerLocation, 0.4);
                    final Location closestSoulLocation = closestSoul.getLocation(player.getWorld());

                    if (dst2 < COLLECTION_DISTANCE2) {
                        SoulPickupEvent soulPickupEvent = new SoulPickupEvent(player, closestSoul);
                        soulPickupEvent.setCancelled(!playerCanCollectByDefault);
                        pluginManager.callEvent(soulPickupEvent);

                        if (!soulPickupEvent.isCancelled()) {
                            // Collect it!
                            if (closestSoul.getExperiencePoints() > 0) {
                                player.giveExp(closestSoul.getExperiencePoints());
                                closestSoul.setExperiencePoints(0);
                                if (!getPluginConfig().getSoundSoulCollectXp().getValue().isEmpty() && closestSoulLocation != null) {
                                    player.playSound(closestSoulLocation, getPluginConfig().getSoundSoulCollectXp().getValue(), 1f, 1f);
                                }
                                databaseChanged = true;
                            }

                            final @NotNull ItemStack[] items = closestSoul.getItems();
                            if (items.length > 0) {
                                final HashMap<Integer, ItemStack> overflow = player.getInventory().addItem(items);
                                if (overflow.isEmpty()) {
                                    closestSoul.setItems(NO_ITEM_STACKS);
                                } else {
                                    closestSoul.setItems(overflow.values().toArray(NO_ITEM_STACKS));
                                }

                                boolean someCollected = false;
                                if (overflow.size() < items.length) {
                                    someCollected = true;
                                    databaseChanged = true;
                                } else {
                                    for (Map.Entry<Integer, ItemStack> overflowEntry : overflow.entrySet()) {
                                        if (!items[overflowEntry.getKey()].equals(overflowEntry.getValue())) {
                                            someCollected = true;
                                            databaseChanged = true;
                                            break;
                                        }
                                    }
                                }

                                if (someCollected && !getPluginConfig().getSoundSoulCollectItem().getValue().isEmpty() && closestSoulLocation != null) {
                                    player.playSound(closestSoulLocation, getPluginConfig().getSoundSoulCollectItem().getValue(), 1f, 0.5f);
                                }
                            }

                            if (closestSoul.getExperiencePoints() <= 0 && closestSoul.getItems().length <= 0) {
                                // Soul is depleted
                                soulDatabase.removeSoul(closestSoul);
                                this.refreshNearbySoulCache = true;

                                // Do some fancy effect
                                if (closestSoulLocation != null) {

                                    if (!getPluginConfig().getSoundSoulDepleted().getValue().isEmpty()) {
                                        player.playSound(closestSoulLocation, getPluginConfig().getSoundSoulDepleted().getValue(), 0.1f, 0.5f);
                                    }
                                    player.spawnParticle(Particle.REDSTONE, closestSoulLocation, 20, 0.2, 0.2, 0.2, getPluginConfig().getSoulDustOptionsGone().getValue());
                                }
                            }
                        }
                    } else if (playCallingSounds && closestSoulLocation != null) {
                        player.playSound(closestSoulLocation, getPluginConfig().getSoundSoulCalling().getValue(), getPluginConfig().getVolumeSoulCalling().getValue(), 0.75f);
                    }
                    break;
                }
            }
        }

        if (databaseChanged) {
            soulDatabase.markDirty();
        }

        final long autoSaveMs = getPluginConfig().getAutoSaveMs().getValue();
        if (now > processPlayers_nextAutoSave) {
            processPlayers_nextAutoSave = now + autoSaveMs;
            soulDatabase.autoSave();
        }
    }

    @Override
    public void onEnable() {
        final FileConfiguration config = RootModule.getConfigurationModule().getValue().getFileConfiguration();
        final Logger LOG = getLogger();

        {
            this.retainedXPPercent = 90;
            this.retainedXPPerLevel = 0;
            final String retainedXp = config.getString("retained-xp");
            if (retainedXp != null) {
                String sanitizedRetainedXp = retainedXp.replaceAll("\\s", "");
                boolean percent = false;
                if (sanitizedRetainedXp.endsWith("%")) {
                    percent = true;
                    sanitizedRetainedXp = sanitizedRetainedXp.substring(0, sanitizedRetainedXp.length() - 1);
                }
                try {
                    final int number = Integer.parseInt(sanitizedRetainedXp);
                    if (percent) {
                        if (number < 0 || number > 100) {
                            LOG.log(Level.WARNING, "Invalid configuration: retained-xp percent must be between 0 and 1");
                        } else {
                            retainedXPPercent = number / 100f;
                            retainedXPPerLevel = 0;
                        }
                    } else {
                        if (number < 0) {
                            LOG.log(Level.WARNING, "Invalid configuration: retained-xp per level must be positive");
                        } else {
                            retainedXPPercent = -1f;
                            retainedXPPerLevel = number;
                        }
                    }
                } catch (NumberFormatException nfe) {
                    LOG.log(Level.WARNING, "Invalid configuration: retained-xp has invalid format");
                }
            }
        }

        {
            String pvpBehaviorString = config.getString("pvp-behavior");
            if (pvpBehaviorString == null) {
                pvpBehavior = PvPBehavior.NORMAL;
            } else {
                try {
                    pvpBehavior = PvPBehavior.valueOf(pvpBehaviorString.trim().toUpperCase());
                } catch (IllegalArgumentException ex) {
                    pvpBehavior = PvPBehavior.NORMAL;
                    final StringBuilder sb = new StringBuilder(128);
                    sb.append("Unrecognized pvp-behavior: '").append(pvpBehaviorString).append("'. ");
                    sb.append("Allowed values are: ");
                    for (PvPBehavior value : PvPBehavior.values()) {
                        sb.append(value.name().toLowerCase()).append(", ");
                    }
                    sb.setLength(sb.length() - 2);
                    LOG.log(Level.WARNING, sb.toString());
                }
            }
        }


        animalsWithSouls.clear();
        for (String animalName : config.getStringList("animals-with-souls")) {
            final EntityType entityType;
            try {
                entityType = EntityType.valueOf(animalName);
            } catch (IllegalArgumentException e) {
                LOG.log(Level.WARNING, "Ignoring animal type for soul \"" + animalName + "\", no such entity name");
                continue;
            }
            animalsWithSouls.add(entityType);
        }

        worldPatterns.clear();
        for (String worlds : config.getStringList("worlds")) {
            worldPatterns.add(Util.compileSimpleGlob(worlds));
        }
        if (worldPatterns.isEmpty()) {
            LOG.warning("No world patterns specified, souls will not be created anywhere.");
        }

        refreshEnabledWorlds();

        saveDefaultConfig();

        final Server server = getServer();
        new SoulsEventListener(this).onEnable(this);

        // Run included tests
        for (String testClassName : new String[]{"com.darkyen.minecraft.ItemStoreTest"}) {
            try {
                final Class<?> testClass;
                try {
                    testClass = Class.forName(testClassName);
                } catch (ClassNotFoundException e) {
                    continue;
                }
                LOG.info("Found test class: " + testClassName);

                final Method runLiveTest = testClass.getMethod("runLiveTest", Plugin.class);
                runLiveTest.invoke(null, this);

                LOG.info("Test successful");
            } catch (Exception e) {
                LOG.log(Level.INFO, "Failed to run tests of " + testClassName, e);
            }
        }

        {
            final Path dataFolder = getDataFolder().toPath();
            final Path soulDb = dataFolder.resolve("soul-db.bin");
            soulDatabase = new SoulDatabase(this, soulDb);
            api = new DeadSoulsAPIImpl(soulDatabase, getPluginConfig().getSoulFreeAfterMs().getValue(), refreshNearbySoulCache);
            final Path legacySoulDb = dataFolder.resolve("souldb.bin");
            if (Files.exists(legacySoulDb)) {
                try {
                    soulDatabase.loadLegacy(legacySoulDb);
                } catch (Exception e) {
                    LOG.log(Level.SEVERE, "Failed to load legacy soul database, old souls will not be present", e);
                }
            }
        }

        refreshNearbySoulCache = true;

        for (Player onlinePlayer : server.getOnlinePlayers()) {
            watchedPlayers.put(onlinePlayer, new PlayerSoulInfo());
        }

        server.getScheduler().runTaskTimer(this, this::processPlayers, 20, 20);

        Objects.requireNonNull(getCommand("souls")).setExecutor(new SoulsCommands(this, soulDatabase));
    }

    public void refreshEnabledWorlds() {
        final HashSet<UUID> worlds = this.enabledWorlds;
        worlds.clear();

        for (World world : getServer().getWorlds()) {
            final String name = world.getName();
            final UUID uuid = world.getUID();
            final String uuidString = uuid.toString();
            for (Pattern pattern : this.worldPatterns) {
                if (pattern.matcher(name).matches() || pattern.matcher(uuidString).matches()) {
                    worlds.add(uuid);
                }
            }
        }

        if (!worldPatterns.isEmpty() && worlds.isEmpty()) {
            getLogger().warning("No worlds match, souls will not be created in any world.");
        }
    }

    @Override
    public void onDisable() {
        final SoulDatabase soulDatabase = this.soulDatabase;
        if (soulDatabase != null) {
            try {
                final int faded = soulDatabase.removeFadedSouls(getPluginConfig().getSoulFadesAfterMs().getValue());
                if (faded > 0) {
                    getLogger().log(Level.FINE, "Removed " + faded + " faded soul(s)");
                }
                soulDatabase.save();
            } catch (Exception e) {
                getLogger().log(Level.SEVERE, "Failed to save soul database", e);
            }
            this.soulDatabase = null;
        }
        GlobalEventListener.INSTANCE.onDisable();
        watchedPlayers.clear();
    }
}
