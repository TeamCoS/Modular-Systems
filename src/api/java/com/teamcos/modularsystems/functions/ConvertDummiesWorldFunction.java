package com.teamcos.modularsystems.functions;

import com.teamcos.modularsystems.core.lib.Reference;
import com.teamcos.modularsystems.utilities.tiles.DummyTile;
import com.teamcos.modularsystems.utilities.tiles.FueledRecipeTile;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import java.util.Map;

public class ConvertDummiesWorldFunction implements WorldFunction {
    private BlockCountWorldFunction bcFunc = new BlockCountWorldFunction();
    private final FueledRecipeTile core;

    public ConvertDummiesWorldFunction(FueledRecipeTile core) {
        this.core = core;
    }

    @Override
    public void outerBlock(World world, int x, int y, int z) {

        bcFunc.outerBlock(world, x, y, z);
        Block block = world.getBlock(x, y, z);
        if (block != null &&
                block != Blocks.air &&
                !Reference.isModularTile(block.getUnlocalizedName())) {
            int metadata = world.getBlockMetadata(x, y, z);

            world.setBlock(x, y, z, core.getDummyBlock());

            world.markBlockForUpdate(x, y, z);
            DummyTile dummyTE = (DummyTile) world.getTileEntity(x, y, z);

            if (block != core.getDummyBlock()) {
                dummyTE.setBlock(Block.getIdFromBlock(block));
                dummyTE.setMetadata(metadata);
            }
            world.markBlockForUpdate(x, y, z);
            dummyTE.setCore(core);
        }
    }

    @Override
    public void innerBlock(World world, int x, int y, int z) {
        bcFunc.innerBlock(world, x, y, z);
    }

    @Override
    public boolean shouldContinue() {
        return true;
    }

    @Override
    public void clear() {
        bcFunc.clear();
    }

    @Override
    public WorldFunction copy() {
        return new ConvertDummiesWorldFunction(core);
    }

    public Map<Block, Integer> blockCounts() {
        return bcFunc.getBlockCounts();
    }
}
