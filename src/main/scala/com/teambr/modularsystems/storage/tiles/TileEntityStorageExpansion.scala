package com.teambr.modularsystems.storage.tiles

import com.teambr.bookshelf.collections.Location
import com.teambr.bookshelf.common.tiles.traits.UpdatingTile
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.{BlockPos, EnumFacing}

import scala.collection.mutable.ListBuffer
import scala.util.Random

/**
 * Modular-Systems
 * Created by Dyonovan on 04/08/15
 */
abstract class TileEntityStorageExpansion extends TileEntity with UpdatingTile {

    protected[storage] var core = new Location()
    protected[storage] var children = new ListBuffer[Location]

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
            if (worldObj.getTileEntity(child.asBlockPos) != null)
                worldObj.getTileEntity(child.asBlockPos) match {
                    case expansion: TileEntityStorageExpansion => expansion.removeFromNetwork(true)
                    case _ =>
                }
        }
        removedFromNetwork()
        core = new Location()
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
        worldObj.getTileEntity(core.asBlockPos) match {
            case tile : TileStorageCore => Some(worldObj.getTileEntity(core.asBlockPos).asInstanceOf[TileStorageCore])
            case _ => None
        }
    }

    /*******************************************************************************************************************
      *********************************************** Tile Methods *****************************************************
      ******************************************************************************************************************/

    override def onServerTick() : Unit = {
        if (core.isValid && worldObj.rand.nextInt(80) == 0)
            searchAndConnect()
        else if (getCore.isEmpty && new Random().nextInt(20) == 0)
            removeFromNetwork(true)
    }

    def searchAndConnect(): Unit = {
        for (dir <- EnumFacing.values()) {
            if (getTileInDirection(pos.add(dir.getDirectionVec)).isDefined) {
                    getTileInDirection(pos.add(dir.getDirectionVec)).get match {
                    case tile: TileStorageCore =>
                        core = new Location(tile.getPos)
                        tile.getNetwork.addNode(new Location(getPos))
                        addedToNetwork()
                    case tile: TileEntityStorageExpansion =>
                        if (tile.getCore.isDefined) {
                            core = new Location(tile.getCore.get.getPos)
                            tile.addChild(new Location(getPos))
                            addedToNetwork()
                        }
                    case _ =>
                }

            }
        }
    }

    def getTileInDirection(pos: BlockPos): Option[(TileEntity)] = {
        worldObj.getTileEntity(pos) match {
            case tile => Some(worldObj.getTileEntity(pos))
            case _ => None
        }
    }
}
