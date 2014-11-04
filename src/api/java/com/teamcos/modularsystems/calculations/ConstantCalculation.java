package com.teamcos.modularsystems.calculations;


public class ConstantCalculation implements Calculation {

    private final StandardValues values;

    public ConstantCalculation(StandardValues values) {
        this.values = values;
    }

    @Override
    public double calculate(int blockCount) {
        return blockCount > 0 ? values.getyOffset() : 0;
    }
}
