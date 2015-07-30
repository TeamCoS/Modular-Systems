package com.pauljoda.modularsystems.generator.tiles;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import com.pauljoda.modularsystems.core.blocks.BlockDummy;
import com.pauljoda.modularsystems.core.functions.BlockCountFunction;
import com.pauljoda.modularsystems.core.managers.BlockManager;
import com.pauljoda.modularsystems.core.providers.FuelProvider;
import com.pauljoda.modularsystems.core.providers.IPowerProvider;
import com.pauljoda.modularsystems.core.registries.BlockValueRegistry;
import com.pauljoda.modularsystems.core.registries.ConfigRegistry;
import com.pauljoda.modularsystems.core.registries.FurnaceBannedBlocks;
import com.pauljoda.modularsystems.core.tiles.AbstractCore;
import com.pauljoda.modularsystems.generator.blocks.BlockGeneratorCore;
import com.pauljoda.modularsystems.generator.container.ContainerGenerator;
import com.pauljoda.modularsystems.generator.gui.GuiGenerator;
import com.pauljoda.modularsystems.power.tiles.TileRFBank;
import com.teambr.bookshelf.collections.Location;
import com.teambr.bookshelf.common.tiles.IOpensGui;
import com.teambr.bookshelf.helpers.BlockHelper;
import com.teambr.bookshelf.helpers.GuiHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TileGeneratorCore extends AbstractCore implements IOpensGui, IEnergyHandler, IPowerProvider {

    public final int MAX_RFTICK_OUT = 1000;
    public final int BASE = 1600;

    protected EnergyStorage energy;

    public TileGeneratorCore() {
        energy = new EnergyStorage(1000000);
    }

    @Override
    public void doWork() {
        if (!worldObj.isRemote) {
            boolean isGenerating = false;
            //Charge Bank
            if (this.values.getBurnTime() > 0) {
                this.values.setBurnTime(values.getBurnTime() - 1);
                energy.receiveEnergy(Math.max((int) Math.round(ConfigRegistry.rfPower * (values.getMultiplicity() + 1) *
                        (values.getSpeed() * -1)), 1), false);
                if (this.values.burnTime <= 0)
                    isGenerating = false;
                else
                    isGenerating = true;
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            }

            //Get Power
            if (values.getBurnTime() == 0 && ((energy.getEnergyStored() + checkRFCreation()) < energy.getMaxEnergyStored() ||
                    (double) energy.getEnergyStored() / energy.getMaxEnergyStored() < 0.5 )) {
                //Check the structure to make sure we have the right stuff
                if(corners == null)
                    corners = getCorners();

                //Still null?
                if(corners == null) {
                    markDirty();
                    return;
                }
                this.values.currentItemBurnTime = this.values.burnTime = getActBurnTime(false);
                isGenerating = true;
            }

            //TODO Charge Tools

            if(isGenerating) {
                updateBlockState(this.values.burnTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
                markDirty();
            }
        }
    }

    private int checkRFCreation() {
        return Math.max(getActBurnTime(true) * (int) Math.round(ConfigRegistry.rfPower * (values.getMultiplicity() + 1) *
                (values.getSpeed() * -1)), 1);
    }

    private int getActBurnTime(boolean simulate) {

        List<FuelProvider> providers = getFuelProviders(corners.getFirst().getAllWithinBounds(corners.getSecond(), false, true));
        if (!providers.isEmpty()) {
            int scaledTicks;
            if (simulate)
                scaledTicks = (int) Math.round(((BASE + values.getEfficiency()) / BASE) * providers.get(0).fuelProvided());
            else
                scaledTicks = (int) Math.round(((BASE + values.getEfficiency()) / BASE) * providers.get(0).consume());

            //Take into account Multiplicity
            scaledTicks = (int)Math.round(scaledTicks / (values.getMultiplicity() + 1));

            return Math.max(scaledTicks, 1);
        }
        return 0;
    }

    @Override
    protected List<FuelProvider> getFuelProviders(List<Location> coords) {
        List<FuelProvider> providers = new ArrayList<>();
        FuelProvider provider;
        for (Location coord : coords) {
            TileEntity te = worldObj.getTileEntity(coord.x, coord.y, coord.z);
            if (te != null && !(te instanceof TileRFBank)) {
                if (te instanceof FuelProvider && (provider = (FuelProvider) te).canProvide()) {
                    providers.add(provider);
                }
            }
        }

        Collections.sort(providers, new FuelProvider.FuelSorter());
        return providers;
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

    /*
     * Tile Entity Functions
     */
    @Override
    public void readFromNBT (NBTTagCompound tags) {
        super.readFromNBT(tags);
        energy.readFromNBT(tags);
    }

    @Override
    public void writeToNBT (NBTTagCompound tags) {
        super.writeToNBT(tags);
        energy.writeToNBT(tags);
    }

    /*
     * Power Supplier Functions
     */
    @Override
    public boolean canSupply() {
        return energy.getEnergyStored() > 0;
    }

    @Override
    public double fuelSupplied(int maxAmount) {
        return energy.extractEnergy(maxAmount, true);
    }

    @Override
    public double supply(int maxAmount) {
        return energy.extractEnergy(maxAmount, false);
    }

    /*
     * Waila Functions
     */
    @Override
    public void returnWailaHead(List<String> list) {
        list.add(GuiHelper.GuiColor.YELLOW + "Available Power: " + GuiHelper.GuiColor.WHITE + getEnergyStored(null)
                + "/" + getMaxEnergyStored(null) + " RF");
    }

    @Override
    public NBTTagCompound returnNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, int x, int y, int z) {
        if (tag.hasKey("Energy")) {
            tag.removeTag("Energy");
            tag.removeTag("MaxStorage");
        }
        return tag;
    }
}
