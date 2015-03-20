package com.teamcos.modularsystems.utilities.tiles;

import com.teamcos.modularsystems.fuelprovider.FuelProvider;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

public class DummyEUTile extends DummyTile implements FuelProvider, IEnergySink {

    public static final int EU_PROCESS = 20;
    public static final int MAX_INPUT = 32;
    public static final int MAX_ENERGY = 64;
    public static final int FUEL_USAGE = 80;

    public double energyEU = 0.0D;
    private boolean addedToEnergyNet = false;

    public DummyEUTile() {

    }

    /*******************************************************************************************************************
     **********************************************Fuel Provider Functions *********************************************
     ******************************************************************************************************************/

    @Override
    public boolean canProvide() {
        return energyEU > EU_PROCESS;
    }

    @Override
    public double fuelProvided() {
        return FUEL_USAGE;
    }

    @Override
    public double consume() {
        if (energyEU - EU_PROCESS >= 0) {
            energyEU -= EU_PROCESS;
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            return FUEL_USAGE;
        }
        return 0;
    }

    @Override
    public FuelProviderType type() {
        return FuelProviderType.POWER;
    }

    /*******************************************************************************************************************
     ************************************************* EU Functions ****************************************************
     ******************************************************************************************************************/

    @Override
    public double getDemandedEnergy() {
        if (energyEU < MAX_ENERGY / 2) return (double) MAX_ENERGY - this.energyEU;
        return 0;
    }

    @Override
    public int getSinkTier() {
        return 1; //LV
    }

    @Override
    public double injectEnergy(ForgeDirection directionFrom, double amount, double voltage) {
        if (amount > MAX_INPUT) return 0;

        energyEU += amount;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        return 0;
    }

    @Override
    public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction) {
        return true;
    }

    @Override
    public void invalidate() {
        removeFromEnergyNet();
        super.invalidate();
    }

    @Override
    public void onChunkUnload() {
        removeFromEnergyNet();
        super.onChunkUnload();
    }

    void removeFromEnergyNet() {
        if (addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            addedToEnergyNet = false;
        }
    }

    /*******************************************************************************************************************
     ************************************************ Tile Functions ***************************************************
     ******************************************************************************************************************/

    @Override
    public void readFromNBT (NBTTagCompound tags)
    {
        super.readFromNBT(tags);
        energyEU = tags.getDouble("EU");
    }

    @Override
    public void writeToNBT (NBTTagCompound tags)
    {
        super.writeToNBT(tags);
        tags.setDouble("EU", energyEU);
    }

    public void updateEntity() {
        if (!worldObj.isRemote) {
            if (!this.addedToEnergyNet) {
                MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
                this.addedToEnergyNet = true;
            }
        }
    }
}
