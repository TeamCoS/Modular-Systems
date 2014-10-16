package com.pauljoda.modularsystems.functions;

import net.minecraft.world.World;

public interface WorldFunction {
    void outerBlock(World world, int x, int y, int z);
    void innerBlock(World world, int x, int y, int z);
    boolean shouldContinue();
}
