package com.pauljoda.modularsystems.power.tiles;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import com.pauljoda.modularsystems.core.registries.ConfigRegistry;
import com.pauljoda.modularsystems.power.gui.GuiManaBank;
import com.teambr.bookshelf.common.tiles.IOpensGui;
import com.teambr.bookshelf.helpers.GuiHelper;
import com.teambr.bookshelf.inventory.ContainerGeneric;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.mana.IManaReceiver;

import java.util.List;

public class TileManaBank extends TilePowerBase implements IOpensGui, IManaReceiver, IEnergyHandler {

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
        int actual = energy.extractEnergy((int)Math.round(ConfigRegistry.manaPower * 200), true);
        return (actual / (ConfigRegistry.manaPower * 200)) * 200;
    }

    @Override
    public double consume() {
        int actual = energy.extractEnergy((int)Math.round(ConfigRegistry.manaPower * 200), false);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        return (actual / (ConfigRegistry.manaPower * 200)) * 200;
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
        return new ContainerGeneric();
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

    @Override
    public void returnWailaTail(List<String> list) {
        if (Minecraft.getMinecraft().thePlayer.isSneaking()) {
            list.add(GuiHelper.GuiColor.CYAN + GuiHelper.GuiTextFormat.ITALICS.toString() + "Useable In:");
            list.add(GuiHelper.GuiColor.GREEN + "Modular Furnace");
            list.add(GuiHelper.GuiColor.GREEN + "Modular Crusher");
            list.add(GuiHelper.GuiColor.GREEN + "Modular Generator");
        } else
            list.add(GuiHelper.GuiColor.CYAN + GuiHelper.GuiTextFormat.ITALICS.toString() + "Press Shift for Usage Cores");

        super.returnWailaTail(list);
    }
}
