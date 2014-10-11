package com.pauljoda.modularsystems.core.structures;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class MaterialValues
{
    private Material material;
    private int speedValue;
    private int efficiencyValue;

    public MaterialValues(String materialName, int speed, int eff)
    {
        Object matObj = materialName;
        material = (Material) matObj;
        speedValue = speed;
        efficiencyValue = eff;
    }

    public MaterialValues(Material mat, int speed, int eff)
    {
        material = mat;
        speedValue = speed;
        efficiencyValue = eff;
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
        return block.getMaterial() == material;
    }
}
