package com.teambr.modularsystems.storage.network

import com.teambr.modularsystems.storage.tiles.TileEntityStorageExpansion
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.BlockPos
import net.minecraft.world.World

import scala.collection.mutable.ListBuffer

/**
 * Modular-Systems
 * Created by Dyonovan on 03/08/15
 */
class StorageNetwork {

    protected[storage] var children = new ListBuffer[BlockPos]

    /**
     * Used to add a new node into the network
     * @param node The node to add
     */
    def addNode(node: BlockPos) {
        children += node
    }

    /**
     * Used to get the node associated with a location
     * @param pos The location of the node
     * @return The node that is at that location
     */
    def getNode(pos: BlockPos): Option[BlockPos] = {
        for (child <- children if child == pos) Some(child)
        None
    }

    /**
     * Deletes the node in the network
     * @param node The node to add
     * @return True if found and deleted
     */
    def deleteNode(node: TileEntityStorageExpansion): Boolean = {
        if (children.contains(node.getPos)) {
            children = children.diff(List(node.getPos))
            return true
        }
        false
    }

    /**
     * Used to let the children know to remove from the network
     * @param world The world
     */
    def destroyNetwork(world: World) {
        for (child <- children) {
            if (!world.isRemote && world.getTileEntity(child) != null &&
                    world.getTileEntity(child).isInstanceOf[TileEntityStorageExpansion]) {
                world.getTileEntity(child).asInstanceOf[TileEntityStorageExpansion].removeFromNetwork(false)
            }
        }
    }

    def writeToNBT(tag: NBTTagCompound) {
        if (children.nonEmpty) {
            tag.setInteger("ChildSize", children.size)
            for (i <- children.indices)
                tag.setLong("Child" + i, children(i).toLong)
        }
    }

    def readFromNBT(tag: NBTTagCompound) {
        if (tag.hasKey("ChildSize")) {
            children = new ListBuffer[BlockPos]
            for (i <- 0 until tag.getInteger("ChildSize")) {
                var childLoc = BlockPos.fromLong(tag.getLong("child" + i))
                children += childLoc
            }
        }
    }
}
