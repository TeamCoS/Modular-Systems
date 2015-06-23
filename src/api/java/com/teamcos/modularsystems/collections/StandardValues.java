package com.teamcos.modularsystems.collections;

import com.teamcos.modularsystems.functions.BlockCountFunction;
import com.teamcos.modularsystems.helpers.Coord;
import com.teamcos.modularsystems.helpers.LocalBlockCollections;
import com.teamcos.modularsystems.helpers.Locatable;
import com.teamcos.modularsystems.interfaces.MSUpgradeBlock;
import com.teamcos.modularsystems.registries.BlockValuesConfig;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StandardValues {

    private boolean validSpeed = false;
    private boolean validEfficiency = false;
    private boolean validSmelting = false;
    private boolean validCrafter = false;
    private boolean validTiles = false;

    private double speedMultiplier = 1;
    private double efficiencyMultiplier = 1;
    private int smeltingMultiplier = 1;

    private boolean hasCrafter = false;

    private BlockCountFunction blockCount;

    private ItemStack input;
    private ItemStack fuel;
    private ItemStack output;

    private Locatable entity;
    private List<Coord> tiles;
    private int maxSize;

    public StandardValues(Locatable entity, BlockCountFunction blockCount, int maxSize) {
        this.blockCount = blockCount;
        this.entity = entity;
        this.maxSize = maxSize;
    }

    public void setSpeedMultiplier(double smeltingMultiplier) {
        this.validSpeed = true;
        this.speedMultiplier = smeltingMultiplier;
    }

    public void setEfficiencyMultiplier(double efficiencyMultiplier) {
        this.validEfficiency = true;
        this.efficiencyMultiplier = efficiencyMultiplier;
    }

    public void setCrafter(boolean hasCrafter) {
        this.validCrafter = true;
        this.hasCrafter = hasCrafter;
    }

    private void setTiles(List<Coord> tiles) {
        this.validTiles = true;
        this.tiles = tiles;
    }

    public void unsetSpeedMultiplier() {
        this.validSpeed = false;
        this.speedMultiplier = 1.0;
    }

    public void unsetEfficiencyMultiplier() {
        this.validEfficiency = false;
        this.efficiencyMultiplier = 1.0;
    }

    public void unsetSmeltingMultiplier() {
        this.validSmelting = false;
        this.smeltingMultiplier = 1;
    }

    public void unsetCrafter() {
        this.validCrafter = false;
        this.hasCrafter = false;
    }

    private void unsetTiles() {
        this.validTiles = false;
        this.tiles = null;
    }

    public double getSpeed() {
        if (!validSpeed) {
            setValues();
        }
        return this.speedMultiplier;
    }

    public double getEfficiency() {
        if (!validEfficiency) {
            setValues();
        }
        return this.efficiencyMultiplier;
    }

    public int getSmeltingMultiplier() {
        if (!validSmelting) {
            setValues();
        }
        return smeltingMultiplier;
    }

    public List<Coord> getTiles() {
        if (!validTiles) {
            setValues();
        }
        return new ArrayList<Coord>(tiles);
    }

    public void setSmeltingMultiplier(int smeltingMultiplier) {
        this.validSmelting = true;
        this.smeltingMultiplier = smeltingMultiplier;
    }

    public boolean hasCrafterUpgrade() {
        if (!validCrafter) {
            setValues();
        }
        return hasCrafter;
    }

    public void setValues() {
        Values ses = getValues(entity.getWorld(), entity.getX(), entity.getY(), entity.getZ());
        setSpeedMultiplier(ses.getSpeedMultiplier());
        setEfficiencyMultiplier(ses.getEfficiencyMultiplier());
        setSmeltingMultiplier(ses.getSmeltingMultiplier());
        setCrafter(ses.isHasCrafter());
        setTiles(ses.getTiles());
    }

    public void unsetValues() {
        unsetSpeedMultiplier();
        unsetEfficiencyMultiplier();
        unsetSmeltingMultiplier();
        unsetCrafter();
    }

    private Values getValues(World worldObj, int x, int y, int z) {

        BlockCountFunction blockCount = this.blockCount.copy();
        LocalBlockCollections.searchCuboidMultiBlock(worldObj, x, y, z, blockCount, maxSize);

        double speedMultiplier = 1;
        double efficiencyMultiplier = 1;
        int smeltingMultiplier = 1;
        boolean hasCrafter = false;

        for (Map.Entry<Block, Integer> blockEntry : blockCount.getBlockCounts().entrySet()) {
            Block block = blockEntry.getKey();
            if (block instanceof MSUpgradeBlock) {
                MSUpgradeBlock upBlock = (MSUpgradeBlock) block;
                speedMultiplier += upBlock.getSpeed(worldObj, x, y, z, blockEntry.getValue());
                efficiencyMultiplier += upBlock.getEfficiency(worldObj, x, y, z, blockEntry.getValue());
                smeltingMultiplier += upBlock.getMultiplier(worldObj, x, y, z, blockEntry.getValue());
                hasCrafter |= upBlock.isCrafter();
            } else {
                speedMultiplier += BlockValuesConfig.getSpeedMultiplierForBlock(worldObj, new Coord(x, y, z), blockEntry.getKey(), blockEntry.getValue());
                efficiencyMultiplier += BlockValuesConfig.getEfficiencyMultiplierForBlock(worldObj, new Coord(x, y, z), blockEntry.getKey(), blockEntry.getValue());
                smeltingMultiplier += BlockValuesConfig.getSmeltingMultiplierForBlock(worldObj, new Coord(x, y, z), blockEntry.getKey(), blockEntry.getValue());
            }
        }
        return new Values(hasCrafter, speedMultiplier, efficiencyMultiplier, smeltingMultiplier, blockCount.getTiles());
    }

    public void setStackInSlot(int slot, ItemStack stack) {
        if (slot == 0) {
            setInput(stack);
        } else if (slot == 1) {
            setFuel(stack);
        } else if (slot == 2) {
            setOutput(stack);
        }
    }

    public void resetInventory() {
        input = null;
        output = null;
        fuel = null;
    }

    public ItemStack getInput() {
        return input;
    }

    public void setInput(ItemStack is) {
        input = is;
    }

    public ItemStack getFuel() {
        return fuel;
    }

    public void setFuel(ItemStack is) {
        fuel = is;
    }

    public void consumeFuel() {
        fuel.stackSize--;
        if (fuel.stackSize == 0) {
            setFuel(fuel.getItem().getContainerItem(fuel));
        }
    }

    public ItemStack getOutput() {
        return output;
    }

    public void setOutput(ItemStack is) {
        output = is;
    }

    public void checkInput() {
        if (getInput() != null && getInput().stackSize <= 0) {
            setInput(null);
        }
    }

    public void checkInventorySlots() {
        checkInput();
        checkOutput();
        checkFuel();
    }

    public void checkOutput() {
        if (getOutput() != null && getOutput().stackSize <= 0) {
            setOutput(null);
        }
    }

    public void checkFuel() {
        if (getFuel() != null && getFuel().stackSize <= 0) {
            setFuel(null);
        }
    }

    public void readFromNBT(NBTTagCompound tagCompound) {
        setSpeedMultiplier(tagCompound.getDouble("Speed"));
        setEfficiencyMultiplier(tagCompound.getDouble("Efficiency"));
        setCrafter(tagCompound.getBoolean("Enabled"));
        setSmeltingMultiplier(tagCompound.getInteger("SmeltingMultiplier"));
    }

    public void writeToNBT(NBTTagCompound tagCompound) {
        tagCompound.setDouble("Efficiency", getEfficiency());
        tagCompound.setBoolean("Enabled", hasCrafterUpgrade());
        tagCompound.setDouble("Speed", getSpeed());
        tagCompound.setInteger("SmeltingMultiplier", getSmeltingMultiplier());
    }
}
