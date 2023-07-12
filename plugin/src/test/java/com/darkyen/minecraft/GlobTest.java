package com.darkyen.minecraft;

import com.darkyen.minecraft.utils.Util;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 */
public class GlobTest {

	@Test
	public void globTest() {
		assertTrue(Util.compileSimpleGlob("*").matcher("").matches());
		assertTrue(Util.compileSimpleGlob("*").matcher("world").matches());
		assertTrue(Util.compileSimpleGlob("*").matcher("world.*&^~$#@").matches());

		assertTrue(Util.compileSimpleGlob("world").matcher("world").matches());
		assertFalse(Util.compileSimpleGlob("world").matcher("World").matches());
		assertFalse(Util.compileSimpleGlob("world").matcher("world1").matches());
		assertFalse(Util.compileSimpleGlob("world").matcher(".worl").matches());

		assertTrue(Util.compileSimpleGlob("world*").matcher("world").matches());
		assertTrue(Util.compileSimpleGlob("world*").matcher("worlds").matches());
		assertTrue(Util.compileSimpleGlob("world*").matcher("world{*&^~$#@()").matches());

		assertTrue(Util.compileSimpleGlob("wo*rld*").matcher("world{*&^~$#@()").matches());
		assertTrue(Util.compileSimpleGlob("wo*rld*").matcher("wo123456789rld{*&^~$#@()").matches());
		assertFalse(Util.compileSimpleGlob("wo*rld*").matcher("w123456789orld{*&^~$#@()").matches());

		assertFalse(Util.compileSimpleGlob("*wo*rld*").matcher("w123456789orld{*&^~$#@()").matches());
		assertTrue(Util.compileSimpleGlob("*wo*rld*").matcher("wo123456789orld{*&^~$#@()").matches());
		assertTrue(Util.compileSimpleGlob("*wo*rld*").matcher(",.,-.,)§)!:_?(/`!wo123456789orld{*&^~$#@()").matches());
	}

}
