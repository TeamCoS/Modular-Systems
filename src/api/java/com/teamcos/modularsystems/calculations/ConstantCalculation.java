package com.teamcos.modularsystems.calculations;


public class ConstantCalculation implements Calculation {

    private final CalculationValues values;

    public ConstantCalculation(CalculationValues values) {
        this.values = values;
    }

    @Override
    public double calculate(int blockCount) {
        return blockCount > 0 ? values.getConstantValue() : 0;
    }
}
