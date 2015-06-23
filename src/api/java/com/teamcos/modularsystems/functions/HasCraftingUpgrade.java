package com.teamcos.modularsystems.functions;

import com.teamcos.modularsystems.interfaces.MSUpgradeBlock;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class HasCraftingUpgrade implements WorldFunction {

    private boolean hasCrafting = false;
    private boolean failed = false;

    @Override
    public void outerBlock(World world, int x, int y, int z) {
        Block blockId = world.getBlock(x, y, z);
        hasCrafting |= blockId instanceof MSUpgradeBlock && ((MSUpgradeBlock) blockId).isCrafter();
    }

    @Override
    public void innerBlock(World world, int x, int y, int z) {

    }

    @Override
    public boolean shouldContinue() {
        return !hasCrafting && !failed;
    }

    @Override
    public void reset() {
        hasCrafting = false;
        failed = false;
    }

    @Override
    public WorldFunction copy() {
        return new HasCraftingUpgrade();
    }

    @Override
    public void fail() {
        failed = true;
    }
}
