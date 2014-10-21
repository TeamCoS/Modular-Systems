package com.teamcos.modularsystems.utilities.tiles;

import com.teamcos.modularsystems.manager.ApiBlockManager;
import com.teamcos.modularsystems.utilities.block.DummyBlock;

public class DummyIOTile extends DummyTile {

    public DummyBlock getBlock() {
        return ApiBlockManager.dummyIOBlock;
    }
}
