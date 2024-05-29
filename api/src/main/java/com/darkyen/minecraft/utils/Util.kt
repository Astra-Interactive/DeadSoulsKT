@file: Suppress("TooManyFunctions", "Indentation", "SwallowedException")

package com.darkyen.minecraft.utils

import com.darkyen.minecraft.models.Soul
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.util.NumberConversions
import java.util.Locale
import java.util.concurrent.TimeUnit
import java.util.logging.Level
import java.util.logging.Logger
import java.util.regex.Pattern
import kotlin.math.max
import kotlin.math.min

object Util {
    @JvmStatic
    fun overlaps(quadMin: Int, quadMax: Int, queryMin: Int, queryMax: Int): Boolean {
        return queryMin <= quadMax && queryMax >= quadMin
    }

    @JvmStatic
    fun getWorld(loc: Location?): World? {
        if (loc == null) {
            return null
        }
        try {
            if (!loc.isWorldLoaded) {
                return null
            }
        } catch (ignored: Throwable) {
            // isWorldLoaded is not available on servers < 1.14
        }
        return try {
            loc.world
        } catch (ignored: Throwable) {
            // (>= 1.14) If the world gets unloaded between check above and now, this could throw, but it is unlikely.
            null
        }
    }

    @JvmStatic
    fun distance2(a: Location, b: Location): Double {
        val aWorld = getWorld(a)
        val bWorld = getWorld(b)
        if (aWorld == null || aWorld != bWorld) {
            return Double.POSITIVE_INFINITY
        }
        return NumberConversions.square(a.x - b.x) +
                NumberConversions.square(a.y - b.y) +
                NumberConversions.square(a.z - b.z)
    }

    @JvmStatic
    fun distance2(a: Location, b: Location, yScale: Double): Double {
        val aWorld = getWorld(a)
        val bWorld = getWorld(b)
        if (aWorld == null || aWorld != bWorld) {
            return Double.POSITIVE_INFINITY
        }
        return NumberConversions.square(a.x - b.x) +
                NumberConversions.square((a.y - b.y) * yScale) +
                NumberConversions.square(a.z - b.z)
    }

    @JvmStatic
    fun distance2(soul: Soul, loc: Location, yScale: Double): Double {
        val locWorld = getWorld(loc)
        if (locWorld == null || soul.world != locWorld.uid) {
            return Double.POSITIVE_INFINITY
        }
        return NumberConversions.square(soul.locationX - loc.x) +
                NumberConversions.square((soul.locationY - loc.y) * yScale) +
                NumberConversions.square(soul.locationZ - loc.z)
    }

    @JvmStatic
    fun isNear(a: Location, b: Location, distance: Float): Boolean {
        val aWorld = getWorld(a)
        val bWorld = getWorld(b)
        if (aWorld == null || aWorld != bWorld) {
            return false
        }
        val dst2 = NumberConversions.square(a.x - b.x) + NumberConversions.square(a.z - b.z)
        return dst2 < distance * distance
    }

    @JvmStatic
    fun set(target: Location, source: Location) {
        target.world = getWorld(source)
        target.x = source.x
        target.y = source.y
        target.z = source.z
    }

    @JvmField
    val TIME_SANITIZER: Pattern = Pattern.compile("[^a-zA-Z0-9]")

    @JvmStatic
    fun parseTimeMs(time: String?, defaultMs: Long, log: Logger): Long {
        if (time == null) {
            return defaultMs
        }
        val sanitized = TIME_SANITIZER.matcher(time).replaceAll("")
        if ("never".equals(sanitized, ignoreCase = true)) {
            return Long.MAX_VALUE
        }
        var firstLetterIndex = 0
        while (firstLetterIndex < sanitized.length && Character.isDigit(sanitized[firstLetterIndex])) {
            firstLetterIndex++
        }
        if (firstLetterIndex >= sanitized.length) {
            log.log(Level.WARNING, "Time \"$time\" is missing an unit")
            return defaultMs
        }
        if (firstLetterIndex == 0) {
            log.log(Level.WARNING, "Time \"$time\" is missing an amount")
            return defaultMs
        }
        val amount: Long
        try {
            amount = sanitized.substring(0, firstLetterIndex).toLong()
        } catch (e: NumberFormatException) {
            log.log(Level.WARNING, "Time \"$time\" is invalid")
            return defaultMs
        }
        val unit = when (sanitized[firstLetterIndex]) {
            's' -> TimeUnit.SECONDS
            'm' -> TimeUnit.MINUTES
            'h' -> TimeUnit.HOURS
            'd' -> TimeUnit.DAYS
            else -> {
                log.log(Level.WARNING, "Time \"$time\" has invalid unit")
                return defaultMs
            }
        }
        return unit.toMillis(amount)
    }

    @JvmStatic
    fun normalizeKey(sound: String?): String {
        if (sound == null) {
            return ""
        }
        return sound.replace("[^_./:0-9A-Za-z-]+".toRegex(), "").lowercase(Locale.getDefault())
    }

    @JvmStatic
    fun parseColor(color: String?, defaultColor: Color, log: Logger): Color {
        if (color == null) {
            return defaultColor
        }
        val colorHex = color.replace("[^0-9A-Fa-f]+".toRegex(), "")
        if (colorHex.length != 6) {
            log.log(
                Level.WARNING,
                "Invalid color: '$color' - must be hexadecimal number in RRGGBB format"
            )
            return defaultColor
        }
        try {
            return Color.fromRGB(color.toInt(16) and 0xFFFFFF)
        } catch (nfe: NumberFormatException) {
            log.log(
                Level.WARNING,
                "Invalid color: '$color' - must be hexadecimal number in RRGGBB format"
            )
            return defaultColor
        }
    }

    // https://stackoverflow.com/a/2633161
    @JvmStatic
    fun saturatedAdd(x: Long, y: Long): Long {
        // Sum ignoring overflow/underflow
        val s = x + y

        // Long.MIN_VALUE if result positive (potential underflow)
        // Long.MAX_VALUE if result negative (potential overflow)
        val limit = Long.MIN_VALUE xor (s shr 63)

        // -1 if overflow/underflow occurred, 0 otherwise
        val overflow = ((x xor s) and (x xor y).inv()) shr 63

        // limit if overflowed/underflowed, else s
        return ((limit xor s) and overflow) xor s
    }

    @JvmStatic
    fun clamp(value: Int, min: Int, max: Int): Int {
        return max(min.toDouble(), min(value.toDouble(), max.toDouble())).toInt()
    }

    @JvmStatic
    fun getExpToLevel(expLevel: Int): Int {
        // From Spigot source
        return if (expLevel >= 30) {
            112 + (expLevel - 30) * 9
        } else if (expLevel >= 15) {
            37 + (expLevel - 15) * 5
        } else {
            7 + expLevel * 2
        }
    }

    @JvmStatic
    fun getExperienceToReach(level: Int): Int {
        // From https://minecraft.gamepedia.com/Experience (16. 7. 2019, Minecraft 1.14.2)
        val level2 = level * level
        return if (level <= 16) {
            level2 + 6 * level
        } else if (level <= 31) {
            level2 * 2 + level2 / 2 - 40 * level - level / 2 + 360
        } else {
            level2 * 4 + level2 / 2 - 162 * level - level / 2 + 2220
        }
    }

    @JvmStatic
    fun getTotalExperience(player: Player): Int {
        // Can't use player.getTotalExperience(), because that does not properly handle XP added via "/xp add level",
        // and, most importantly, it does not factor in experience spent on enchanting.
        return getExperienceToReach(player.level) + Math.round(getExpToLevel(player.level) * player.exp)
    }

    @JvmStatic
    fun safeToString(item: ItemStack?): String {
        if (item == null) {
            return "null"
        }
        return try {
            item.toString()
        } catch (e: Exception) {
            "ItemStack{" + item.type.name + " x " + item.amount + ", [broken meta]}"
        }
    }

    @JvmStatic
    fun compileSimpleGlob(glob: String): Pattern {
        val pattern = StringBuilder()
        var begin = 0
        while (begin < glob.length) {
            val end = glob.indexOf('*', begin)

            if (end == -1) {
                pattern.append(Pattern.quote(glob.substring(begin)))
                break
            } else {
                pattern.append(Pattern.quote(glob.substring(begin, end)))
                pattern.append(".*")
            }
            begin = end + 1
        }

        return Pattern.compile(pattern.toString())
    }
}
