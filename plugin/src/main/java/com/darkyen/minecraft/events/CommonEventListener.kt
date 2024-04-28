package com.darkyen.minecraft.events

import com.darkyen.minecraft.DeadSouls
import com.darkyen.minecraft.utils.PlayerSoulInfo
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.world.WorldLoadEvent
import ru.astrainteractive.astralibs.event.EventListener

class CommonEventListener(private val instance: DeadSouls) : EventListener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        instance.watchedPlayers[event.player] = PlayerSoulInfo()
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onPlayerLeave(event: PlayerQuitEvent) {
        instance.watchedPlayers.remove(event.player)
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    @Suppress("UnusedPrivateMember")
    fun onWorldLoaded(event: WorldLoadEvent?) {
        instance.refreshEnabledWorlds()
    }
}
