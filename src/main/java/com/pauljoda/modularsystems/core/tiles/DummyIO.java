package com.pauljoda.modularsystems.core.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class DummyIO extends DummyTile {

    protected boolean input = true;
    protected boolean output = true;

    /******************************************************************************************************************
     **************************************************  Tile Methods  ************************************************
     ******************************************************************************************************************/

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

    }

    /*****************************************************************************************************************
     *********************************************** Inventory methods ***********************************************
     *****************************************************************************************************************/

    @Override
    public int getSizeInventory() {
        AbstractCore core = getCore();
        return core == null ? 0 : core.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        AbstractCore core = getCore();
        return core == null ? null : core.getStackInSlot(i);
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        AbstractCore core = getCore();
        return core == null ? null : core.decrStackSize(i, j);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        AbstractCore core = getCore();
        return core == null ? null : core.getStackInSlotOnClosing(i);
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        AbstractCore core = getCore();
        if(core != null) core.setInventorySlotContents(i, itemstack);
    }

    @Override
    public int getInventoryStackLimit() {
        AbstractCore core = getCore();
        return core == null ? 0 : core.getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return getCore() != null;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        AbstractCore core = getCore();
        return core != null && core.isItemValidForSlot(i, itemstack);
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        AbstractCore core = getCore();
        return core != null && core.canInsertItem(i, itemstack, j);
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        AbstractCore core = getCore();
        return core != null && core.canExtractItem(i, itemstack, j);
    }

    @Override
    public String getInventoryName() {
        AbstractCore core = getCore();
        return core == null ? "" : core.getInventoryName();
    }

    @Override
    public boolean hasCustomInventoryName() {
        AbstractCore core = getCore();
        return core != null && core.hasCustomInventoryName();
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    @Override
    public int[] getAccessibleSlotsFromSide(int var1) {
        AbstractCore core = getCore();
        return core != null ? core.getAccessibleSlotsFromSide(var1) : new int[1];
    }
}
