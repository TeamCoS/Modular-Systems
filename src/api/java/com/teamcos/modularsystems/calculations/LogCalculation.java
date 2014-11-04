package com.teamcos.modularsystems.calculations;


public class LogCalculation extends AbstractCalculation {

    public LogCalculation(StandardValues values) {
        super(values);
    }

    @Override
    protected double doCalculation(double value) {
        return Math.log(value);
    }
}

