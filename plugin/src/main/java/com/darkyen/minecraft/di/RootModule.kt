package com.darkyen.minecraft.di

import com.darkyen.minecraft.DeadSouls
import com.darkyen.minecraft.utils.Configuration
import ru.astrainteractive.astralibs.Lateinit
import ru.astrainteractive.astralibs.Module
import ru.astrainteractive.astralibs.Provider
import ru.astrainteractive.astralibs.Reloadable
import ru.astrainteractive.astralibs.filemanager.DefaultSpigotFileManager
import ru.astrainteractive.astralibs.getValue

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
    val pluginConfigModule = Reloadable {
        val fileConfig by configurationModule
        Configuration(fileConfig.fileConfiguration)
    }

    @JvmStatic
    val pluginConfig: Configuration
        get() = pluginConfigModule.value
}
