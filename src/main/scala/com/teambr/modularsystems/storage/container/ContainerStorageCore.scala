package com.teambr.modularsystems.storage.container

import java.util

import com.teambr.bookshelf.common.container.BaseContainer
import com.teambr.modularsystems.storage.tiles.TileStorageCore
import net.minecraft.inventory.{ InventoryCraftResult, InventoryCrafting, IInventory, Slot }

/**
 * Modular-Systems
 * Created by Dyonovan on 04/08/15
 */
class ContainerStorageCore(playerInventory: IInventory, val storageCore: TileStorageCore)
        extends BaseContainer(playerInventory, storageCore) {

    var displaySlots = new util.ArrayList[Slot]
    displaySlots.addAll(inventorySlots[_ <: Slot])
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

    class DummyCraftingInventory(tile : TileStorageCore, container : ContainerStorageCore)
            extends InventoryCrafting(null, 3, 3) {

    }
}
