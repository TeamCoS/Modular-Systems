package com.pauljoda.modularsystems.core.calculations;

public class Calculation {
    protected boolean isFactorFraction = false;
    protected double scaleFactor = 1;
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
     * y = m(x + t)^p + b
     *
     * The floor and ceiling are used to define the range this should not break from
     */
    public Calculation(double m, double t, double p, double b, double f, double c, boolean fraction) {
        scaleFactor = m;
        xOffset = t;
        power = p;
        yOffset = b;
        floor = f;
        ceiling = c;
        isFactorFraction = fraction;
    }

    /**
     * Used to create a new instance, copying the values of the other
     * @param calculation The other
     */
    public Calculation(Calculation calculation) {
        this(calculation.scaleFactor, calculation.xOffset, calculation.power, calculation.yOffset, calculation.floor, calculation.ceiling, calculation.isFactorFraction);
    }

    /**
     * Calculate the result using the defined function
     * @param x The block count or 'x' in the function
     * @return F(x)
     */
    public double F(int x) {
        double factor = isFactorFraction ? (1 / scaleFactor) : scaleFactor;
        return Math.max(floor, Math.min(ceiling, (factor * (Math.pow((x + xOffset), power))) + yOffset));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Calculation that = (Calculation) o;

        if (isFactorFraction != that.isFactorFraction) return false;
        if (Double.compare(that.scaleFactor, scaleFactor) != 0) return false;
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
        result = (isFactorFraction ? 1 : 0);
        temp = Double.doubleToLongBits(scaleFactor);
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
