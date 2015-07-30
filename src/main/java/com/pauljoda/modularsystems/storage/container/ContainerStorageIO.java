package com.pauljoda.modularsystems.storage.container;

import com.teambr.bookshelf.inventory.BaseContainer;
import net.minecraft.inventory.IInventory;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/29/2015
 */
public class ContainerStorageIO extends BaseContainer {
    public ContainerStorageIO(IInventory playerInventory, IInventory ownerInventory) {
        super(playerInventory, ownerInventory);
        addInventoryLine(8, 35, 0, 9);
        addInventoryLine(8, 78, 9, 9);

        addPlayerInventorySlots(100);
    }
}
