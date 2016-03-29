package com.teambr.modularsystems.storage.tiles

import java.util
import java.util.{Comparator, Collections}

import com.teambr.bookshelf.common.tiles.traits.Syncable
import com.teambr.modularsystems.core.functions.ItemSorter
import com.teambr.modularsystems.storage.collections.StorageNetwork
import net.minecraft.item.ItemStack
import net.minecraft.nbt.{NBTTagCompound, NBTTagList}
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.items.{CapabilityItemHandler, IItemHandler}

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
class TileStorageCore extends Syncable with IItemHandler {

    var network = new StorageNetwork

    /**
      * Used to remove a node from the network
      * @param node The node to remove
      * @return True if found and removed
      */
    def deleteFromNetwork(node : TileStorageExpansion) : Boolean = {
        network.deleteNode(node)
    }

    /**
      * Used to get the storage network
      * @return The network of this tile
      */
    def getNetwork : StorageNetwork = network

    /**
      * Used to destroy the network
      */
    def destroyNetwork() : Unit = {
        network.destroyNetwork(worldObj)
        network = null
    }

    /*******************************************************************************************************************
      * IItemHandler                                                                                                   *
      ******************************************************************************************************************/

    // The actual inventory
    private var inventory = new util.LinkedHashMap[ItemStack, Int]()

    // Converts the keyset into a list
    def keysToList = new util.ArrayList[ItemStack](inventory.keySet())

    // Cached size
    private var cachedSize = 0

    // Maximum item count
    private def maxItems = Integer.MAX_VALUE

    // How many slots are allowed for this tile
    private var currentSlots = 60

    /**
      * Used to get the instance of the inventory
      *
      * @return The inventory map
      */
    def getInventory : util.LinkedHashMap[ItemStack, Int] = inventory

    /**
      * Used to get how many slots are available
      *
      * @return How many slots this has
      */
    def getCurrentSlots : Int = 60

    /**
      * Used to get how many items can still fit
      *
      * @return
      */
    def getAmountRemaining : Int = maxItems - cachedSize

    /**
      * Used to get the stack object out of the inventory
      *
      * @param stack The stack to look for
      * @return Returns out stack, meaning it found something and also works as the key to the inventory, null if not found
      */
    def getStack(stack : ItemStack, slot : Int = -1) : ItemStack = {
        if(slot == -1) {
            for (ourStack <- inventory.keySet()) {
                if (ourStack.getItem == stack.getItem &&
                        ourStack.getItemDamage == stack.getItemDamage &&
                        ItemStack.areItemStackTagsEqual(ourStack, stack))
                    return ourStack
            }
            null
        } else {
            val list = keysToList
            if(slot >= list.size())
                null
            else
                list.get(slot)
        }
    }

    /**
      * Used to update how many items we have cached
      */
    def updateCachedSize(): Unit = {
        cachedSize = 0 // Reset the size
        for(stack <- inventory.keySet()) {
            stack.stackSize = inventory.get(stack) // Set stack to same size, useful for a few reasons
            cachedSize += inventory.get(stack) // Update the cached size
        }

        //TODO: Add different modes
        sortInventory(0)
        markForUpdate() // Send information to client
    }

    /**
      * Used to sort the inventory. Int mode for how to do
      *
      * @param mode
      *             0: By size
      */
    def sortInventory(mode : Int): Unit = {
        mode match  {
            case 0 =>
                // Convert our map into a list
                val list = new util.LinkedList[java.util.Map.Entry[ItemStack, Int]](inventory.entrySet())

                // Sort the list
                Collections.sort(list, new Comparator[java.util.Map.Entry[ItemStack, Int]] {
                    override def compare(o1: java.util.Map.Entry[ItemStack, Int], o2: java.util.Map.Entry[ItemStack, Int]): Int =
                        ItemSorter.INSTANCE.compare(o1.getKey, o2.getKey)
                })

                // Convert back into map
                val newInventory = new util.LinkedHashMap[ItemStack, Int]()
                val iterator = list.iterator()
                while(iterator.hasNext) {
                    val entry = iterator.next()
                    newInventory.put(entry.getKey, entry.getValue)
                }

                // Reassign
                inventory = newInventory

            case _ =>
        }
    }

    /**
      * Returns the number of slots available
      *
      * @return The number of slots available
      */
    override def getSlots: Int = currentSlots

    /**
      * Returns the ItemStack in a given slot.
      *
      * The result's stack size may be greater than the itemstacks max size.
      *
      * If the result is null, then the slot is empty.
      * If the result is not null but the stack size is zero, then it represents
      * an empty slot that will only accept* a specific itemstack.
      *
      * <p/>
      * IMPORTANT: This ItemStack MUST NOT be modified. This method is not for
      * altering an inventories contents. Any implementers who are able to detect
      * modification through this method should throw an exception.
      * <p/>
      * SERIOUSLY: DO NOT MODIFY THE RETURNED ITEMSTACK
      *
      * @param slot Slot to query
      * @return ItemStack in given slot. May be null.
      */
    override def getStackInSlot(slot: Int): ItemStack = {
        // Make sure we are within limits
        if(slot >= inventory.keySet().size())
            throw new ArrayIndexOutOfBoundsException("Attempted to access slot outside of bounds "
                    + slot + " with inventory of size " + inventory.keySet().size())

        keysToList.get(slot)
    }

    /**
      * Inserts an ItemStack into the given slot and return the remainder.
      * The ItemStack should not be modified in this function!
      * Note: This behaviour is subtly different from IFluidHandlers.fill()
      *
      * @param slot     Slot to insert into, -1 just inserts no problem
      * @param stack    ItemStack to insert.
      * @param simulate If true, the insertion is only simulated
      * @return The remaining ItemStack that was not inserted (if the entire stack is accepted, then return null).
      *         May be the same as the input ItemStack if unchanged, otherwise a new ItemStack.
      */
    override def insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack = {
        // Safe check
        if(stack == null) return null

        // Find out if we have this stack already
        val ourKey = getStack(stack, -1)
        if(ourKey != null) { // We already have an instance of this stack
        // How much we can fit
        val insertAmount = Math.min(stack.getMaxStackSize, maxItems - cachedSize)
            if(stack.stackSize <= insertAmount) {
                if(!simulate) {
                    val copiedAmount = stack.stackSize
                    inventory.put(ourKey, inventory.get(ourKey) + copiedAmount)
                    updateCachedSize()
                }
                null
            } else {
                if(!simulate) {
                    val stackCopy = stack.splitStack(insertAmount)
                    inventory.put(ourKey, inventory.get(ourKey) + stackCopy.stackSize)
                    updateCachedSize()
                    stack
                } else {
                    stack.stackSize -= insertAmount
                    updateCachedSize()
                    stack
                }
            }
        } else {
            val insertAmount = Math.min(stack.getMaxStackSize, maxItems - cachedSize)
            if(insertAmount < stack.stackSize) {
                if(!simulate) {
                    val insertStack = stack.splitStack(insertAmount)
                    if(insertStack.stackSize > 0)
                        inventory.put(insertStack, insertStack.stackSize)
                    updateCachedSize()
                    stack
                } else {
                    stack.stackSize -= insertAmount
                    updateCachedSize()
                    stack
                }
            } else {
                if(!simulate) {
                    inventory.put(stack.copy(), stack.stackSize)
                    updateCachedSize()
                }
                null
            }
        }
    }

    /**
      * Extracts an ItemStack from the given slot. The returned value must be null
      * if nothing is extracted, otherwise it's stack size must not be greater than amount or the
      * itemstacks getMaxStackSize().
      *
      * @param slot  Slot to extract from.
      * @param amount       Amount to extract (may be greater than the current stacks max limit)
      * @param simulate     If true, the extraction is only simulated
      * @return             ItemStack extracted from the slot, must be null, if nothing can be extracted
      */
    override def extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack = {
        if(amount == 0) return null

        val list = keysToList
        if(slot >= list.size()) return null

        val ourKey = list.get(slot)

        if(ourKey == null) return null

        if(simulate) {
            if(inventory.get(ourKey) < amount) {
                val returnStack = ourKey.copy()
                ourKey.stackSize = inventory.get(ourKey)
                updateCachedSize()
                returnStack
            } else {
                val returnStack = ourKey.copy()
                ourKey.stackSize = amount
                updateCachedSize()
                returnStack
            }
        } else {
            val min = Math.min(inventory.get(ourKey), amount) // How much we can remove
            val listAmount = inventory.get(ourKey) // The last value
            inventory.put(ourKey, inventory.get(ourKey) - min) // Change our amount
            var returnStack = ourKey.copy() // Copy the stack
            val loweredSize = listAmount - inventory.get(ourKey) // Get the new size value
            returnStack.stackSize = loweredSize // Set the size to the new amount
            if(inventory.get(ourKey) == 0) // If we drain the inventory, update
                inventory.remove(ourKey)

            // Null stack
            if(returnStack.stackSize == 0)
                returnStack = null

            updateCachedSize()
            returnStack
        }
    }

    /*******************************************************************************************************************
      * Tile Methods                                                                                                   *
      ******************************************************************************************************************/

    /**
      * Checks if we have a certain capability
      *
      * @param capability The capability to check for
      * @param facing Which face, can be different by face
      * @return True if we have the said capability
      */
    override def hasCapability(capability: Capability[_], facing : EnumFacing) = {
        capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
    }

    /**
      * Used to get an instance of a capability
      *
      * @param capability The capability
      * @param facing Which face
      * @tparam T The object to return
      * @return The capability if valid
      */
    override def getCapability[T](capability: Capability[T], facing: EnumFacing): T = {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            this.asInstanceOf[T] else
            super.getCapability[T](capability, facing)
    }

    private lazy val TAG_LIST_LOCATION = "InventoryTags"

    /**
      * Used to save the inventory to an NBT tag
      *
      * @param tag The tag to save to
      */
    override def writeToNBT(tag : NBTTagCompound) : Unit = {
        super[TileEntity].writeToNBT(tag)

        // Send Current Amount
        tag.setInteger("CurrentSlots", currentSlots)
        tag.setInteger("CachedSize",   cachedSize)

        // Write inventory
        val stackTagList = new NBTTagList
        for(stack <- inventory.keySet()) {
            val tagList = new NBTTagList
            val stackTag = new NBTTagCompound
            stack.writeToNBT(stackTag)
            stackTag.setInteger("Amount", inventory.get(stack))
            tagList.appendTag(stackTag)
            stackTagList.appendTag(tagList)
        }
        tag.setTag(TAG_LIST_LOCATION, stackTagList)
    }

    /**
      * Used to read the inventory from an NBT tag compound
      *
      * @param tag The tag to read from
      */
    override def readFromNBT(tag : NBTTagCompound) : Unit = {
        super[TileEntity].readFromNBT(tag)

        // Read Value
        currentSlots = tag.getInteger("CurrentSlots")
        cachedSize   = tag.getInteger("CachedSize")

        // Read Inventory
        inventory.clear()
        val stackTagList = tag.getTagList(TAG_LIST_LOCATION, 9)
        if(stackTagList != null) {
            for (i <- 0 until stackTagList.tagCount()) {
                val localList = stackTagList.get(i).asInstanceOf[NBTTagList]
                val stackCompound = localList.getCompoundTagAt(0)
                val stack = ItemStack.loadItemStackFromNBT(stackCompound)
                val amount = stackCompound.getInteger("Amount")
                inventory.put(stack, amount)
            }
        }
    }

    /*******************************************************************************************************************
      * Syncable                                                                                                       *
      ******************************************************************************************************************/

    /**
      * Used to set a variable, the packet to sync values will call this method
      *
      * @param id The variable ID, these should be constant values defined by the tile
      * @param value The new value to set to, use it how you wish per case
      */
    override def setVariable(id: Int, value: Double): Unit = {}

    /**
      * Gets the variable, also forces the value to sync
      *
      * @param id The variable id
      * @return The value from the other side, will sync values and return the new one
      */
    override def getVariable(id: Int): Double = { 0.0 }
}
