package com.pauljoda.modularsystems.power.container;

import com.pauljoda.modularsystems.power.tiles.TileLiquidsPower;
import com.teambr.bookshelf.inventory.BaseContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;

public class ContainerLiquidsPower extends BaseContainer {

    public ContainerLiquidsPower(InventoryPlayer playerInventory, TileLiquidsPower tileEntity) {
        super(playerInventory, tileEntity);

        addSlotToContainer(new Slot(tileEntity, tileEntity.BUCKET_IN, 25, 20));
        addSlotToContainer(new SlotFurnace(playerInventory.player, tileEntity, tileEntity.BUCKET_OUT, 25, 50));

        addPlayerInventorySlots(8, 84);
    }
}
