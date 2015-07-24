package com.pauljoda.modularsystems.storage.container;

import com.pauljoda.modularsystems.storage.tiles.TileStorageCore;
import com.teambr.bookshelf.inventory.BaseContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/23/2015
 */
public class ContainerStorageCore extends BaseContainer {
    public TileStorageCore storage;

    public ContainerStorageCore(IInventory playerInventory, IInventory ownerInventory, TileStorageCore tile) {
        super(playerInventory, ownerInventory);
        storage = tile;
        addInventoryGrid(25, 17, 11, 6);
        if(storage.getInventoryRowCount() > 6) {
            int slotId = inventorySlots.size();
            for(int i = 66; i < storage.getSizeInventory(); i++)
                addSlotToContainer(new RestrictedSlot(inventory, slotId++, -10000, -10000));
        }
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

    public void scrollTo(float index) {
        int outsideDisplaySize = storage.getInventoryRowCount() - 6;
        int currentTopRow = (int)((double)(index * (float)outsideDisplaySize) + 0.5D);
        if(currentTopRow < 0)
            currentTopRow = 0;

        //Move it all off
        for(int i = 0; i < storage.getSizeInventory(); i++) {
            Slot slot = (Slot)this.inventorySlots.get(i);
            slot.xDisplayPosition = slot.yDisplayPosition = -10000;
        }

        //Move back what we need
        for(int y = 0; y < 6; y++) {
            for(int x = 0; x < 11; x++) {
                Slot slot = (Slot)this.inventorySlots.get((x + (y + currentTopRow) * 11));
                slot.xDisplayPosition = 25 + x * 18;
                slot.yDisplayPosition = 17 + y * 18;
            }
        }
    }
}
