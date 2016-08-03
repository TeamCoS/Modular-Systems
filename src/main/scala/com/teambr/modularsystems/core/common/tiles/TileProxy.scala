package com.teambr.modularsystems.core.common.tiles

import com.teambr.bookshelf.common.tiles.traits.{UpdatingTile, InventorySided}
import com.teambr.modularsystems.core.lib.Reference
import com.teambr.modularsystems.crusher.tiles.TileCrusherCore
import com.teambr.modularsystems.furnace.tiles.TileEntityFurnaceCore
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.items.CapabilityItemHandler

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
class TileProxy extends InventorySided with UpdatingTile {
    var coreLocation : Option[BlockPos] = None
    var storedBlock : Int = -1
    var metaData : Int = 0

    /**
      * Get the core associated with this dummy
      *
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
      *
      * @param core The core
      */
    def setCore(core : AbstractCore) : Unit = {
        coreLocation = Some(core.getPos)
    }

    /**
      * Returns the blocks stored
      *
      * @return The blocks if found, air if not
      */
    def getStoredBlock : Block = {
        Block.getBlockById(storedBlock) match {
            case block : Block => block
            case _ => Blocks.STONE
        }
    }

    def getOverlay : TextureAtlasSprite = {
        getCore match {
            case Some(core : TileEntityFurnaceCore) =>
                Minecraft.getMinecraft.getTextureMapBlocks.getTextureExtry(Reference.MOD_ID + ":blocks/furnaceOverlay")
            case Some(core : TileCrusherCore) =>
                Minecraft.getMinecraft.getTextureMapBlocks.getTextureExtry(Reference.MOD_ID + ":blocks/crusherOverlay")
            case _ => Minecraft.getMinecraft.getTextureMapBlocks.getTextureExtry(Reference.MOD_ID + ":blocks/furnaceOverlay")
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

    override def writeToNBT(tag : NBTTagCompound) : NBTTagCompound = {
        super[TileEntity].writeToNBT(tag)
        if(coreLocation.isDefined)
            tag.setLong("CoreLocation", coreLocation.get.toLong)
        tag.setInteger("StoredBlock", storedBlock)
        tag.setInteger("MetaData", metaData)
        tag
    }

    /*******************************************************************************************************************
      ************************************************ Inventory methods ***********************************************
      * ****************************************************************************************************************/

    override def initialSize: Int = 0

    override def hasCapability(capability: Capability[_], facing : EnumFacing) = {
        capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && getCore.isDefined
    }

    override def getCapability[T](capability: Capability[T], facing: EnumFacing): T = {
        getCore match {
            case Some(core) => core.getCapability(capability, facing)
            case _ => super[TileEntity].getCapability(capability, facing)
        }
    }

    override def getSizeInventory : Int = {
        getCore match {
            case Some(core) => core.getSizeInventory
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

    override def setInventorySlotContents(index : Int, stack : ItemStack) : Unit = {
        getCore match {
            case Some(core) => core.setInventorySlotContents(index, stack)
            case _ =>
        }
    }

    override def getInventoryStackLimit : Int = {
        getCore match {
            case Some(core) => core.getInventoryStackLimit
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

    override def getSlotsForFace(side : EnumFacing) : Array[Int] = {
        getCore match {
            case Some(core) => core.getSlotsForFace(side)
            case _ => Array[Int]()
        }
    }

    override def canExtractItem(index : Int, stack : ItemStack, direction : EnumFacing) : Boolean = {
        getCore match {
            case Some(core) => core.canExtractItem(index, stack, direction)
            case _ => false
        }
    }

    override def canInsertItem(index : Int, itemStackIn : ItemStack, direction : EnumFacing) : Boolean = {
        getCore match {
            case Some(core) => core.canInsertItem(index, itemStackIn, direction)
            case _ => false
        }
    }

    override def removeStackFromSlot(index: Int): ItemStack = {
        getCore match {
            case Some(core) => core.removeStackFromSlot(index)
            case _ => null
        }
    }
}
