package com.teamcos.modularsystems.oreprocessing.tiles;

import com.teamcos.modularsystems.core.lib.Reference;
import com.teamcos.modularsystems.functions.WorldFunction;
import com.teamcos.modularsystems.helpers.Coord;
import com.teamcos.modularsystems.helpers.LocalBlockCollections;
import com.teamcos.modularsystems.manager.ApiBlockManager;
import com.teamcos.modularsystems.oreprocessing.blocks.BlockSmelteryCore;
import com.teamcos.modularsystems.registries.OreProcessingRegistry;
import com.teamcos.modularsystems.utilities.tiles.FueledRecipeTile;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Arrays;

public class TileEntitySmelteryCore extends FueledRecipeTile {

    //Automation related
    private static final int[] slots_top = new int[]{0};
    private static final int[] slots_bottom = new int[]{2, 1};
    private static final int[] slots_sides = new int[]{1};
    private static final int[] slots_output_only = new int[]{2};

    //Empty Constructor
    public TileEntitySmelteryCore() {
        super();
    }

    public void updateBlockState(boolean positiveBurnTime, World world, int x, int y, int z) {
        BlockSmelteryCore.updateFurnaceBlockState(positiveBurnTime, world, x, y, z);
    }

    @Override
    protected int getItemBurnTime(ItemStack is) {
        return is == null ? 0 : GameRegistry.getFuelValue(is);
    }

    @Override
    protected ItemStack recipe(ItemStack is) {
        return OreProcessingRegistry.getOutput(is);
    }

    @Override
    public Block getOverlay() {
        return ApiBlockManager.smelteryOverlay;
    }

    @Override
    public Block getDummyBlock() {
        return ApiBlockManager.dummyBlock;
    }

    @Override
    public boolean exploreWorld(WorldFunction function) {
        LocalBlockCollections.searchCuboidMultiBlock(worldObj, xCoord, yCoord, zCoord, function, Reference.MAX_FURNACE_SIZE);
        return function.shouldContinue();
    }

    @Override
    public boolean exploreWorld(WorldFunction function, Coord c1, Coord c2) {
        LocalBlockCollections.searchCuboidMultiBlock(worldObj, xCoord, yCoord, zCoord, function, Reference.MAX_FURNACE_SIZE);
        return function.shouldContinue();
    }

    //Reworked for I/O
    @Override
    public int[] getAccessibleSlotsFromSide(int par1)
    {
        switch (par1)
        {
            case 0:
                return Arrays.copyOf(slots_bottom, slots_bottom.length);
            case 1:
                return Arrays.copyOf(slots_top, slots_top.length);
            case 2:
                return Arrays.copyOf(slots_output_only, slots_output_only.length);
            default:
                return Arrays.copyOf(slots_sides, slots_sides.length);
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

    public double getEfficiency() {
        return values.getEfficiency();
    }

    public int getSmeltingMultiplier() {
        return values.getSmeltingMultiplier();
    }

    public void checkInventorySlots() {
        values.checkInventorySlots();
    }
}
