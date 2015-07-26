package com.pauljoda.modularsystems.storage.network;

import com.pauljoda.modularsystems.storage.tiles.TileEntityStorageExpansion;
import com.teambr.bookshelf.collections.Location;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/24/2015
 */
public class StorageNetwork {
    protected List<Location> children;

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
    public void addNode(Location node) {
        children.add(node);
    }

    /**
     * Used to get the node associated with a location
     * @param loc The location of the node
     * @return The node that is at that location
     */
    public Location getNode(Location loc) {
        for(Location node : children) {
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
    public boolean deleteNode(TileEntityStorageExpansion node) {
        for(Iterator<Location> iterator = children.iterator(); iterator.hasNext(); ) {
            Location child = iterator.next();
            if(child.equals(node.getLocation())) {
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
        for(Location loc : children) {
            if(!world.isRemote && world.getTileEntity(loc.x, loc.y, loc.z) != null && world.getTileEntity(loc.x, loc.y, loc.z) instanceof TileEntityStorageExpansion)
                ((TileEntityStorageExpansion)world.getTileEntity(loc.x, loc.y, loc.z)).removeFromNetwork(false);
        }
    }

    public void writeToNBT(NBTTagCompound tag) {
        if(children != null && !children.isEmpty()) {
            tag.setInteger("ChildSize", children.size());
            for(int i = 0; i < children.size(); i++)
                children.get(i).writeToNBT(tag, "child" + i);
        }
    }

    public void readFromNBT(NBTTagCompound tag) {
        if(tag.hasKey("ChildSize")) {
            children = new ArrayList<>();
            for(int i = 0; i < tag.getInteger("ChildSize"); i++) {
                Location childLoc = new Location();
                childLoc.readFromNBT(tag, "child" + i);
                children.add(childLoc);
            }
        }
    }
}
