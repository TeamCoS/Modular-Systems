package com.teamcos.modularsystems.functions;

import com.teamcos.modularsystems.core.managers.BlockManager;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class HasCraftingUpgrade implements WorldFunction {

    private boolean hasCrafting = false;

    @Override
    public void outerBlock(World world, int x, int y, int z) {
        Block blockId = world.getBlock(x, y, z);
        hasCrafting = blockId == BlockManager.furnaceCraftingUpgrade;
    }

    @Override
    public void innerBlock(World world, int x, int y, int z) {

    }

    @Override
    public boolean shouldContinue() {
        return !hasCrafting;
    }

    @Override
    public void clear() {
        hasCrafting = false;
    }

    @Override
    public WorldFunction copy() {
        return new HasCraftingUpgrade();
    }
}
