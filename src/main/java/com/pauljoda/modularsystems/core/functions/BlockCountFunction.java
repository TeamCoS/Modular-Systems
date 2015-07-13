package com.pauljoda.modularsystems.core.functions;

import com.pauljoda.modularsystems.core.registries.BlockValueRegistry;
import com.teambr.bookshelf.helpers.BlockHelper;
import net.minecraft.block.Block;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

public class BlockCountFunction {
    protected HashMap<String, Integer> blockCount;
    protected HashMap<String, Integer> materialCount;

    public BlockCountFunction() {
        blockCount = new LinkedHashMap<>();
        materialCount = new LinkedHashMap<>();
    }

    /**
     * Add a block to the count function
     *
     * Also adds to the material count if the block isn't mapped already
     *
     * @param block The block to add
     * @param meta The block metadata
     */
    public void addBlock(Block block, int meta) {
        int i = blockCount.get(BlockHelper.getBlockString(block, meta)) != null ? blockCount.get(BlockHelper.getBlockString(block, meta)) : 0;
        blockCount.put(BlockHelper.getBlockString(block, meta), ++i);
        if(!BlockValueRegistry.INSTANCE.isBlockRegistered(block, meta)) {
            int j = materialCount.get(BlockValueRegistry.getMaterialString(block.getMaterial())) != null ? materialCount.get(BlockValueRegistry.getMaterialString(block.getMaterial())) : 0;
            materialCount.put(BlockValueRegistry.getMaterialString(block.getMaterial()), ++j);
        }
    }

    /**
     * Get the block count for a block
     * @param block The block
     * @param meta Metadata
     * @return How many are in the function
     */
    public int getBlockCount(Block block, int meta) {
        return blockCount.get(BlockHelper.getBlockString(block, meta)) != null ? blockCount.get(BlockHelper.getBlockString(block, meta)) : 0;
    }

    /**
     * Get the material count for
     * @param mat Material to check
     * @return How many of this material are in the count, doesn't include those with block mappings
     */
    public int getMaterialCount(String mat) {
        return materialCount.get(mat) != null ? materialCount.get(mat) : 0;
    }

    public Set<String> getBlockIds() {
        return blockCount.keySet();
    }

    public Set<String> getMaterialStrings() { return materialCount.keySet(); }
}
