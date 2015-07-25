package com.pauljoda.modularsystems.storage.tiles;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/24/2015
 */
public class TileStorageSearch extends TileStorageBasic {
    @Override
    public void addedToNetwork() {
        super.addedToNetwork();
        if(getCore() != null) {
            getCore().hasSearchUpgrade = true;
            worldObj.markBlockForUpdate(core.x, core.y, core.z);

        }
    }

    @Override
    public void removedFromNetwork() {
        super.removedFromNetwork();
        if(getCore() != null) {
            getCore().hasSearchUpgrade = false;
            worldObj.markBlockForUpdate(core.x, core.y, core.z);
        }
    }
}
