package com.darkyen.minecraft.api

import org.bukkit.Location
import org.bukkit.inventory.ItemStack
import java.util.*

/**
 * A soul representation.
 * All methods are thread safe, unless specified otherwise.
 * Custom implementations are not allowed.
 */
interface ISoul {
    /** Get the {@link Player#getUniqueId()} of the player which owns this soul or null if already released. */
    val owner: UUID?
    /** Get the {@link World#getUID()} of the world in which this soul is. */
    val world: UUID
    /** Get the X coordinate of the soul in the world. */
    val locationX: Double
    /** Get the Y coordinate of the soul in the world. */
    val locationY: Double
    /** Get the Z coordinate of the soul in the world. */
    val locationZ: Double
    /** Get the location in the Bukkit format. May return null if the world does not exist.
     * Not thread safe, call only from main thread (because of {@link org.bukkit.Bukkit#getWorld(UUID)}). */
    val location: Location?
    /** Get the timestamp of when the soul was created. (Using the semantics of {@link System#currentTimeMillis()}.) */
    val creationTimestamp: Long
    /** Get the items which are stored in the soul.
     * DO NOT MODIFY THE ARRAY, NOR THE ItemStacks! */
    val items: Array<ItemStack>
    /** Get the experience points stored in the soul. */
    val experiencePoints: Int
}