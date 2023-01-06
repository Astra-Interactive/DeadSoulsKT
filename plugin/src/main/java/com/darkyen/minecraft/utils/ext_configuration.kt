package com.darkyen.minecraft.utils

import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.configuration.file.FileConfiguration
import ru.astrainteractive.astralibs.AstraLibs
import ru.astrainteractive.astralibs.configuration.configuration
import ru.astrainteractive.astralibs.utils.getFloat

fun FileConfiguration.cDustOptions(path: String, defaultColor: Color, defaultDustSize: Float) = configuration(path) {
    Particle.DustOptions(Util.parseColor(getString(path), defaultColor, AstraLibs.instance.logger), defaultDustSize)
}

fun FileConfiguration.cString(path: String, default: String) = configuration(path) {
    this.getString(path, default) ?: default
}

fun FileConfiguration.cBoolean(path: String, default: Boolean) = configuration(path) {
    this.getBoolean(path, default)
}

fun FileConfiguration.dDustOptions(path: String) = configuration(path) {
    this.getString(path)
}

fun FileConfiguration.cDouble(path: String, default: Double) = configuration(path) {
    this.getDouble(path, default)
}
fun FileConfiguration.cFloat(path: String, default: Float) = configuration(path) {
    this.getFloat(path, default)
}

fun FileConfiguration.cNormalizedKey(path: String, default: String) = configuration(path) {
    Util.normalizeKey(this.getString(path, default))
}

fun FileConfiguration.cTimeMs(
    path: String,
    defaultMs: Long,
) = configuration(path) {
    Util.parseTimeMs(this.getString(path), defaultMs, AstraLibs.instance.logger)
}