package com.teambr.modularsystems.storage.container.slot;

import com.teambr.bookshelf.common.container.slots.ICustomSlot;
import com.teambr.bookshelf.common.container.slots.SLOT_SIZE;
import com.teambr.modularsystems.storage.tiles.TileStorageCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import scala.Enumeration;
import scala.Tuple2;

import java.awt.*;
import java.util.ArrayList;

/**
 * This file was created for Modular-Systems
 * <p>
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis "pauljoda"
 * @since 3/29/2016
 */
public class SlotCraftingOutput extends SlotCrafting implements ICustomSlot {

    private TileStorageCore storageCore;

    /** The craft matrix inventory linked to this result slot. */
    private final InventoryCrafting craftMatrix;
    /** The player that is using the GUI where this slot resides. */
    private final EntityPlayer thePlayer;
    /** The number of items that have been crafted so far. Gets passed to ItemStack.onCrafting before being reset. */
    private int amountCrafted;

    public SlotCraftingOutput(EntityPlayer player,
                              InventoryCrafting craftingInventory, IInventory craftResult, TileStorageCore core,
                              int slotIndex, int xPosition, int yPosition) {
        super(player, craftingInventory, craftResult, slotIndex, xPosition, yPosition);
        storageCore = core;
        this.thePlayer = player;
        this.craftMatrix = craftingInventory;
    }

    @Override
    public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
        net.minecraftforge.fml.common.FMLCommonHandler.instance().firePlayerCraftingEvent(playerIn, stack, craftMatrix);
        this.onCrafting(stack);
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(playerIn);
        ItemStack[] remainingStacks = CraftingManager.getInstance().getRemainingItems(this.craftMatrix, playerIn.worldObj);
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);

        for (int i = 0; i < remainingStacks.length; ++i) {
            ItemStack stackInInventory = this.craftMatrix.getStackInSlot(i);
            ItemStack remainedStack = remainingStacks[i];

            if (stackInInventory != null) {
                ItemStack previousStack = stackInInventory.copy();
                this.craftMatrix.decrStackSize(i, 1);

                // Check for a change
                if (!ItemStack.areItemStacksEqual(previousStack, this.craftMatrix.getStackInSlot(i))) {
                    ItemStack heldStack = this.craftMatrix.getStackInSlot(i);
                    int stackSizeChanged = heldStack == null ?
                            previousStack.stackSize :
                            previousStack.stackSize - heldStack.stackSize;

                    ItemStack key = storageCore.getStack(previousStack, -1);
                    int extractSlot = -1;

                    ArrayList<ItemStack> list = storageCore.keysToList();

                    for (int j = 0; j < list.size(); j++) {
                        if (list.get(j) == key) {
                            extractSlot = j;
                            break;
                        }
                    }

                    if (extractSlot != -1) {
                        ItemStack extractStack = storageCore.extractItem(extractSlot, stackSizeChanged, false);
                        if (extractStack != null) {
                            if (this.craftMatrix.getStackInSlot(i) == null)
                                this.craftMatrix.setInventorySlotContents(i, extractStack);
                            else
                                this.craftMatrix.getStackInSlot(i).stackSize += extractStack.stackSize;
                        }
                    }
                }

                stackInInventory = this.craftMatrix.getStackInSlot(i);
            }

            if (remainedStack != null) {
                if (stackInInventory == null) {
                    this.craftMatrix.setInventorySlotContents(i, remainedStack);
                }
                else if (ItemStack.areItemsEqual(stackInInventory, remainedStack) &&
                        ItemStack.areItemStackTagsEqual(stackInInventory, remainedStack)) {
                    remainedStack.stackSize += stackInInventory.stackSize;
                    this.craftMatrix.setInventorySlotContents(i, remainedStack);
                }
                else if (!this.thePlayer.inventory.addItemStackToInventory(remainedStack)) {
                    this.thePlayer.dropItem(remainedStack, false);
                }
            }
        }
    }

    @Override
    public Enumeration.Value getSlotSize() {
        return SLOT_SIZE.LARGE();
    }

    @Override
    public Tuple2<Integer, Integer> getPoint() {
        return new Tuple2<>(xDisplayPosition - 5, yDisplayPosition - 5);
    }

    @Override
    public boolean hasColor() {
        return false;
    }

    @Override
    public Color getColor() {
        return new Color(0, 0, 0, 0);
    }
}
