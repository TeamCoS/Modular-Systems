package com.pauljoda.modularsystems.power.container;

import com.pauljoda.modularsystems.power.tiles.TileSolidsBank;
import com.teambr.bookshelf.inventory.BaseContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerSolidsBank extends BaseContainer {

    public ContainerSolidsBank(InventoryPlayer playerInventory, TileSolidsBank tileEntity) {
        super(playerInventory, tileEntity);

        for (int y = 0; y < 3; y++)
            for (int x = 0; x < 9; x++)
                addSlotToContainer(new RestrictedSlot(tileEntity, x + y * 9, 20 + x * 18, 20 + y * 18));

        bindPlayerInventory(playerInventory);
    }

    private void bindPlayerInventory(InventoryPlayer playerInventory)
    {
        // Inventory
        for(int y = 0; y < 3; y++)
            for(int x = 0; x < 9; x++)
                addSlotToContainer(new Slot(playerInventory, x + y * 9 + 9, 20 + x * 18, 84 + y * 18));

        // Action Bar
        for(int x = 0; x < 9; x++)
            addSlotToContainer(new Slot(playerInventory, x, 20 + x * 18, 142));
    }
}
