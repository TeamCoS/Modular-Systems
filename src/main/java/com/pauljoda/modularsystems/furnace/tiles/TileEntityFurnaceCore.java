package com.pauljoda.modularsystems.furnace.tiles;

import com.pauljoda.modularsystems.core.tiles.BaseTile;
import com.pauljoda.modularsystems.furnace.collections.FurnaceValues;

public class TileEntityFurnaceCore extends BaseTile {
    protected FurnaceValues values;

    public TileEntityFurnaceCore() {
        values = new FurnaceValues();
    }
}
