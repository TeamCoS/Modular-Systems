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

public abstract class TilePowerBase extends DummyTile implements FuelProvider, IWaila {

    protected int priority;

    public TilePowerBase() {
        priority = 0;
    }

    public abstract int getPowerLevelScaled(int scale);

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

    /*
     * Fuel Provider Functions
     */
    @Override
    public boolean canProvide() {
        return false;
    }

    @Override
    public double fuelProvided() {
        return 0;
    }

    @Override
    public double consume() {
        return 0;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public FuelProviderType type() {
        return FuelProviderType.POWER;
    }

    public void setPriority(int value) {
        priority = value;
    }

    /*
     * Waila Info
     */

    @Override
    public void returnWailaHead(List<String> list) {

    }

    @Override
    public void returnWailaBody(List<String> list) {

    }

    @Override
    public void returnWailaTail(List<String> list) {
        list.add(GuiHelper.GuiColor.ORANGE + GuiHelper.GuiTextFormat.ITALICS.toString() + "Shift+Click to access GUI");
    }

    @Override
    public ItemStack returnWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
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
