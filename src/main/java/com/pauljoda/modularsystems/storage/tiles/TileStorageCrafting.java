package com.pauljoda.modularsystems.storage.tiles;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/26/2015
 */
public class TileStorageCrafting extends TileStorageBasic {
    @Override
    public void addedToNetwork() {
        super.addedToNetwork();
        if(getCore() != null) {
            getCore().setHasCraftingUpgrade(true);
            worldObj.markBlockForUpdate(core.x, core.y, core.z);

        }
    }

    @Override
    public void removedFromNetwork() {
        super.removedFromNetwork();
        if(getCore() != null) {
            getCore().setHasCraftingUpgrade(false);
            worldObj.markBlockForUpdate(core.x, core.y, core.z);
        }
    }
}
