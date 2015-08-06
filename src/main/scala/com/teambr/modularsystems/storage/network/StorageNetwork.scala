package com.teambr.modularsystems.storage.network

import com.teambr.bookshelf.collections.Location
import com.teambr.modularsystems.storage.tiles.TileEntityStorageExpansion
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World

import scala.collection.mutable.ListBuffer

/**
 * Modular-Systems
 * Created by Dyonovan on 03/08/15
 */
class StorageNetwork {

    protected[storage] var children = new ListBuffer[Location]

    /**
     * Used to add a new node into the network
     * @param node The node to add
     */
    def addNode(node: Location) {
        children += node
    }

    /**
     * Used to get the node associated with a location
     * @param pos The location of the node
     * @return The node that is at that location
     */
    def getNode(pos: Location): Option[Location] = {
        for (child <- children if child == pos) Some(child)
        None
    }

    /**
     * Deletes the node in the network
     * @param node The node to add
     * @return True if found and deleted
     */
    def deleteNode(node: TileEntityStorageExpansion): Boolean = {
        /*if (children.contains(node.getLocation)) {
            children = children.diff(List(node.getpos))
            return true
        }*/
        false
    }

    /**
     * Used to let the children know to remove from the network
     * @param world The world
     */
    def destroyNetwork(world: World) {
        /*for (child <- children) {
            if (!world.isRemote && world.getTileEntity(child.asBlockPos) != null &&
                    world.getTileEntity(child.asBlockPos).isInstanceOf[TileEntityStorageExpansion]) {
                world.getTileEntity(child.asBlockPos).asInstanceOf[TileEntityStorageExpansion].removeFromNetwork(false)
            }
        }*/
    }

    def writeToNBT(tag: NBTTagCompound) {
        if (children != null && children.nonEmpty) {
            tag.setInteger("ChildSize", children.size)
            for (i <- children.indices)
                children(i).writeToNBT(tag, "child" + i)
        }
    }

    def readFromNBT(tag: NBTTagCompound) {
        if (tag.hasKey("ChildSize")) {
            children = new ListBuffer[Location]
            for (i <- 0 until tag.getInteger("ChildSize")) {
                var childLoc = new Location()
                children += childLoc
            }
        }
    }
}
