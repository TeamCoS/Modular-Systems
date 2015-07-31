package com.pauljoda.modularsystems.power.tiles;

import com.pauljoda.modularsystems.core.tiles.DummyTile;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.List;

/**
 * Modular-Systems
 * Created by Dyonovan on 24/07/15
 */
public abstract class TileProviderBase extends DummyTile {

    protected int currentOutput;
    protected int lastOutput;

    public TileProviderBase() {
        currentOutput = 0;
    }

    /**
     * Used to update our current, but still needed sometimes as the int returned is from consuming
     * @param i How much was drained
     * @return What is passed
     */
    public int updateOutput(int i) {
        if(!worldObj.isRemote) {
            currentOutput += i;
        }
        return i;
    }

    public int getCurrentOutput() {
        return lastOutput;
    }

    /**
     * This is need for the display of what is being output.
     *
     * For instance, the rf output will provide "RF"
     *
     * This is the tag that will be put at the end of the current output
     *
     * @return The tag to write
     */
    public abstract String getEnergyTag();

    /*******************************************************************************************************************
     ******************************************** Tile Methods *********************************************************
     *******************************************************************************************************************/

    @Override
    public void updateEntity() {
        super.updateEntity();
        if(!worldObj.isRemote) {
            lastOutput = currentOutput;
            currentOutput = 0;
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        lastOutput = tag.getInteger("output");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setInteger("output", lastOutput);
    }

    /*******************************************************************************************************************
     ************************************************ Waila ************************************************************
     *******************************************************************************************************************/

    @Override
    public void returnWailaHead(List<String> tip) {
        super.returnWailaHead(tip);
        tip.add("Currently outputting: " + lastOutput + " " + getEnergyTag());
    }

    @Override
    public NBTTagCompound returnNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, int x, int y, int z) {
        if (tag.hasKey("Energy")) {
            tag.removeTag("Energy");
            tag.removeTag("MaxStorage");
        }
        return tag;
    }
}
