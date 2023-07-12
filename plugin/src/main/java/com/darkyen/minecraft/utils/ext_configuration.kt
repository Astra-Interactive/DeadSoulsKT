@file:Suppress("Filename")

package com.darkyen.minecraft.utils

import com.darkyen.minecraft.di.RootModule
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.configuration.file.FileConfiguration
import ru.astrainteractive.astralibs.configuration.DefaultConfiguration
import ru.astrainteractive.astralibs.configuration.api.MutableConfiguration
import ru.astrainteractive.astralibs.getValue
import ru.astrainteractive.astralibs.utils.getFloat

private val plugin by RootModule.plugin
private val logger: java.util.logging.Logger
    get() = RootModule.logger.provide()

private fun <T> configuration(
    default: T? = null,
    load: MutableConfiguration<T?>.() -> T?
) = DefaultConfiguration(
    default = default,
    load = load,
    save = {}
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
    getFloat(path, default)
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
