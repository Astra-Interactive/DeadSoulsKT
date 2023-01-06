package com.darkyen.minecraft.utils;

import com.darkyen.minecraft.models.Soul;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public final class ComparatorSoulDistanceTo implements Comparator<Soul> {

    final double toX, toY, toZ;

    public ComparatorSoulDistanceTo(double toX, double toY, double toZ) {
        this.toX = toX;
        this.toY = toY;
        this.toZ = toZ;
    }

    public double distanceTo(@NotNull Soul s) {
        final double x = toX - s.getLocationX();
        final double y = toY - s.getLocationY();
        final double z = toZ - s.getLocationZ();
        return x*x + y*y + z*z;
    }

    @Override
    public int compare(@NotNull Soul o1, @NotNull Soul o2) {
        return Double.compare(distanceTo(o1), distanceTo(o2));
    }
}