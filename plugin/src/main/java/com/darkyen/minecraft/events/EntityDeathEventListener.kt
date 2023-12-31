package com.darkyen.minecraft.events

import com.darkyen.minecraft.DeadSouls
import com.darkyen.minecraft.database.SoulDatabase
import com.darkyen.minecraft.di.RootModule.pluginConfig
import org.bukkit.SoundCategory
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDeathEvent
import ru.astrainteractive.astralibs.event.EventListener
import ru.astrainteractive.klibs.kdi.Provider
import ru.astrainteractive.klibs.kdi.getValue
import java.util.logging.Level

class EntityDeathEventListener(
    private val instance: DeadSouls,
) : EventListener {
    private val soulDatabase: SoulDatabase? by Provider {
        instance.soulDatabase
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    fun onEntityDeath(event: EntityDeathEvent) {
        val entity = event.entity

        if (entity is Player || !instance.animalsWithSouls.contains(entity.type)) {
            return
        }

        val soulItems = event.drops.toTypedArray()
        val soulXp = event.droppedExp

        if (soulXp == 0 && soulItems.isEmpty()) {
            // Soul would be empty
            return
        }

        if (soulDatabase == null) {
            instance.logger.log(Level.WARNING, "onEntityDeath: soulDatabase not loaded yet")
            return
        }

        val soulLocation = entity.location

        val world = entity.world
        soulDatabase?.addSoul(null, world.uid, soulLocation.x, soulLocation.y, soulLocation.z, soulItems, soulXp)
        instance.refreshNearbySoulCache = true

        if (pluginConfig.soundSoulDropped.value.isNotEmpty()) {
            world.playSound(soulLocation, pluginConfig.soundSoulDropped.value, SoundCategory.MASTER, 1.1f, 1.7f)
        }

        event.drops.clear()
        event.droppedExp = 0
    }
}
