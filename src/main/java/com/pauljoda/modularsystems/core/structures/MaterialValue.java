package com.pauljoda.modularsystems.core.structures;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class MaterialValue
{
    private Material material;
    private double speedValue;
    private double efficiencyValue;

    public MaterialValue(Material materialName, double speed, double eff)
    {
        material = materialName;
        speedValue = speed;
        efficiencyValue = eff;
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
        return block.getMaterial() == material;
    }

    public static Material getMaterialFromString(String mat)
    {
        if(mat.equals("rock"))
            return Material.rock;
        else if(mat.equals("iron"))
            return Material.iron;
        return null;
    }

}
