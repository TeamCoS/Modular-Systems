package com.teamcos.modularsystems.interfaces;

public interface MSUpgradeBlock {
    double getEfficiency(int blockCount);
    double getSpeed(int blockCount);
    int getMultiplier(int blockCount);
    boolean isCrafter();
}
