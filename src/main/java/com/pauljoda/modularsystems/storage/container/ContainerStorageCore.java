package com.pauljoda.modularsystems.storage.container;

import com.teambr.bookshelf.inventory.BaseContainer;
import net.minecraft.inventory.IInventory;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/23/2015
 */
public class ContainerStorageCore extends BaseContainer {
    public ContainerStorageCore(IInventory playerInventory, IInventory ownerInventory) {
        super(playerInventory, ownerInventory);
        addInventoryGrid(25, 17, 11, 6);
        addPlayerInventorySlots(44, 130);
    }

    protected void addInventoryGrid(int xOffset, int yOffset, int width, int rows) {
        for (int y = 0, slotId = 0; y < rows; y++) {
            for (int x = 0; x < width; x++, slotId++) {
                addSlotToContainer(new RestrictedSlot(inventory, slotId,
                        xOffset + x * 18,
                        yOffset + y * 18));
            }
        }
    }
}
