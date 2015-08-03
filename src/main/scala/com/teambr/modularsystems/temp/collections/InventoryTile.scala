package com.teambr.modularsystems.temp.collections

import net.minecraft.item.ItemStack
import net.minecraft.nbt.{NBTTagByte, NBTTagCompound, NBTTagList}

import scala.collection.mutable

/**
 * Modular-Systems
 * Created by Dyonovan on 02/08/15
 */

/**
 * Used to create the inventory
 * @param size Here you can set the initial size, if needed
 */
class InventoryTile(var size: Int) {

    var inventory = new mutable.Stack[ItemStack]
    inventory.padTo(size, ItemStack)

    /**
     * Get the stack in a particular slot
     * @param slot The slot to get
     * @return The itemstack in the slot, can be null
     */
    def getStackInSlot(slot: Int): ItemStack = {
        if (slot < inventory.size) inventory(slot)
        else null
    }

    /**
     * Set the stack in a particular slot
     * @param stack The stack to set
     * @param slot The slot to set it in
     */
    def setStackInSlot(stack: ItemStack, slot: Int) {
        if (slot < inventory.size) inventory.update(slot, stack)
    }

    /**
     * Get the size of the inventory
     * @return How big this inventory is
     */
    def getSizeInventory: Int = {
        inventory.size
    }

    /**
     * Get the stack
     * @return The itemstack stack
     */
    def getValues: mutable.Stack[ItemStack] = {
        inventory
    }

    /**
     * Remove all stacks in the stack
     */
    def clear() {
        inventory.clear()
    }

    /**
     * Used to push a stack into the stack
     * @param stack The stack to stack
     */
    def push(stack: ItemStack) {
        inventory.push(stack)
    }

    /**
     * Removes the top of the stack
     * @return The top of the stack, now removed
     */
    def pop: ItemStack = {
        inventory.pop()
    }

    /**
     * Used to load from the NBT tag
     * @param tagCompound The tag to read from
     */
    def readFromNBT(tagCompound: NBTTagCompound) {
        var size = 30
        if (tagCompound.hasKey("Size"))
            size = tagCompound.getInteger("Size")

        val itemsTag = tagCompound.getTagList("Items", 10)
        this.inventory = new mutable.Stack[ItemStack]
        inventory.padTo(size, ItemStack)
        for (i <- 0 to (itemsTag.tagCount() - 1)) {
            val nbtTagCompound1 = itemsTag.getCompoundTagAt(i)
            val nbt = nbtTagCompound1.getTag("Slot")
            var j = -1
            if (nbt.isInstanceOf[NBTTagByte])
                j = nbtTagCompound1.getByte("Slot") & 0xFF
            else
                j = nbtTagCompound1.getShort("Slot")

            if (j >= 0)
                this.inventory.update(j, ItemStack.loadItemStackFromNBT(nbtTagCompound1))
        }
    }

    /**
     * Used to store information on the tag
     * @param tagCompound The tag to write on
     */
    def writeToNBT(tagCompound: NBTTagCompound) {
        val nbtTagList = new NBTTagList
        tagCompound.setInteger("Size", inventory.size)
        for (i <- this.inventory.indices) {
            if (this.inventory(i) != null) {
                val nbtTagCompound1 = new NBTTagCompound
                nbtTagCompound1.setShort("Slot", i.toShort)
                this.inventory(i).writeToNBT(nbtTagCompound1)
                nbtTagList.appendTag(nbtTagCompound1)
            }
        }
        tagCompound.setTag("Items", nbtTagList)
    }
}
