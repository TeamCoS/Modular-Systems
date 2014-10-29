package com.teamcos.modularsystems.calculations;


public class ParabolicCalculation implements Calculation {

    private final double coefficient;
    private final double xOffset;
    private final double yOffset;

    public ParabolicCalculation(double coefficient, double xOffset, double yOffset) {
        this.coefficient = coefficient;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public ParabolicCalculation(double coefficient) {
        this(coefficient, 0, 0);
    }

    @Override
    public double calculate(int blockCount) {
        return coefficient * xOffset(blockCount) * xOffset(blockCount) + yOffset;
    }

    private final double xOffset(int blockCount) {
        return blockCount + xOffset;
    }
}
