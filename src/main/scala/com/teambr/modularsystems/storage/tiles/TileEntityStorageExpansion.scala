package com.teambr.modularsystems.storage.tiles

import com.teambr.bookshelf.common.tiles.traits.UpdatingTile
import com.teambr.modularsystems.storage.blocks.BlockStorageExpansion
import net.minecraft.block.properties.PropertyBool
import net.minecraft.client.Minecraft
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.{BlockPos, EnumFacing}
import net.minecraft.world.World

import scala.collection.mutable.ListBuffer
import scala.util.Random

/**
 * Modular-Systems
 * Created by Dyonovan on 04/08/15
 */
abstract class TileEntityStorageExpansion extends TileEntity with UpdatingTile {

    protected[storage] var core: Option[BlockPos] = None
    protected[storage] var children = new ListBuffer[BlockPos]

    /**
     * Called after this has been added to a network
     */
    def addedToNetwork(): Unit = {
        Minecraft.getMinecraft.renderGlobal.markBlockRangeForRenderUpdate(pos.getX, pos.getY, pos.getZ, pos.getX, pos.getY, pos.getZ)
    }

    /**
     * Called right before this is removed from a network
     */
    def removedFromNetwork(): Unit = {
        Minecraft.getMinecraft.renderGlobal.markBlockRangeForRenderUpdate(pos.getX, pos.getY, pos.getZ, pos.getX, pos.getY, pos.getZ)
    }

    /**
     * Called when this block is removed from the network
     */
    def removeFromNetwork(deleteSelf: Boolean): Unit = {
        if (getCore.isDefined && deleteSelf) getCore.get.deleteFromNetwork(this)

        for (child <- children) {
                worldObj.getTileEntity(child) match {
                    case expansion: TileEntityStorageExpansion => expansion.removeFromNetwork(true)
                    case _ =>
                }
        }
        removedFromNetwork()
        core = None
        children.clear()
        if (worldObj.getTileEntity(pos).isInstanceOf[TileEntityStorageExpansion])
            Minecraft.getMinecraft.renderGlobal.markBlockRangeForRenderUpdate(pos.getX, pos.getY, pos.getZ, pos.getX, pos.getY, pos.getZ)
        worldObj.markBlockForUpdate(getPos)
    }

    /**
     * Used to add a child to this node
     * @param childLoc The child to add to this
     */
    def addChild(childLoc: BlockPos) {
        if (core.isDefined) {
            getCore.get.network.addNode(childLoc)
            children += childLoc
        }
    }

    /**
     * Used to get what network this is connected to
     * @return The network we are in
     */
    def getCore: Option[TileStorageCore] = {
        if (core.isDefined && worldObj.getTileEntity(core.get).isInstanceOf[TileStorageCore])
            Some(worldObj.getTileEntity(core.get).asInstanceOf[TileStorageCore])
        else None
    }

    /*******************************************************************************************************************
      *********************************************** Tile Methods *****************************************************
      ******************************************************************************************************************/

    override def onServerTick() : Unit = {
        if (core.isEmpty && worldObj.rand.nextInt(80) == 0)
            searchAndConnect()
        else if (getCore.isEmpty && new Random().nextInt(20) == 0)
            removeFromNetwork(true)
    }

    override def onClientTick(): Unit = { }

    def searchAndConnect(): Unit = {
        for (dir <- EnumFacing.values()) {
            if (getTileInDirection(pos.add(dir.getDirectionVec)).isDefined) {
                    getTileInDirection(pos.add(dir.getDirectionVec)).get match {
                    case tile: TileStorageCore =>
                        core = Some(tile.getPos)
                        tile.getNetwork.addNode(getPos)
                        addedToNetwork()
                    case tile: TileEntityStorageExpansion =>
                        if (tile.getCore.isDefined) {
                            core = Some(tile.getCore.get.getPos)
                            tile.addChild(getPos)
                            addedToNetwork()
                        }
                    case _ =>
                }
            }
        }
        worldObj.markBlockForUpdate(getPos)
    }

    def getTileInDirection(pos: BlockPos): Option[(TileEntity)] = {
        worldObj.getTileEntity(pos) != null match {
            case true => Some(worldObj.getTileEntity(pos))
            case _ => None
        }
    }

    override def readFromNBT(tag: NBTTagCompound): Unit = {
        super[TileEntity].readFromNBT(tag)
        children = new ListBuffer[BlockPos]
        if (tag.hasKey("ChildSize")) {
            for (i <- 0 until tag.getInteger("ChildSize"))
                children += BlockPos.fromLong(tag.getLong("Child" + i))
        }
        if (tag.hasKey("IsInNetwork"))
            core = Some(BlockPos.fromLong(tag.getLong("IsInNetwork")))
        else core = None
    }

    override def writeToNBT(tag: NBTTagCompound) {
        super[TileEntity].writeToNBT(tag)
        if (children.nonEmpty) {
            tag.setInteger("ChildSize", children.size)
            for (i <- children.indices)
                tag.setLong("Child" + i, children(i).toLong)
        }
        if (core.isDefined) {
            tag.setLong("IsInNetwork", core.get.toLong)
        }
    }
}
