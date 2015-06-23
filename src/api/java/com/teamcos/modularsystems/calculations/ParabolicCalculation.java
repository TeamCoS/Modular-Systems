package com.teamcos.modularsystems.calculations;


public class ParabolicCalculation extends AbstractCalculation {

    public ParabolicCalculation(CalculationValues values) {
        super(values);
    }

    @Override
    protected double doCalculation(double value) {
        return value * value;
    }
}
