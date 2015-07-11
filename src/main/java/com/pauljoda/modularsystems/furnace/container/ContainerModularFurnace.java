package com.pauljoda.modularsystems.furnace.container;

import com.pauljoda.modularsystems.furnace.tiles.TileEntityFurnaceCore;
import com.teambr.bookshelf.inventory.BaseContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;

public class ContainerModularFurnace extends BaseContainer {
    public ContainerModularFurnace(InventoryPlayer playerInventory, TileEntityFurnaceCore tileEntity) {
        super(playerInventory, tileEntity);
        // Input
        addSlotToContainer(new Slot(tileEntity, 0, 56, 35));
        // Output
        addSlotToContainer(new SlotFurnace(playerInventory.player, tileEntity, 1, 116, 35));

        addPlayerInventorySlots(8, 84);
    }
}
