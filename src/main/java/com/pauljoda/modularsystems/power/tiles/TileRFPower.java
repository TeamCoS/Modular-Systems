package com.pauljoda.modularsystems.power.tiles;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import com.pauljoda.modularsystems.core.providers.FuelProvider;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class TileRFPower extends TilePowerBase implements IEnergyHandler, FuelProvider {

    public static final int RF_PROCESS = 80;
    public static final int FUEL_USAGE = 80;

    protected EnergyStorage energyRF;

    public TileRFPower() {
        energyRF = new EnergyStorage(10000);
    }

    /*
     * Fuel Provider Functions
     */

    @Override
    public boolean canProvide() {
        return energyRF.getEnergyStored() > RF_PROCESS;
    }

    @Override
    public double fuelProvided() {
        return FUEL_USAGE;
    }

    @Override
    public double consume() {
        energyRF.extractEnergy(RF_PROCESS, false);
        return FUEL_USAGE;
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
}
