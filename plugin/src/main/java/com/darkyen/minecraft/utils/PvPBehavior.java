package com.darkyen.minecraft.utils;

public enum PvPBehavior {
    /** no change */
    NORMAL,
    /** souls are not created in PvP, items and XP drops like in vanilla Minecraft */
    DISABLED,
    /** created souls are immediately free and can be collected by anyone */
    FREE
}
