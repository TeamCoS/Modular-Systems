package com.pauljoda.modularsystems.power.tiles;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import com.teambr.bookshelf.helpers.GuiHelper;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

/**
 * Modular-Systems
 * Created by Dyonovan on 24/07/15
 */
public class TileRFSupplier extends TileSupplierBase implements IEnergyProvider {

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (worldObj.isRemote || genCore == null) return;

        if (genCore.getEnergyStored(null) > 0) {
            Vec3 current = Vec3.createVectorHelper(xCoord, yCoord, zCoord);

            for (int i = 0; i < 6; i++) {
                ForgeDirection change = ForgeDirection.getOrientation(i);
                Vec3 side = current.addVector(change.offsetX, change.offsetY, change.offsetZ);

                if (worldObj.getTileEntity((int) Math.round(side.xCoord), (int) Math.round(side.yCoord),
                        (int) Math.round(side.zCoord)) instanceof IEnergyReceiver) {

                    IEnergyReceiver rf = (IEnergyReceiver) worldObj.getTileEntity((int) Math.round(side.xCoord), (int) Math.round(side.yCoord),
                            (int) Math.round(side.zCoord));

                    int needed = rf.receiveEnergy(change.getOpposite(), genCore.MAX_RFTICK_OUT, true);
                    if (needed > 0) {
                        int available = extractEnergy(null, needed, true);
                        extractEnergy(null, rf.receiveEnergy(change.getOpposite(), Math.min(available, needed), false),
                                false);
                        worldObj.markBlockForUpdate(coreLocation.x, coreLocation.y, coreLocation.z);
                    }
                }
            }
        }

    }

    /*
     * RF Provider Methods
     */
    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
        if (genCore != null)
            return genCore.extractEnergy(from, maxExtract, simulate);

        return 0;
    }

    @Override
    public int getEnergyStored(ForgeDirection from) {
        if (genCore != null)
            return genCore.getEnergyStored(from);

        return 0;
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {
        if (genCore != null)
            return genCore.getMaxEnergyStored(from);
        return 0;
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from) {
        return true;
    }

    /*
     * Waila Functions
     */
    @Override
    public void returnWailaHead(List<String> list) {
        if (genCore != null)
            list.add(GuiHelper.GuiColor.YELLOW + "Available Power: " + GuiHelper.GuiColor.WHITE + genCore.getEnergyStored(null)
                    + "/" + genCore.getMaxEnergyStored(null) + " RF");
    }
}
