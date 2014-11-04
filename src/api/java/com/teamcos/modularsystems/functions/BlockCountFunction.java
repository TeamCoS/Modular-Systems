package com.teamcos.modularsystems.functions;

import com.teamcos.modularsystems.helpers.Coord;
import net.minecraft.block.Block;

import java.util.List;
import java.util.Map;

public interface BlockCountFunction extends WorldFunction {
    Map<Block, Integer> getBlockCounts();
    List<Coord> getTiles();

    /**
     * Creates a copy of the function with fresh internal state.
     */
    BlockCountFunction copy();
}
