package com.pauljoda.modularsystems.furnace.tiles;

import com.dyonovan.brlib.collections.Couplet;
import com.dyonovan.brlib.common.tiles.BaseTile;
import com.dyonovan.brlib.common.tiles.IOpensGui;
import com.pauljoda.modularsystems.furnace.blocks.BlockFurnaceCore;
import com.pauljoda.modularsystems.furnace.collections.FurnaceValues;
import com.pauljoda.modularsystems.furnace.container.ContainerModularFurnace;
import com.pauljoda.modularsystems.furnace.gui.GuiModularFurnace;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;

public class TileEntityFurnaceCore extends BaseTile implements ISidedInventory, IOpensGui {
    protected FurnaceValues values;
    private static final int cookSpeed = 200;


    public TileEntityFurnaceCore() {
        values = new FurnaceValues();
    }

    /******************************************************************************************************************
     ************************************************  Furnace Methods  ***********************************************
     ******************************************************************************************************************/

    protected void doFurnaceWork() {
        boolean didWork = false;
        if (!worldObj.isRemote) {
            if (this.values.getBurnTime() > 0) {
                this.values.setBurnTime(values.getBurnTime() - 1);
            }

            if (canSmelt(values.getInput(), recipe(values.getInput()), values.getOutput())) {
                if (this.values.getBurnTime() <= 0 && values.getFuel() != null) {
                    int scaledBurnTime = getAdjustedBurnTime(TileEntityFurnace.getItemBurnTime(values.getFuel()));
                    values.checkInventorySlots();
                    this.values.currentItemBurnTime = this.values.burnTime = scaledBurnTime;
                    this.values.consumeFuel();
                    cook();
                    didWork = true;
                } else if (isBurning()) {
                    didWork = cook();
                } else {
                    this.values.cookTime = 0;
                    this.values.burnTime = 0;
                    didWork = true;
                }
            }

            if (didWork) {
                updateBlockState(this.values.burnTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
                markDirty();
            }
        }
    }

    private boolean cook() {
        ++this.values.cookTime;

        if (this.values.cookTime >= getAdjustedCookTime()) {
            this.values.cookTime = 0;
            this.smeltItem();
            return true;
        } else {
            return false;
        }
    }

    private boolean canSmelt(ItemStack input, ItemStack result, ItemStack output) {
        if (input == null || result == null) {
            return false;
        } else if (output == null) {
            return true;
        } else if (!output.isItemEqual(result)) {
            return false;
        } else {
            //The size below would be if the smeltingMultiplier = 1
            //If the smelting multiplier is > 1,
            //there is no guarantee that all potential operations will be completed.
            int minStackSize = output.stackSize + result.stackSize;
            return (minStackSize <= getInventoryStackLimit() && minStackSize <= result.getMaxStackSize());
        }
    }

    private void smeltItem() {
        values.checkInventorySlots();
        Couplet<Integer, Integer> smeltCount = smeltCountAndSmeltSize();
        if (smeltCount != null && smeltCount.getSecond() > 0) {
            ItemStack recipeResult = recipe(values.getInput());
            values.getInput().stackSize -= smeltCount.getFirst();
            if (values.getOutput() == null) {
                recipeResult = recipeResult.copy();
                recipeResult.stackSize = smeltCount.getSecond();
                values.setOutput(recipeResult);
            } else {
                values.getOutput().stackSize += smeltCount.getSecond();
            }
        }
        values.checkInventorySlots();
    }

    private Couplet<Integer, Integer> smeltCountAndSmeltSize() {
        ItemStack input = values.getInput();
        if (input == null) {
            return null;
        }
        ItemStack output = values.getOutput();
        ItemStack recipeResult = recipe(input);
        if (recipeResult == null) {
            return null;
        } else if (output != null && !output.isItemEqual(recipeResult)) {
            return null;
        } else if (output == null) {
            output = recipeResult.copy();
            output.stackSize = 0;
        }

        input = input.copy();

        int recipeStackSize = recipeResult.stackSize > 0 ? recipeResult.stackSize : 1;

        int outMax =
                getInventoryStackLimit() < output.getMaxStackSize()
                        ? output.getMaxStackSize()
                        : getInventoryStackLimit();
        int outAvailable = outMax - output.stackSize;
        int smeltAvailable = (int)values.getMultiplicity() * recipeStackSize;
        int inAvailable = input.stackSize * recipeStackSize;

        int avail;
        int count;

        if (smeltAvailable < inAvailable) {
            avail = smeltAvailable;
            count = (int)values.getMultiplicity();
        } else {
            avail = inAvailable;
            count = input.stackSize;
        }

        if (avail > outAvailable) {
            //If there is a remainder, this results in a difference from just outputting outAvailable
            count = outAvailable / recipeStackSize;
            avail = count * recipeStackSize;
        }

        values.checkInventorySlots();

        return new Couplet<>(count, avail);
    }

    public boolean isBurning() {
        return values.burnTime > 0;
    }

    protected void updateBlockState(boolean positiveBurnTime, World world, int x, int y, int z) {
        BlockFurnaceCore.updateFurnaceBlockState(positiveBurnTime, world, x, y, z);
    }

    protected ItemStack recipe(ItemStack is) {
        return is == null ? null : FurnaceRecipes.smelting().getSmeltingResult(is);
    }

    public int getAdjustedBurnTime(double fuelValue) {
        return (int) (fuelValue * values.getEfficiency());
    }

    private double getAdjustedCookTime() {
       return cookSpeed * values.getSpeed();
    }

    @SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int scaleVal) {
        return (int) ((this.values.cookTime * scaleVal) / getAdjustedCookTime());
    }

    @SideOnly(Side.CLIENT)
    public int getBurnTimeRemainingScaled(int scaleVal) {
        return (values.burnTime * scaleVal) / this.values.currentItemBurnTime;
    }

    /**
     * ***************************************************************************************************************
     * *************************************************  Tile Methods  ************************************************
     * ****************************************************************************************************************
     */

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            //updateMultiblock();
        }
        doFurnaceWork();
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
