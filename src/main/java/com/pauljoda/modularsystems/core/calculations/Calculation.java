package com.pauljoda.modularsystems.core.calculations;

public class Calculation {
    protected double scaleFactorNumerator = 1;
    protected double scaleFactorDenominator = 1;
    protected double xOffset = 0;
    protected double power = 1;
    protected double yOffset = 0;
    protected double floor = 0;
    protected double ceiling = 1;

    /**
     * Create a calculation object using the given parameters
     *
     * A function will be created that will allow user to define behaviors
     *
     * The function relates to:
     * y = (m / m1)(x + t)^p + b
     *
     * The floor and ceiling are used to define the range this should not break from
     */
    public Calculation(double m, double m1, double t, double p, double b, double f, double c) {
        scaleFactorNumerator = m;
        scaleFactorDenominator = m1;
        xOffset = t;
        power = p;
        yOffset = b;
        floor = f;
        ceiling = c;
    }

    /**
     * Used to create a new instance, copying the values of the other
     * @param calculation The other
     */
    public Calculation(Calculation calculation) {
        this(calculation.scaleFactorNumerator, calculation.scaleFactorDenominator, calculation.xOffset, calculation.power, calculation.yOffset, calculation.floor, calculation.ceiling);
    }

    /**
     * Calculate the result using the defined function
     * @param x The blocks count or 'x' in the function
     * @return F(x)
     */
    public double F(int x) {
        return Math.max(floor, Math.min(ceiling, ((scaleFactorNumerator / scaleFactorDenominator) * (Math.pow((x + xOffset), power))) + yOffset));
    }

    public double getScaleFactorDenominator() {
        return scaleFactorDenominator;
    }

    public void setScaleFactorDenominator(double scaleFactorDenominator) {
        this.scaleFactorDenominator = scaleFactorDenominator;
    }

    public double getCeiling() {
        return ceiling;
    }

    public void setCeiling(double ceiling) {
        this.ceiling = ceiling;
    }

    public double getFloor() {
        return floor;
    }

    public void setFloor(double floor) {
        this.floor = floor;
    }

    public double getyOffset() {
        return yOffset;
    }

    public void setyOffset(double yOffset) {
        this.yOffset = yOffset;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public double getxOffset() {
        return xOffset;
    }

    public void setxOffset(double xOffset) {
        this.xOffset = xOffset;
    }

    public double getScaleFactorNumerator() {
        return scaleFactorNumerator;
    }

    public void setScaleFactorNumerator(double scaleFactorNumerator) {
        this.scaleFactorNumerator = scaleFactorNumerator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Calculation that = (Calculation) o;

        if (Double.compare(that.scaleFactorNumerator, scaleFactorNumerator) != 0) return false;
        if (Double.compare(that.scaleFactorDenominator, scaleFactorDenominator) != 0) return false;
        if (Double.compare(that.xOffset, xOffset) != 0) return false;
        if (Double.compare(that.power, power) != 0) return false;
        if (Double.compare(that.yOffset, yOffset) != 0) return false;
        if (Double.compare(that.floor, floor) != 0) return false;
        return Double.compare(that.ceiling, ceiling) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(scaleFactorNumerator);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(scaleFactorDenominator);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(xOffset);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(power);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(yOffset);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(floor);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(ceiling);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return (scaleFactorNumerator / scaleFactorDenominator) + "(x + " + xOffset + ")" + "^" + power + " + " + yOffset;
    }
}
