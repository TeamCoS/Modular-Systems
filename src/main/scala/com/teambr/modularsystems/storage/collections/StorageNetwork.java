package com.teambr.modularsystems.storage.collections;

import com.teambr.modularsystems.storage.tiles.TileStorageExpansion;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This file was created for Modular-Systems
 * <p/>
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis "pauljoda"
 * @since 3/28/2016
 */
public class StorageNetwork {
    protected List<BlockPos> children;

    /**
     * Used to create a new network for storage
     */
    public StorageNetwork() {
        children = new ArrayList<>();
    }

    /**
     * Used to add a new node into the network
     * @param node The node to add
     */
    public void addNode(BlockPos node) {
        children.add(node);
    }

    /**
     * Used to get the node associated with a location
     * @param loc The location of the node
     * @return The node that is at that location
     */
    public BlockPos getNode(BlockPos loc) {
        for(BlockPos node : children) {
            if(node.equals(loc))
                return node;
        }
        return null;
    }


    /**
     * Deletes the node in the network
     * @param node The node to add
     * @return True if found and deleted
     */
    public boolean deleteNode(TileStorageExpansion node) {
        for(Iterator<BlockPos> iterator = children.iterator(); iterator.hasNext(); ) {
            BlockPos child = iterator.next();
            if(child.equals(node.getPos())) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * Used to let the children know to remove from the network
     * @param world The world
     */
    public void destroyNetwork(World world) {
        for(BlockPos loc : children) {
            if(!world.isRemote && world.getTileEntity(loc) != null && world.getTileEntity(loc) instanceof TileStorageExpansion)
                ((TileStorageExpansion)world.getTileEntity(loc)).removeFromNetwork(false);
        }
    }

    public void writeToNBT(NBTTagCompound tag) {
        if(children != null && !children.isEmpty()) {
            tag.setInteger("ChildSize", children.size());
            for(int i = 0; i < children.size(); i++)
                tag.setLong( "child" + i, children.get(i).toLong());
        }
    }

    public void readFromNBT(NBTTagCompound tag) {
        if(tag.hasKey("ChildSize")) {
            children = new ArrayList<>();
            for(int i = 0; i < tag.getInteger("ChildSize"); i++) {
                children.add(BlockPos.fromLong(tag.getLong("child" + i)));
            }
        }
    }
}
