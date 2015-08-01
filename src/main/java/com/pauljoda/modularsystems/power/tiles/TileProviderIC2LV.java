package com.pauljoda.modularsystems.power.tiles;

import com.pauljoda.modularsystems.core.registries.ConfigRegistry;
import com.pauljoda.modularsystems.generator.tiles.TileGeneratorCore;
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
public class TileProviderIC2LV extends TileProviderBase implements IEnergySource {

    private boolean firstRun;

    public TileProviderIC2LV() {
        firstRun = true;
    }

    @Override
    public String getEnergyTag() {
        return "EU";
    }

    /**
     * Used to convert the stored energy into EU
     * @return An array
     *         0 - available
     *         1 - total
     */
    private int[] convertToEU() {
        if (getCore() instanceof TileGeneratorCore) {
            TileGeneratorCore core = (TileGeneratorCore) getCore();
            int avail = (int) Math.round(core.getEnergyStored(null) / ConfigRegistry.rfPower * ConfigRegistry.EUPower);
            int total = (int) Math.round(core.getMaxEnergyStored(null) / ConfigRegistry.rfPower * ConfigRegistry.EUPower);
            return new int[]{avail, total};
        }
        return null;
    }

    /**
     * Used to put EU back into RF
     * @param euIN Besides acting in Star Wars as Obi-wan kenobie, the EU value to convert
     * @return The RF of the EU
     */
    private int convertFromEU(double euIN) {
        return (int) Math.round(euIN / ConfigRegistry.EUPower * ConfigRegistry.rfPower);
    }

    /*******************************************************************************************************************
     *************************************** IC2 EnergyNet Methods *****************************************************
     *******************************************************************************************************************/

    @Override
    public double getOfferedEnergy() {
        if (getCore() instanceof TileGeneratorCore && !((TileGeneratorCore) getCore()).hasFuelProviderType(getEnergyTag())) {
            int[] convertedPower = convertToEU();
            if (convertedPower != null) {
                return Math.min(EnergyNet.instance.getPowerFromTier(getSourceTier()), convertedPower[0]);
            }
        }
        return 0;
    }

    @Override
    public void drawEnergy(double v) {
        if (getCore() instanceof TileGeneratorCore && !((TileGeneratorCore) getCore()).hasFuelProviderType(getEnergyTag())) {
            TileGeneratorCore core = (TileGeneratorCore) getCore();
            double convertedPower = convertFromEU(v);
            updateOutput(core.extractEnergy(null, (int) Math.round(convertedPower), false));
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

    /*******************************************************************************************************************
     ******************************************** Tile Methods *********************************************************
     *******************************************************************************************************************/

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (worldObj.isRemote) return;
        if (firstRun) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            firstRun = false;
        }
    }

    @Override
    public void onChunkUnload() {
        if (!worldObj.isRemote)
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        super.onChunkUnload();
    }

    @Override
    public void invalidate() {
        if (!worldObj.isRemote)
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        super.invalidate();
    }

    /*******************************************************************************************************************
     ************************************************ Waila ************************************************************
     *******************************************************************************************************************/

    @Override
    public void returnWailaHead(List<String> list) {
        if (getCore() instanceof TileGeneratorCore) {
            super.returnWailaHead(list);
            int[] power = convertToEU();
            if (power != null)
                list.add(GuiHelper.GuiColor.YELLOW + "Available Power: " + GuiHelper.GuiColor.WHITE +
                        power[0] + " / " + power[1] + " EU");
        }
    }
}
