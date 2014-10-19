package com.teamcos.modularsystems.helpers;

import net.minecraft.tileentity.TileEntity;

public class WorldUtil {
	
	/**
	 * Returns true if tiles are in same location
	 * @param tile1 First Tile Entity
	 * @param tile2 Second Tile Entity
	 * @return
	 */
	public static boolean areTilesSame(TileEntity tile1, TileEntity tile2)
	{
		return (tile1.xCoord == tile2.xCoord) && (tile1.yCoord == tile2.yCoord) && (tile1.zCoord == tile2.zCoord);
	}

    public static boolean areSame(Locatable l1, Locatable l2) {
        return l1.getX() == l2.getX() && l1.getY() == l2.getY() && l1.getZ() == l2.getZ();
    }
}
