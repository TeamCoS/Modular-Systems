package com.teamcos.modularsystems.collections;



public class Values {
    private final boolean hasCrafter;
    private final double speedMultiplier;
    private final double efficiencyMultiplier;
    private final int smeltingMultiplier;

    public Values(boolean hasCrafter, double speedMultiplier, double efficiencyMultiplier, int smeltingMultiplier) {
        this.hasCrafter = hasCrafter;
        this.speedMultiplier = speedMultiplier;
        this.efficiencyMultiplier = efficiencyMultiplier;
        this.smeltingMultiplier = smeltingMultiplier;
    }

    public boolean isHasCrafter() {
        return hasCrafter;
    }

    public double getSpeedMultiplier() {
        return speedMultiplier;
    }

    public double getEfficiencyMultiplier() {
        return efficiencyMultiplier;
    }

    public int getSmeltingMultiplier() {
        return smeltingMultiplier;
    }
}
