package com.darkyen.minecraft.di

import com.darkyen.minecraft.DeadSouls
import com.darkyen.minecraft.utils.Configuration
import ru.astrainteractive.astralibs.filemanager.DefaultSpigotFileManager
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
    val pluginConfigModule = Reloadable {
        val fileConfig by configurationModule
        Configuration(fileConfig.fileConfiguration)
    }

    @JvmStatic
    val pluginConfig: Configuration
        get() = pluginConfigModule.value
}
