package com.darkyen.minecraft;

import com.darkyen.minecraft.utils.Util;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 */
public final class ExpTest {

    @Test
    public void expTest() {
        int total = 0;
        for (int level = 0; level < 100; level++) {
            total += Util.getExpToLevel(level);
            assertEquals(total, Util.getExperienceToReach(level + 1));
        }
    }

}
