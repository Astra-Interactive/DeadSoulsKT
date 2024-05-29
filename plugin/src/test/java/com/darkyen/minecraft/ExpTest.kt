package com.darkyen.minecraft

import com.darkyen.minecraft.utils.Util
import org.junit.Assert
import org.junit.Test

class ExpTest {
    @Test
    @Suppress("MemberNameEqualsClassName")
    fun expTest() {
        var total = 0
        for (level in 0..99) {
            total += Util.getExpToLevel(level)
            Assert.assertEquals(total.toLong(), Util.getExperienceToReach(level + 1).toLong())
        }
    }
}
