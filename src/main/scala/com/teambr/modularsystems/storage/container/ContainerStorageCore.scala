package com.teambr.modularsystems.storage.container

import com.teambr.bookshelf.common.container.slots.IPhantomSlot
import com.teambr.bookshelf.util.InventoryUtils
import com.teambr.modularsystems.storage.container.slot.SlotStorageCore
import com.teambr.modularsystems.storage.tiles.TileStorageCore
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.{ClickType, Container, IInventory, Slot}
import net.minecraft.item.ItemStack

import scala.collection.JavaConversions._

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

    var INVENTORY_START = 0
    var INVENTORY_END = 0

    var PLAYER_INV_START_MAIN = 0
    var PLAYER_INV_END_MAIN = 0
    var PLAYER_INV_START_HOTBAR = 0
    var PLAYER_INV_END_HOTBAR = 0

    addInventoryGrid(25, 27, 11, 6)

    addPlayerInventorySlots(140)

    /**
      * Adds an inventory grid to the container
      *
      * @param xOffset X pixel offset
      * @param yOffset Y pixel offset
      * @param width How many wide
      */
    def addInventoryGrid(xOffset : Int, yOffset : Int, width : Int, height : Int) : Unit = {
        INVENTORY_START = 0
        var slotId = -1
        for(y <- 0 until height) {
            for(x <- 0 until width) {
                slotId += 1
                addSlotToContainer(new SlotStorageCore(storageCore, slotId, xOffset + x * 18, yOffset + y * 18))
            }
        }
        INVENTORY_END = slotId
    }

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
        PLAYER_INV_START_MAIN = INVENTORY_END + 1
        PLAYER_INV_END_MAIN = PLAYER_INV_START_MAIN
        for (row <- 0 until 3) {
            for (column <- 0 until 9) {
                addSlotToContainer(new Slot(playerInventory,
                    column + row * 9 + 9,
                    offsetX + column * 18,
                    offsetY + row * 18))
                PLAYER_INV_END_MAIN += 1
            }
        }

        PLAYER_INV_START_HOTBAR = PLAYER_INV_END_MAIN + 1
        PLAYER_INV_END_HOTBAR = PLAYER_INV_START_HOTBAR
        for (slot <- 0 until 9) {
            addSlotToContainer(new Slot(playerInventory, slot, offsetX + slot * 18, offsetY + 58))
            PLAYER_INV_END_HOTBAR += 1
        }
    }

    override def canInteractWith(entityPlayer: EntityPlayer): Boolean = true

    override def func_184996_a(slotId : Int, dragType: Int, clickTypeIn: ClickType, player: EntityPlayer) : ItemStack = {
        if(!storageCore.getWorld.isRemote && slotId >= 0 && slotId < inventorySlots.size()) {
            val slot = inventorySlots.get(slotId)
            // Has something to drop
            if (slot != null && slot.isInstanceOf[SlotStorageCore] && player.inventory.getItemStack != null) {
                val stack = storageCore.insertItem(-1, player.inventory.getItemStack, simulate = false)
                player.inventory.setItemStack(stack)
                storageCore.markForUpdate()
                return stack
            }

            // Trying to pick up something
            if(slot != null && slot.isInstanceOf[SlotStorageCore] &&
                    slotId >= 0 && slotId < storageCore.keysToList.size()) {
                var stack = storageCore.keysToList.get(slotId)
                stack = storageCore.extractItem(slotId, stack.getMaxStackSize, simulate = false)
                player.inventory.setItemStack(stack)
                storageCore.markForUpdate()
                return stack
            }
            else if(slot != null && !slot.isInstanceOf[SlotStorageCore])
                return super.func_184996_a(slotId, dragType, clickTypeIn, player)
            null
        } else super.func_184996_a(slotId, dragType, clickTypeIn, player)
    }

    def updateSlots() : Unit = {
        for(slot <- inventorySlots) {
            slot match {
                case storage : SlotStorageCore =>
                    storage.updateStackInformation()
                case _ =>
            }
        }
    }

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
            if (slotId >= PLAYER_INV_START_HOTBAR &&
                    !mergeItemStackSafe(itemToTransfer, PLAYER_INV_START_MAIN, PLAYER_INV_END_MAIN, reverse = false)) return null
            else if (slotId < PLAYER_INV_START_HOTBAR && slotId > INVENTORY_END
                    && !mergeItemStackSafe(itemToTransfer, PLAYER_INV_START_HOTBAR, PLAYER_INV_END_HOTBAR, reverse = false)) return null
            if (itemToTransfer.stackSize == 0) slot.putStack(null)
            else slot.onSlotChanged()
            if (itemToTransfer.stackSize != copy.stackSize) return copy
        }
        null
    }
}