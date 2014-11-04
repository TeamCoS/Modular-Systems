package com.teamcos.modularsystems.fuelprovider;

public class LiquidFuelProvider implements FuelProvider {
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
    public FuelProviderType type() {
        return FuelProviderType.LIQUID;
    }
}
