package com.teambr.modularsystems.storage.tiles

import java.util

import com.teambr.bookshelf.common.tiles.traits.UpdatingTile
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.items.CapabilityItemHandler

import scala.collection.JavaConversions._

/**
  * This file was created for Modular-Systems
  *
  * Modular-Systems is licensed under the
  * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
  * http://creativecommons.org/licenses/by-nc-sa/4.0/
  *
  * @author Paul Davis "pauljoda"
  * @since 3/28/2016
  */
abstract class TileStorageExpansion extends UpdatingTile {
    var core : BlockPos = null
    var children : java.util.List[BlockPos] = new util.ArrayList[BlockPos]()

    /**
      * Called after this has been added to a network
      */
    def addedToNetwork()

    /**
      * Called right before this is removed from a network
      */
    def removedFromNetwork()

    /**
      * Called when this block is removed from the network
      */
    def removeFromNetwork(deleteSelf : Boolean) : Unit = {
        if(getCore != null && deleteSelf)
            getCore.deleteFromNetwork(this)
        for(child <- children) {
            worldObj.getTileEntity(child) match {
                case expansion : TileStorageExpansion =>
                    expansion.removeFromNetwork(true)
                case _ =>
            }
        }
        removedFromNetwork()
        core = null
        children.clear()
        markForUpdate()
    }

    /**
      * Used to add a child to this node
      *
      * @param blockPos The child to add to this
      */
    def addChild(blockPos: BlockPos) : Unit = {
        children.add(blockPos)
    }

    /**
      * Used to get what network this is connected to
      *
      * @return The network we are in
      */
    def getCore: TileStorageCore = {
        if(core == null)
            return null
        worldObj.getTileEntity(core) match {
            case core : TileStorageCore => core
            case _ => null
        }
    }

    /*******************************************************************************************************************
      * Tile Methods                                                                                                   *
      ******************************************************************************************************************/

    override def hasCapability(capability: Capability[_], facing : EnumFacing) = {
        capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && getCore != null
    }

    override def getCapability[T](capability: Capability[T], facing: EnumFacing): T = {
        getCore match {
            case core : TileStorageCore => core.getCapability(capability, facing)
            case _ => super[TileEntity].getCapability(capability, facing)
        }
    }

    override def onServerTick(): Unit = {
        if(core == null && worldObj.getTotalWorldTime % 80 == 0) // No network, attach if possible
            searchAndConnect()
        else if(getCore == null && worldObj.getTotalWorldTime % 20 == 0)
            removeFromNetwork(true)
    }

    def searchAndConnect(): Unit = {
        for(dir <- EnumFacing.values()) {
            worldObj.getTileEntity(pos.offset(dir)) match {
                case core : TileStorageCore => // A core, attach here first
                    this.core = core.getPos
                    core.getNetwork.addNode(getPos)
                    addedToNetwork()
                    markForUpdate(3)
                    return
                case expansion : TileStorageExpansion if expansion.getCore != null => // Expansion with core
                    core = expansion.getCore.getPos
                    expansion.addChild(getPos)
                    addedToNetwork()
                    markForUpdate(3)
                    return
                case _ =>
            }
        }
    }

    override def writeToNBT(tag: NBTTagCompound) {
        super[TileEntity].writeToNBT(tag)
        if (!children.isEmpty) {
            tag.setInteger("ChildSize", children.size)
            for (i <- 0 until children.size())
                tag.setLong("Child" + i, children(i).toLong)
        }
        if (core != null) {
            tag.setLong("IsInNetwork", core.toLong)
        }
    }

    override def readFromNBT(tag: NBTTagCompound): Unit = {
        super[TileEntity].readFromNBT(tag)
        if (tag.hasKey("ChildSize")) {
            for (i <- 0 until tag.getInteger("ChildSize"))
                children.add(BlockPos.fromLong(tag.getLong("Child" + i)))
        }
        if (tag.hasKey("IsInNetwork"))
            core = BlockPos.fromLong(tag.getLong("IsInNetwork"))
        else
            core = null
        if (worldObj != null)
            worldObj.markBlockRangeForRenderUpdate(pos, pos)
    }
}
