package com.pauljoda.modularsystems.helpers;


import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.pauljoda.modularsystems.utilities.inventory.GenericInventory;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Set;

/**
 * From OpenModLib
 */
public class InventoryUtil
{
    /***
     * Try to merge the supplied stack into the supplied slot in the target
     * inventory
     *
     * @param targetInventory
     *            Although it doesn't return anything, it'll REDUCE the stack
     *            size of the stack that you pass in
     *
     * @param slot
     * @param stack
     */
    public static void tryInsertStack(IInventory targetInventory, int slot, ItemStack stack, boolean canMerge) {
        if (targetInventory.isItemValidForSlot(slot, stack)) {
            ItemStack targetStack = targetInventory.getStackInSlot(slot);
            if (targetStack == null) {
                targetInventory.setInventorySlotContents(slot, stack.copy());
                stack.stackSize = 0;
            } else if (canMerge) {
                if (targetInventory.isItemValidForSlot(slot, stack) &&
                        areMergeCandidates(stack, targetStack)) {
                    int space = targetStack.getMaxStackSize()
                            - targetStack.stackSize;
                    int mergeAmount = Math.min(space, stack.stackSize);
                    ItemStack copy = targetStack.copy();
                    copy.stackSize += mergeAmount;
                    targetInventory.setInventorySlotContents(slot, copy);
                    stack.stackSize -= mergeAmount;
                }
            }
        }
    }

    public static boolean areItemAndTagEqual(final ItemStack stackA, ItemStack stackB) {
        return stackA.isItemEqual(stackB) && ItemStack.areItemStackTagsEqual(stackA, stackB);
    }

    public static boolean areMergeCandidates(ItemStack source, ItemStack target) {
        return areItemAndTagEqual(source, target) && target.stackSize < target.getMaxStackSize();
    }
    public static void insertItemIntoInventory(IInventory inventory, ItemStack stack) {
        insertItemIntoInventory(inventory, stack, ForgeDirection.UNKNOWN, -1);
    }

    public static void insertItemIntoInventory(IInventory inventory, ItemStack stack, ForgeDirection side, int intoSlot) {
        insertItemIntoInventory(inventory, stack, side, intoSlot, true);
    }

    public static void insertItemIntoInventory(IInventory inventory, ItemStack stack, ForgeDirection side, int intoSlot, boolean doMove) {
        insertItemIntoInventory(inventory, stack, side, intoSlot, doMove, true);
    }

    public static void insertItemIntoInventory(IInventory inventory, ItemStack stack, ForgeDirection side, int intoSlot, boolean doMove, boolean canStack) {
        if (stack == null) return;

        final int sideId = side.ordinal();
        IInventory targetInventory = inventory;

        // if we're not meant to move, make a clone of the inventory
        if (!doMove) {
            GenericInventory copy = new GenericInventory("temporary.inventory", false, targetInventory.getSizeInventory());
            copy.copyFrom(inventory);
            targetInventory = copy;
        }

        final Set<Integer> attemptSlots = Sets.newTreeSet();

        // if it's a sided inventory, get all the accessible slots
        final boolean isSidedInventory = inventory instanceof ISidedInventory && side != ForgeDirection.UNKNOWN;

        if (isSidedInventory) {
            int[] accessibleSlots = ((ISidedInventory)inventory).getAccessibleSlotsFromSide(sideId);
            for (int slot : accessibleSlots)
                attemptSlots.add(slot);
        } else {
            // if it's just a standard inventory, get all slots
            for (int a = 0; a < inventory.getSizeInventory(); a++) {
                attemptSlots.add(a);
            }
        }

        // if we've defining a specific slot, we'll just use that
        if (intoSlot > -1) attemptSlots.retainAll(ImmutableSet.of(intoSlot));

        if (attemptSlots.isEmpty()) return;

        for (Integer slot : attemptSlots) {
            if (stack.stackSize <= 0) break;
            if (isSidedInventory && !((ISidedInventory)inventory).canInsertItem(slot, stack, sideId)) continue;
            tryInsertStack(targetInventory, slot, stack, canStack);
        }
    }

    /***
     * Move an item from the fromInventory, into the target. The target can be
     * an inventory or pipe.
     * Double checks are automagically wrapped. If you're not bothered what slot
     * you insert into, pass -1 for intoSlot. If you're passing false for
     * doMove, it'll create a dummy inventory and its calculations on that
     * instead
     *
     * @param fromInventory
     *            the inventory the item is coming from
     * @param fromSlot
     *            the slot the item is coming from
     * @param target
     *            the inventory you want the item to be put into. can be BC pipe
     *            or IInventory
     * @param intoSlot
     *            the target slot. Pass -1 for any slot
     * @param maxAmount
     *            The maximum amount you wish to pass
     * @param direction
     *            The direction of the move. Pass UNKNOWN if not applicable
     * @param doMove
     * @param canStack
     * @return The amount of items moved
     */
    public static int moveItemInto(IInventory fromInventory, int fromSlot, Object target, int intoSlot, int maxAmount, ForgeDirection direction, boolean doMove, boolean canStack) {

        fromInventory = getInventory(fromInventory);

        // if we dont have a stack in the source location, return 0
        ItemStack sourceStack = fromInventory.getStackInSlot(fromSlot);
        if (sourceStack == null) { return 0; }

        if (fromInventory instanceof ISidedInventory
                && !((ISidedInventory)fromInventory).canExtractItem(fromSlot, sourceStack, direction.ordinal())) return 0;

        // create a clone of our source stack and set the size to either
        // maxAmount or the stackSize
        ItemStack clonedSourceStack = sourceStack.copy();
        clonedSourceStack.stackSize = Math.min(clonedSourceStack.stackSize, maxAmount);
        int amountToMove = clonedSourceStack.stackSize;
        int inserted = 0;


        if (target instanceof IInventory) {
            IInventory targetInventory = getInventory((IInventory)target);
            ForgeDirection side = direction.getOpposite();
            // try insert the item into the target inventory. this'll reduce the
            // stackSize of our stack
            insertItemIntoInventory(targetInventory, clonedSourceStack, side, intoSlot, doMove, canStack);
            inserted = amountToMove - clonedSourceStack.stackSize;

        }

        // if we've done the move, reduce/remove the stack from our source
        // inventory
        if (doMove) {
            ItemStack newSourcestack = sourceStack.copy();
            newSourcestack.stackSize -= inserted;
            if (newSourcestack.stackSize == 0) {
                fromInventory.setInventorySlotContents(fromSlot, null);
            } else {
                fromInventory.setInventorySlotContents(fromSlot, newSourcestack);
            }
        }

        return inserted;
    }

    private static IInventory doubleChestFix(net.minecraft.tileentity.TileEntity te) {
        final World world = te.getWorldObj();
        final int x = te.xCoord;
        final int y = te.yCoord;
        final int z = te.zCoord;
        if (world.getBlock(x - 1, y, z) == Blocks.chest) return new InventoryLargeChest("Large chest", (IInventory)world.getTileEntity(x - 1, y, z), (IInventory)te);
        if (world.getBlock(x + 1, y, z) == Blocks.chest) return new InventoryLargeChest("Large chest", (IInventory)te, (IInventory)world.getTileEntity(x + 1, y, z));
        if (world.getBlock(x, y, z - 1) == Blocks.chest) return new InventoryLargeChest("Large chest", (IInventory)world.getTileEntity(x, y, z - 1), (IInventory)te);
        if (world.getBlock(x, y, z + 1) == Blocks.chest) return new InventoryLargeChest("Large chest", (IInventory)te, (IInventory)world.getTileEntity(x, y, z + 1));
        return (te instanceof IInventory)? (IInventory)te : null;
    }

    public static IInventory getInventory(World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityChest) return doubleChestFix(tileEntity);
        if (tileEntity instanceof IInventory) return (IInventory)tileEntity;
        return null;
    }

    public static IInventory getInventory(World world, int x, int y, int z, ForgeDirection direction) {
        if (direction != null) {
            x += direction.offsetX;
            y += direction.offsetY;
            z += direction.offsetZ;
        }
        return getInventory(world, x, y, z);

    }

    public static IInventory getInventory(IInventory inventory) {
        if (inventory instanceof TileEntityChest) return doubleChestFix((TileEntity)inventory);
        return inventory;
    }
}
