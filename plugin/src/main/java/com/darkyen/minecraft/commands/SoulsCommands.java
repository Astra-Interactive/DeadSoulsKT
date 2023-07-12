package com.darkyen.minecraft.commands;

import com.darkyen.minecraft.DeadSouls;
import com.darkyen.minecraft.database.SoulDatabase;
import com.darkyen.minecraft.models.Soul;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import static com.darkyen.minecraft.di.RootModule.getPluginConfig;
import static com.darkyen.minecraft.utils.Util.distance2;

public class SoulsCommands implements CommandExecutor {
    final DeadSouls instance;
    final SoulDatabase soulDatabase;

    public SoulsCommands(DeadSouls instance, SoulDatabase soulDatabase) {
        this.instance = instance;
        this.soulDatabase = soulDatabase;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        final SoulDatabase soulDatabase = this.soulDatabase;
        if (soulDatabase == null) {
            instance.getLogger().log(Level.WARNING, "processPlayers: soulDatabase not loaded yet");
            return false;
        }

        if (!"souls".equalsIgnoreCase(command.getName())) {
            return false;
        }

        final String word = args.length >= 1 ? args[0] : "";
        int number;
        try {
            number = args.length >= 2 ? Integer.parseInt(args[1]) : -1;
        } catch (NumberFormatException nfe) {
            number = -1;
        }

        if ("free".equalsIgnoreCase(word)) {
            if (!getPluginConfig().getSoulFreeingEnabled()) {
                sender.sendMessage(org.bukkit.ChatColor.RED+"This world does not understand the concept of freeing");
                return true;
            }

            soulDatabase.freeSoul(sender, number, getPluginConfig().getSoulFreeAfterMs().getValue(),
                    sender.hasPermission("com.darkyen.minecraft.deadsouls.souls.free"),
                    sender.hasPermission("com.darkyen.minecraft.deadsouls.souls.free.all"));
            return true;
        }

        if ("goto".equalsIgnoreCase(word)) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(org.bukkit.ChatColor.RED+"This sub-command is only accessible in-game");
                return true;
            }

            final Soul soul = soulDatabase.getSoulById(number);
            if (soul == null) {
                sender.sendMessage(org.bukkit.ChatColor.RED+"This soul does not exist");
                return true;
            }

            if (!sender.hasPermission("com.darkyen.minecraft.deadsouls.souls.goto.all")) {
                if (soul.isOwnedBy(sender)) {
                    if (!sender.hasPermission("com.darkyen.minecraft.deadsouls.souls.goto")) {
                        sender.sendMessage(org.bukkit.ChatColor.RED+"You are not allowed to do that");
                        return true;
                    }
                } else {
                    sender.sendMessage(org.bukkit.ChatColor.RED+"You are not allowed to do that");
                    return true;
                }
            }

            final World world = instance.getServer().getWorld(soul.getWorld());
            if (world == null) {
                sender.sendMessage(org.bukkit.ChatColor.RED+"The soul is not in any world");
                return true;
            }

            ((Player)sender).teleport(new Location(world, soul.getLocationX(), soul.getLocationY(), soul.getLocationZ()), PlayerTeleportEvent.TeleportCause.COMMAND);
            sender.sendMessage(org.bukkit.ChatColor.AQUA+"Teleported");
            return true;
        }

        if ("reload".equalsIgnoreCase(word) && sender.isOp()) {
            sender.sendMessage(org.bukkit.ChatColor.RED+"----------------------------");
            sender.sendMessage(org.bukkit.ChatColor.RED+"Reloading plugin Dead Souls");
            sender.sendMessage(org.bukkit.ChatColor.RED+"RELOAD FUNCTIONALITY IS ONLY FOR TESTING AND EXPERIMENTING AND SHOULD NEVER BE USED ON A LIVE SERVER!!!");
            sender.sendMessage(org.bukkit.ChatColor.RED+"If you encounter any problems with the plugin after the reload, restart the server!");
            sender.sendMessage(org.bukkit.ChatColor.RED+"----------------------------");

            final Server server = instance.getServer();
            server.getPluginManager().disablePlugin(instance);
            instance.reloadConfig();
            server.getPluginManager().enablePlugin(instance);

            sender.sendMessage(org.bukkit.ChatColor.RED+" - Reload done - ");
            return true;
        }

        boolean listOwnSouls = sender.hasPermission("com.darkyen.minecraft.deadsouls.souls");
        boolean listAllSouls = sender.hasPermission("com.darkyen.minecraft.deadsouls.souls.all");

        if (!listOwnSouls && !listAllSouls) {
            return false;
        }

        if (word.isEmpty()) {
            if (number < 0) {
                number = 0;
            }
        } else if (!"page".equalsIgnoreCase(word)) {
            return false;
        }

        final UUID senderUUID = (sender instanceof OfflinePlayer) ? ((OfflinePlayer) sender).getUniqueId() : null;

        if (!(sender instanceof Player)) {
            // Console output
            final List<@Nullable Soul> soulsById = soulDatabase.getSoulsById();
            int shownSouls = 0;
            synchronized (soulsById) {
                for (int id = 0; id < soulsById.size(); id++) {
                    final Soul soul = soulsById.get(id);
                    if (soul == null) {
                        continue;
                    }
                    shownSouls++;

                    final World world = instance.getServer().getWorld(soul.getWorld());
                    final String worldStr = world == null ? soul.getWorld().toString() : world.getName();

                    final String ownerStr;
                    if (soul.getOwner() == null) {
                        ownerStr = "<free>";
                    } else {
                        final OfflinePlayer ownerPlayer = instance.getServer().getOfflinePlayer(soul.getOwner());
                        final String ownerPlayerName = ownerPlayer.getName();
                        if (ownerPlayerName == null) {
                            ownerStr = soul.getOwner().toString();
                        } else {
                            ownerStr = ownerPlayerName;
                        }
                    }

                    sender.sendMessage(String.format("%d) %s %.1f %.1f %.1f   %s", id, worldStr, soul.getLocationX(), soul.getLocationY(), soul.getLocationZ(), ownerStr));
                }
            }
            sender.sendMessage(shownSouls+" souls");
        } else {
            // Normal player output
            final List<@NotNull Soul> souls = soulDatabase.getSoulsByOwnerAndWorld(listAllSouls ? null : senderUUID, ((Player) sender).getWorld().getUID());
            final Location location = ((Player) sender).getLocation();
            souls.sort(Comparator.comparingLong(soulAndId -> -soulAndId.getCreationTimestamp()));

            final boolean canFree = sender.hasPermission("com.darkyen.minecraft.deadsouls.souls.free");
            final boolean canFreeAll = sender.hasPermission("com.darkyen.minecraft.deadsouls.souls.free.all");
            final boolean canGoto = sender.hasPermission("com.darkyen.minecraft.deadsouls.souls.goto");
            final boolean canGotoAll = sender.hasPermission("com.darkyen.minecraft.deadsouls.souls.goto.all");

            final int soulsPerPage = 6;
            final long now = System.currentTimeMillis();
            for (int i = Math.max(soulsPerPage * number, 0), end = Math.min(i + soulsPerPage, souls.size()); i < end; i++) {
                final Soul soul = souls.get(i);
                final float distance = (float) Math.sqrt(distance2(soul, location, 1));

                final TextComponent baseText = new TextComponent((i+1)+" ");
                baseText.setColor(ChatColor.AQUA);
                baseText.setBold(true);

                long minutesOld = TimeUnit.MILLISECONDS.toMinutes(now - soul.getCreationTimestamp());
                if (minutesOld >= 0) {
                    String age;
                    if (minutesOld <= 1) {
                        age = " Fresh";
                    } else if (minutesOld < 60 * 2) {
                        age = " " + minutesOld + " minutes old";
                    } else if (minutesOld < 24 * 60) {
                        age = " " + (minutesOld / 60) + " hours old";
                    } else if (minutesOld < 24 * 60 * 100) {
                        age = " " + (minutesOld / (24 * 60)) + " days old";
                    } else {
                        age = " Ancient";
                    }

                    final TextComponent ageText = new TextComponent(age);
                    ageText.setColor(ChatColor.WHITE);
                    ageText.setItalic(true);
                    baseText.addExtra(ageText);
                }

                if (sender.hasPermission("com.darkyen.minecraft.deadsouls.coordinates")) {
                    final TextComponent coords = new TextComponent(String.format(" %d / %d / %d", Math.round(soul.getLocationX()), Math.round(soul.getLocationY()), Math.round(soul.getLocationZ())));
                    coords.setColor(ChatColor.GRAY);
                    baseText.addExtra(coords);
                }

                if (sender.hasPermission("com.darkyen.minecraft.deadsouls.distance")) {
                    final TextComponent dist = new TextComponent(String.format(" %.1f m", distance));
                    dist.setColor(ChatColor.AQUA);
                    baseText.addExtra(dist);
                }


                final boolean ownSoul = soul.isOwnedBy(sender);

                if (soul.getOwner() != null && (canFreeAll || (ownSoul && canFree))) {
                    final TextComponent freeButton = new TextComponent("Free");
                    freeButton.setColor(ChatColor.GREEN);
                    freeButton.setBold(true);
                    freeButton.setUnderlined(true);
                    freeButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/souls free "+ soul.getId()));
                    baseText.addExtra("  ");
                    baseText.addExtra(freeButton);
                }

                if (canGotoAll || (ownSoul && canGoto)) {
                    final TextComponent gotoButton = new TextComponent("Teleport");
                    gotoButton.setColor(ChatColor.GOLD);
                    gotoButton.setBold(true);
                    gotoButton.setUnderlined(true);
                    gotoButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/souls goto "+ soul.getId()));
                    baseText.addExtra("  ");
                    baseText.addExtra(gotoButton);
                }

                sender.spigot().sendMessage(baseText);
            }

            final boolean leftArrow = number > 0;
            final int pages = (souls.size() + soulsPerPage - 1) / soulsPerPage;
            final boolean rightArrow = number + 1 < pages;

            final TextComponent arrows = new TextComponent();
            final TextComponent left = new TextComponent(leftArrow ? "<<" : "  ");
            left.setColor(ChatColor.GRAY);
            if (leftArrow) {
                left.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/souls page " + (number - 1)));
            }
            arrows.addExtra(left);

            arrows.addExtra(" page "+(number + 1)+"/"+pages+" ");

            final TextComponent right = new TextComponent(rightArrow ? ">>" : "  ");
            right.setColor(ChatColor.GRAY);
            if (rightArrow) {
                right.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/souls page "+(number + 1)));
            }
            arrows.addExtra(right);
            arrows.addExtra(" ("+souls.size()+" souls total)");

            sender.spigot().sendMessage(arrows);
        }

        return true;
    }
}
