package com.teamcos.modularsystems.calculations;


public class LogCalculation implements Calculation {

    private final double scalar;

    public LogCalculation(double scalar) {
        this.scalar = scalar;
    }

    @Override
    public double calculate(int blockCount) {
        return scalar * Math.log(blockCount);
    }
}

