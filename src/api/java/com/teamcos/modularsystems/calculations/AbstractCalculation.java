package com.teamcos.modularsystems.calculations;

public abstract class AbstractCalculation implements Calculation {

    protected final Calculation.StandardValues values;

    public AbstractCalculation(Calculation.StandardValues values) {
        this.values = values;
    }

    @Override
    public double calculate(int blockCount) {
        return values.yCoefficient * Math.min(values.perBlockCap * blockCount, doCalculation(blockCount) * doCalculation(blockCount) + values.yOffset);
    }

    protected abstract double doCalculation(double value);
}
