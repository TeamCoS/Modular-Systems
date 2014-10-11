package com.pauljoda.modularsystems.core.structures;

import net.minecraft.block.Block;

public class BlockValues
{
    private String unlocalizedName;
    private int speedValue;
    private int efficiencyValue;

    public BlockValues(String name, int speed, int efficiency)
    {
        unlocalizedName = name;
        speedValue = speed;
        efficiencyValue = efficiency;
    }

    public BlockValues(Block block, int speed, int efficiency)
    {
        unlocalizedName = block.getUnlocalizedName();
        speedValue = speed;
        efficiencyValue = efficiency;
    }

    public int getSpeedValue()
    {
        return speedValue;
    }

    public int getEfficiencyValue()
    {
        return efficiencyValue;
    }

    public boolean compareBlock(Block block)
    {
        return block.getUnlocalizedName().equals(unlocalizedName);
    }
}
