package com.teambr.modularsystems.core.tiles

import com.teambr.bookshelf.common.tiles.traits.UpdatingTile
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.{ IChatComponent, BlockPos }

/**
 * This file was created for Modular-Systems
 *
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis <pauljoda>
 * @since August 06, 2015
 */
class ProxyTile extends UpdatingTile with IInventory {
    var coreLocation : Option[BlockPos] = None
    var storedBlock : Int = 0
    var metaData : Int = 0

    /**
     * Get the core associated with this dummy
     * @return The core object
     */
    def getCore : Option[AbstractCore] = {
        if(coreLocation.isDefined && worldObj.getTileEntity(coreLocation.get).isInstanceOf[AbstractCore])
            Some(worldObj.getTileEntity(coreLocation.get).asInstanceOf[AbstractCore])
        else
            None
    }

    /**
     * Returns the blocks stored
     * @return The blocks if found, air if not
     */
    def getStoredBlock : Block = {
        Block.getBlockById(storedBlock) match {
            case block : Block => block
            case _ => Blocks.air
        }
    }

    /*******************************************************************************************************************
      **************************************************  Tile Methods  ************************************************
      * *****************************************************************************************************************/

    override def readFromNBT(tag : NBTTagCompound) : Unit = {
        super[TileEntity].readFromNBT(tag)
        if(tag.hasKey("CoreLocation"))
            coreLocation = Some(BlockPos.fromLong(tag.getLong("CoreLocation")))
        storedBlock = tag.getInteger("StoredBlock")
        metaData = tag.getInteger("MetaData")
    }

    override def writeToNBT(tag : NBTTagCompound) : Unit = {
        super[TileEntity].writeToNBT(tag)
        if(coreLocation.isDefined)
            tag.setLong("CoreLocation", coreLocation.get.toLong)
        tag.setInteger("StoredBlock", storedBlock)
        tag.setInteger("MetaData", metaData)
    }

    /*******************************************************************************************************************
      ************************************************ Inventory methods ***********************************************
      * ****************************************************************************************************************/

    override def getSizeInventory : Int = {
        getCore match {
            case Some(core) => core.getSizeInventory()
            case _ => 0
        }
    }

    override def getStackInSlot(index : Int) : ItemStack = {
        getCore match {
            case Some(core) => core.getStackInSlot(index)
            case _ => null
        }
    }

    override def decrStackSize(index : Int, count : Int) : ItemStack = {
        getCore match {
            case Some(core) => core.decrStackSize(index, count)
            case _ => null
        }
    }

    override def getStackInSlotOnClosing(index : Int) : ItemStack = {
        getCore match {
            case Some(core) => core.getStackInSlotOnClosing(index)
        }
    }

    override def setInventorySlotContents(index : Int, stack : ItemStack) : Unit = ???


    override def closeInventory(player : EntityPlayer) : Unit = ???


    override def getInventoryStackLimit : Int = ???

    override def clear() : Unit = ???

    override def isItemValidForSlot(index : Int, stack : ItemStack) : Boolean = ???


    override def openInventory(player : EntityPlayer) : Unit = ???

    override def getFieldCount : Int = ???

    override def getField(id : Int) : Int = ???


    override def isUseableByPlayer(player : EntityPlayer) : Boolean = ???


    override def setField(id : Int, value : Int) : Unit = ???

    override def getDisplayName : IChatComponent = ???

    override def getName : String = ???

    override def hasCustomName : Boolean = ???
}
