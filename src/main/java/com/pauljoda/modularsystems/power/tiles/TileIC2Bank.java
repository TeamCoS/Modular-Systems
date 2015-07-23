package com.pauljoda.modularsystems.power.tiles;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import com.pauljoda.modularsystems.core.registries.ConfigRegistry;
import com.pauljoda.modularsystems.power.gui.GuiEUBank;
import com.teambr.bookshelf.common.tiles.IOpensGui;
import com.teambr.bookshelf.helpers.GuiHelper;
import com.teambr.bookshelf.inventory.ContainerGeneric;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class TileIC2Bank extends TilePowerBase implements IOpensGui, IEnergySink, IEnergyHandler {

    private EnergyStorage energy;

    private boolean firstRun;

    public TileIC2Bank() {
        energy = new EnergyStorage(32000);
        firstRun = true;
    }

    @Override
    public void updateEntity() {
        if (worldObj.isRemote) return;
        if (firstRun) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            firstRun = false;
        }
    }

    @Override
    public int getPowerLevelScaled(int scale) {
        return energy.getEnergyStored() * scale / energy.getMaxEnergyStored();
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
        int actual = energy.extractEnergy((int)Math.round(ConfigRegistry.EUPower * 200), true);
        return (actual / (ConfigRegistry.EUPower * 200)) * 200;
    }

    @Override
    public double consume() {
        int actual = energy.extractEnergy((int)Math.round(ConfigRegistry.EUPower * 200), false);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        return (actual / (ConfigRegistry.EUPower * 200)) * 200;
    }

    /*
     * GUI Functions
     */
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new ContainerGeneric();
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new GuiEUBank(this);
    }

    /*
     * IC2 EnergyNet
     */
    @Override
    public double getDemandedEnergy() {
        if (getCore() != null)
            return (double) energy.getMaxEnergyStored() - energy.getEnergyStored();

        return 0;
    }

    @Override
    public int getSinkTier() {
        return 1; //LV
    }

    @Override
    public double injectEnergy(ForgeDirection forgeDirection, double v, double v1) {
        if (getCore() != null) {
            energy.receiveEnergy((int)Math.floor(v + 0.5d), false);
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
        return 0;
    }

    @Override
    public boolean acceptsEnergyFrom(TileEntity tileEntity, ForgeDirection forgeDirection) {
        return true;
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
     * Tile Entity Functions
     */
    @Override
    public void onChunkUnload()
    {
        if (!worldObj.isRemote)
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        super.onChunkUnload();
    }

    @Override
    public void invalidate()
    {
        if (!worldObj.isRemote)
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        super.invalidate();
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
     * Waila Functions
     */
    @Override
    public void returnWailaHead(List<String> list) {
        list.add(GuiHelper.GuiColor.YELLOW + "Available Power: " + GuiHelper.GuiColor.WHITE + energy.getEnergyStored() + "/" + energy.getMaxEnergyStored());
    }
}
