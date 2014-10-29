package com.teamcos.modularsystems.calculations;


public class LinearCalculation implements Calculation {

    private double perBlock;

    public LinearCalculation(double perBlock) {
        this.perBlock = perBlock;
    }

    public double calculate(int blockCount) {
        return blockCount * perBlock;
    }
}
