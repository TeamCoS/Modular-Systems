package com.teambr.modularsystems.storage.container

import java.util
import java.util.{ Comparator, Collections }

import com.teambr.bookshelf.common.container.BaseContainer
import com.teambr.modularsystems.storage.tiles.TileStorageCore
import net.minecraft.entity.player.{ EntityPlayer, InventoryPlayer }
import net.minecraft.inventory._
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.CraftingManager
import net.minecraftforge.fml.common.registry.GameRegistry

import scala.collection.mutable.ArrayBuffer

/**
 * Modular-Systems
 * Created by Dyonovan on 04/08/15
 */
class ContainerStorageCore(playerInventory: IInventory, val storageCore: TileStorageCore)
        extends BaseContainer(playerInventory, storageCore) {

    var displaySlots = new util.ArrayList[Slot]
    displaySlots.addAll(inventorySlots.asInstanceOf)
    var allSlots = new util.ArrayList[Slot]
    allSlots.addAll(displaySlots)
    var currentSearch = ""

    var craftingGrid : InventoryCrafting = null
    var craftResult = new InventoryCraftResult

    addInventoryGrid(25, 27, 11, 6)

    if(storageCore.getInventoryRowCount > 6) {
        var slotId = inventorySlots.size()
        for(i <- 66 until storageCore.getSizeInventory()) {
            addSlotToContainer(new RestrictedSlot(inventory, slotId, -10000, -10000))
            slotId += 1
        }
    }

    addPlayerInventorySlots(if(storageCore.hasCraftingUpgrade) 8 else 44, 140)

    if(storageCore.hasCraftingUpgrade) {
        craftingGrid = new DummyCraftingInventory(storageCore, this)
        addCraftingGrid(craftingGrid, 0, 180, 162, 3, 3)
        this.addSlotToContainer(new SlotCrafting(playerInventory.asInstanceOf[InventoryPlayer].player, craftingGrid, craftResult, inventorySize + 1, 198, 140))
        onCraftMatrixChanged(craftingGrid)
    }


    private def addInventoryGrid(xOffset : Int, yOffset : Int, width : Int, rows : Int) : Unit = {
        var slotId = 0
        for(y <- 0 until rows) {
            for(x <- 0 until width) {
                addSlotToContainer(new RestrictedSlot(inventory, slotId, xOffset + x * 18, yOffset + y * 18))
                slotId += 1
            }
        }
    }

    def scrollTo (index : Float) : Unit = {
        val outsideDisplaySize = ((allSlots.size() / 11) + (if(allSlots.size() % 11 > 0 ) 1 else 0)) - 6
        var currentTopRow = Math.round(index * outsideDisplaySize.asInstanceOf[Float])
        if (currentTopRow < 0)
            currentTopRow = 0

        displaySlots.clear()

        for(y <- 0 until 6) {
            for(x <- 0 until 11) {
                if ((x + ((y + currentTopRow) * 11)) <= allSlots.size) {
                    val slot : Slot = this.allSlots.get(x + ((y + currentTopRow) * 11))
                    displaySlots.add(slot)
                }
            }
        }
        updateDisplayedSlots()
    }

    def keyTyped(str : String) {
        currentSearch = str.toLowerCase
        displaySlots.clear()
        displaySlots.addAll(allSlots)
        updateDisplayedSlots()
    }

    def updateDisplayedSlots() : Unit = {
        //Move it all off
        for(i <- 0 until allSlots.size()) {
            val displaySlot = allSlots.get(i)
            displaySlot.xDisplayPosition = -10000
            displaySlot.yDisplayPosition = -10000
        }

        //Trim out what doesn't match the string
        val i = displaySlots.iterator()
        while(i.hasNext) {
            val slot = i.next()
            if(slot.getHasStack) {
                if (currentSearch.startsWith("@")) {
                    val id : GameRegistry.UniqueIdentifier = GameRegistry.findUniqueIdentifierFor(slot.getStack.getItem)
                    if (currentSearch.length > 1 && !id.modId.toLowerCase.contains(currentSearch.split("@")(1).toLowerCase)) i.remove()
                }
                else if (!slot.getStack.getDisplayName.toLowerCase.contains(currentSearch.toLowerCase)) i.remove()
            }
        }

        if(!currentSearch.isEmpty && currentSearch.equalsIgnoreCase(""))
            Collections.sort(displaySlots, new SlotComparator())

        for(y <- 0 until 6) {
            for(x <- 0 until 11) {
                if ((x + (y * 11)) <= displaySlots.size) {
                    val slot : Slot = this.displaySlots.get(x + (y * 11))
                    slot.xDisplayPosition = 25 + x * 18
                    slot.yDisplayPosition = 27 + y * 18
                }
            }
        }
    }

    override def detectAndSendChanges() {
        onCraftMatrixChanged(craftingGrid)
        super.detectAndSendChanges()
    }

    override def onCraftMatrixChanged(inv : IInventory) {
        if (craftingGrid != null && inv == craftingGrid)
            craftResult.setInventorySlotContents(0, CraftingManager.getInstance.findMatchingRecipe(craftingGrid, storageCore.getWorld))
    }

    def addCraftingGrid(inventory : IInventory, startSlot : Int, x : Int, y : Int, width : Int, height : Int) : Unit = {
        var i = 0
        for(h <- 0 until height) {
            for(w <- 0 until width) {
                this.addSlotToContainer(new Slot(inventory, startSlot + i, x + (w * 18), y + (h * 18)))
                i += 1
            }
        }
    }

    def clearCraftingGrid() : Unit = {
        if(storageCore.hasCraftingUpgrade) {
            for(i <- inventorySlots.size() - 10 until inventorySlots.size()) {
                slotClick(i, 0, 1, playerInventory.asInstanceOf[InventoryPlayer].player)
            }
        }
    }

    override def transferStackInSlot(player : EntityPlayer, slotId : Int) : ItemStack = {
        val slot : Slot = inventorySlots.get(slotId).asInstanceOf[Slot]
        if (slot != null && slot.getHasStack) {
            val itemToTransfer : ItemStack = slot.getStack
            val copy : ItemStack = itemToTransfer.copy
            if (slotId < inventorySize) {
                if (!mergeItemStackSafe(itemToTransfer, inventorySize, if (storageCore.hasCraftingUpgrade) inventorySlots.size - 10 else inventorySlots.size, reverse = true)) return null
            }
            else if (slotId >= inventorySlots.size - 10 && storageCore.hasCraftingUpgrade) {
                if (slotId == inventorySlots.size - 1) {
                    if (!mergeItemStackSafe(itemToTransfer, 0, if (storageCore.hasCraftingUpgrade) inventorySlots.size - 10 else inventorySlots.size, reverse = true)) return null
                }
                else {
                    if (!mergeItemStackSafe(itemToTransfer, 0, if (storageCore.hasCraftingUpgrade) inventorySlots.size - 10 else inventorySlots.size, reverse = false)) return null
                }
            }
            else if (!mergeItemStackSafe(itemToTransfer, 0, inventorySize, reverse = false)) return null
            slot.onSlotChange(itemToTransfer, copy)
            slot.onPickupFromSlot(player, itemToTransfer)
            if (itemToTransfer.stackSize == 0) slot.putStack(null)
            else slot.onSlotChanged()
            onCraftMatrixChanged(craftingGrid)
            if (itemToTransfer.stackSize != copy.stackSize) return copy
        }
        null
    }

    class DummyCraftingInventory(val tile : TileStorageCore, val container : ContainerStorageCore)
            extends InventoryCrafting(null, 3, 3) {

        val stacks = new ArrayBuffer[ItemStack](9)

        private def onCraftingChanged() : Unit = container.onCraftMatrixChanged(this)

        override def getSizeInventory : Int = 9

        override def getStackInSlot(slot : Int) : ItemStack =
            if (slot >= getSizeInventory) null else tile.craftingInventory.getStackInSlot(slot)

        override def getStackInRowAndColumn(row : Int, column : Int) : ItemStack = {
            if (row >= 0 && row < 3) {
                val k : Int = row + column * 3
                 getStackInSlot(k)
            }
            else  null
        }

        override def getStackInSlotOnClosing(slot : Int) : ItemStack =
             tile.craftingInventory.getStackInSlot(slot)

        override def decrStackSize(slot : Int, amount : Int) : ItemStack = {
            if (tile.craftingInventory.getStackInSlot(slot) != null) {
                var returnStack : ItemStack = null
                if (tile.craftingInventory.getStackInSlot(slot).stackSize <= amount) {
                    returnStack = tile.craftingInventory.getStackInSlot(slot)
                    tile.craftingInventory.setInventorySlotContents(slot, null)
                    onCraftingChanged()
                    return returnStack
                }
                else {
                    returnStack = tile.craftingInventory.getStackInSlot(slot).splitStack(amount)
                    if (tile.craftingInventory.getStackInSlot(slot).stackSize <= 0) tile.craftingInventory.setInventorySlotContents(slot, null)
                    onCraftingChanged()
                    return returnStack
                }
            }
            null
        }

        override def setInventorySlotContents(slot : Int, stack : ItemStack) {
            tile.craftingInventory.setInventorySlotContents(slot, stack)
            onCraftingChanged()
        }
    }

    protected class SlotComparator extends Comparator[Slot] {
        override def compare(o1 : Slot, o2 : Slot) : Int = {
            val thisSlot : Slot = o1.asInstanceOf[Slot]
            val thatSlot : Slot = o2.asInstanceOf[Slot]
            if (!thisSlot.getHasStack && !thatSlot.getHasStack) return 0
            else if (thisSlot.getHasStack && !thatSlot.getHasStack) return -1
            else if (!thisSlot.getHasStack) return 1
            else if (thisSlot.getStack.getDisplayName.startsWith(currentSearch) && !thatSlot.getStack.getDisplayName.startsWith(currentSearch)) return -1
            else if (!thisSlot.getStack.getDisplayName.startsWith(currentSearch) && thatSlot.getStack.getDisplayName.startsWith(currentSearch)) return 1
            0
        }
    }
}
