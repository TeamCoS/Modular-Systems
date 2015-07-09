package com.pauljoda.modularsystems.core.providers;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

public class ItemFuelProvider implements FuelProvider {

    private ItemStack fuel;

    public ItemFuelProvider (ItemStack fuel) {
        this.fuel = fuel;
    }

    @Override
    public boolean canProvide() {
        checkFuel();
        return fuel != null && fuel.stackSize > 0 && fuelProvided() > 0;
    }

    @Override
    public double fuelProvided() {
        return TileEntityFurnace.getItemBurnTime(fuel);
    }

    @Override
    public double consume() {
        if (canProvide()) {
            fuel.stackSize--;
            double retVal = fuel == null ? 0 : fuelProvided();
            checkFuel();
            return retVal;
        } else {
            return 0;
        }
    }

    private void checkFuel() {
        if (fuel != null && fuel.stackSize <= 0) {
            fuel = null;
        }
    }

    @Override
    public FuelProviderType type() {
        return null;
    }
}
