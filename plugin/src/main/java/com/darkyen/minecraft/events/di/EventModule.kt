package com.darkyen.minecraft.events.di

import com.darkyen.minecraft.DeadSouls
import com.darkyen.minecraft.events.CommonEventListener
import com.darkyen.minecraft.events.DeathEventListener
import com.darkyen.minecraft.events.EntityDeathEventListener
import ru.astrainteractive.astralibs.lifecycle.Lifecycle

interface EventModule {
    val lifecycle: Lifecycle

    class Default(instance: DeadSouls) : EventModule {
        private val eventListeners by lazy {
            listOf(
                CommonEventListener(instance),
                DeathEventListener(instance),
                EntityDeathEventListener(instance)
            )
        }
        override val lifecycle: Lifecycle by lazy {
            Lifecycle.Lambda(
                onEnable = {
                    eventListeners.forEach { it.onEnable(instance) }
                },
                onDisable = {
                    eventListeners.forEach { it.onDisable() }
                }
            )
        }
    }
}
