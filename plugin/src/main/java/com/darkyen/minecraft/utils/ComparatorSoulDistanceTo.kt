package com.darkyen.minecraft.utils

import com.darkyen.minecraft.models.Soul

class ComparatorSoulDistanceTo(
    private val toX: Double,
    private val toY: Double,
    private val toZ: Double
) : Comparator<Soul> {
    private fun distanceTo(s: Soul): Double {
        val x = toX - s.locationX
        val y = toY - s.locationY
        val z = toZ - s.locationZ
        return x * x + y * y + z * z
    }

    override fun compare(o1: Soul, o2: Soul): Int {
        return distanceTo(o1).compareTo(distanceTo(o2))
    }
}
