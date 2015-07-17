package com.pauljoda.modularsystems.core.tiles;

import com.pauljoda.modularsystems.core.gui.GuiIO;
import com.teambr.bookshelf.common.tiles.IOpensGui;
import com.teambr.bookshelf.helpers.GuiHelper;
import com.teambr.bookshelf.inventory.ContainerGeneric;
import com.teambr.bookshelf.util.InventoryUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class DummyIO extends DummyTile implements IOpensGui {

    public boolean input = true;
    public boolean output = true;
    public boolean auto = true;

    protected int coolDown = 80;

    public void setInput(boolean bool) {
        input = bool;
    }

    public void setOutput(boolean bool) {
        output = bool;
    }

    public void setAuto(boolean bool) {
        auto = bool;
    }


    /******************************************************************************************************************
     **************************************************  Tile Methods  ************************************************
     ******************************************************************************************************************/

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        input = tagCompound.getBoolean("Input");
        output = tagCompound.getBoolean("Output");
        auto = tagCompound.getBoolean("Auto");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setBoolean("Input", input);
        tagCompound.setBoolean("Output", output);
        tagCompound.setBoolean("Auto", auto);
    }

    @Override
    public void updateEntity() {
        if(coolDown <= 0 && getCore() != null) {
            for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                TileEntity tile = getTileInDirection(dir);
                if(tile != null && getCore() != null && (!(tile instanceof DummyTile) && !(tile instanceof AbstractCore)) && tile instanceof IInventory && auto) {
                    if(input) {
                        for(int i = 0; i < ((IInventory)tile).getSizeInventory(); i++) {
                            if (InventoryUtils.moveItemInto((IInventory) tile, i, getCore(), -1, 64, dir.getOpposite(), true, true) > 0) {
                                worldObj.markBlockForUpdate(getCore().xCoord, getCore().yCoord, getCore().zCoord);
                                return;
                            }
                        }
                    }
                    if(output) {
                        for(int i = 0; i < ((IInventory)tile).getSizeInventory(); i++) {
                            if (InventoryUtils.moveItemInto(getCore(), 1, tile, i, 64, dir.getOpposite(), true, true) > 0) {
                                worldObj.markBlockForUpdate(getCore().xCoord, getCore().yCoord, getCore().zCoord);
                                return;
                            }
                        }
                    }
                }
            }
            coolDown = 80;
            worldObj.markBlockForUpdate(getCore().xCoord, getCore().yCoord, getCore().zCoord);
        }
        coolDown--;
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
        return input && core != null && core.canInsertItem(i, itemstack, j);
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        AbstractCore core = getCore();
        return output && core != null && core.canExtractItem(i, itemstack, j);
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

    @Override
    public Object getServerGuiElement(int i, EntityPlayer entityPlayer, World world, int i1, int i2, int i3) {
        return new ContainerGeneric();
    }

    @Override
    public Object getClientGuiElement(int i, EntityPlayer entityPlayer, World world, int i1, int i2, int i3) {
        return new GuiIO(this, new ContainerGeneric(), 100, 70, "inventory.io.title");
    }

    /*
     * Waila
     */
    @Override
    public void returnWailaTail(List<String> list) {
        list.add(GuiHelper.GuiColor.ORANGE + "§oShift+Click to access GUI");
    }
}
