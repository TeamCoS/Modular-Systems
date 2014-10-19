package com.teamcos.modularsystems.furnace.config;

public class BlockConfig {

    private final String blockName;
    private final Calculation efficiency;
    private final Calculation speed;

    public BlockConfig(String blockName, Calculation efficiency, Calculation speed) {
        this.blockName = blockName;
        this.efficiency = efficiency;
        this.speed = speed;
    }

    public double efficiency(int blockCount) {
        return efficiency.calculate(blockCount);
    }

    public double speed(int blockCount) {
        return speed.calculate(blockCount);
    }

    public String getBlockName() {
        return blockName;
    }
}
