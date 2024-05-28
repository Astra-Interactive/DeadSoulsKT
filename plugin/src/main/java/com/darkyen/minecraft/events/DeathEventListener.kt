package com.darkyen.minecraft.events

import com.darkyen.minecraft.DeadSouls
import com.darkyen.minecraft.api.DeadSoulsAPIImpl
import com.darkyen.minecraft.di.RootModule.pluginConfig
import com.darkyen.minecraft.utils.PlayerSoulInfo
import com.darkyen.minecraft.utils.PvPBehavior
import com.darkyen.minecraft.utils.SpigotCompat.textComponentSetHoverText
import com.darkyen.minecraft.utils.Util
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.GameRule
import org.bukkit.Location
import org.bukkit.SoundCategory
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.PlayerDeathEvent
import ru.astrainteractive.astralibs.event.EventListener
import ru.astrainteractive.klibs.kdi.Provider
import ru.astrainteractive.klibs.kdi.getValue
import java.util.logging.Level

class DeathEventListener(private val instance: DeadSouls) : EventListener {
    private val soulDatabase by Provider {
        instance.soulDatabase
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    @Suppress("CyclomaticComplexMethod", "LongMethod")
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val player = event.entity
        if (!player.hasPermission("com.darkyen.minecraft.deadsouls.hassoul")) {
            return
        }

        val world = player.world
        if (!instance.enabledWorlds.contains(world.uid)) {
            return
        }

        val isPvp = player.killer != null && player != player.killer
        if (isPvp && instance.pvpBehavior == PvPBehavior.DISABLED) {
            return
        }

        if (soulDatabase == null) {
            instance.logger.log(Level.WARNING, "onPlayerDeath: soulDatabase not loaded yet")
            return
        }

        // Actually clearing the drops is deferred to the end of the method:
        // in case of any bug that causes this method to crash, we don't want to just delete the items
        var clearItemDrops = false
        var clearXPDrops = false

        val soulItems = when {
            event.keepInventory -> {
                // We don't modify drops for this death at all
                DeadSoulsAPIImpl.NO_ITEM_STACKS
            }

            !player.hasPermission("com.darkyen.minecraft.deadsouls.hassoul.items") -> {
                DeadSoulsAPIImpl.NO_ITEM_STACKS
            }

            else -> {
                clearItemDrops = true
                event.drops.toTypedArray()
            }
        }

        var soulXp: Int
        when {
            event.keepLevel -> {
                // We don't modify XP for this death at all
                soulXp = 0
            }

            java.lang.Boolean.TRUE == world.getGameRuleValue(GameRule.KEEP_INVENTORY) -> {
                soulXp = 0
            }

            !player.hasPermission("com.darkyen.minecraft.deadsouls.hassoul.xp") -> {
                soulXp = 0
            }

            else -> {
                val totalExperience = Util.getTotalExperience(player)
                soulXp = when {
                    instance.retainedXPPercent >= 0 -> {
                        Math.round(totalExperience * instance.retainedXPPercent)
                    }

                    else -> {
                        instance.retainedXPPerLevel * player.level
                    }
                }
                soulXp = Util.clamp(soulXp, 0, totalExperience)
                clearXPDrops = true
            }
        }

        if (soulXp == 0 && soulItems.isEmpty()) {
            // Soul would be empty
            return
        }

        var soulLocation: Location? = null
        try {
            if (pluginConfig.smartSoulPlacement.value) {
                var info = instance.watchedPlayers[player]
                if (info == null) {
                    instance.logger.log(Level.WARNING, "Player $player was not watched!")
                    info = PlayerSoulInfo()
                    instance.watchedPlayers[player] = info
                }
                soulLocation = info.findSafeSoulSpawnLocation(player)
                info.lastSafeLocation.world = null // Reset it, so it isn't used twice
            } else {
                soulLocation = PlayerSoulInfo.findFallbackSoulSpawnLocation(player, player.location, false)
            }
        } catch (bugException: Exception) {
            // Should never happen, but just in case!
            instance.logger.log(
                Level.SEVERE,
                "Failed to find soul location, defaulting to player location!",
                bugException
            )
        }
        if (soulLocation == null) {
            soulLocation = player.location
        }
        val owner = when {
            isPvp -> null
            instance.pvpBehavior == PvPBehavior.FREE -> null
            pluginConfig.soulFreeAfterMs.value <= 0 -> null
            else -> player.uniqueId
        }

        val soulId = soulDatabase?.addSoul(
            owner,
            world.uid,
            soulLocation.x,
            soulLocation.y,
            soulLocation.z,
            soulItems,
            soulXp
        )?.id ?: error("Database has been disabled")

        instance.refreshNearbySoulCache = true

        // Show coordinates if the player has poor taste
        if (player.hasPermission("com.darkyen.minecraft.deadsouls.coordinates")) {
            val skull = TextComponent("☠")
            skull.color = ChatColor.BLACK
            @Suppress("ImplicitDefaultLocale")
            val coords = TextComponent(
                String.format(
                    " %d / %d / %d ",
                    Math.round(soulLocation.x),
                    Math.round(soulLocation.y),
                    Math.round(soulLocation.z)
                )
            )
            coords.color = ChatColor.GRAY
            player.spigot().sendMessage(skull, coords, skull)
        }

        // Do not offer to free the soul if it will be free sooner than the player can click the button
        @Suppress("ComplexCondition")
        if (owner != null &&
            pluginConfig.soulFreeAfterMs.value > 1000 &&
            pluginConfig.soulFreeingEnabled &&
            pluginConfig.textFreeMySoul.value.isNotEmpty() &&
            (
                player.hasPermission("com.darkyen.minecraft.deadsouls.souls.free") || player.hasPermission(
                    "com.darkyen.minecraft.deadsouls.souls.free.all"
                )
                )
        ) {
            val star = TextComponent("✦")
            star.color = ChatColor.YELLOW
            val freeMySoul = TextComponent(" " + pluginConfig.textFreeMySoul.value + " ")
            freeMySoul.color = ChatColor.GOLD
            freeMySoul.isBold = true
            freeMySoul.isUnderlined = true
            if (pluginConfig.textFreeMySoulTooltip.value.isNotEmpty()) {
                textComponentSetHoverText(freeMySoul, pluginConfig.textFreeMySoulTooltip.value)
            }
            freeMySoul.clickEvent = ClickEvent(
                ClickEvent.Action.RUN_COMMAND,
                "/souls free $soulId"
            )
            player.spigot().sendMessage(ChatMessageType.CHAT, star, freeMySoul, star)
        }

        if (pluginConfig.soundSoulDropped.value.isNotEmpty()) {
            world.playSound(soulLocation, pluginConfig.soundSoulDropped.value, SoundCategory.MASTER, 1.1f, 1.7f)
        }

        // No need to set setKeepInventory/Level to false, because if we got here, it already is false
        if (clearItemDrops) {
            event.drops.clear()
        }
        if (clearXPDrops) {
            event.newExp = 0
            event.newLevel = 0
            event.newTotalExp = 0
            event.droppedExp = 0
        }
    }
}
