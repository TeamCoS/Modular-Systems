package com.pauljoda.modularsystems.power.tiles;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import com.pauljoda.modularsystems.core.registries.ConfigRegistry;
import com.pauljoda.modularsystems.power.gui.GuiManaBank;
import com.teambr.bookshelf.common.tiles.IOpensGui;
import com.teambr.bookshelf.helpers.GuiHelper;
import com.teambr.bookshelf.inventory.ContainerGeneric;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.mana.IManaReceiver;

import java.util.List;

public class TileBankMana extends TileBankBase implements IOpensGui, IManaReceiver, IEnergyHandler {

    private EnergyStorage energy;

    public TileBankMana() {
        energy = new EnergyStorage(32000);
    }

    @Override
    public double getPowerLevelScaled(int scale) {
        return energy.getEnergyStored() * scale / energy.getMaxEnergyStored();
    }

    /*******************************************************************************************************************
     ***************************************** Fuel Provider Functions *************************************************
     *******************************************************************************************************************/

    @Override
    public boolean canProvide() {
        return energy.getEnergyStored() > 0;
    }

    @Override
    public double fuelProvided() {
        int actual = energy.extractEnergy((int)Math.round(ConfigRegistry.manaPower * 200), true);
        return (actual / (ConfigRegistry.manaPower * 200)) * 200;
    }

    @Override
    public double consume() {
        int actual = energy.extractEnergy((int)Math.round(ConfigRegistry.manaPower * 200), false);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        return (actual / (ConfigRegistry.manaPower * 200)) * 200;
    }

    @Override
    public FuelProviderType type() {
        return FuelProviderType.POWER;
    }

    /*******************************************************************************************************************
     ***************************************** Mana Receiver Functions *************************************************
     *******************************************************************************************************************/

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

    /*******************************************************************************************************************
     ********************************************** Energy Functions ***************************************************
     *******************************************************************************************************************/

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

    /*******************************************************************************************************************
     ******************************************** Tile Methods *********************************************************
     *******************************************************************************************************************/

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

    /*******************************************************************************************************************
     ****************************************** IOpensGui Methods ******************************************************
     *******************************************************************************************************************/

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new ContainerGeneric();
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new GuiManaBank(this);
    }

    /*******************************************************************************************************************
     ************************************************ Waila ************************************************************
     *******************************************************************************************************************/

    @Override
    public void returnWailaHead(List<String> list) {
        list.add(GuiHelper.GuiColor.YELLOW + "Available Power: " + GuiHelper.GuiColor.WHITE + energy.getEnergyStored() + "/" + energy.getMaxEnergyStored());
    }
}
