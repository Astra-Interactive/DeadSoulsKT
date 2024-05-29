package com.darkyen.minecraft

import com.darkyen.minecraft.utils.Util
import org.junit.Assert
import org.junit.Test

/**
 *
 */
class GlobTest {
    @Test
    @Suppress("MemberNameEqualsClassName")
    fun globTest() {
        Assert.assertTrue(Util.compileSimpleGlob("*").matcher("").matches())
        Assert.assertTrue(Util.compileSimpleGlob("*").matcher("world").matches())
        Assert.assertTrue(Util.compileSimpleGlob("*").matcher("world.*&^~$#@").matches())

        Assert.assertTrue(Util.compileSimpleGlob("world").matcher("world").matches())
        Assert.assertFalse(Util.compileSimpleGlob("world").matcher("World").matches())
        Assert.assertFalse(Util.compileSimpleGlob("world").matcher("world1").matches())
        Assert.assertFalse(Util.compileSimpleGlob("world").matcher(".worl").matches())

        Assert.assertTrue(Util.compileSimpleGlob("world*").matcher("world").matches())
        Assert.assertTrue(Util.compileSimpleGlob("world*").matcher("worlds").matches())
        Assert.assertTrue(Util.compileSimpleGlob("world*").matcher("world{*&^~$#@()").matches())

        Assert.assertTrue(Util.compileSimpleGlob("wo*rld*").matcher("world{*&^~$#@()").matches())
        Assert.assertTrue(Util.compileSimpleGlob("wo*rld*").matcher("wo123456789rld{*&^~$#@()").matches())
        Assert.assertFalse(Util.compileSimpleGlob("wo*rld*").matcher("w123456789orld{*&^~$#@()").matches())

        Assert.assertFalse(Util.compileSimpleGlob("*wo*rld*").matcher("w123456789orld{*&^~$#@()").matches())
        Assert.assertTrue(Util.compileSimpleGlob("*wo*rld*").matcher("wo123456789orld{*&^~$#@()").matches())
        Assert.assertTrue(
            Util.compileSimpleGlob("*wo*rld*").matcher(",.,-.,)§)!:_?(/`!wo123456789orld{*&^~$#@()").matches()
        )
    }
}
