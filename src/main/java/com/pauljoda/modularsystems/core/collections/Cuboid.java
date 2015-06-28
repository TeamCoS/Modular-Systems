package com.pauljoda.modularsystems.core.collections;

import net.minecraftforge.common.util.ForgeDirection;

public class Cuboid {
    public int width;
    public int height;
    public int length;

    public Location startPos;
    public ForgeDirection facing;

    public Cuboid(Location starting, ForgeDirection dir) {
        startPos = starting.createNew();
        facing = dir;
    }
}
