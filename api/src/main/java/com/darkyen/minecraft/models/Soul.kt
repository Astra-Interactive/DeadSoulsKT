package com.darkyen.minecraft.models

import com.darkyen.minecraft.database.SoulDatabase
import com.darkyen.minecraft.database.SpatialDatabase
import com.darkyen.minecraft.utils.Util
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import org.bukkit.World
import org.bukkit.command.CommandSender
import org.bukkit.inventory.ItemStack
import org.bukkit.util.NumberConversions
import java.util.*

/** A soul in a database. The fields that are not final can be modified,
 * but don't forget to [SoulDatabase.markDirty] if you do to ensure that the changes are saved.  */
class Soul(
    /** Current owner of the soul by [Player.getUniqueId].  */
    override var owner: UUID?,
    /** World in which the soul is by [World.getUID].  */
    override val world: UUID,
    /** Precise location of the soul in the world.  */
    override val locationX: Double,
    override val locationY: Double,
    override val locationZ: Double,
    /** When was the soul created on clock of [System.currentTimeMillis].  */
    override val creationTimestamp: Long,
    /** Can be changed when collected  */
    override var items: Array<ItemStack>,
    /** Can be changed when collected  */
    override var experiencePoints: Int
) : SpatialDatabase.Entry, ISoul {
    /** Index at which this Soul is or was stored, if any.
     * This is highly transient and does not serve as a way of identification.  */
    @Transient
    var id = -1
    //region API Getters

    fun isOwnedBy(commandSender: CommandSender?): Boolean {
        val owner = owner
        return owner != null && commandSender is OfflinePlayer && owner == (commandSender as OfflinePlayer).uniqueId
    }

    fun isAccessibleBy(player: OfflinePlayer, now: Long, soulFreeAfterMs: Long): Boolean {
        val owner = owner
        if (owner != null && owner != player.uniqueId) {
            // Soul of somebody else, not accessible unless expired
            if (Util.saturatedAdd(creationTimestamp, soulFreeAfterMs) <= now) {
                // Soul should become free
                this.owner = null
                return true
            }
            return false
        }
        return true
    }

    /** @return true if free, false if already freed
     */
    fun freeSoul(now: Long, soulFreeAfterMs: Long): Boolean {
        if (owner == null) {
            return false
        }
        owner = null
        // Did soul become free on its own?
        return Util.saturatedAdd(creationTimestamp, soulFreeAfterMs) > now
    }

    override fun x(): Int {
        return NumberConversions.floor(locationX) / SoulDatabase.SOUL_STORE_SCALE
    }

    override fun y(): Int {
        return NumberConversions.floor(locationZ) / SoulDatabase.SOUL_STORE_SCALE
    }

    @Transient
    private var locationCache: Location? = null
    fun getLocation(worldHint: World?): Location? {
        var locationCache = locationCache
        if (locationCache != null && Util.getWorld(locationCache) != null) {
            return locationCache
        }
        val world: World?
        world = if (worldHint != null && this.world == worldHint.uid) {
            worldHint
        } else {
            Bukkit.getWorld(this.world)
        }
        locationCache = if (world == null) {
            null
        } else {
            Location(world, locationX, locationY, locationZ)
        }
        this.locationCache = locationCache
        return locationCache
    }

    override val location: Location?
        get() = getLocation(null)

    //endregion
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val soul = o as Soul
        if (java.lang.Double.compare(soul.locationX, locationX) != 0) return false
        if (java.lang.Double.compare(soul.locationY, locationY) != 0) return false
        if (java.lang.Double.compare(soul.locationZ, locationZ) != 0) return false
        if (creationTimestamp != soul.creationTimestamp) return false
        if (experiencePoints != soul.experiencePoints) return false
        if (owner != soul.owner) return false
        return if (world != soul.world) false else Arrays.equals(items, soul.items)
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
    }

    override fun hashCode(): Int {
        var result: Int
        var temp: Long
        result = if (owner != null) owner.hashCode() else 0
        result = 31 * result + world.hashCode()
        temp = java.lang.Double.doubleToLongBits(locationX)
        result = 31 * result + (temp xor (temp ushr 32)).toInt()
        temp = java.lang.Double.doubleToLongBits(locationY)
        result = 31 * result + (temp xor (temp ushr 32)).toInt()
        temp = java.lang.Double.doubleToLongBits(locationZ)
        result = 31 * result + (temp xor (temp ushr 32)).toInt()
        result = 31 * result + (creationTimestamp xor (creationTimestamp ushr 32)).toInt()
        result = 31 * result + Arrays.hashCode(items)
        result = 31 * result + experiencePoints
        return result
    }

    override fun toString(): String {
        return "Soul{" +
            "id=" + id +
            ", owner=" + owner +
            ", locationWorld=" + world +
            ", locationX=" + locationX +
            ", locationY=" + locationY +
            ", locationZ=" + locationZ +
            ", timestamp=" + creationTimestamp +
            ", items=" + Arrays.toString(items) +
            ", xp=" + experiencePoints +
            '}'
    }
}
