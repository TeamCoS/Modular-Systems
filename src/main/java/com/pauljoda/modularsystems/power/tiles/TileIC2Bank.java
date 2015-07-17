package com.pauljoda.modularsystems.power.tiles;

import cofh.api.energy.EnergyStorage;
import com.pauljoda.modularsystems.power.container.ContainerEUBank;
import com.pauljoda.modularsystems.power.gui.GuiEUBank;
import com.teambr.bookshelf.common.tiles.IOpensGui;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

public class TileIC2Bank extends TilePowerBase implements IOpensGui, IEnergySink {

    public static final int EU_PROCESS = 8;

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

    /*
     * Fuel Provider Functions
     */

    @Override
    public double fuelProvided() {
        return energy.extractEnergy(EU_PROCESS, true);
    }

    @Override
    public double consume() {
        int actual = energy.extractEnergy(EU_PROCESS, false);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        return actual;
    }

    /*
     * GUI Functions
     */
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new ContainerEUBank();
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




}
