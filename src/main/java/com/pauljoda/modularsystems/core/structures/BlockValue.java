package com.pauljoda.modularsystems.core.structures;

import net.minecraft.block.Block;

public class BlockValue
{
    private String unlocalizedName;
    private double speedValue;
    private double efficiencyValue;

    public BlockValue(String name, double speed, double efficiency)
    {
        unlocalizedName = name;
        speedValue = speed;
        efficiencyValue = efficiency;
    }

    public BlockValue(String name, String speed, String efficiency)
    {
        unlocalizedName = name;
        speedValue = Double.parseDouble(speed);
        efficiencyValue = Double.parseDouble(efficiency);
    }

    public BlockValue(Block block, double speed, double efficiency)
    {
        unlocalizedName = block.getUnlocalizedName();
        speedValue = speed;
        efficiencyValue = efficiency;
    }

    public double getSpeedValue()
    {
        return speedValue;
    }

    public double getEfficiencyValue()
    {
        return efficiencyValue;
    }

    public boolean compareBlock(Block block)
    {
        return block.getUnlocalizedName().equals(unlocalizedName);
    }
}
