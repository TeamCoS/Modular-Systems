package com.pauljoda.modularsystems.functions;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import java.util.LinkedHashMap;
import java.util.Map;

public interface WorldFunction {
    void outerBlock(World world, int x, int y, int z);
    void innerBlock(World world, int x, int y, int z);
    boolean shouldContinue();

    public static class BlockCountWorldFunction implements WorldFunction {
        private Map<Block, Integer> blocks = new LinkedHashMap<Block, Integer>();

        @Override
        public void outerBlock(World world, int x, int y, int z) {
            Block block = world.getBlock(x, y, z);
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
    }
}
