package com.teamcos.modularsystems.utilities.tiles;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import com.teamcos.modularsystems.fuelprovider.FuelProvider;
import com.teamcos.modularsystems.manager.ApiBlockManager;
import com.teamcos.modularsystems.utilities.block.DummyBlock;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class DummyRFTile extends DummyTile implements IEnergyHandler, FuelProvider {

    public static final int RF_TICK = 80;

    protected EnergyStorage energyRF;

    public DummyRFTile() {
        energyRF = new EnergyStorage(32000, 1000, 1000);
    }

    public DummyBlock getBlock() {
        return ApiBlockManager.dummyRFBlock;
    }

    /*******************************************************************************************************************
     ************************************************* RF Functions ****************************************************
     ******************************************************************************************************************/
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

    /*******************************************************************************************************************
     **********************************************Fuel Provider Functions *********************************************
     ******************************************************************************************************************/
    @Override
    public boolean canProvide() {
        return energyRF.getEnergyStored() > RF_TICK;
    }

    @Override
    public double fuelProvided() {
        return RF_TICK;
    }

    @Override
    public double consume() {
        int actual = energyRF.extractEnergy(RF_TICK, false);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        return actual;
    }

    @Override
    public FuelProviderType type() {
        return FuelProviderType.POWER;
    }

    /*******************************************************************************************************************
     *********************************************** Tile Functions ****************************************************
     ******************************************************************************************************************/
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

    /* Packets */
    @Override
    public Packet getDescriptionPacket ()
    {
        NBTTagCompound tag = new NBTTagCompound();
        this.writeToNBT(tag);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tag);
    }

    @Override
    public void onDataPacket (NetworkManager net, S35PacketUpdateTileEntity packet)
    {
        this.readFromNBT(packet.func_148857_g());
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
}