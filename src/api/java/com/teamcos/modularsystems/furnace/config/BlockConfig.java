package com.teamcos.modularsystems.furnace.config;

import com.teamcos.modularsystems.calculations.Calculation;

public class BlockConfig {

    private final String blockName;
    private final Calculation efficiency;
    private final Calculation speed;
    private final Calculation smeltingMultiplier;

    public BlockConfig(String blockName, Calculation efficiency, Calculation speed, Calculation smeltingMultiplier) {
        this.blockName = blockName;
        this.efficiency = efficiency;
        this.speed = speed;
        this.smeltingMultiplier = smeltingMultiplier;
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
