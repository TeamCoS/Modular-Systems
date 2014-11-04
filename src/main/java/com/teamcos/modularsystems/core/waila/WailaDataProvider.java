package com.teamcos.modularsystems.core.waila;

import com.teamcos.modularsystems.notification.GuiColor;
import com.teamcos.modularsystems.oreprocessing.tiles.TileEntitySmelteryCore;
import com.teamcos.modularsystems.utilities.tiles.DummyIOTile;
import com.teamcos.modularsystems.utilities.tiles.DummyTile;
import com.teamcos.modularsystems.utilities.tiles.FueledRecipeTile;
import com.teamcos.modularsystems.utilities.tiles.TankLogic;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTankInfo;

import java.util.List;

public class WailaDataProvider implements IWailaDataProvider {
    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if(accessor.getTileEntity() instanceof FueledRecipeTile)
        {
            FueledRecipeTile tileEntity = (FueledRecipeTile)accessor.getTileEntity();
            final double speedValue = tileEntity.getSpeed() / 8;
            String speedText = String.format("%.1f", speedValue) + "x";

            final double efficiencyValue = tileEntity.getScaledEfficiency();
            String efficiencyText = String.format("%.1f", efficiencyValue) + "x";

            currenttip.add(GuiColor.RED + "Speed: " + speedText);
            currenttip.add(GuiColor.GRAY + "Efficiency: " + efficiencyText);

            if(!(accessor.getTileEntity() instanceof TileEntitySmelteryCore)) {
                final int multiplicity = tileEntity.getSmeltingMultiplier();
                String multiplicityText = String.valueOf(multiplicity) + "x";
                currenttip.add(GuiColor.GREEN + "Multiplicity: " + multiplicityText);
            }
        }
        else if(accessor.getTileEntity() instanceof DummyTile)
        {
            DummyTile tileEntity = (DummyTile)accessor.getTileEntity();
            if(accessor.getTileEntity() instanceof TankLogic)
            {
                TankLogic tank = (TankLogic)accessor.getTileEntity();
                FluidTankInfo[] fluidTankInfo = tank.getTankInfo(null);
                if(fluidTankInfo[0].fluid != null) {
                    currenttip.add(GuiColor.TURQUISE + "Fluid: " + FluidRegistry.getFluidName(fluidTankInfo[0].fluid));
                    currenttip.add(GuiColor.ORANGE + "" + fluidTankInfo[0].fluid.amount + " / " + fluidTankInfo[0].capacity + "mb");
                }
                else
                    currenttip.add("Empty");

                return currenttip;
            }
            if(accessor.getTileEntity() instanceof DummyIOTile)
            {
                tileEntity = (DummyIOTile)accessor.getTileEntity();
                currenttip.add(tileEntity.getSlotNameForChat());
            }
            else {
                currenttip.add(tileEntity.getBlock().getLocalizedName());
            }

        }

        return currenttip;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    public static void callbackRegister(IWailaRegistrar registrar)
    {
        registrar.registerHeadProvider(new WailaDataProvider(), FueledRecipeTile.class);
        registrar.registerHeadProvider(new WailaDataProvider(), DummyTile.class);
    }
}
