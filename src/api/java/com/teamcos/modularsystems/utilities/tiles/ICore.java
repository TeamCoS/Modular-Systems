package com.teamcos.modularsystems.utilities.tiles;

import com.teamcos.modularsystems.utilities.block.ModularSystemsTile;
import net.minecraft.block.Block;

/**
 * Created by mathill on 10/25/14.
 */
public interface ICore extends ModularSystemsTile {
    Block getOverlay();

    Block getDummyBlock();

    boolean updateMultiblock();
}
