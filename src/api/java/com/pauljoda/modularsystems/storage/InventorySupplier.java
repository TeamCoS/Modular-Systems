package com.pauljoda.modularsystems.storage;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public interface InventorySupplier extends IInventory {

    boolean hasInventory();
    int availableInventory();
    ItemStack[] getInventory();

    /**
     * Sets the inventory. Any items that can't be in the inventory can be rejected
     * by being returned to the caller. If an item isn't acceptable, it should not be returned on the validItems call.
     * @param inventory
     * @return Rejected ItemStacks
     */
    ItemStack[] setInventory(ItemStack[] inventory);

    /**
     * Returns any items that can go into the inventory.(Whitelist)
     * If the returned arrays from validItems and invalidItems is null, it is assumed any items can be in the inventory.
     * @return
     */
    ItemStack[] validItems();

    /**
     * Returns any items that cannot go in the inventory.(Blacklist)
     * If the returned array from validItems and invalidItems is null, it is assumed any items can be in the inventory.
     * @return
     */
    ItemStack[] invalidItems();
}
