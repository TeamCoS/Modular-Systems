package com.teamcos.modularsystems.furnace.tiles;

import com.teamcos.modularsystems.core.helper.ConfigHelper;
import com.teamcos.modularsystems.core.lib.Reference;
import com.teamcos.modularsystems.furnace.blocks.BlockFurnaceCore;
import com.teamcos.modularsystems.manager.ApiBlockManager;
import com.teamcos.modularsystems.utilities.tiles.FueledRecipeTile;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class TileEntityFurnaceCore extends FueledRecipeTile {
    //Automation related
    private static final int[] slots_top = new int[]{0};
    private static final int[] slots_bottom = new int[]{2, 1};
    private static final int[] slots_sides = new int[]{1};
    private static final int[] slots_output_only = new int[]{2};

    //Empty Constructor
    public TileEntityFurnaceCore() {
        super(Reference.MAX_FURNACE_SIZE);
    }

    @Override
    protected void updateBlockState(boolean positiveBurnTime, World world, int x, int y, int z) {
        BlockFurnaceCore.updateFurnaceBlockState(positiveBurnTime, world, x, y, z);
    }

    protected int getItemBurnTime(ItemStack is) {
        return is == null ? 0 : GameRegistry.getFuelValue(is);
    }

    protected ItemStack recipe(ItemStack is) {
        return is == null ? null : FurnaceRecipes.smelting().getSmeltingResult(is);
    }

    @Override
    public int getMaxSize() {
        return ConfigHelper.maxFurnaceSize;
    }

    @Override
    public Block getOverlay() {
        return ApiBlockManager.furnaceOverlay;
    }

    @Override
    public Block getDummyBlock() {
        return ApiBlockManager.dummyBlock;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
    }

    //Reworked for I/O
    @Override
    public int[] getAccessibleSlotsFromSide(int par1) {
        switch (par1) {
            case 0:
                return slots_bottom;
            case 1:
                return slots_top;
            case 2:
                return slots_output_only;
            default:
                return slots_sides;
        }
    }

    @Override
    public World getWorld() {
        return worldObj;
    }

    @Override
    public int getX() {
        return xCoord;
    }

    @Override
    public int getY() {
        return yCoord;
    }

    @Override
    public int getZ() {
        return zCoord;
    }

    public double getSpeed() {
        return values.getSpeed();
    }

    public double getGuiSpeed() {
        return Math.max(0.001, getSpeed());
    }

    public double getEfficiency() {
        return values.getEfficiency();
    }

    public double getGuiEfficiency() {
        return Math.max(0.001, getEfficiency());
    }

    public int getSmeltingMultiplier() {
        return values.getSmeltingMultiplier();
    }

    public void checkInventorySlots() {
        values.checkInventorySlots();
    }
}

