package com.darkyen.minecraft.utils

import org.bukkit.Color
import org.bukkit.configuration.file.FileConfiguration

class Configuration(fileConfiguration: FileConfiguration) {
    val soulFreeAfterMs = fileConfiguration.cTimeMs("soul-free-after", Long.MAX_VALUE)
    val soulFadesAfterMs = fileConfiguration.cTimeMs("soul-fades-after", Long.MAX_VALUE)
    val autoSaveMs = fileConfiguration.cTimeMs("auto-save", 0L)

    val soundSoulCollectXp = fileConfiguration.cNormalizedKey("sound-soul-collect-xp", "entity.experience_orb.pickup")
    val soundSoulCollectItem = fileConfiguration.cNormalizedKey("sound-soul-collect-item", "item.trident.return")
    val soundSoulDepleted = fileConfiguration.cNormalizedKey("sound-soul-depleted", "entity.generic.extinguish_fire")
    val soundSoulCalling = fileConfiguration.cNormalizedKey("sound-soul-calling", "block.beacon.ambient")
    val soundSoulDropped = fileConfiguration.cNormalizedKey("sound-soul-dropped", "block.bell.resonate")

    val volumeSoulCalling = fileConfiguration.cFloat("volume-soul-calling", 16f)

    val textFreeMySoul = fileConfiguration.cString("text-free-my-soul", "Free my soul")
    val textFreeMySoulTooltip = fileConfiguration.cString("text-free-my-soul-tooltip", "Allows other players to collect the soul immediately")
    val soulFreeingEnabled: Boolean
        get() = textFreeMySoul.value.isNotEmpty();

    val smartSoulPlacement = fileConfiguration.cBoolean("smart-soul-placement", true)

    val soulDustOptionsItems = fileConfiguration.cDustOptions("color-soul-items", Color.WHITE, 2f)
    val soulDustOptionsXp = fileConfiguration.cDustOptions("color-soul-xp", Color.AQUA, 2f)
    val soulDustOptionsGone = fileConfiguration.cDustOptions("color-soul-gone", Color.YELLOW, 3f)
}




