package com.pauljoda.modularsystems.storage.tiles;

import com.pauljoda.modularsystems.storage.container.ContainerStorageRemote;
import com.pauljoda.modularsystems.storage.gui.GuiStorageRemote;
import com.pauljoda.modularsystems.storage.items.ItemStorageRemote;
import com.teambr.bookshelf.collections.InventoryTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * Modular-Systems
 * Created by Dyonovan on 01/08/15
 */
public class TileStorageRemote extends TileStorageBasic {
    protected InventoryTile inventory;

    public TileStorageRemote() {
        inventory = new InventoryTile(2);
    }

    @Override
    public void addedToNetwork() {
        worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 2);
    }

    @Override
    public void removedFromNetwork() {
        worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 5, 2);
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
    public ItemStack decrStackSize(int slot, int count) {
        ItemStack itemStack = inventory.getStackInSlot(slot);
        if (itemStack != null) {
            if (itemStack.stackSize <= count) {
                setInventorySlotContents(slot, null);
            }
            itemStack = itemStack.splitStack(count);
        }
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        return itemStack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        ItemStack stack = getStackInSlot(slot);
        if (stack != null) {
            setInventorySlotContents(slot, null);
        }
        return stack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inventory.setStackInSlot(stack, slot);
    }

    /**
     * Returns the name of the inventory
     */
    public String getInventoryName() {
        return null;
    }

    /**
     * Returns if the inventory is named
     */
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    /**
     * Do not give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return slot == 0 && stack.getItem() instanceof ItemStorageRemote;
    }

    /*******************************************************************************************************************
     *********************************************** Tile Methods ******************************************************
     *******************************************************************************************************************/

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (getCore() != null && inventory.getStackInSlot(0) != null && inventory.getStackInSlot(1) == null &&
                inventory.getStackInSlot(0).getItem() instanceof ItemStorageRemote) {
            if (inventory.getStackInSlot(0).stackTagCompound == null)
                inventory.getStackInSlot(0).stackTagCompound = new NBTTagCompound();
            inventory.getStackInSlot(0).stackTagCompound.setInteger("coreX", getCore().xCoord);
            inventory.getStackInSlot(0).stackTagCompound.setInteger("coreY", getCore().yCoord);
            inventory.getStackInSlot(0).stackTagCompound.setInteger("coreZ", getCore().zCoord);

            inventory.setStackInSlot(inventory.getStackInSlot(0).copy(), 1);
            inventory.setStackInSlot(null, 0);
        }
    }

    @Override
    public void readFromNBT (NBTTagCompound tag) {
        super.readFromNBT(tag);
        inventory.readFromNBT(tag);
    }

    @Override
    public void writeToNBT (NBTTagCompound tag) {
        super.writeToNBT(tag);
        inventory.writeToNBT(tag);
    }

    /*******************************************************************************************************************
     ****************************************** IOpensGui Methods ******************************************************
     *******************************************************************************************************************/

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return getCore() != null ? (player.isSneaking() && getCore().canOpen(player) ?
                new ContainerStorageRemote(player.inventory, this) :
                getCore().getServerGuiElement(ID, player, world, x, y, z)) : null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return getCore() != null ? (player.isSneaking() && getCore().canOpen(player) ?
                new GuiStorageRemote(new ContainerStorageRemote(player.inventory, this)) :
                getCore().getClientGuiElement(ID, player, world, x, y, z)) : null;
    }
}
