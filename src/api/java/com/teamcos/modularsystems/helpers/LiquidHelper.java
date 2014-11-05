package com.teamcos.modularsystems.helpers;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class LiquidHelper {
    public static int getLiquidBurnTime(FluidStack fluid)
    {
        if(fluid.getFluid() == FluidRegistry.LAVA)
            return 16000;
        String fluidName = FluidRegistry.getFluidName(fluid);
        if(fluidName != null) {
            if (fluidName.equals("oil"))
                return 2000;
            else if (fluidName.toLowerCase().equals("fuel"))
                return 22000;
            else if (fluidName.toLowerCase().equals("rocket_fuel"))
                return 600;
            else if (fluidName.toLowerCase().equals("fire_water"))
                return 800;
            else if (fluidName.toLowerCase().equals("bioethanol"))
                return 20000;
            else if (fluidName.toLowerCase().equals("biofuel"))
                return 20000;
            else if (fluidName.toLowerCase().equals("redstone"))
                return 8000;
            else if (fluidName.toLowerCase().equals("glowstone"))
                return 10000;
            else if (fluidName.toLowerCase().equals("ender"))
                return 12000;
            else if (fluidName.toLowerCase().equals("pyrotheum"))
                return 12000;
        }
        return 0;
    }
}
