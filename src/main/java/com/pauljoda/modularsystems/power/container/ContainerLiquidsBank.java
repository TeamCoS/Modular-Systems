package com.pauljoda.modularsystems.power.container;

import com.pauljoda.modularsystems.power.tiles.TileLiquidsBank;
import com.teambr.bookshelf.inventory.BaseContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.SlotFurnace;

public class ContainerLiquidsBank extends BaseContainer {

    public ContainerLiquidsBank(InventoryPlayer playerInventory, TileLiquidsBank tileEntity) {
        super(playerInventory, tileEntity);

        addSlotToContainer(new RestrictedSlot(tileEntity, TileLiquidsBank.BUCKET_IN, 25, 20));
        addSlotToContainer(new SlotFurnace(playerInventory.player, tileEntity, TileLiquidsBank.BUCKET_OUT, 25, 50));

        addPlayerInventorySlots(8, 84);
    }
}
