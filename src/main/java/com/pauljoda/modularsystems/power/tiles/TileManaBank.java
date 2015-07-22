package com.pauljoda.modularsystems.power.tiles;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import com.pauljoda.modularsystems.power.container.ContainerManaBank;
import com.pauljoda.modularsystems.power.gui.GuiManaBank;
import com.teambr.bookshelf.common.tiles.IOpensGui;
import com.teambr.bookshelf.helpers.GuiHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.mana.IManaReceiver;

import java.util.List;

public class TileManaBank extends TilePowerBase implements IOpensGui, IManaReceiver, IEnergyHandler {

    public static final int MANA_USAGE = 6000;
    private EnergyStorage energy;

    public TileManaBank() {
        energy = new EnergyStorage(32000);
    }

    @Override
    public int getPowerLevelScaled(int scale) {
        return energy.getEnergyStored() * scale / energy.getMaxEnergyStored();
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
    public boolean canProvide() {
        return energy.getEnergyStored() > 0;
    }

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
     * Waila Functions
     */
    @Override
    public void returnWailaHead(List<String> list) {
        list.add(GuiHelper.GuiColor.YELLOW + "Available Power: " + GuiHelper.GuiColor.WHITE + energy.getEnergyStored() + "/" + energy.getMaxEnergyStored());
    }
}
