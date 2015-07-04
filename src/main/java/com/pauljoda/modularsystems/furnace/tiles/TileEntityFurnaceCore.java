package com.pauljoda.modularsystems.furnace.tiles;

import com.dyonovan.brlib.common.tiles.BaseTile;
import com.pauljoda.modularsystems.furnace.collections.FurnaceValues;

public class TileEntityFurnaceCore extends BaseTile {
    protected FurnaceValues values;

    public TileEntityFurnaceCore() {
        values = new FurnaceValues();
    }
}
