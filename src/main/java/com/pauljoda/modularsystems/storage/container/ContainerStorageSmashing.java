package com.pauljoda.modularsystems.storage.container;

import com.teambr.bookshelf.inventory.BaseContainer;
import net.minecraft.inventory.IInventory;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/27/2015
 */
public class ContainerStorageSmashing extends BaseContainer {

    public ContainerStorageSmashing(IInventory playerInventory, IInventory ownerInventory) {
        super(playerInventory, ownerInventory);
        addSlotToContainer(new RestrictedSlot(ownerInventory, 0, 80, 35));
        addPlayerInventorySlots(84);
    }
}
