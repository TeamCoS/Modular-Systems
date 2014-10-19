package com.teamcos.modularsystems.functions;

import net.minecraft.world.World;

public interface WorldFunction {
    void outerBlock(World world, int x, int y, int z);
    void innerBlock(World world, int x, int y, int z);
    boolean shouldContinue();

    /**
     * Clears internal state.
     */
    void clear();

    /**
     * Creates a copy of the function with fresh internal state.
     */
    WorldFunction copy();
}
