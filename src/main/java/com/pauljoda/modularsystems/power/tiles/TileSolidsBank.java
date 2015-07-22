package com.pauljoda.modularsystems.power.tiles;

import com.pauljoda.modularsystems.power.container.ContainerSolidsBank;
import com.pauljoda.modularsystems.power.gui.GuiSolidsBank;
import com.teambr.bookshelf.collections.InventoryTile;
import com.teambr.bookshelf.common.tiles.IOpensGui;
import com.teambr.bookshelf.helpers.GuiHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;

import java.util.List;

public class TileSolidsBank extends TilePowerBase implements IOpensGui, ISidedInventory {

    private InventoryTile inventory;
    private int cooldown;

    public TileSolidsBank() {
        inventory = new InventoryTile(27);
        cooldown = 0;
    }

    @Override
    public int getPowerLevelScaled(int scale) {
        return getFuelCount() * scale / inventory.getSizeInventory();
    }

    private int getFuelCount() {
        int count = 0;
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            if (inventory.getStackInSlot(i) != null && inventory.getStackInSlot(i).stackSize > 0)
                count++;
        }
        return count;
    }

    /*@Override
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
    }*/

    private int consumeFuel(boolean simulate) {
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            if (inventory.getStackInSlot(i) != null && inventory.getStackInSlot(i).stackSize > 0 &&
                    TileEntityFurnace.isItemFuel(inventory.getStackInSlot(i))) {
                int burnValue = TileEntityFurnace.getItemBurnTime(inventory.getStackInSlot(i));
                if (!simulate) {
                    decrStackSize(i, 1);
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                }
                return burnValue;
            }
        }
        return 0;
    }

    /*
     * Fuel Provider Functions
     */

    @Override
    public boolean canProvide() {
        return consumeFuel(true) > 0;
    }

    @Override
    public double fuelProvided() {
        return consumeFuel(true);
    }

    @Override
    public double consume() {
        return consumeFuel(false);
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

    /*
     * Waila Functions
     */
    @Override
    public void returnWailaHead(List<String> list) {
        list.add(GuiHelper.GuiColor.YELLOW + "Available Fuel Slots: " + GuiHelper.GuiColor.WHITE + getFuelCount() + "/" + inventory.getSizeInventory());
    }
}
