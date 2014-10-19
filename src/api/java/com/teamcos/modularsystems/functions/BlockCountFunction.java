package com.teamcos.modularsystems.functions;

import net.minecraft.block.Block;

import java.util.Map;

public interface BlockCountFunction extends WorldFunction {
    Map<Block, Integer> getBlockCounts();

    /**
     * Creates a copy of the function with fresh internal state.
     */
    BlockCountFunction copy();
}
