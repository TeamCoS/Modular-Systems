package com.pauljoda.modularsystems.power.tiles;

import cofh.api.energy.EnergyStorage;
import com.pauljoda.modularsystems.power.container.ContainerManaBank;
import com.pauljoda.modularsystems.power.gui.GuiManaBank;
import com.teambr.bookshelf.common.tiles.IOpensGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaReceiver;

public class TileManaBank extends TilePowerBase implements IOpensGui, IManaReceiver {

    public static final int MANA_USAGE = 6000;

    public TileManaBank() {
        energy = new EnergyStorage(32000);
    }

    /*
     * Mana Reciever Methods
     */
    @Override
    public boolean isFull() {
        return energy.getEnergyStored() >= energy.getMaxEnergyStored();
    }

    @Override
    public void recieveMana(int mana) {
        energy.receiveEnergy(mana, false);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public boolean canRecieveManaFromBursts() {
        return !isFull();
    }

    @Override
    public int getCurrentMana() {
        return energy.getEnergyStored();
    }

    /*
     * Fuel Provider Functions
     */

    @Override
    public double fuelProvided() {
        return energy.extractEnergy(MANA_USAGE, true) / 10;
    }

    @Override
    public double consume() {
        int actual = energy.extractEnergy(MANA_USAGE, false) / 10;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        return actual;
    }

    /*
     * GUI Methods
     */
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new ContainerManaBank();
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new GuiManaBank(this);
    }
}
