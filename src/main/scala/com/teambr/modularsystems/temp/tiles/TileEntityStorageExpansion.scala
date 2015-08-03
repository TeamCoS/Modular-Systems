package com.teambr.modularsystems.temp.tiles

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.IChatComponent

/**
 * Modular-Systems
 * Created by Dyonovan on 02/08/15
 */
class TileEntityStorageExpansion extends BaseTile with IInventory {

    def removeFromNetwork(deleteSelf: Boolean): Unit = {

    }

    /********************************************************************************************************************
      ******************************************* IInventory Methods ******************************************************
      *********************************************************************************************************************/

    override def decrStackSize(index: Int, count: Int): ItemStack = ???

    override def closeInventory(player: EntityPlayer): Unit = ???

    override def getSizeInventory: Int = ???

    override def getInventoryStackLimit: Int = ???

    override def clear(): Unit = ???

    override def isItemValidForSlot(index: Int, stack: ItemStack): Boolean = ???

    override def getStackInSlotOnClosing(index: Int): ItemStack = ???

    override def openInventory(player: EntityPlayer): Unit = ???

    override def getFieldCount: Int = ???

    override def getField(id: Int): Int = ???

    override def setInventorySlotContents(index: Int, stack: ItemStack): Unit = ???

    override def isUseableByPlayer(player: EntityPlayer): Boolean = ???

    override def getStackInSlot(index: Int): ItemStack = ???

    override def setField(id: Int, value: Int): Unit = ???

    override def getDisplayName: IChatComponent = ???

    override def getName: String = ???

    override def hasCustomName: Boolean = ???
}
