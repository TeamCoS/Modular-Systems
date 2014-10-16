package com.pauljoda.modularsystems.tiles;

import com.pauljoda.modularsystems.helpers.Locatable;
import net.minecraft.tileentity.TileEntity;

public class TileLocatable implements Locatable {

    private final int x;
    private final int y;
    private final int z;

    public TileLocatable(TileEntity tile) {
        this.x = tile.xCoord;
        this.y = tile.yCoord;
        this.z = tile.zCoord;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
}
