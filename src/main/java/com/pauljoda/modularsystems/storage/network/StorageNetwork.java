package com.pauljoda.modularsystems.storage.network;

import com.pauljoda.modularsystems.storage.tiles.TileEntityStorageExpansion;
import com.pauljoda.modularsystems.storage.tiles.TileStorageCore;
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
public class StorageNetwork<T extends TileStorageCore, N extends TileEntityStorageExpansion> {
    protected T root;
    protected List<N> children;

    /**
     * Used to create a new network for storage
     * @param topNode The core, or the root node
     */
    public StorageNetwork(T topNode) {
        root = topNode;
        children = new ArrayList<>();
    }

    /**
     * Used to add a new node into the network
     * @param node The node to add
     */
    public void addNode(N node) {
        children.add(node);
    }

    /**
     * Used to get the node associated with a location
     * @param loc The location of the node
     * @return The node that is at that location
     */
    public N getNode(Location loc) {
        for(N node : children) {
            if(node.getLocation().equals(loc))
                return node;
        }
        return null;
    }

    /**
     * Used to get the root of network, or the core
     * @return The core
     */
    public T getRoot() {
        return root;
    }

    /**
     * Deletes the node in the network
     * @param node The node to add
     * @return True if found and deleted
     */
    public boolean deleteNode(N node) {
        for(Iterator<N> iterator = children.iterator(); iterator.hasNext(); ) {
            N child = iterator.next();
            if(child.getLocation().equals(node.getLocation())) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    public void destroyNetwork() {
        for(Iterator<N> iterator = children.iterator(); iterator.hasNext(); ) {
            N child = iterator.next();
            child.removeFromNetwork(false);
        }
    }

    public void writeToNBT(NBTTagCompound tag) {
        root.getLocation().writeToNBT(tag, "Root");
        if(children != null && !children.isEmpty()) {
            tag.setInteger("ChildSize", children.size());
            for(int i = 0; i < children.size(); i++)
                children.get(i).getLocation().writeToNBT(tag, "child" + i);
        }
    }

    @SuppressWarnings("unchecked")
    public void readFromNBT(NBTTagCompound tag, World world) {
        Location rootLoc = new Location();
        rootLoc.readFromNBT(tag, "Root");

        if(world.getTileEntity(rootLoc.x, rootLoc.y, rootLoc.z) instanceof TileStorageCore)
            root = (T) world.getTileEntity(rootLoc.x, rootLoc.y, rootLoc.z);

        if(tag.hasKey("ChildSize")) {
            children = new ArrayList<>();
            for(int i = 0; i < tag.getInteger("ChildSize"); i++) {
                Location childLoc = new Location();
                childLoc.readFromNBT(tag, "child" + i);
                if(world.getTileEntity(childLoc.x, childLoc.y, childLoc.z) instanceof TileEntityStorageExpansion)
                    children.add((N) world.getTileEntity(childLoc.x, childLoc.y, childLoc.z));
            }
        }
    }
}
