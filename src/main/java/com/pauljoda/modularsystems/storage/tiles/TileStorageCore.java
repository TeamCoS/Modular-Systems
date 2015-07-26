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
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/23/2015
 */
public class TileStorageCore extends BaseTile implements IInventory, IOpensGui {

    private String customName;

    protected InventoryTile inventory;
    protected StorageNetwork network;

    protected String ownerUUID;
    protected boolean isSecured;
    protected List<String> allowedPlayers;
    protected boolean hasSearchUpgrade;
    protected boolean hasSortingUpgrade;

    public TileStorageCore() {
        inventory = new InventoryTile(66);
        customName = StatCollector.translateToLocal("inventory.storageCore.title");
        allowedPlayers = new ArrayList<>();
        network = new StorageNetwork();
    }

    /**
     * Used to get how many rows this has in the GUI
     * @return The number of rows in the container
     */
    public int getInventoryRowCount() {
        return (inventory.getSizeInventory() / 11) + (inventory.getSizeInventory() % 11 > 0 ? 1 : 0);
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
        network.destroyNetwork(worldObj);
        network = null;
    }

    /**
     * Used to get the inventory
     * @return The inventory stack
     */
    public Stack<ItemStack> getInventory() {
        return inventory.getValues();
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
     ******************************************* Expansion Methods *****************************************************
     *******************************************************************************************************************/

    /**
     * Get the owners string
     * @return The owner's UUID
     */
    public String getOwnerUUID() {
        return ownerUUID;
    }

    /**
     * Set the owner UUID by string
     * @param ownerUUID The new string UUID
     */
    public void setOwnerUUID(String ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    /**
     * Used to check if the player opening is the owner
     * @param player The player trying to open
     * @return True if you can open this
     */
    public boolean canOpen(EntityPlayer player) {
        return (ownerUUID == null || ownerUUID.equalsIgnoreCase("") || !isSecured) || (((player.getUniqueID().toString().equals(this.ownerUUID) || isInAllowPlayers(player.getUniqueID().toString()))) || player.capabilities.isCreativeMode);
    }

    /**
     * Sets the owner to what opened
     * @param player The new owner
     */
    public void setOwner(EntityPlayer player) {
        this.ownerUUID = player.getUniqueID().toString();
    }

    /**
     * Checks if this player is allowed to open it, though he is not the owner
     * @param UUID The UUID of the player
     * @return True if allowed
     */
    public boolean isInAllowPlayers(String UUID) {
        for(String playerID : allowedPlayers) {
            if(playerID.equals(UUID))
                return true;
        }
        return false;
    }

    /**
     * Used to get the list of allowed players
     * @return The list of allowed players
     */
    public List<String> getAllowedPlayers() {
        return allowedPlayers;
    }

    /**
     * Used to add a player to the list of allowed player
     * @param player The player to add
     */
    public void addPlayerToAllowedPlayers(EntityPlayer player) {
        allowedPlayers.add(player.getUniqueID().toString());
    }

    /**
     * Used to remove a player from the list of allowed players
     * @param player The player to remove
     * @return True if the player was found and removed
     */
    public boolean removePlayerFromAllowdPlayers(EntityPlayer player) {
        for(Iterator<String> iterator = allowedPlayers.iterator(); iterator.hasNext(); ) {
            String uuid = iterator.next();
            if(uuid.equals(player.getUniqueID().toString())) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * Used to check if this is secured
     * @return True if secured
     */
    public boolean isSecured() {
        return isSecured;
    }

    /**
     * Used to lock and unlock
     * @param isSecured New value
     */
    public void setIsSecured(boolean isSecured) {
        this.isSecured = isSecured;
    }

    /**
     * Checks if this tile has a search upgrade
     * @return True if it can search
     */
    public boolean hasSearchUpgrade() {
        return hasSearchUpgrade;
    }

    /**
     * Used to set if this can search
     * @param hasSearchUpgrade True if you want this feature
     */
    public void setHasSearchUpgrade(boolean hasSearchUpgrade) {
        this.hasSearchUpgrade = hasSearchUpgrade;
    }

    /**
     * Used to check if this has sorting upgrade
     * @return True if it can sort
     */
    public boolean hasSortingUpgrade() {
        return hasSortingUpgrade;
    }

    /**
     * Used to set if this can sort
     * @param hasSortingUpgrade True if you want this feature
     */
    public void setHasSortingUpgrade(boolean hasSortingUpgrade) {
        this.hasSortingUpgrade = hasSortingUpgrade;
    }

    /*******************************************************************************************************************
     *********************************************** Tile Methods ******************************************************
     *******************************************************************************************************************/

    @Override
    public void readFromNBT (NBTTagCompound tags) {
        super.readFromNBT(tags);
        inventory.readFromNBT(tags);
        network.readFromNBT(tags);
        customName = tags.getString("CustomName");

        ownerUUID = tags.getString("Owner");

        hasSearchUpgrade = tags.getBoolean("Search");
        hasSortingUpgrade = tags.getBoolean("Sorting");
        isSecured = tags.getBoolean("Secured");

        allowedPlayers = new ArrayList<>(tags.getInteger("AllowedListSize"));
        for(int i = 0; i < allowedPlayers.size(); i++)
            allowedPlayers.set(i, tags.getString("AllowedPlayer" + i));
    }

    @Override
    public void writeToNBT (NBTTagCompound tags) {
        super.writeToNBT(tags);
        inventory.writeToNBT(tags);
        network.writeToNBT(tags);
        tags.setString("CustomName", customName);

        tags.setString("Owner", ownerUUID);

        tags.setBoolean("Search", hasSearchUpgrade);
        tags.setBoolean("Sorting", hasSortingUpgrade);
        tags.setBoolean("Secured", isSecured);

        tags.setInteger("AllowedListSize", allowedPlayers.size());
        for(int i = 0; i < allowedPlayers.size(); i++)
            tags.setString("AllowedPlayer" + i, allowedPlayers.get(i));
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
        if(inventory.getStackInSlot(slot) != null) {
            ItemStack returnStack;
            if(inventory.getStackInSlot(slot).stackSize <= amount) {
                returnStack = inventory.getStackInSlot(slot);
                inventory.setStackInSlot(null, slot);
                this.markDirty();
                return returnStack;
            } else {
                returnStack = inventory.getStackInSlot(slot).splitStack(amount);
                if(inventory.getStackInSlot(slot).stackSize <= 0)
                    inventory.setStackInSlot(null, slot);
                this.markDirty();
                return returnStack;
            }
        }
        return null;
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
        return this.hasCustomInventoryName() ? this.customName : "inventory.storageCore.title";
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
        return canOpen(player) ? new ContainerStorageCore(player.inventory, this, this) : null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return canOpen(player) ? new GuiStorageCore(new ContainerStorageCore(player.inventory, this, this), this) : null;
    }
}
