package com.teambr.modularsystems.storage.tiles

import com.teambr.bookshelf.collections.Location
import com.teambr.bookshelf.common.tiles.traits.{OpensGui, UpdatingTile}
import net.minecraft.entity.player.EntityPlayer
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
abstract class TileEntityStorageExpansion extends TileEntity with UpdatingTile with OpensGui {

    protected[storage] var core: Option[BlockPos] = None
    protected[storage] var children = new ListBuffer[BlockPos]

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
    def removeFromNetwork(deleteSelf: Boolean): Unit = {
        if (getCore.isDefined && deleteSelf) getCore.get.deleteFromNetwork(this)

        for (child <- children) {
            if (worldObj.getTileEntity(child) != null)
                worldObj.getTileEntity(child) match {
                    case expansion: TileEntityStorageExpansion => expansion.removeFromNetwork(true)
                    case _ =>
                }
        }
        removedFromNetwork()
        core = None
        children.clear()
        worldObj.markBlockForUpdate(pos)
    }

    /**
     * Used to add a child to this node
     * @param childLoc The child to add to this
     */
    def addChild(childLoc: Location) {
        children :+ childLoc
    }

    /**
     * Used to get what network this is connected to
     * @return The network we are in
     */
    def getCore: Option[TileStorageCore] = {
        if (core.isDefined)
            Some(worldObj.getTileEntity(core.get).asInstanceOf[TileStorageCore])
        else None
    }

    /*******************************************************************************************************************
      *********************************************** Tile Methods *****************************************************
      ******************************************************************************************************************/

    override def onServerTick() : Unit = {
        if (core.isDefined && worldObj.rand.nextInt(80) == 0)
            searchAndConnect()
        else if (getCore.isEmpty && new Random().nextInt(20) == 0)
            removeFromNetwork(true)
    }

    def searchAndConnect(): Unit = {
        for (dir <- EnumFacing.values()) {
            if (getTileInDirection(pos.add(dir.getDirectionVec)).isDefined) {
                    getTileInDirection(pos.add(dir.getDirectionVec)).get match {
                    case tile: TileStorageCore =>
                        core = Some(tile.getPos)
                        tile.getNetwork.addNode(new Location(getPos))
                        addedToNetwork()
                    case tile: TileEntityStorageExpansion =>
                        if (tile.getCore.isDefined) {
                            core = Some(tile.getCore.get.getPos)
                            tile.addChild(new Location(getPos))
                            addedToNetwork()
                        }
                    case _ =>
                }
            }
        }
        worldObj.markBlockForUpdate(getPos)
    }

    def getTileInDirection(pos: BlockPos): Option[(TileEntity)] = {
        worldObj.getTileEntity(pos) match {
            case tile => Some(worldObj.getTileEntity(pos))
            case _ => None
        }
    }

    override def readFromNBT(tag: NBTTagCompound): Unit = {
        super.readFromNBT(tag)
        if (tag.hasKey("ChildSize")) {
            children = new ListBuffer[BlockPos]
            for (i <- 0 until tag.getInteger("ChildSize"))
                children :+ BlockPos.fromLong(tag.getLong("Child" + i))
        }
        if (tag.hasKey("IsInNetwork"))
            core = Some(BlockPos.fromLong(tag.getLong("IsInNetwork")))
    }

    override def writeToNBT(tag: NBTTagCompound) {
        super.writeToNBT(tag)
        if (children.nonEmpty) {
            tag.setInteger("ChildSize", children.size)
            for (i <- children.indices)
                tag.setLong("Child" + i, children(i).toLong)
        }
        if (core.isDefined) {
            tag.setLong("IsInNetwork", core.get.toLong)
        }
    }

    /*******************************************************************************************************************
      ****************************************** IOpensGui Methods *****************************************************
      ******************************************************************************************************************/

    override def getServerGuiElement(id: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Object = {
        getCore.orNull.getServerGuiElement(id, player, world, x, y, z)
    }

    override def getClientGuiElement(id: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Object = {
        getCore.orNull.getClientGuiElement(id, player, world, x, y, z)
    }
}
