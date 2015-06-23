package com.teamcos.modularsystems.interfaces;

import com.teamcos.modularsystems.utilities.block.ModularSystemsTile;
import net.minecraft.block.Block;

public interface ICore extends ModularSystemsTile {
    Block getOverlay();

    Block getDummyBlock();

    boolean updateMultiblock();
}
