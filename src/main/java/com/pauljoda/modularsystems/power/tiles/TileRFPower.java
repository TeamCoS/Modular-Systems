package com.pauljoda.modularsystems.power.tiles;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import com.pauljoda.modularsystems.core.providers.FuelProvider;
import com.pauljoda.modularsystems.furnace.container.ContainerModularFurnace;
import com.pauljoda.modularsystems.power.container.ContainerRFPower;
import com.pauljoda.modularsystems.power.gui.GuiRFPower;
import com.teambr.bookshelf.common.tiles.IOpensGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class TileRFPower extends TilePowerBase implements IEnergyHandler, FuelProvider, IOpensGui {

    public static final int RF_PROCESS = 80;

    public EnergyStorage energyRF;

    public TileRFPower() {
        energyRF = new EnergyStorage(10000);
    }

    /*
     * Fuel Provider Functions
     */

    @Override
    public boolean canProvide() {
        return energyRF.getEnergyStored() > 0;
    }

    @Override
    public double fuelProvided() {
        return energyRF.extractEnergy(RF_PROCESS, true);
    }

    @Override
    public double consume() {
        int actual = energyRF.extractEnergy(RF_PROCESS, false);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        return actual;
    }

    @Override
    public FuelProviderType type() {
        return FuelProviderType.POWER;
    }

    /*
     * RF Functions
     */

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        int actual = energyRF.receiveEnergy(maxReceive, simulate);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        return actual;
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public int getEnergyStored(ForgeDirection from) {
        return energyRF.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {
        return energyRF.getMaxEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from) {
        return true;
    }

    /*
     * Tile Entity Functions
     */

    @Override
    public void readFromNBT (NBTTagCompound tags)
    {
        super.readFromNBT(tags);
        energyRF.readFromNBT(tags);
    }

    @Override
    public void writeToNBT (NBTTagCompound tags)
    {
        super.writeToNBT(tags);
        energyRF.writeToNBT(tags);
    }

    /*
     * Gui Functions
     */

    @Override
    public Object getServerGuiElement(int i, EntityPlayer entityPlayer, World world, int i1, int i2, int i3) {
        return new ContainerRFPower(entityPlayer.inventory, this);
    }

    @Override
    public Object getClientGuiElement(int i, EntityPlayer entityPlayer, World world, int i1, int i2, int i3) {
        return new GuiRFPower(entityPlayer.inventory, this);
    }
}
