package com.teamcos.modularsystems.calculations;


public class ConstantCalculation implements Calculation {

    private final double efficiency;

    public ConstantCalculation(double efficiency) {
        this.efficiency = efficiency;
    }

    @Override
    public double calculate(int blockCount) {
        return blockCount > 0 ? efficiency : 0;
    }
}
