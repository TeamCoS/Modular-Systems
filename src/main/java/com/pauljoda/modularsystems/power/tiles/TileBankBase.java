package com.pauljoda.modularsystems.power.tiles;

import com.pauljoda.modularsystems.core.providers.FuelProvider;
import com.pauljoda.modularsystems.core.tiles.DummyTile;
import com.teambr.bookshelf.api.waila.IWaila;
import com.teambr.bookshelf.helpers.GuiHelper;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.List;

/**
 * The base tile for all fuel banks
 */
public abstract class TileBankBase extends DummyTile implements FuelProvider, IWaila {

    //Current Priority
    protected int priority;

    public TileBankBase() {
        priority = 0;
    }

    /**
     * Used to scale the current power level
     * @param scale The scale to move to
     * @return A number from 0 - {@param scale} for current level
     */
    public abstract double getPowerLevelScaled(int scale);

    /*******************************************************************************************************************
     ******************************************* Tile Methods **********************************************************
     *******************************************************************************************************************/
    @Override
    public void readFromNBT (NBTTagCompound tags) {
        super.readFromNBT(tags);
        priority = tags.getInteger("priority");
    }

    @Override
    public void writeToNBT (NBTTagCompound tags) {
        super.writeToNBT(tags);
        tags.setInteger("priority", priority);
    }

    /*******************************************************************************************************************
     ***************************************** Fuel Provider Functions *************************************************
     *******************************************************************************************************************/

    @Override
    public int getPriority() {
        return priority;
    }

    public void setPriority(int value) {
        priority = value;
    }

    /*******************************************************************************************************************
     ************************************************ Waila ************************************************************
     *******************************************************************************************************************/

    @Override
    public NBTTagCompound returnNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, int x, int y, int z) {
        if (tag.hasKey("Energy")) {
            tag.removeTag("Energy");
            tag.removeTag("MaxStorage");
        }
        return tag;
    }

    @Override
    public void returnWailaTail(List<String> list) {
        list.add(GuiHelper.GuiColor.ORANGE + GuiHelper.GuiTextFormat.ITALICS.toString() + "Shift+Click to access GUI");
    }

    @Override
    public void returnWailaHead(List<String> list) {}

    @Override
    public void returnWailaBody(List<String> list) {}

    @Override
    public ItemStack returnWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) { return null; }
}
