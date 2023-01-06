package com.darkyen.minecraft.utils;

import com.darkyen.minecraft.models.Soul;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.darkyen.minecraft.utils.Util.isNear;
import static com.darkyen.minecraft.utils.Util.set;

public final class PlayerSoulInfo {
    static final double SOUL_HOVER_OFFSET = 1.2;

    @NotNull
    public final Location lastKnownLocation = new Location(null, 0, 0, 0);

    @NotNull
    public final Location lastSafeLocation = new Location(null, 0, 0, 0);

    public final ArrayList<Soul> visibleSouls = new ArrayList<>();

    @NotNull
    public Location findSafeSoulSpawnLocation(@NotNull Player player) {
        final Location playerLocation = player.getLocation();
        if (isNear(lastSafeLocation, playerLocation, 20)) {
            set(playerLocation, lastSafeLocation);
            playerLocation.setY(playerLocation.getY() + SOUL_HOVER_OFFSET);
            return playerLocation;
        }

        // Too far, now we have to find a better location
        return findFallbackSoulSpawnLocation(player, playerLocation, true);
    }

    @NotNull
    public static Location findFallbackSoulSpawnLocation(@NotNull Player player, @NotNull Location playerLocation, boolean improve) {
        final World world = player.getWorld();

        final int x = playerLocation.getBlockX();
        int y = Util.clamp(playerLocation.getBlockY(), SpigotCompat.worldGetMinHeight(world), world.getMaxHeight());
        final int z = playerLocation.getBlockZ();

        if (improve) {
            int yOff = 0;
            while (true) {
                final Material type = world.getBlockAt(x, y + yOff, z).getType();
                if (type.isSolid()) {
                    // Soul either started in a block or ended in it, do not want
                    yOff = 0;
                    break;
                } else if (type == Material.LAVA) {
                    yOff++;

                    if (yOff > 8) {
                        // Probably dead in a lava column, we don't want to push it up
                        yOff = 0;
                        break;
                    }
                    // continue
                } else {
                    // This place looks good
                    break;
                }
            }

            y += yOff;
        }

        playerLocation.setY(y + SOUL_HOVER_OFFSET);
        return playerLocation;
    }
}