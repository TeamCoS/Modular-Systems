package com.teambr.modularsystems.core.common.container

import com.teambr.bookshelf.common.container.InventoryCallback
import com.teambr.bookshelf.common.tiles.traits.Inventory
import net.minecraft.block.Block
import net.minecraftforge.items.IItemHandler

/**
  * This file was created for Modular-Systems
  *
  * Modular-Systems is licensed under the
  * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
  * http://creativecommons.org/licenses/by-nc-sa/4.0/
  *
  * @author Paul Davis "pauljoda"
  * @since 3/24/2016
  */
class InventoryBlockConfig(var container : ContainerBlockValueConfig) extends Inventory {
    override def initialSize: Int = 1

    def setContainer(c : ContainerBlockValueConfig) = container = c

    addCallback(new InventoryCallback {
        override def onInventoryChanged(inventory: IItemHandler, slotNumber: Int): Unit = {
            if (container.inventory.getStackInSlot(slotNumber) != null) {
                container.itemName = inventory.getStackInSlot(slotNumber).getDisplayName
                container.currentBlock = Block.getBlockFromItem(inventory.getStackInSlot(slotNumber).getItem)
                container.meta = inventory.getStackInSlot(slotNumber).getItemDamage
            }
        }
    })
}
