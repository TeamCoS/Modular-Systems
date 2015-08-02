package com.pauljoda.modularsystems.storage.container;

import com.teambr.bookshelf.inventory.BaseContainer;
import net.minecraft.inventory.IInventory;

/**
 * Modular-Systems
 * Created by Dyonovan on 01/08/15
 */
public class ContainerStorageRemote extends BaseContainer {
    public ContainerStorageRemote(IInventory playerInventory, IInventory ownerInventory) {
        super(playerInventory, ownerInventory);
        addSlotToContainer(new RestrictedSlot(ownerInventory, 0, 80, 35));
        addSlotToContainer(new RestrictedSlot(ownerInventory, 1, 80, 60));
        addPlayerInventorySlots(84);
    }
}
