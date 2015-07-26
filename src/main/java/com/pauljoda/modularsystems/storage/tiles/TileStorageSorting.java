package com.pauljoda.modularsystems.storage.tiles;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/25/2015
 */
public class TileStorageSorting extends TileStorageBasic {
    @Override
    public void addedToNetwork() {
        super.addedToNetwork();
        if(getCore() != null) {
            getCore().setHasSortingUpgrade(true);
            worldObj.markBlockForUpdate(core.x, core.y, core.z);
        }
    }

    @Override
    public void removedFromNetwork() {
        super.removedFromNetwork();
        if(getCore() != null) {
            getCore().setHasSortingUpgrade(false);
            worldObj.markBlockForUpdate(core.x, core.y, core.z);
        }
    }
}
