package com.teamcos.modularsystems.calculations;


public class LinearCalculation extends AbstractCalculation {

    public LinearCalculation(StandardValues value) {
        super(value);
    }

    @Override
    protected double doCalculation(double value) {
        return value;
    }
}
