package com.teamcos.modularsystems.collections;


import com.teamcos.modularsystems.helpers.Coord;

import java.util.List;

public class Values {
    private final boolean hasCrafter;
    private final double speedMultiplier;
    private final double efficiencyMultiplier;
    private final int smeltingMultiplier;
    private final List<Coord> tiles;

    public Values(boolean hasCrafter, double speedMultiplier, double efficiencyMultiplier, int smeltingMultiplier, List<Coord> tiles) {
        this.hasCrafter = hasCrafter;
        this.speedMultiplier = speedMultiplier;
        this.efficiencyMultiplier = efficiencyMultiplier;
        this.smeltingMultiplier = smeltingMultiplier;
        this.tiles = tiles;
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

    public List<Coord> getTiles() {
        return tiles;
    }
}
