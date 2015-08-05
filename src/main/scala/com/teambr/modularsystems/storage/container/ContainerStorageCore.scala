package com.teambr.modularsystems.storage.container

import java.util

import com.google.common.collect.Lists
import com.teambr.bookshelf.common.container.BaseContainer
import com.teambr.modularsystems.storage.tiles.TileStorageCore
import net.minecraft.inventory.{IInventory, Slot}

import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer

/**
 * Modular-Systems
 * Created by Dyonovan on 04/08/15
 */
class ContainerStorageCore(playerInventory: IInventory, ownerInventory: IInventory)
        extends BaseContainer(playerInventory, ownerInventory){

    var displaySlots = new Lists.newArrayList;
    var allSlots = new ListBuffer[Slot]
    var currentSearch: String = ""

    {
        addInventoryGrid(25, 27, 11)
        displaySlots.addAll(inventorySlots)
        allSlots.addAll(displaySlots)
        addPlayerInventorySlots(44, 140)

    }

}
