package com.darkyen.minecraft.utils

import com.darkyen.minecraft.models.Soul
import com.darkyen.minecraft.utils.SpigotCompat.worldGetMinHeight
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player

class PlayerSoulInfo {
    @JvmField
    val lastKnownLocation: Location = Location(null, 0.0, 0.0, 0.0)

    @JvmField
    val lastSafeLocation: Location = Location(null, 0.0, 0.0, 0.0)

    @JvmField
    val visibleSouls: ArrayList<Soul> = ArrayList()

    fun findSafeSoulSpawnLocation(player: Player): Location {
        val playerLocation = player.location
        if (Util.isNear(lastSafeLocation, playerLocation, 20f)) {
            Util.set(playerLocation, lastSafeLocation)
            playerLocation.y += SOUL_HOVER_OFFSET
            return playerLocation
        }

        // Too far, now we have to find a better location
        return findFallbackSoulSpawnLocation(player, playerLocation, true)
    }

    companion object {
        const val SOUL_HOVER_OFFSET: Double = 1.2

        @Suppress("LoopWithTooManyJumpStatements", "NestedBlockDepth")
        fun findFallbackSoulSpawnLocation(player: Player, playerLocation: Location, improve: Boolean): Location {
            val world = player.world

            val x = playerLocation.blockX
            var y = Util.clamp(playerLocation.blockY, worldGetMinHeight(world), world.maxHeight)
            val z = playerLocation.blockZ

            if (improve) {
                var yOff = 0
                while (true) {
                    val type = world.getBlockAt(x, y + yOff, z).type
                    if (type.isSolid) {
                        // Soul either started in a block or ended in it, do not want
                        yOff = 0
                        break
                    } else if (type == Material.LAVA) {
                        yOff++

                        if (yOff > 8) {
                            // Probably dead in a lava column, we don't want to push it up
                            yOff = 0
                            break
                        }
                        // continue
                    } else {
                        // This place looks good
                        break
                    }
                }

                y += yOff
            }

            playerLocation.y = y + SOUL_HOVER_OFFSET
            return playerLocation
        }
    }
}
