package com.pauljoda.modularsystems.furnace.tiles;

import com.dyonovan.brlib.common.tiles.BaseTile;
import com.dyonovan.brlib.common.tiles.IOpensGui;
import com.pauljoda.modularsystems.furnace.collections.FurnaceValues;
import com.pauljoda.modularsystems.furnace.container.ContainerModularFurnace;
import com.pauljoda.modularsystems.furnace.gui.GuiModularFurnace;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class TileEntityFurnaceCore extends BaseTile implements ISidedInventory, IOpensGui {
    protected FurnaceValues values;

    public TileEntityFurnaceCore() {
        values = new FurnaceValues();
    }

    /**
     * ****************************************************************************************************************
     * ********************************************** Inventory methods ***********************************************
     * ****************************************************************************************************************
     */

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        return this.isItemValidForSlot(slot, stack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        return side != 0 || slot != 1 || stack.getItem() == Items.bucket;
    }

    @Override
    public int getSizeInventory() {
        return 3;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        ItemStack temp = null;
        if (slot == 0) {
            temp = values.getInput();
        } else if (slot == 1) {
            temp = values.getFuel();
        } else if (slot == 2) {
            temp = values.getOutput();
        }
        return temp;
    }

    @Override
    public ItemStack decrStackSize(int slot, int count) {
        ItemStack is = getStackInSlot(slot);
        if (is != null) {
            is = is.splitStack(count);
            values.checkInventorySlots();
            return is;
        } else {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        ItemStack is = getStackInSlot(slot);
        values.setStackInSlot(slot, null);
        return is;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack) {
        if (slot == 0) {
            values.setInput(itemStack);
        } else if (slot == 1) {
            values.setFuel(itemStack);
        } else if (slot == 2) {
            values.setOutput(itemStack);
        }

        if (itemStack != null && itemStack.stackSize > getInventoryStackLimit())
            itemStack.stackSize = getInventoryStackLimit();
    }

    @Override
    public String getInventoryName() {
        return null;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return true;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        if (slot == 2)
            return false;
        if (slot == 1 && isItemFuel(stack))
            return true;
        return slot == 0;
    }

    /**
     * ***************************************************************************************************************
     * ************************************************  Helper Methods  ***********************************************
     * ****************************************************************************************************************
     */

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        values.readFromNBT(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        values.writeToNBT(tagCompound);
    }

    protected boolean isItemFuel(ItemStack par0ItemStack) {
        return getItemBurnTime(par0ItemStack) > 0;
    }

    protected int getItemBurnTime(ItemStack is) {
        return is == null ? 0 : GameRegistry.getFuelValue(is);
    }

    public int getFurnaceBurnTime() {
        return values.getBurnTime();
    }

    public int getCurrentItemBurnTime() {
        return values.getCurrentItemBurnTime();
    }

    public int getFurnaceCookTime() {
        return values.getCookTime();
    }

    public void setFurnaceBurnTime(int furnaceBurnTime) {
        values.setBurnTime(furnaceBurnTime);
    }

    public void setCurrentItemBurnTime(int currentItemBurnTime) {
        values.setCurrentItemBurnTime(currentItemBurnTime);
    }

    public void setFurnaceCookTime(int furnaceCookTime) {
        values.setCookTime(furnaceCookTime);
    }

    @Override
    public Object getServerGuiElement(int i, EntityPlayer entityPlayer, World world, int i1, int i2, int i3) {
        return new ContainerModularFurnace(entityPlayer.inventory, this);
    }

    @Override
    public Object getClientGuiElement(int i, EntityPlayer entityPlayer, World world, int i1, int i2, int i3) {
        return new GuiModularFurnace(entityPlayer.inventory, this);
    }
}
