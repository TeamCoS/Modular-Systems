package com.teambr.modularsystems.storage.container

import com.teambr.bookshelf.common.container.BaseContainer
import com.teambr.modularsystems.storage.tiles.TileStorageCore
import net.minecraft.inventory.{IInventory, Slot}

import scala.collection.mutable.ListBuffer

/**
 * Modular-Systems
 * Created by Dyonovan on 04/08/15
 */
class ContainerStorageCore(playerInventory: IInventory, val storageCore: TileStorageCore)
        extends BaseContainer(playerInventory, storageCore) {

   // var displaySlots = new Lists.newArrayList;
    var allSlots = new ListBuffer[Slot]
    var currentSearch: String = ""

    {
        addInventoryGrid(25, 27, 11)
        //displaySlots.addAll(inventorySlots)
       // allSlots.addAll(displaySlots)
        addPlayerInventorySlots(44, 140)

    }

}
