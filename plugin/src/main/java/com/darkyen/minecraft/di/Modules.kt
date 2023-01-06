package com.darkyen.minecraft.di

import com.darkyen.minecraft.utils.Configuration
import ru.astrainteractive.astralibs.di.getValue
import ru.astrainteractive.astralibs.di.module
import ru.astrainteractive.astralibs.file_manager.FileManager

object Modules {
    @JvmStatic
    val configurationModule = module {
        FileManager("config.yml")
    }
    @JvmStatic
    val pluginConfigModule = module {
        val fileConfig by configurationModule
        Configuration(fileConfig.fileConfiguration)
    }
    @JvmStatic
    val pluginConfig:Configuration
        get() = pluginConfigModule.value
}