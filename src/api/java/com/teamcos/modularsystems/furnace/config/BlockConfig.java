package com.teamcos.modularsystems.furnace.config;

import com.teamcos.modularsystems.calculations.Calculation;
import com.teamcos.modularsystems.calculations.ConstantCalculation;

public class BlockConfig {

    private final String blockName;
    private final Calculation efficiency;
    private final Calculation speed;
    private final Calculation smeltingMultiplier;

    public BlockConfig(String blockName, Calculation efficiency, Calculation speed) {
        this.blockName = blockName;
        this.efficiency = efficiency;
        this.speed = speed;
        smeltingMultiplier = new ConstantCalculation(0);
    }

    public double efficiency(int blockCount) {
        return efficiency.calculate(blockCount);
    }

    public double speed(int blockCount) {
        return speed.calculate(blockCount);
    }

    public int multiplier(int blockCount) {
        return (int) Math.round(smeltingMultiplier.calculate(blockCount));
    }

    public String getBlockName() {
        return blockName;
    }
}
