package com.darkyen.minecraft.utils

import com.darkyen.minecraft.models.Soul
import org.bukkit.Bukkit

object ExtSoul {
    val Soul.playerName: String
        get() = owner?.let(Bukkit::getOfflinePlayer)?.name ?: "Unknown"
}
