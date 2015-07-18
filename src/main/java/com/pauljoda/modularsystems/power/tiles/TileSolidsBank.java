package com.pauljoda.modularsystems.power.tiles;

import cofh.api.energy.EnergyStorage;
import com.pauljoda.modularsystems.power.container.ContainerSolidsBank;
import com.pauljoda.modularsystems.power.gui.GuiSolidsBank;
import com.teambr.bookshelf.collections.InventoryTile;
import com.teambr.bookshelf.common.tiles.IOpensGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;

public class TileSolidsBank extends TilePowerBase implements IOpensGui, ISidedInventory {

    public static final int POWER_PROCESS = 200;

    private InventoryTile inventory;
    private int cooldown;

    public TileSolidsBank() {
        energy = new EnergyStorage(20000);
        inventory = new InventoryTile(27);
        cooldown = 0;
    }

    @Override
    public void updateEntity()
    {
        if (worldObj.isRemote) return;

        if (cooldown >= 0)
            cooldown++;

        if (energy.getEnergyStored() < energy.getMaxEnergyStored() && cooldown >= 20) {
            cooldown = 0;
            for (int i = 0; i < 27; i++) {
                if (inventory.getStackInSlot(i) != null) {
                    int value = TileEntityFurnace.getItemBurnTime(inventory.getStackInSlot(i));
                    if (value <= 0) continue;
                    if (value + energy.getEnergyStored() < energy.getMaxEnergyStored()) {
                        energy.modifyEnergyStored(value);
                        decrStackSize(i, 1);
                        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    }
                }
            }
        }
    }

    /*
     * Fuel Provider Functions
     */

    @Override
    public double fuelProvided() {
        return energy.extractEnergy(POWER_PROCESS, true);
    }

    @Override
    public double consume() {
        int actual = energy.extractEnergy(POWER_PROCESS, false);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        return actual;
    }

    /*
     * Tile Entity Functions
     */

    @Override
    public void readFromNBT (NBTTagCompound tags)
    {
        super.readFromNBT(tags);
        inventory.readFromNBT(tags);
        cooldown = tags.getInteger("cooldown");
    }

    @Override
    public void writeToNBT (NBTTagCompound tags)
    {
        super.writeToNBT(tags);
        inventory.writeToNBT(tags);
        tags.setInteger("cooldown", cooldown);
    }

    /*
     * Gui Functions
     */

    @Override
    public Object getServerGuiElement(int i, EntityPlayer entityPlayer, World world, int i1, int i2, int i3) {
        return new ContainerSolidsBank(entityPlayer.inventory, this);
    }

    @Override
    public Object getClientGuiElement(int i, EntityPlayer entityPlayer, World world, int i1, int i2, int i3) {
        return new GuiSolidsBank(entityPlayer.inventory, this);
    }

    /*
     * Inventory Functions
     */
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

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return TileEntityFurnace.isItemFuel(itemstack);
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        return getCore() != null && isItemValidForSlot(i, itemstack);
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return false;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int var1) {
        int[] sides = new int[inventory.getSizeInventory()];
        for (int x = 0; x < inventory.getSizeInventory(); x++) {
            sides[x] = x;
        }
        return sides;
    }
}
