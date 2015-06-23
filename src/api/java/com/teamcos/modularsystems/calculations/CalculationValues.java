package com.teamcos.modularsystems.calculations;

public class CalculationValues {
    public static final CalculationValues LinearIdentity = new CalculationValues(1);
    public static final CalculationValues Constant0 = new CalculationValues("constant", 0, 0, 0, 0);

    private final double constantValue;
    private final double factor;
    private final double ceiling;
    private final double floor;
    private final String equation;

    public CalculationValues(String equation1, double constantValue, double factor, double ceiling, double floor) {
        this.equation = equation1;
        this.constantValue = constantValue;
        this.factor = factor;
        this.ceiling = ceiling;
        this.floor = floor;
    }

    public CalculationValues(double factor) {
        this.equation = "linear";
        this.constantValue = 0;
        this.factor = factor;
        this.ceiling = Double.MAX_VALUE;
        this.floor =  -Double.MAX_VALUE;
    }

    public double getConstantValue() {
        return constantValue;
    }

    public double getFactor() {
        return factor;
    }

    public double getCeiling() {
        return ceiling;
    }

    public double getFloor() {
        return floor;
    }

    public String getEquation() {
        return equation;
    }
}
