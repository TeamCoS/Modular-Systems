package com.pauljoda.modularsystems.generator.tiles;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import com.pauljoda.modularsystems.core.blocks.BlockDummy;
import com.pauljoda.modularsystems.core.functions.BlockCountFunction;
import com.pauljoda.modularsystems.core.managers.BlockManager;
import com.pauljoda.modularsystems.core.registries.BlockValueRegistry;
import com.pauljoda.modularsystems.core.registries.FurnaceBannedBlocks;
import com.pauljoda.modularsystems.core.tiles.AbstractCore;
import com.pauljoda.modularsystems.generator.blocks.BlockGeneratorCore;
import com.pauljoda.modularsystems.generator.container.ContainerGenerator;
import com.pauljoda.modularsystems.generator.gui.GuiGenerator;
import com.teambr.bookshelf.common.tiles.IOpensGui;
import com.teambr.bookshelf.helpers.BlockHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class TileGeneratorCore extends AbstractCore implements IOpensGui, IEnergyHandler {

    protected EnergyStorage energy;

    public TileGeneratorCore() {
        energy = new EnergyStorage(32000);
    }

    @Override
    public void doWork() {

    }

    @Override
    protected void updateBlockState(boolean positiveBurnTime, World world, int x, int y, int z) {
        BlockGeneratorCore.updateGeneratorBlockState(positiveBurnTime, world, x, y, z);
    }

    @Override
    protected ItemStack recipe(ItemStack is) {
        return null;
    }

    @Override
    protected boolean isBlockBanned(Block block, int meta) {
        return !(block instanceof BlockDummy) && (FurnaceBannedBlocks.INSTANCE.isBlockBanned(block, meta) || FurnaceBannedBlocks.isBadBlockFromBlock(block));
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

        for(String i : function.getMaterialStrings()) {
            if(BlockValueRegistry.INSTANCE.isMaterialRegistered(i)) {
                values.addToSpeed(BlockValueRegistry.INSTANCE.getSpeedValueMaterial(i, function.getMaterialCount(i)));
                values.addToEfficiency(BlockValueRegistry.INSTANCE.getEfficiencyValueMaterial(i, function.getMaterialCount(i)));
                values.addToMultiplicity(BlockValueRegistry.INSTANCE.getMultiplicityValueMaterial(i, function.getMaterialCount(i)));
            }
        }
    }

    @Override
    protected Block getOnBlock() {
        return BlockManager.crusherCoreActive;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new ContainerGenerator(player.inventory, this);
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new GuiGenerator(player.inventory, this);
    }


    /*
     * Energy Functions
     */

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        return energy.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
        return energy.extractEnergy(maxExtract, simulate);
    }

    @Override
    public int getEnergyStored(ForgeDirection from) {
        return energy.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {
        return energy.getMaxEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from) {
        return false;
    }
}
