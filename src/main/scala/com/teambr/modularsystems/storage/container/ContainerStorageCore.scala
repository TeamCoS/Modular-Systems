package com.teambr.modularsystems.storage.container

import com.teambr.bookshelf.common.container.slots.IPhantomSlot
import com.teambr.bookshelf.util.InventoryUtils
import com.teambr.modularsystems.storage.container.slot.{SlotCraftingOutput, SlotStorageCore}
import com.teambr.modularsystems.storage.tiles.TileStorageCore
import net.minecraft.entity.player.{InventoryPlayer, EntityPlayer}
import net.minecraft.inventory._
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.CraftingManager
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.oredict.OreDictionary

import scala.collection.mutable.ArrayBuffer
import scala.util.control.Breaks._

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

    var CRAFTING_GRID_START = 0
    var CRAFTING_GRID_END = 0

    var PLAYER_INV_START_MAIN = 0
    var PLAYER_INV_END_MAIN = 0
    var PLAYER_INV_START_HOTBAR = 0
    var PLAYER_INV_END_HOTBAR = 0

    var rowStart = 0

    var filterString : String = ""
    lazy val listedItems = new java.util.ArrayList[ItemStack]()

    addInventoryGrid(25, 27, 11, rowCount)

    lazy val craftMatrix = new InventoryCrafting(this, 3, 3)
    lazy val craftResult = new InventoryCraftResult

    if(storageCore.hasCraftingUpgrade)
        addCraftingGrid(98, 102, 3, 3)
    else
        CRAFTING_GRID_END = INVENTORY_END

    updateSlots()

    addPlayerInventorySlots(44, 160)

    def rowCount : Int = if(storageCore.hasCraftingUpgrade) 4 else 6

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
                addSlotToContainer(new SlotStorageCore(this, storageCore, slotId, xOffset + x * 18, yOffset + y * 18))
            }
        }
        INVENTORY_END = slotId
    }

    /**
      * Adds an inventory grid to the container
      *
      * @param xOffset X pixel offset
      * @param yOffset Y pixel offset
      * @param width How many wide
      */
    def addCraftingGrid(xOffset : Int, yOffset : Int, width : Int, height : Int) : Unit = {
        CRAFTING_GRID_START = INVENTORY_END + 1
        var slotId = -1
        for(y <- 0 until height) {
            for(x <- 0 until width) {
                slotId += 1
                addSlotToContainer(new Slot(this.craftMatrix, slotId, xOffset + x * 18, yOffset + y * 18))
            }
        }

        addSlotToContainer(new SlotCraftingOutput(playerInventory.asInstanceOf[InventoryPlayer].player,
            this.craftMatrix, this.craftResult, storageCore, 0, 171, 120))
        CRAFTING_GRID_END = CRAFTING_GRID_START + slotId + 1
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
        PLAYER_INV_START_MAIN = CRAFTING_GRID_END + 1
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

        PLAYER_INV_START_HOTBAR = PLAYER_INV_END_MAIN
        PLAYER_INV_END_HOTBAR = PLAYER_INV_START_HOTBAR
        for (slot <- 0 until 9) {
            addSlotToContainer(new Slot(playerInventory, slot, offsetX + slot * 18, offsetY + 58))
            PLAYER_INV_END_HOTBAR += 1
        }
    }

    override def canInteractWith(entityPlayer: EntityPlayer): Boolean = true
    /**
      * Looks for changes made in the container, sends them to every listener.
      */
    override def detectAndSendChanges() : Unit = {
        updateSlots()
        this.craftResult.setInventorySlotContents(0,
            CraftingManager.getInstance.findMatchingRecipe(this.craftMatrix, this.storageCore.getWorld))
    }

    override def func_184996_a(slotId : Int, dragType: Int, clickTypeIn: ClickType, player: EntityPlayer) : ItemStack = {
        if(slotId >= 0 && slotId < inventorySlots.size()) {
            val slot = inventorySlots.get(slotId)
            // Is a storage slot
            if(slot != null && slot.isInstanceOf[SlotStorageCore]) {
                // Wanting to quick move
                if(clickTypeIn == ClickType.QUICK_MOVE && slotId >= 0 && slotId < listedItems.size() && listedItems.get(slotId) != null) {
                    var stack = listedItems.get(slotId)
                    stack = storageCore.getStack(stack)
                    var i = 0
                    val list = storageCore.keysToList
                    breakable {
                        for (x <- 0 until list.size()) {
                            if (list.get(x) == stack) {
                                break
                            }
                            i += 1
                        }
                    }
                    stack = storageCore.getStack(stack)
                    val amount = storageCore.getInventory.get(stack)
                    stack = stack.copy()
                    stack.stackSize = amount
                    stack.stackSize = if(stack.stackSize >= stack.getMaxStackSize) stack.getMaxStackSize else stack.stackSize
                    if(stack != null && i < storageCore.getInventory.size()) {
                        val copy = stack.copy()
                        // Something was moved
                        if (mergeItemStackSafe(copy, PLAYER_INV_START_MAIN, PLAYER_INV_END_HOTBAR, reverse = true)) {
                            player.inventoryContainer.detectAndSendChanges()
                            player.inventory.markDirty()
                            storageCore.extractItem(i, stack.stackSize - copy.stackSize, simulate = false)
                        }
                    }
                    return null
                }

                // Has something to drop
                if (player.inventory.getItemStack != null) {
                    val stack = storageCore.insertItem(-1, player.inventory.getItemStack, simulate = false)
                    player.inventory.setItemStack(stack)
                    storageCore.markForUpdate()
                    return stack
                }

                // Trying to pick up something
                if (slotId >= 0 && slotId < listedItems.size() && listedItems.get(slotId) != null) {
                    var stack = listedItems.get(slotId)
                    stack = storageCore.getStack(stack)
                    var i = 0
                    val list = storageCore.keysToList
                    breakable {
                        for (x <- 0 until list.size()) {
                            if (list.get(x) == stack) {
                                break
                            }
                            i += 1
                        }
                    }
                    if(stack != null && i < storageCore.getInventory.size()) {
                        stack = storageCore.extractItem(i, stack.getMaxStackSize, simulate = false)
                        player.inventory.setItemStack(stack)
                        storageCore.markForUpdate()
                    }
                    return stack
                }
            }
            else if(slot != null && !slot.isInstanceOf[SlotStorageCore]) {
                val value =  super.func_184996_a(slotId, dragType, clickTypeIn, player)
                return value
            }
            null
        } else
            super.func_184996_a(slotId, dragType, clickTypeIn, player)
    }

    def updateSlots(): Unit = {
        listedItems.clear()
        storageCore.updateCachedSize()
        val list = storageCore.keysToList

        // Add valid
        for(x <- 0 until PLAYER_INV_START_MAIN) {
            if(list.size() > x + (rowStart * 11) && isValidForFilter(list.get(x + (rowStart * 11))))
                listedItems.add(list.get(x + (rowStart * 11)))
        }

        // Pad with nulls
        for(i <- listedItems.size() until PLAYER_INV_START_MAIN)
            listedItems.add(null)
    }

    def isValidForFilter(stack : ItemStack) : Boolean = {
        if(stack == null || stack.getItem == null || filterString.isEmpty)
            true
        else {
            filterString.charAt(0) match {
                case '@' => // Matching by mod
                    if(filterString.length > 1) {
                        val uniqueName = stack.getItem.getRegistryName
                        val modName = uniqueName.split(':')(0)
                        modName.toLowerCase.contains(filterString.substring(1).toLowerCase)
                    } else
                        true
                case '$' => // Ore Dict
                    if(filterString.length > 1) {
                        val oreDicts = OreDictionary.getOreIDs(stack)

                        // Go though all registered IDS
                        for(oreDictID <- oreDicts) {
                            val oreName = OreDictionary.getOreName(oreDictID)
                            if(oreName.toLowerCase.contains(filterString.substring(1).toLowerCase))
                                return true
                        }

                        false
                    }
                    else true
                case _ =>
                    stack.getDisplayName.toLowerCase.contains(filterString.toLowerCase)
            }
        }
    }

    def scrollTo(index : Float) : Unit = {
        val outsideDisplaySize = ((storageCore.getInventory.size() / 11) + (if(storageCore.getInventory.size() % 11 > 0)  1 else 0)) - 6
        this.rowStart = Math.round(index * outsideDisplaySize.toFloat)
        if(rowStart < 0)
            rowStart = 0
    }

    /**
      * Returns true if the player can "drag-spilt" items into this slot,. returns true by default. Called to check if
      * the slot can be added to a list of Slots to split the held ItemStack across.
      */
    override def canDragIntoSlot(slotIn: Slot) : Boolean = {
        !slotIn.isInstanceOf[SlotStorageCore]
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
        if (slot != null && slot.getHasStack && !slot.isInstanceOf[SlotStorageCore]) { // Not the inventory
        var itemToTransfer: ItemStack = slot.getStack
            val copy: ItemStack = itemToTransfer.copy

            // Attempt from outside into inventory
            val stackLeft = storageCore.insertItem(-1, itemToTransfer, simulate = false)
            // If a change happened, update the held stack
            if(stackLeft == null || stackLeft.stackSize != itemToTransfer.stackSize)
                itemToTransfer = stackLeft

            if (itemToTransfer != null && slotId >= PLAYER_INV_START_HOTBAR && // From main inv into hotbar
                    !mergeItemStackSafe(itemToTransfer, PLAYER_INV_START_MAIN, PLAYER_INV_END_MAIN, reverse = false)) return null
            else if (itemToTransfer != null && slotId < PLAYER_INV_START_HOTBAR && slotId > INVENTORY_END // From hotbar into main inv
                    && !mergeItemStackSafe(itemToTransfer, PLAYER_INV_START_HOTBAR, PLAYER_INV_END_HOTBAR, reverse = false)) return null
            if (itemToTransfer == null || itemToTransfer.stackSize == 0) slot.putStack(null)
            else slot.onSlotChanged()
            if (itemToTransfer == null || itemToTransfer.stackSize != copy.stackSize) return copy
        }
        null
    }

    /**
      * Clears the crafting grid and returns true if gone
      *
      * @return
      */
    def clearCraftingGrid : Boolean = {
        if(storageCore.hasCraftingUpgrade) {
            for (x <- CRAFTING_GRID_START to CRAFTING_GRID_END) {
                func_184996_a(x, 0, ClickType.QUICK_MOVE, playerInventory.asInstanceOf[InventoryPlayer].player)
            }
        }

        for(i <- 0 until this.craftMatrix.getSizeInventory)
            if(this.craftMatrix.getStackInSlot(i) != null)
                return false
        true
    }

    /**
      * Fills the crafting grid with the info sent by JEI
      *
      * @param recipe The recipe NBTTagCompound
      */
    def fillCraftingGrid(recipe : NBTTagCompound): Unit = {
        if(storageCore.hasCraftingUpgrade) {
            // Try and clear grid
            if(clearCraftingGrid) { // Grid cleared continue
            val recipeBuffer = new ArrayBuffer[ArrayBuffer[ItemStack]]()
                // Create the slots objects
                for(x <- 0 until 9) {
                    // Get the list at the location
                    val list = recipe.getTagList("Stack:" + x, 10)

                    // If possible, read from the list
                    if(list != null && list.tagCount() > 0) {
                        // Create buffer that holds object
                        val buffer = new ArrayBuffer[ItemStack]()

                        // Iterate the list and add to held buffer
                        for(i <- 0 until list.tagCount())
                            buffer += ItemStack.loadItemStackFromNBT(list.getCompoundTagAt(i))

                        // Put slot stacks
                        recipeBuffer += buffer
                    } else // Nothing, put null to move around
                        recipeBuffer += new ArrayBuffer[ItemStack]()
                }

                // Try to fill
                for(x <- 0 until 9) {
                    val recipeStacks = recipeBuffer(x)
                    if(recipeStacks != null) {
                        for(stack <- recipeStacks) {
                            if (this.craftMatrix.getStackInSlot(x) == null && stack != null) {
                                // Get the key
                                var extractStack = storageCore.getStack(stack)

                                // If we have extracted something
                                if (extractStack != null) {
                                    var i = 0
                                    val list = storageCore.keysToList
                                    breakable {
                                        for (x <- 0 until list.size()) {
                                            if (list.get(x) == extractStack) {
                                                break
                                            }
                                            i += 1
                                        }
                                    }
                                    if (i < storageCore.getInventory.size()) {
                                        extractStack = storageCore.extractItem(i, stack.stackSize, simulate = false)
                                        this.craftMatrix.setInventorySlotContents(x, extractStack)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override def onContainerClosed(playerIn: EntityPlayer): Unit = {
        var i = 0
        while(i < this.craftMatrix.getSizeInventory) {
            val stack = storageCore.insertItem(-1, this.craftMatrix.getStackInSlot(i), simulate = false)
            if(stack != null)
                playerIn.dropPlayerItemWithRandomChoice(stack, false)
            i += 1
        }
    }
}