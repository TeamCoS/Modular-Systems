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
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

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
    protected InventoryTile craftingGrid;
    protected StorageNetwork network;

    protected String ownerName;
    protected boolean isSecured;
    protected List<String> allowedPlayers;

    protected boolean hasSearchUpgrade;
    protected boolean hasSortingUpgrade;
    protected boolean hasCraftingUpgrade;

    public TileStorageCore() {
        inventory = new InventoryTile(66);
        craftingGrid = new InventoryTile(9);
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
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * Set the owner UUID by string
     * @param name The new string UUID
     */
    public void setOwnerName(String name) {
        this.ownerName = name;
    }

    /**
     * Used to check if the player opening is the owner
     * @param player The player trying to open
     * @return True if you can open this
     */
    public boolean canOpen(EntityPlayer player) {
        return (ownerName == null || ownerName.equalsIgnoreCase("") || !isSecured) || (((player.getDisplayName().equals(this.ownerName) || isInAllowPlayers(player.getDisplayName()))) || player.capabilities.isCreativeMode);
    }

    /**
     * Sets the owner to what opened
     * @param player The new owner
     */
    public void setOwner(EntityPlayer player) {
        this.ownerName = player.getDisplayName();
    }

    /**
     * Checks if this player is allowed to open it, though he is not the owner
     * @param name The name of the player
     * @return True if allowed
     */
    public boolean isInAllowPlayers(String name) {
        for(String playerID : allowedPlayers) {
            if(playerID.equals(name))
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
        allowedPlayers.add(player.getDisplayName());
    }

    /**
     * Used to add a player to the list of allowed player, by name
     * @param player The player to add
     */
    public void addPlayerToAllowedPlayers(String player) {
        allowedPlayers.add(player);
    }

    /**
     * Used to remove a player from the list of allowed players
     * @param player The player to remove
     * @return True if the player was found and removed
     */
    public boolean removePlayerFromAllowdPlayers(EntityPlayer player) {
        for(Iterator<String> iterator = allowedPlayers.iterator(); iterator.hasNext(); ) {
            String uuid = iterator.next();
            if(uuid.equals(player.getDisplayName())) {
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

    /**
     * Used to tell if this has the crafting upgrade
     * @return True if available
     */
    public boolean hasCraftingUpgrade() {
        return hasCraftingUpgrade;
    }

    /**
     * Used to set if it can craft
     * @param hasCraftingUpgrade True if can craft
     */
    public void setHasCraftingUpgrade(boolean hasCraftingUpgrade) {
        this.hasCraftingUpgrade = hasCraftingUpgrade;
    }

    /**
     * Used to get the crafting inventory
     * @return The crafting inventory
     */
    public InventoryTile getCraftingGrid() {
        return craftingGrid;
    }

    /**
     * Used to set the crafting grid
     * @param craftingGrid
     */
    public void setCraftingGrid(InventoryTile craftingGrid) {
        this.craftingGrid = craftingGrid;
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

        ownerName = tags.getString("Owner");

        hasSearchUpgrade = tags.getBoolean("Search");
        hasSortingUpgrade = tags.getBoolean("Sorting");
        hasCraftingUpgrade = tags.getBoolean("Crafting");

        isSecured = tags.getBoolean("Secured");

        allowedPlayers = new ArrayList<>();
        NBTTagList securityList = tags.getTagList("SecurityList", Constants.NBT.TAG_COMPOUND);
        for(int i = 0; i < securityList.tagCount(); i++) {
            NBTTagCompound tag = securityList.getCompoundTagAt(i);
            String player = tag.getString("AllowedPlayer" + i);
            allowedPlayers.add(player);
        }

        NBTTagList itemsTag = tags.getTagList("ItemsCrafting", 10);
        this.craftingGrid = new InventoryTile(9);
        for (int i = 0; i < itemsTag.tagCount(); i++) {
            NBTTagCompound nbtTagCompound1 = itemsTag.getCompoundTagAt(i);
            NBTBase nbt = nbtTagCompound1.getTag("Slot");
            int j = -1;
            if ((nbt instanceof NBTTagByte)) {
                j = nbtTagCompound1.getByte("Slot") & 0xFF;
            } else {
                j = nbtTagCompound1.getShort("Slot");
            }
            if ((j >= 0)) {
                this.craftingGrid.setStackInSlot(ItemStack.loadItemStackFromNBT(nbtTagCompound1), j);
            }
        }
    }

    @Override
    public void writeToNBT (NBTTagCompound tags) {
        super.writeToNBT(tags);
        inventory.writeToNBT(tags);
        network.writeToNBT(tags);
        tags.setString("CustomName", customName);

        tags.setString("Owner", ownerName != null && ownerName.length() > 1 ? ownerName : "NULL");

        tags.setBoolean("Search", hasSearchUpgrade);
        tags.setBoolean("Sorting", hasSortingUpgrade);
        tags.setBoolean("Crafting", hasCraftingUpgrade);

        tags.setBoolean("Secured", isSecured);

        NBTTagList securityList = new NBTTagList();
        for(int i = 0; i < allowedPlayers.size(); i++) {
            String player = allowedPlayers.get(i);
            if(player != null) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setString("AllowedPlayer" + i, player);
                securityList.appendTag(tag);
            }
        }
        tags.setTag("SecurityList", securityList);

        NBTTagList nbtTagList = new NBTTagList();
        for (int i = 0; i < this.craftingGrid.getSizeInventory(); i++) {
            if (this.craftingGrid.getStackInSlot(i) != null) {
                NBTTagCompound nbtTagCompound1 = new NBTTagCompound();
                nbtTagCompound1.setShort("Slot", (short)i);
                this.craftingGrid.getStackInSlot(i).writeToNBT(nbtTagCompound1);
                nbtTagList.appendTag(nbtTagCompound1);
            }
        }
        tags.setTag("ItemsCrafting", nbtTagList);
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

    public void checkInventory() {
        for(int i = 0; i < inventory.getSizeInventory(); i++) {
            if(inventory.getStackInSlot(i) != null) {
                if(inventory.getStackInSlot(i).stackSize <= 0)
                    inventory.setStackInSlot(null, i);
            }
        }
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
