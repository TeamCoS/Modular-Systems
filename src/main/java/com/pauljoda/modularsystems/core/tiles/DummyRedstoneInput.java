package com.pauljoda.modularsystems.core.tiles;

import com.pauljoda.modularsystems.core.managers.BlockManager;
import net.minecraft.block.Block;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/30/2015
 */
public class DummyRedstoneInput extends DummyTile {

    public Block getStoredBlock() {
        return BlockManager.redstoneControlIn;
    }
}
