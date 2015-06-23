package com.teamcos.modularsystems.calculations;

public abstract class AbstractCalculation implements Calculation {

    protected final CalculationValues values;

    public AbstractCalculation(CalculationValues values) {
        this.values = values;
    }

    @Override
    public double calculate(int blockCount) {
        double floor = values.getFloor() * blockCount;
        double ceiling = values.getCeiling() * blockCount;
        double value = values.getFactor() * (doCalculation(blockCount) + values.getConstantValue());
        return Math.max(floor, Math.min(ceiling, value));
    }

    protected abstract double doCalculation(double blockCount);
}
