package com.teambr.modularsystems.core.common.tiles

import com.teambr.bookshelf.common.tiles.traits.UpdatingTile
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.{ BlockPos, IChatComponent }

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
class TileProxy extends UpdatingTile with IInventory {
    var coreLocation : Option[BlockPos] = None
    var storedBlock : Int = -1
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
     * Sets the coreLocation to a core
     * @param core The core
     */
    def setCore(core : AbstractCore) : Unit = {
        coreLocation = Some(core.getPos)
    }

    /**
     * Returns the blocks stored
     * @return The blocks if found, air if not
     */
    def getStoredBlock : Block = {
        Block.getBlockById(storedBlock) match {
            case block : Block => block
            case _ => Blocks.stone
        }
    }

    /*******************************************************************************************************************
      **************************************************  Tile Methods  ************************************************
      * *****************************************************************************************************************/

    override def readFromNBT(tag : NBTTagCompound) : Unit = {
        super[TileEntity].readFromNBT(tag)
        if(tag.hasKey("CoreLocation"))
            coreLocation = Some(BlockPos.fromLong(tag.getLong("CoreLocation")))
        else
            coreLocation = None
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
            case _ => null
        }
    }

    override def setInventorySlotContents(index : Int, stack : ItemStack) : Unit = {
        getCore match {
            case Some(core) => core.setInventorySlotContents(index, stack)
            case _ =>
        }
    }

    override def getInventoryStackLimit : Int = {
        getCore match {
            case Some(core) => getInventoryStackLimit
            case _ => 0
        }
    }

    override def isUseableByPlayer(player : EntityPlayer) : Boolean = {
        getCore match {
            case Some(core) => true
            case _ => false
        }
    }

    override def isItemValidForSlot(index : Int, stack : ItemStack) : Boolean = {
        getCore match {
            case Some(core) => core.isItemValidForSlot(index, stack)
            case _ => false
        }
    }

    override def openInventory(player : EntityPlayer) : Unit = {
        getCore match {
            case Some(core) => core.openInventory(player)
            case _ =>
        }
    }

    override def closeInventory(player : EntityPlayer) : Unit = {
        getCore match {
            case Some(core) => core.closeInventory(player)
            case _ =>
        }
    }

    override def clear() : Unit = {
        getCore match {
            case Some(core) => core.clear()
            case _ =>
        }
    }

    override def getFieldCount : Int = {
        getCore match {
            case Some(core) => core.getFieldCount()
            case _ => 0
        }
    }

    override def getField(id : Int) : Int = {
        getCore match {
            case Some(core) => core.getField(id)
            case _ => 0
        }
    }

    override def setField(id : Int, value : Int) : Unit = {
        getCore match {
            case Some(core) => core.setField(id, value)
            case _ =>
        }
    }

    override def getDisplayName : IChatComponent = {
        getCore match {
            case Some(core) => core.getDisplayName()
            case _ => null
        }
    }

    override def getName : String = {
        getCore match {
            case Some(core) => core.getName()
            case _ => ""
        }
    }

    override def hasCustomName : Boolean = {
        getCore match {
            case Some(core) => core.hasCustomName()
            case _ => false
        }
    }
}
