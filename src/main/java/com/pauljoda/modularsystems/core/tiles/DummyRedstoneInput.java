package com.pauljoda.modularsystems.core.tiles;

import com.pauljoda.modularsystems.core.gui.GuiRedstoneInput;
import com.pauljoda.modularsystems.core.managers.BlockManager;
import com.teambr.bookshelf.common.tiles.IOpensGui;
import com.teambr.bookshelf.helpers.GuiHelper;
import com.teambr.bookshelf.inventory.ContainerGeneric;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/30/2015
 */
public class DummyRedstoneInput extends DummyRedstoneOutput implements IOpensGui {

    public int setLevel;

    public DummyRedstoneInput() {
        setLevel = 16;
    }

    public Block getStoredBlock() {
        return BlockManager.redstoneControlIn;
    }

    /******************************************************************************************************************
     **************************************************  Tile Methods  ************************************************
     ******************************************************************************************************************/

    @Override
    public void updateEntity() {
        super.updateEntity();
        if(getCore() != null && !worldObj.isRemote) {
            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                int weakPower = worldObj.getBlock(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ).isProvidingWeakPower(worldObj, xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ, dir.ordinal());
                int strongPower = worldObj.getBlock(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ).isProvidingStrongPower(worldObj, xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ, dir.ordinal());
                if (weakPower >= setLevel || strongPower >= setLevel) {
                    getCore().values.isPowered = true;
                    return;
                }
            }
            getCore().values.isPowered = false;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        setLevel = tagCompound.getInteger("SetLevel");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setInteger("SetLevel", setLevel);
    }

    /*******************************************************************************************************************
     ***************************************** IOpensGui Methods *******************************************************
     *******************************************************************************************************************/

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new ContainerGeneric();
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new GuiRedstoneInput(this);
    }

    /*******************************************************************************************************************
     ************************************************ Waila ************************************************************
     *******************************************************************************************************************/

    @Override
    public void returnWailaHead(List<String> tip) {
        tip.add(GuiHelper.GuiColor.YELLOW + "Redstone Trigger Level: " + GuiHelper.GuiColor.WHITE + setLevel);
    }

    @Override
    public void returnWailaTail(List<String> list) {
        list.add(GuiHelper.GuiColor.ORANGE + GuiHelper.GuiTextFormat.ITALICS.toString() + "Shift+Click to access GUI");
    }
}
