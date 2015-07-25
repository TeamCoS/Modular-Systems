package com.pauljoda.modularsystems.storage.tiles;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/24/2015
 */
public class TileStorageBasic extends TileEntityStorageExpansion {
    @Override
    public void addedToNetwork() {
        worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 2);
    }

    @Override
    public void removedFromNetwork() {
        worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 5, 2);
    }
}
