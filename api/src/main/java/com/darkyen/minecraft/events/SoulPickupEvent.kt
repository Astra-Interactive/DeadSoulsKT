package com.darkyen.minecraft.events

import com.darkyen.minecraft.models.ISoul
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class SoulPickupEvent(
    val player: Player,
    val soul: ISoul
) : Event(), Cancellable {
    private var isCancelled = false

    override fun getHandlers(): HandlerList {
        return HANDLERS
    }

    override fun isCancelled(): Boolean {
        return isCancelled
    }
    override fun setCancelled(cancel: Boolean) {
        isCancelled = cancel
    }

    companion object {
        val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = HANDLERS
    }
}
