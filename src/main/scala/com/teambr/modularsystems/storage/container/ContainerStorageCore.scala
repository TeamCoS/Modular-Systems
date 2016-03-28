package com.teambr.modularsystems.storage.container

import com.teambr.bookshelf.common.container.slots.IPhantomSlot
import com.teambr.bookshelf.util.InventoryUtils
import com.teambr.modularsystems.storage.container.slot.SlotStorageCore
import com.teambr.modularsystems.storage.tiles.TileStorageCore
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.{Container, IInventory, Slot}
import net.minecraft.item.ItemStack

/**
  * This file was created for Modular-Systems
  *
  * Modular-Systems is licensed under the
  * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
  * http://creativecommons.org/licenses/by-nc-sa/4.0/
  *
  * @author Paul Davis "pauljoda"
  * @since 3/27/2016
  */
class ContainerStorageCore(val playerInventory: IInventory, val storageCore: TileStorageCore) extends Container {

    addSlotToContainer(new SlotStorageCore(storageCore, 0, 8, 16))
    addPlayerInventorySlots(140)

    /**
      * Adds the player offset with Y offset
      *
      * @param offsetY How far down
      */
    def addPlayerInventorySlots(offsetY: Int) {
        addPlayerInventorySlots(8, offsetY)
    }

    /**
      * Adds player inventory at location, includes space between normal and hotbar
      *
      * @param offsetX X offset
      * @param offsetY Y offset
      */
    def addPlayerInventorySlots(offsetX: Int, offsetY: Int): Unit = {
        for (row <- 0 until 3) {
            for (column <- 0 until 9) {
                addSlotToContainer(new Slot(playerInventory,
                    column + row * 9 + 9,
                    offsetX + column * 18,
                    offsetY + row * 18))
            }
        }

        for (slot <- 0 until 9) {
            addSlotToContainer(new Slot(playerInventory, slot, offsetX + slot * 18, offsetY + 58))
        }
    }

    override def canInteractWith(entityPlayer: EntityPlayer): Boolean = true

    protected def mergeItemStackSafe(stackToMerge: ItemStack, start: Int, stop: Int, reverse: Boolean): Boolean = {
        var inventoryChanged: Boolean = false
        val delta: Int = if (reverse) -1 else 1
        val slots: java.util.List[Slot] = inventorySlots
        if (stackToMerge.isStackable) {
            var slotId: Int = if (reverse) stop - 1 else start
            while (stackToMerge.stackSize > 0 && ((!reverse && slotId < stop) || (reverse && slotId >= start))) {
                val slot: Slot = slots.get(slotId)
                if (slot.isItemValid(stackToMerge) && !slot.isInstanceOf[IPhantomSlot]) {
                    val stackInSlot: ItemStack = slot.getStack
                    if (InventoryUtils.tryMergeStacks(stackToMerge, stackInSlot)) {
                        slot.onSlotChanged()
                        inventoryChanged = true
                    }
                }
                slotId += delta
            }
        }
        if (stackToMerge.stackSize > 0) {
            var slotId: Int = if (reverse) stop - 1 else start
            while ((!reverse && slotId < stop) || (reverse && slotId >= start)) {
                val slot: Slot = slots.get(slotId)
                if (slot.isItemValid(stackToMerge) && !slot.isInstanceOf[IPhantomSlot]) {
                    val stackInSlot: ItemStack = slot.getStack
                    if (stackInSlot == null) {
                        slot.putStack(stackToMerge.copy)
                        slot.onSlotChanged()
                        stackToMerge.stackSize = 0
                        return true
                    }
                }
                slotId += delta
            }
        }
        inventoryChanged
    }

    override def transferStackInSlot(player: EntityPlayer, slotId: Int): ItemStack = {
        val slot: Slot = inventorySlots.get(slotId)
        if (slot != null && slot.getHasStack) {
            val itemToTransfer: ItemStack = slot.getStack
            val copy: ItemStack = itemToTransfer.copy
            println(slotId)
            if (slotId <= 9 * 3 - 1 && !mergeItemStackSafe(itemToTransfer, 9 * 3, 9 * 4, reverse = false)) return null
            else if (slotId > 9 * 3 - 1 && !mergeItemStackSafe(itemToTransfer, 0, inventorySlots.size() - 9, reverse = false)) return null
            if (itemToTransfer.stackSize == 0) slot.putStack(null)
            else slot.onSlotChanged()
            if (itemToTransfer.stackSize != copy.stackSize) return copy
        }
        null
    }
}