package com.pauljoda.modularsystems.power.tiles;

import com.pauljoda.modularsystems.core.registries.ConfigRegistry;
import com.teambr.bookshelf.helpers.GuiHelper;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySource;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

/**
 * Modular-Systems
 * Created by Dyonovan on 24/07/15
 */
public class TileIC2LVProvider extends TileSupplierBase implements IEnergySource {

    private boolean firstRun;

    public TileIC2LVProvider() {
        firstRun = true;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (worldObj.isRemote) return;
        if (firstRun) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            firstRun = false;
        }

    }

    /*
     * IC2 Energy Functions
     */
    @Override
    public double getOfferedEnergy() {
        if (validCore()) {
            int[] convertedPower = convertToEU();
            return Math.min(EnergyNet.instance.getPowerFromTier(getSourceTier()), convertedPower[0]);
        }

        return 0;
    }

    @Override
    public void drawEnergy(double v) {
        if (validCore()) {
            double convertedPower = convertFromEU(v);
            genCore.extractEnergy(null, (int)Math.round(convertedPower), false);
            worldObj.markBlockForUpdate(coreLocation.x, coreLocation.y, coreLocation.z);
        }
    }

    @Override
    public int getSourceTier() {
        return 1; //LV
    }

    @Override
    public boolean emitsEnergyTo(TileEntity tileEntity, ForgeDirection forgeDirection) {
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

    private int[] convertToEU() {
        int avail = (int) Math.round(genCore.getEnergyStored(null) / ConfigRegistry.rfPower * ConfigRegistry.EUPower);
        int total = (int) Math.round(genCore.getMaxEnergyStored(null) / ConfigRegistry.rfPower * ConfigRegistry.EUPower);
        return new int[]{avail, total};
    }

    private int convertFromEU(double euIN) {
        return (int)Math.round(euIN / ConfigRegistry.EUPower * ConfigRegistry.rfPower);
    }

    /*
     * Waila Functions
     */
    @Override
    public void returnWailaHead(List<String> list) {
        if (genCore != null) {
            int[] power = convertToEU();
            list.add(GuiHelper.GuiColor.YELLOW + "Available Power: " + GuiHelper.GuiColor.WHITE +
                    power[0] + " / " + power[1] + " EU");
        }
    }
}
