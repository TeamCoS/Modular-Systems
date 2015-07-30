package com.pauljoda.modularsystems.storage.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/29/2015
 */
public class SlotPhantom extends Slot {
    public SlotPhantom(IInventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    public void onSlotChange(ItemStack stack1, ItemStack stack2) {
        if (stack1 != null && stack2 != null) {
            if (stack1.getItem() == stack2.getItem()) {
                int i = stack2.stackSize - stack1.stackSize;

                if (i > 0) {
                    this.onCrafting(stack1, i);
                }
            }
        }
    }
}
