package com.pauljoda.modularsystems.storage.tiles;

import com.pauljoda.modularsystems.storage.gui.GuiStorageSecurity;
import com.teambr.bookshelf.inventory.ContainerGeneric;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/25/2015
 */
public class TileStorageSecurity extends TileStorageBasic {

    @Override
    public void addedToNetwork() {
        super.addedToNetwork();
        if(getCore() != null) {
            getCore().setIsSecured(true);
            worldObj.markBlockForUpdate(core.x, core.y, core.z);

        }
    }

    @Override
    public void removedFromNetwork() {
        super.removedFromNetwork();
        if(getCore() != null) {
            getCore().setIsSecured(false);
            worldObj.markBlockForUpdate(core.x, core.y, core.z);
        }
    }

    /*******************************************************************************************************************
     ****************************************** IOpensGui Methods ******************************************************
     *******************************************************************************************************************/

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return getCore() != null ? (player.isSneaking() && getCore().canOpen(player) ? new ContainerGeneric(): getCore().getServerGuiElement(ID, player, world, x, y, z)) : null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return getCore() != null ? (player.isSneaking() && getCore().canOpen(player) ? new GuiStorageSecurity(this) : getCore().getClientGuiElement(ID, player, world, x, y, z)) : null;
    }
}
