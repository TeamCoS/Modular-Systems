package com.pauljoda.modularsystems.core.calculations;

public class Calculation {
    protected double scaleFactorNumberator = 1;
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
        scaleFactorNumberator = m;
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
        this(calculation.scaleFactorNumberator, calculation.scaleFactorDenominator, calculation.xOffset, calculation.power, calculation.yOffset, calculation.floor, calculation.ceiling);
    }

    /**
     * Calculate the result using the defined function
     * @param x The block count or 'x' in the function
     * @return F(x)
     */
    public double F(int x) {
        return Math.max(floor, Math.min(ceiling, ((scaleFactorNumberator / scaleFactorDenominator) * (Math.pow((x + xOffset), power))) + yOffset));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Calculation that = (Calculation) o;

        if (Double.compare(that.scaleFactorNumberator, scaleFactorNumberator) != 0) return false;
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
        temp = Double.doubleToLongBits(scaleFactorNumberator);
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
}
