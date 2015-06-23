package com.teamcos.modularsystems.calculations;


public class LinearCalculation extends AbstractCalculation {

    public LinearCalculation(CalculationValues value) {
        super(value);
    }

    @Override
    protected double doCalculation(double value) {
        return value;
    }
}
