package com.pauljoda.modularsystems.core.functions;

import net.minecraft.block.Block;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class BlockCountFunction {
    protected HashMap<Integer, Integer> blockCount;

    public BlockCountFunction() {
        blockCount = new LinkedHashMap<>();
    }

    public void addBlock(Block block) {
        int i = blockCount.get(Block.getIdFromBlock(block)) != null ? blockCount.get(Block.getIdFromBlock(block)) : 0;
        blockCount.put(Block.getIdFromBlock(block), ++i);
    }

    public int getBlockCount(Block block) {
        return blockCount.get(Block.getIdFromBlock(block)) != null ? blockCount.get(Block.getIdFromBlock(block)) : 0;
    }
}
