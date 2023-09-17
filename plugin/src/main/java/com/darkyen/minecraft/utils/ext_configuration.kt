@file:Suppress("Filename")

package com.darkyen.minecraft.utils

import com.darkyen.minecraft.di.RootModule
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.configuration.file.FileConfiguration
import ru.astrainteractive.klibs.kdi.getValue
import ru.astrainteractive.klibs.kstorage.MutableStorageValue
private val plugin by RootModule.plugin
private val logger: java.util.logging.Logger
    get() = RootModule.logger.provide()

private fun <T> configuration(
    default: T? = null,
    load: () -> T?
) = MutableStorageValue(
    default = default,
    loadSettingsValue = load,
    saveSettingsValue = {}
)

fun FileConfiguration.cDustOptions(path: String, defaultColor: Color, defaultDustSize: Float) = configuration {
    Particle.DustOptions(Util.parseColor(getString(path), defaultColor, logger), defaultDustSize)
}

fun FileConfiguration.cString(path: String, default: String) = configuration {
    getString(path, default) ?: default
}

fun FileConfiguration.cBoolean(path: String, default: Boolean) = configuration {
    getBoolean(path, default)
}

fun FileConfiguration.dDustOptions(path: String) = configuration {
    getString(path)
}

fun FileConfiguration.cDouble(path: String, default: Double) = configuration {
    getDouble(path, default)
}

fun FileConfiguration.cFloat(path: String, default: Float) = configuration {
    getDouble(path, default.toDouble()).toFloat()
}

fun FileConfiguration.cNormalizedKey(path: String, default: String) = configuration {
    Util.normalizeKey(getString(path, default))
}

fun FileConfiguration.cTimeMs(
    path: String,
    defaultMs: Long,
) = configuration {
    Util.parseTimeMs(getString(path), defaultMs, logger)
}
