package com.teamcos.modularsystems.functions;


import com.teamcos.modularsystems.core.lib.Reference;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class ProperlyFormedWorldFunction implements WorldFunction {

    boolean shouldContinue = true;

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
        return shouldContinue;
    }

    @Override
    public void clear() {
        shouldContinue = true;
    }

    @Override
    public WorldFunction copy() {
        return new ProperlyFormedWorldFunction();
    }
}
