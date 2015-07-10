package com.pauljoda.modularsystems.power.container;

import com.pauljoda.modularsystems.power.tiles.TileRFPower;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public class ContainerRFPower extends Container {

    private TileRFPower tileEntity;

    public ContainerRFPower(InventoryPlayer player, TileRFPower tileEntity) {
        this.tileEntity = tileEntity;
    }


    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return tileEntity.isUseableByPlayer(entityPlayer);
    }
}
