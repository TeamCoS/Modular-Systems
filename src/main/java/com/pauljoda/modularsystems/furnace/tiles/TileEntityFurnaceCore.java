package com.pauljoda.modularsystems.furnace.tiles;

import com.pauljoda.modularsystems.core.functions.BlockCountFunction;
import com.pauljoda.modularsystems.core.registries.BlockValueRegistry;
import com.pauljoda.modularsystems.core.registries.FurnaceBannedBlocks;
import com.pauljoda.modularsystems.core.tiles.AbstractCore;
import com.pauljoda.modularsystems.core.waila.IWaila;
import com.pauljoda.modularsystems.furnace.blocks.BlockFurnaceCore;
import com.pauljoda.modularsystems.furnace.blocks.BlockDummy;
import com.pauljoda.modularsystems.furnace.container.ContainerModularFurnace;
import com.pauljoda.modularsystems.furnace.gui.GuiModularFurnace;
import com.teambr.bookshelf.common.tiles.IOpensGui;
import com.teambr.bookshelf.helpers.BlockHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;

import java.util.List;

public class TileEntityFurnaceCore extends AbstractCore implements IOpensGui, IWaila {

    @Override
    protected void updateBlockState(boolean positiveBurnTime, World world, int x, int y, int z) {
        BlockFurnaceCore.updateFurnaceBlockState(positiveBurnTime, world, x, y, z);
    }

    @Override
    protected ItemStack recipe(ItemStack is) {
        return is == null ? null : FurnaceRecipes.smelting().getSmeltingResult(is);
    }

    @Override
    protected int getItemBurnTime(ItemStack is) {
        return is == null ? 0 : TileEntityFurnace.getItemBurnTime(is);
    }

    @Override
    @SuppressWarnings("all")
    protected boolean isBlockBanned(Block block, int meta) {
        return block instanceof BlockDummy ? false : FurnaceBannedBlocks.INSTANCE.isBlockBanned(block, meta) || FurnaceBannedBlocks.isBadBlockFromBlock(block);
    }

    @Override
    protected void generateValues(BlockCountFunction function) {
        for(String i : function.getBlockIds()) {
            if (BlockValueRegistry.INSTANCE.isBlockRegistered(BlockHelper.getBlockFromString(i).getFirst(), BlockHelper.getBlockFromString(i).getSecond())) {
                values.addToSpeed(BlockValueRegistry.INSTANCE.getSpeedValue(BlockHelper.getBlockFromString(i).getFirst(), BlockHelper.getBlockFromString(i).getSecond(), function.getBlockCount(BlockHelper.getBlockFromString(i).getFirst(), BlockHelper.getBlockFromString(i).getSecond())));
                values.addToEfficiency(BlockValueRegistry.INSTANCE.getEfficiencyValue(BlockHelper.getBlockFromString(i).getFirst(), BlockHelper.getBlockFromString(i).getSecond(), function.getBlockCount(BlockHelper.getBlockFromString(i).getFirst(), BlockHelper.getBlockFromString(i).getSecond())));
                values.addToMultiplicity(BlockValueRegistry.INSTANCE.getMultiplicityValue(BlockHelper.getBlockFromString(i).getFirst(), BlockHelper.getBlockFromString(i).getSecond(), function.getBlockCount(BlockHelper.getBlockFromString(i).getFirst(), BlockHelper.getBlockFromString(i).getSecond())));
            }
        }
    }

    @Override
    public Object getServerGuiElement(int i, EntityPlayer entityPlayer, World world, int i1, int i2, int i3) {
        return new ContainerModularFurnace(entityPlayer.inventory, this);
    }

    @Override
    public Object getClientGuiElement(int i, EntityPlayer entityPlayer, World world, int i1, int i2, int i3) {
        return new GuiModularFurnace(entityPlayer.inventory, this);
    }

    /*******************************************************************************************************************
     *************************************************** Waila *********************************************************
     *******************************************************************************************************************/

    @Override
    public void returnWailaHead(List<String> head) {

    }

    @Override
    public void returnWailaBody(List<String> body) {

    }

    @Override
    public void returnWailaTail(List<String> tail) {

    }

}
