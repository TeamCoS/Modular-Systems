package com.teamcos.modularsystems.helpers;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class LiquidHelper {
    public static int getLiquidBurnTime(FluidStack fluid)
    {
        if(fluid.getFluid() == FluidRegistry.LAVA)
            return 16000;

        String fluidName = FluidRegistry.getFluidName(fluid.fluidID);

        if(fluidName.equals("oil"))
            return 6000;
        if(fluidName.equals("fuel"))
            return 20000;

        return 0;
    }
}
