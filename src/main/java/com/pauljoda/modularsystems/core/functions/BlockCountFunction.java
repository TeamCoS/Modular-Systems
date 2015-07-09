package com.pauljoda.modularsystems.core.functions;

import com.teambr.bookshelf.helpers.BlockHelper;
import net.minecraft.block.Block;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

public class BlockCountFunction {
    protected HashMap<String, Integer> blockCount;

    public BlockCountFunction() {
        blockCount = new LinkedHashMap<>();
    }

    public void addBlock(Block block, int meta) {
        int i = blockCount.get(BlockHelper.getBlockString(block, meta)) != null ? blockCount.get(BlockHelper.getBlockString(block, meta)) : 0;
        blockCount.put(BlockHelper.getBlockString(block, meta), ++i);
    }

    public int getBlockCount(Block block, int meta) {
        return blockCount.get(BlockHelper.getBlockString(block, meta)) != null ? blockCount.get(BlockHelper.getBlockString(block, meta)) : 0;
    }

    public Set<String> getBlockIds() {
        return blockCount.keySet();
    }
}
