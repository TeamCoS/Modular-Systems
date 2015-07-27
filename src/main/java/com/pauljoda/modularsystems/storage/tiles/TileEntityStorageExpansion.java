package com.pauljoda.modularsystems.storage.tiles;

import com.teambr.bookshelf.collections.Location;
import com.teambr.bookshelf.common.tiles.BaseTile;
import com.teambr.bookshelf.common.tiles.IOpensGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/24/2015
 */
public abstract class TileEntityStorageExpansion extends BaseTile implements IInventory, IOpensGui {

    protected Location core;
    protected List<Location> children;

    public TileEntityStorageExpansion() {
        children = new ArrayList<>();
        core = new Location();
    }

    /**
     * Called after this has been added to a network
     */
    public abstract void addedToNetwork();

    /**
     * Called right before this is removed from a network
     */
    public abstract void removedFromNetwork();

    /**
     * Called when this block is removed from the network
     */
    public void removeFromNetwork(boolean deleteSelf) {
        if(getCore() != null && deleteSelf)
            getCore().deleteFromNetwork(this);
        for(Location child : children) {
            if(worldObj.getTileEntity(child.x, child.y, child.z) != null) {
                if(worldObj.getTileEntity(child.x, child.y, child.z) instanceof TileEntityStorageExpansion) { //Just to be safe
                    ((TileEntityStorageExpansion) worldObj.getTileEntity(child.x, child.y, child.z)).removeFromNetwork(true);
                }
            }
        }
        removedFromNetwork();
        core = new Location();
        children.clear();
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    /**
     * Used to add a child to this node
     * @param childLoc The child to add to this
     */
    public void addChild(Location childLoc) {
        children.add(childLoc);
    }

    /**
     * Used to get what network this is connected to
     * @return The network we are in
     */
    public TileStorageCore getCore() {
        return worldObj.getTileEntity(core.x, core.y, core.z) instanceof TileStorageCore ? (TileStorageCore) worldObj.getTileEntity(core.x, core.y, core.z) : null;
    }

    /*******************************************************************************************************************
     *********************************************** Tile Methods ******************************************************
     *******************************************************************************************************************/

    @Override
    @SuppressWarnings("unchecked")
    public void updateEntity() {
        if(!worldObj.isRemote && !core.isValid() && worldObj.rand.nextInt(80) == 0) { //We don't have a network, lets look for one
           searchAndConnect();
        } else if(!worldObj.isRemote && getCore() == null && new Random().nextInt(20) == 0)
            removeFromNetwork(true);
    }

    public void searchAndConnect() {
        for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            if(getTileInDirection(dir) != null) { //Found a tile entity
                if(getTileInDirection(dir) instanceof TileStorageCore) { //Is it the core?
                    core = ((TileStorageCore)getTileInDirection(dir)).getLocation(); //Set the core location to the core
                    ((TileStorageCore)getTileInDirection(dir)).getNetwork().addNode(this.getLocation()); //Let the network know we are here
                    addedToNetwork(); //Do extras
                    return; //No need to continue
                } else if(getTileInDirection(dir) instanceof TileEntityStorageExpansion) { //Is it another expansion?
                    if(((TileEntityStorageExpansion) getTileInDirection(dir)).getCore() != null) { //Make sure the other one is in a network
                        core = ((TileEntityStorageExpansion)getTileInDirection(dir)).getCore().getLocation(); //Get the core location
                        ((TileEntityStorageExpansion)getTileInDirection(dir)).addChild(getLocation()); //Add to the other
                        addedToNetwork(); //Do extras
                        return; //We are done here
                    }
                }
            }
        }
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public void readFromNBT (NBTTagCompound tag) {
        super.readFromNBT(tag);
        if(tag.hasKey("ChildSize")) {
            children = new ArrayList<>();
            for(int i = 0; i < tag.getInteger("ChildSize"); i++) {
                Location childLoc = new Location();
                childLoc.readFromNBT(tag, "child" + i);
                children.add(childLoc);
            }
        }
        if(tag.hasKey("IsInNetwork")) {
            core.readFromNBT(tag);
        }
    }

    @Override
    public void writeToNBT (NBTTagCompound tag) {
        super.writeToNBT(tag);
        if(children != null && !children.isEmpty()) {
            tag.setInteger("ChildSize", children.size());
            for(int i = 0; i < children.size(); i++)
                children.get(i).writeToNBT(tag, "child" + i);
        }
        if(core.isValid() && worldObj.getTileEntity(core.x, core.y, core.z) instanceof TileStorageCore) {
            tag.setBoolean("IsInNetwork", true);
            core.writeToNBT(tag);
        }
    }

    /*******************************************************************************************************************
     ***************************************** IInventory Methods ******************************************************
     *******************************************************************************************************************/

    @Override
    public int getSizeInventory() {
        return getCore() != null ? getCore().getSizeInventory() : 0;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return getCore() != null ? getCore().getStackInSlot(slot) : null;
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        return getCore() != null ? getCore().decrStackSize(slot, amount) : null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        return getCore() != null ? getCore().getStackInSlotOnClosing(slot) : null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        if(getCore() != null) getCore().setInventorySlotContents(slot, stack);
    }

    /**
     * Returns the name of the inventory
     */
    public String getInventoryName() {
        return getCore() != null ? getCore().getInventoryName() : "";
    }

    /**
     * Returns if the inventory is named
     */
    public boolean hasCustomInventoryName() {
        return getCore() != null && getCore().hasCustomInventoryName();
    }

    @Override
    public int getInventoryStackLimit() {
        return getCore() != null ? getCore().getInventoryStackLimit() : 0;
    }

    /**
     * Do not give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer player) {
        return getCore() != null;
    }

    @Override
    public void openInventory() {
        if(getCore() != null) getCore().openInventory();
    }

    @Override
    public void closeInventory() {
        if(getCore() != null) getCore().closeInventory();
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return getCore() != null && getCore().isItemValidForSlot(slot, stack);
    }

    /*******************************************************************************************************************
     ****************************************** IOpensGui Methods ******************************************************
     *******************************************************************************************************************/

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return getCore() != null ? getCore().getServerGuiElement(ID, player, world, x, y, z) : null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return getCore() != null ? getCore().getClientGuiElement(ID, player, world, x, y, z) : null;
    }
}
