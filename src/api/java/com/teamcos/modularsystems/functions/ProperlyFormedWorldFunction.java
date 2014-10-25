package com.teamcos.modularsystems.functions;


import com.teamcos.modularsystems.core.lib.Reference;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class ProperlyFormedWorldFunction implements WorldFunction {

    boolean shouldContinue = true;
    boolean failed = false;

    @Override
    public void outerBlock(World world, int x, int y, int z) {
        Block blockId = world.getBlock(x, y, z);
        if (world.isAirBlock(x, y, z) || (Reference.isBadBlock(blockId) && !Reference.isModularTile(blockId.getUnlocalizedName()))) {
            shouldContinue = false;
        } else if (!Reference.isValidBlock(blockId.getUnlocalizedName())) {
            shouldContinue = Reference.isModularTile(blockId.getUnlocalizedName());
        }
    }

    @Override
    public void innerBlock(World world, int x, int y, int z) {
        shouldContinue = world.isAirBlock(x, y, z);
    }

    @Override
    public boolean shouldContinue() {
        return shouldContinue && !failed;
    }

    @Override
    public void reset() {
        shouldContinue = true;
        failed = false;
    }

    @Override
    public WorldFunction copy() {
        return new ProperlyFormedWorldFunction();
    }

    @Override
    public void fail() {
        failed = true;
    }
}
