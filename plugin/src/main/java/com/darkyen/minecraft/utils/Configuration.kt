package com.darkyen.minecraft.utils

import com.darkyen.minecraft.utils.ExtConfiguration.cDustOptions
import com.darkyen.minecraft.utils.ExtConfiguration.cNormalizedKey
import org.bukkit.Color
import org.bukkit.configuration.file.FileConfiguration
import ru.astrainteractive.astralibs.configuration.BukkitMutableStorageValue.anyMutableStorageValue
import ru.astrainteractive.klibs.kstorage.withDefault

class Configuration(fileConfiguration: FileConfiguration) {

    val soulFreeAfterMs = fileConfiguration
        .anyMutableStorageValue<Long?>("soul-free-after")
        .withDefault(Long.MAX_VALUE)

    val soulFadesAfterMs = fileConfiguration
        .anyMutableStorageValue<Long?>("soul-fades-after")
        .withDefault(Long.MAX_VALUE)

    val autoSaveMs = fileConfiguration
        .anyMutableStorageValue<Long?>("auto-save")
        .withDefault(0L)

    val soundSoulCollectXp = fileConfiguration
        .cNormalizedKey("sound-soul-collect-xp", "entity.experience_orb.pickup")

    val soundSoulCollectItem = fileConfiguration
        .cNormalizedKey("sound-soul-collect-item", "item.trident.return")

    val soundSoulDepleted = fileConfiguration
        .cNormalizedKey("sound-soul-depleted", "entity.generic.extinguish_fire")

    val soundSoulCalling = fileConfiguration
        .cNormalizedKey("sound-soul-calling", "block.beacon.ambient")

    val soundSoulDropped = fileConfiguration
        .cNormalizedKey("sound-soul-dropped", "block.bell.resonate")

    val volumeSoulCalling = fileConfiguration
        .anyMutableStorageValue<Float>("volume-soul-calling")
        .withDefault(16f)

    val textFreeMySoul = fileConfiguration
        .anyMutableStorageValue<String>("text-free-my-soul")
        .withDefault("Free my soul")

    val textFreeMySoulTooltip = fileConfiguration
        .anyMutableStorageValue<String>("text-free-my-soul-tooltip")
        .withDefault("Allows other players to collect the soul immediately")

    val soulFreeingEnabled: Boolean
        get() = textFreeMySoul.value.isNotEmpty()

    val smartSoulPlacement = fileConfiguration
        .anyMutableStorageValue<Boolean>("smart-soul-placement")
        .withDefault(true)

    val soulDustOptionsItems = fileConfiguration
        .cDustOptions("color-soul-items", Color.WHITE, 2f)
    val soulDustOptionsXp = fileConfiguration
        .cDustOptions("color-soul-xp", Color.AQUA, 2f)
    val soulDustOptionsGone = fileConfiguration
        .cDustOptions("color-soul-gone", Color.YELLOW, 3f)
}
