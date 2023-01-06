package com.darkyen.minecraft.utils

import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.chat.hover.content.Text
import org.bukkit.World

object SpigotCompat {

    /** Return the minimal possible block height in the world.  */
    @JvmStatic
    fun worldGetMinHeight(world: World): Int = kotlin.runCatching {
        world.minHeight
    }.getOrNull() ?: 0


    /** Set the hover event to text.  */
    @Suppress("deprecation")
    @JvmStatic
    fun textComponentSetHoverText(textComponent: TextComponent, tooltip: String?){
        kotlin.runCatching {
            textComponent.hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, Text(tooltip))
        }.onFailure {
            textComponent.hoverEvent =
                HoverEvent(HoverEvent.Action.SHOW_TEXT, arrayOf<BaseComponent>(TextComponent(tooltip)))
        }
    }
}
