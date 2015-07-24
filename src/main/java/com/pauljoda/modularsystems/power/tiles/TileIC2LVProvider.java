package com.pauljoda.modularsystems.power.tiles;

import com.pauljoda.modularsystems.core.registries.ConfigRegistry;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySource;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

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
            double convertedPower = genCore.getEnergyStored(null) * ConfigRegistry.EUPower;
            return Math.min(EnergyNet.instance.getPowerFromTier(getSourceTier()), convertedPower);
        }

        return 0;
    }

    @Override
    public void drawEnergy(double v) {
        if (validCore()) {
            double convertedPower = v * ConfigRegistry.EUPower;
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
}
