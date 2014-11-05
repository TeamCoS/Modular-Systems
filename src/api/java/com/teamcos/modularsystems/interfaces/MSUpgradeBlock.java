package com.teamcos.modularsystems.interfaces;

import net.minecraft.world.World;

public interface MSUpgradeBlock {
    double getEfficiency(World world, int x, int y, int z, int blockCount);
    double getSpeed(World world, int x, int y, int z, int blockCount);
    int getMultiplier(World world, int x, int y, int z, int blockCount);
    boolean isCrafter();
}
