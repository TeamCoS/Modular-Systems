package com.teamcos.modularsystems.functions;

import com.teamcos.modularsystems.utilities.tiles.DummyTile;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.LinkedHashMap;
import java.util.Map;

public class BlockCountWorldFunction implements BlockCountFunction {
    private Map<Block, Integer> blocks = new LinkedHashMap<Block, Integer>();

    @Override
    public void outerBlock(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof DummyTile) {
            DummyTile dummyTE = (DummyTile) tileEntity;
            block = dummyTE.getBlock();
        }
        GameRegistry.UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(block);
        block = GameRegistry.findBlock(id.modId, id.name);
        Integer count = blocks.get(block);

        if (count == null) {
            count = 1;
        } else {
            count += 1;
        }
        blocks.put(block, count);
    }

    @Override
    public void innerBlock(World world, int x, int y, int z) {

    }

    @Override
    public boolean shouldContinue() {
        return true;
    }

    public Map<Block, Integer> getBlockCounts() {
        return blocks;
    }

    @Override
    public void clear() {
        blocks = new LinkedHashMap<Block, Integer>();
    }

    @Override
    public BlockCountFunction copy() {
        return new BlockCountWorldFunction();
    }
}
