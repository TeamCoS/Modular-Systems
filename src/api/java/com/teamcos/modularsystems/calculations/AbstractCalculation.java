package com.teamcos.modularsystems.calculations;

public abstract class AbstractCalculation implements Calculation {

    protected final StandardValues values;

    public AbstractCalculation(StandardValues values) {
        this.values = values;
    }

    @Override
    public double calculate(int blockCount) {
        double floor = values.getPerBlockFloor() * blockCount;
        double ceiling = values.getPerBlockCap() * blockCount;
        double value = values.getyCoefficient() * doCalculation(blockCount) + values.getyOffset();
        return Math.max(floor, Math.min(ceiling, value));
    }

    protected abstract double doCalculation(double blockCount);
}
