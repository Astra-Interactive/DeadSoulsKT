@file:Suppress("Filename")

package com.darkyen.minecraft.utils

import com.darkyen.minecraft.di.RootModule
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.configuration.file.FileConfiguration
import ru.astrainteractive.astralibs.configuration.BukkitMutableStorageValue.anyMutableStorageValue
import ru.astrainteractive.klibs.kstorage.MutableStorageValue
import ru.astrainteractive.klibs.kstorage.api.MutableStorageValue
import ru.astrainteractive.klibs.kstorage.withDefault

object ExtConfiguration {
    private val logger: java.util.logging.Logger
        get() = RootModule.logger.provide()

    fun FileConfiguration.cDustOptions(
        path: String,
        defaultColor: Color,
        defaultDustSize: Float
    ): MutableStorageValue<Particle.DustOptions> = anyMutableStorageValue<String>(path)
        .transform { Particle.DustOptions(Util.parseColor(getString(path), defaultColor, logger), defaultDustSize) }
        .withDefault(Particle.DustOptions(defaultColor, defaultDustSize))

    fun FileConfiguration.cNormalizedKey(
        path: String,
        default: String
    ): MutableStorageValue<String> = anyMutableStorageValue<String>(path)
        .transform { Util.normalizeKey(getString(path, default)) }
        .withDefault(default)

    inline fun <T, K> MutableStorageValue<T>.map(
        crossinline mapTo: (T?) -> K?,
        crossinline mapFrom: (K?) -> T?
    ) = MutableStorageValue(
        default = null,
        loadSettingsValue = { mapTo.invoke(load()) },
        saveSettingsValue = { mapFrom.invoke(it) }
    )

    inline fun <T, K> MutableStorageValue<T>.transform(
        crossinline mapTo: (T?) -> K?,
    ) = MutableStorageValue(
        default = null,
        loadSettingsValue = { mapTo.invoke(load()) },
        saveSettingsValue = { error("Save is not implemented for transform modifier") }
    )
}
