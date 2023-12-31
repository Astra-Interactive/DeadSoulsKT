package com.darkyen.minecraft.di

import com.darkyen.minecraft.DeadSouls
import com.darkyen.minecraft.events.di.EventModule
import com.darkyen.minecraft.utils.Configuration
import ru.astrainteractive.astralibs.filemanager.DefaultSpigotFileManager
import ru.astrainteractive.astralibs.lifecycle.Lifecycle
import ru.astrainteractive.klibs.kdi.Lateinit
import ru.astrainteractive.klibs.kdi.Module
import ru.astrainteractive.klibs.kdi.Provider
import ru.astrainteractive.klibs.kdi.Reloadable
import ru.astrainteractive.klibs.kdi.getValue

object RootModule : Module {
    @JvmStatic
    val plugin = Lateinit<DeadSouls>()

    val logger = Provider {
        plugin.value.logger
    }

    @JvmStatic
    val configurationModule = Reloadable {
        val plugin by plugin
        DefaultSpigotFileManager(plugin, "config.yml")
    }

    @JvmStatic
    val pluginConfig: Configuration by Reloadable {
        val fileConfig by configurationModule
        Configuration(fileConfig.fileConfiguration)
    }

    private val eventModule by lazy {
        EventModule.Default(plugin.value)
    }
    private val lifecycles: List<Lifecycle> by Provider {
        listOf(
            eventModule.lifecycle
        )
    }

    @JvmStatic
    val lifecycle: Lifecycle by lazy {
        Lifecycle.Lambda(
            onEnable = {
                lifecycles.forEach(Lifecycle::onEnable)
            },
            onDisable = {
                lifecycles.forEach(Lifecycle::onDisable)
            },
            onReload = {
                lifecycles.forEach(Lifecycle::onReload)
            }
        )
    }
}
