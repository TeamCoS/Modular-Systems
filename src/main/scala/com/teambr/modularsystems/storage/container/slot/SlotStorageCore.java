package com.teambr.modularsystems.storage.container.slot;

import com.teambr.modularsystems.storage.tiles.TileStorageCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * This file was created for Modular-Systems
 * <p/>
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis "pauljoda"
 * @since 3/27/2016
 */
public class SlotStorageCore extends Slot {

    private static IInventory emptyInventory = new InventoryBasic("[Null]", true, 0);
    private ItemStack heldStack = null;
    private TileStorageCore storageCore;

    public SlotStorageCore(TileStorageCore storageCoreIn,
                           int index, int xPosition, int yPosition) {
        super(emptyInventory, index, xPosition, yPosition);
        storageCore = storageCoreIn;
        heldStack = null;
        if(storageCore.keysToList().size() > index) {
            heldStack = storageCore.keysToList().get(index);
            int amount = (int) storageCore.getInventory().get(heldStack);
            heldStack = heldStack.copy();
            heldStack.stackSize = amount;
        }
    }

    @Override
    public void onSlotChange(ItemStack firstStack, ItemStack secondStack) {
       super.onSlotChange(firstStack, secondStack);
        storageCore.markForUpdate(6);
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack != null;
    }

    /**
     * Helper fnct to get the stack in the slot.
     */
    @Override
    public ItemStack getStack() {
        return heldStack;
    }

    /**
     * Helper method to put a stack in the slot.
     */
    @Override
    public void putStack(ItemStack stack) {
        storageCore.insertItem(-1, stack, false);
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return storageCore.getAmountRemaining();
    }

    /**
     * Return whether this slot's stack can be taken from this slot.
     */
    @Override
    public boolean canTakeStack(EntityPlayer playerIn) {
        return heldStack != null;
    }

    /**
     * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
     * stack.
     */
    @Override
    public ItemStack decrStackSize(int amount) {
        if(heldStack != null) {
            ItemStack stack;

            if(heldStack.stackSize <= amount) {
                stack = heldStack;
                heldStack = null;
                storageCore.markForUpdate(6);
                return stack;
            }

            stack = heldStack.splitStack(amount);

            if(heldStack.stackSize <= 0)
                heldStack = null;

            storageCore.markForUpdate(6);
            return stack;
        }
       return null;
    }
}
