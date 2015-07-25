package com.pauljoda.modularsystems.storage.tiles;

import com.pauljoda.modularsystems.storage.container.ContainerStorageCore;
import com.pauljoda.modularsystems.storage.gui.GuiStorageCore;
import com.pauljoda.modularsystems.storage.network.StorageNetwork;
import com.teambr.bookshelf.collections.InventoryTile;
import com.teambr.bookshelf.common.tiles.BaseTile;
import com.teambr.bookshelf.common.tiles.IOpensGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/23/2015
 */
public class TileStorageCore extends BaseTile implements IInventory, IOpensGui {

    private String customName;

    protected InventoryTile inventory;
    protected StorageNetwork<TileStorageCore, TileEntityStorageExpansion> network;

    public TileStorageCore() {
        inventory = new InventoryTile(66);
        customName = StatCollector.translateToLocal("inventory.storage.title");
        network = new StorageNetwork(this);
    }

    /**
     * Used to get how many rows this has in the GUI
     * @return The number of rows in the container
     */
    public int getInventoryRowCount() {
        return (inventory.getSizeInventory() / 11) + (inventory.getSizeInventory() % 11 > 0 ? 1 : 0);
    }

    /**
     * Used to add a node to the network
     * @param node The node to add
     */
    public void addToNetwork(TileEntityStorageExpansion node) {
        network.addNode(node);
    }

    /**
     * Used to remove a node from the network
     * @param node The node to remove
     * @return True if found and removed
     */
    public boolean deleteFromNetwork(TileEntityStorageExpansion node) {
        return network.deleteNode(node);
    }

    /**
     * Used to get the storage network
     * @return The network of this tile
     */
    public StorageNetwork getNetwork() {
        return network;
    }

    /**
     * Used to destroy the network
     */
    public void destroyNetwork() {
        network.destroyNetwork();
        network = null;
    }

    /**
     * Used to add inventory space
     * @param count How many slots to add
     */
    public void pushNewInventory(int count) {
        for(int i = 0; i < count; i++)
            inventory.push(null);
    }

    /**
     * Used to remove inventory space
     * @param count How many slots to remove
     * @return The stacks that were in those top slots
     */
    public ArrayList<ItemStack> popInventory(int count) {
        ArrayList<ItemStack> stacks = new ArrayList<>();
        for(int i = 0; i < count; i++)
            stacks.add(inventory.pop());
        return stacks;
    }

    /*******************************************************************************************************************
     *********************************************** Tile Methods ******************************************************
     *******************************************************************************************************************/

    @Override
    public void readFromNBT (NBTTagCompound tags) {
        super.readFromNBT(tags);
        inventory.readFromNBT(tags);
        customName = tags.getString("CustomName");
        network.readFromNBT(tags, worldObj);
    }

    @Override
    public void writeToNBT (NBTTagCompound tags) {
        super.writeToNBT(tags);
        inventory.writeToNBT(tags);
        tags.setString("CustomName", customName);
        network.writeToNBT(tags);
    }

    /*******************************************************************************************************************
     ***************************************** IInventory Methods ******************************************************
     *******************************************************************************************************************/

    @Override
    public int getSizeInventory() {
        return inventory.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory.getStackInSlot(slot);
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        ItemStack returnStack = inventory.getStackInSlot(slot).copy();
        inventory.getStackInSlot(slot).stackSize -= amount;
        if(inventory.getStackInSlot(slot).stackSize <= 0)
            inventory.setStackInSlot(null, slot);
        return returnStack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        return inventory.getStackInSlot(slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inventory.setStackInSlot(stack, slot);
    }

    /**
     * Returns the name of the inventory
     */
    public String getInventoryName() {
        return this.hasCustomInventoryName() ? this.customName : "inventory.storage.title";
    }

    /**
     * Returns if the inventory is named
     */
    public boolean hasCustomInventoryName() {
        return this.customName != null && this.customName.length() > 0;
    }

    /**
     * The vanilla code to set names from anvil
     * @param newName The new name
     */
    public void setCustomName(String newName) {
        this.customName = newName;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    /**
     * Do not give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return true;
    }

    /*******************************************************************************************************************
     ****************************************** IOpensGui Methods ******************************************************
     *******************************************************************************************************************/

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new ContainerStorageCore(player.inventory, this, this);
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new GuiStorageCore(new ContainerStorageCore(player.inventory, this, this), this);
    }
}
