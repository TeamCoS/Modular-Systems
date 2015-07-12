package com.pauljoda.modularsystems.power.tiles;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import com.pauljoda.modularsystems.core.providers.FuelProvider;
import com.pauljoda.modularsystems.core.tiles.DummyTile;
import com.teambr.bookshelf.api.waila.IWaila;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class TilePowerBase extends DummyTile implements IEnergyHandler, FuelProvider, IWaila {

    protected EnergyStorage energy;

    public int getPowerLevelScaled(int scale) {
        return energy.getEnergyStored() * scale / energy.getMaxEnergyStored();
    }

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
     * Energy Functions
     */

    @Override
    public int receiveEnergy(ForgeDirection forgeDirection, int i, boolean b) {
        return 0;
    }

    @Override
    public int extractEnergy(ForgeDirection forgeDirection, int i, boolean b) {
        return 0;
    }

    @Override
    public int getEnergyStored(ForgeDirection forgeDirection) {
        return energy.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection forgeDirection) {
        return energy.getMaxEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection forgeDirection) {
        return false;
    }

    /*
     * Fuel Provider Functions
     */
    @Override
    public boolean canProvide() {
        return energy.getEnergyStored() > 0;
    }

    @Override
    public double fuelProvided() {
        return 0;
    }

    @Override
    public double consume() {
        return 0;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public FuelProviderType type() {
        return FuelProviderType.POWER;
    }

    /*
     * Waila Info
     */

    @Override
    public void returnWailaHead(List<String> list) {
        if (energy.getMaxEnergyStored() > 0) {
            list.add("Available Power: " + energy.getEnergyStored() + "/" + energy.getMaxEnergyStored());
            list.add("§oShift+Click to access GUI");
        }
    }

    @Override
    public void returnWailaBody(List<String> list) {

    }

    @Override
    public void returnWailaTail(List<String> list) {

    }
}
