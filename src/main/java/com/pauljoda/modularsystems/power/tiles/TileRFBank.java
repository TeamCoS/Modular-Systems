package com.pauljoda.modularsystems.power.tiles;

import cofh.api.energy.EnergyStorage;
import com.pauljoda.modularsystems.power.container.ContainerRFBank;
import com.pauljoda.modularsystems.power.gui.GuiRFBank;
import com.teambr.bookshelf.common.tiles.IOpensGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class TileRFBank extends TilePowerBase implements IOpensGui {

    public static final int RF_PROCESS = 80;

    public TileRFBank() {
        energy = new EnergyStorage(10000);
    }

    /*
     * Fuel Provider Functions
     */

    @Override
    public double fuelProvided() {
        return energy.extractEnergy(RF_PROCESS, true);
    }

    @Override
    public double consume() {
        int actual = energy.extractEnergy(RF_PROCESS, false);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        return actual;
    }

    /*
     * RF Functions
     */

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        int actual = energy.receiveEnergy(maxReceive, simulate);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        return actual;
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from) {
        return getCore() != null;
    }

    /*
     * Gui Functions
     */

    @Override
    public Object getServerGuiElement(int i, EntityPlayer entityPlayer, World world, int i1, int i2, int i3) {
        return new ContainerRFBank();
    }

    @Override
    public Object getClientGuiElement(int i, EntityPlayer entityPlayer, World world, int i1, int i2, int i3) {
        return new GuiRFBank(this);
    }
}