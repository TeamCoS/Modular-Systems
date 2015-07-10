package com.pauljoda.modularsystems.furnace.collections;

import com.teambr.bookshelf.collections.InventoryTile;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class StandardValues {
    public int burnTime = 0;
    public int currentItemBurnTime = 0;
    public int cookTime = 0;

    public double speed = 0;
    public double efficiency = 0;
    public double multiplicity = 0;

    InventoryTile inventory;

    public StandardValues() {
        inventory = new InventoryTile(2);
    }

    public void resetStructureValues() {
        speed = efficiency = multiplicity = 0;
    }

    /*******************************************************************************************************************
     ****************************************** Furnace Values *********************************************************
     *******************************************************************************************************************/

    /**
     * Get the current burn time for the furnace
     * @return How long in ticks the furnace has been burning
     */
    public int getBurnTime() {
        return burnTime;
    }

    /**
     * Set the furnace burn time
     * @param burnTime New burn time
     */
    public void setBurnTime(int burnTime) {
        this.burnTime = burnTime;
    }

    /**
     * How long the current item has been burning, used in GUI flames
     * @return How long the current item has been burning
     */
    public int getCurrentItemBurnTime() {
        return currentItemBurnTime;
    }

    /**
     * Set how long the current item is said to burn
     * @param currentItemBurnTime How many ticks the current item burning would burn
     */
    public void setCurrentItemBurnTime(int currentItemBurnTime) {
        this.currentItemBurnTime = currentItemBurnTime;
    }

    /**
     * Get how long the current item has been cooking
     * @return How many ticks the item has been burning
     */
    public int getCookTime() {
        return cookTime;
    }

    /**
     * Set how long the furnace has been cooking
     * @param cookTime How many ticks the furnace has been cooking
     */
    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }

    /**
     * Get the speed multiplier for the furnace
     * @return A speed multiplier for this set
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Set the speed multiplier
     * @param speed Speed multiplier to set to
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * Add a value to the speed
     * @param i What to add
     */
    public void addToSpeed(double i) {
        this.speed += i;
    }

    /**
     * Get the efficiency multiplier for this furnace
     * @return The efficiency multiplier
     */
    public double getEfficiency() {
        return efficiency;
    }

    /**
     * Set the current efficiency multiplier
     * @param efficiency Efficiency multiplier to set
     */
    public void setEfficiency(double efficiency) {
        this.efficiency = efficiency;
    }

    /**
     * Add to the efficiency value
     * @param i The value to add
     */
    public void addToEfficiency(double i) {
        this.efficiency += i;
    }

    /**
     * Multiplicity multiplier for this furnace (heh)
     * @return Multiplicity multiplier
     */
    public double getMultiplicity() {
        return multiplicity;
    }

    /**
     * Set the current multiplicity multiplier
     * @param multiplicity The new multiplicity multiplier
     */
    public void setMultiplicity(double multiplicity) {
        this.multiplicity = multiplicity;
    }

    /**
     * Add to the multiplicity value
     * @param i The value to add
     */
    public void addToMultiplicity(double i) {
        this.multiplicity += i;
    }

    /*******************************************************************************************************************
     ***************************************** Inventory Values ********************************************************
     *******************************************************************************************************************/

    /**
     * Set the itemstack into a slot
     *
     * 0 - Input
     * 1 - Output
     *
     * @param slot What slot to insert into
     * @param stack The stack to set
     */
    public void setStackInSlot(int slot, ItemStack stack) {
        if (slot == 0) {
            setInput(stack);
        } else if (slot == 1) {
            setOutput(stack);
        }
    }

    /**
     * Clear the current inventory
     */
    public void resetInventory() {
        inventory.clear();
    }

    /**
     * Get the {@link ItemStack} in the input slot
     * @return {@link ItemStack} In the input slot
     */
    public ItemStack getInput() {
        return inventory.getStackInSlot(0);
    }

    /**
     * Set the current input
     * @param is {@link ItemStack} to set
     */
    public void setInput(ItemStack is) {
        inventory.setStackInSlot(is, 0);
    }

    /**
     * Get the ItemStack in the output slot
     * @return The ItemStack in the output slot
     */
    public ItemStack getOutput() {
        return inventory.getStackInSlot(1);
    }

    /**
     * Set the output
     * @param is The ItemStack to set into the output
     */
    public void setOutput(ItemStack is) {
        inventory.setStackInSlot(is, 1);
    }

    /**
     * Makes sure the inventory is all valid
     */
    public void checkInventorySlots() {
        checkInput();
        checkOutput();
    }

    /**
     * Checks to make sure the input is valid, clears if below 0
     */
    public void checkInput() {
        if (getInput() != null && getInput().stackSize <= 0) {
            setInput(null);
        }
    }

    /**
     * Checks to make sure the output is valid, clears if below 0
     */
    public void checkOutput() {
        if (getOutput() != null && getOutput().stackSize <= 0) {
            setOutput(null);
        }
    }

    public void readFromNBT(NBTTagCompound tagCompound) {
        setBurnTime(tagCompound.getInteger("Burn Time"));
        setCookTime(tagCompound.getInteger("Cook Time"));
        setCurrentItemBurnTime(tagCompound.getInteger("Current Burn"));

        setSpeed(tagCompound.getDouble("Speed"));
        setEfficiency(tagCompound.getDouble("Efficiency"));
        setMultiplicity(tagCompound.getDouble("Multiplicity"));

        inventory.readFromNBT(tagCompound, 3);
    }

    public void writeToNBT(NBTTagCompound tagCompound) {
        tagCompound.setInteger("Burn Time", getBurnTime());
        tagCompound.setInteger("Cook Time", getCookTime());
        tagCompound.setInteger("Current Burn", getCurrentItemBurnTime());

        tagCompound.setDouble("Speed", getSpeed());
        tagCompound.setDouble("Efficiency", getEfficiency());
        tagCompound.setDouble("Multiplicity", getMultiplicity());

        inventory.writeToNBT(tagCompound);
    }
}
