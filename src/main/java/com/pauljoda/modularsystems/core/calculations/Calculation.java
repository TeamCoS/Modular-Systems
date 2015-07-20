package com.pauljoda.modularsystems.core.calculations;

import java.util.Arrays;

/**
 * This is the mathematical usage for equations
 *
 * This will allow us to create functions
 */
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
        scaleFactorDenominator = m1 != 0 ? m1 : 1; //Never divide by zero
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

    /**
     * Calculate the result using the defined function
     * @param x The blocks count or 'x' in the function
     * @return F(x)
     */
    public double F_NoClamp(int x) {
        return ((scaleFactorNumerator / scaleFactorDenominator) * (Math.pow((x + xOffset), power))) + yOffset;
    }

    /*******************************************************************************************************************
     **************************************** Accessors and Mutators ***************************************************
     *******************************************************************************************************************/

    /**
     * Accessor for the Scale Factor Numerator
     * @return m1
     */
    public double getScaleFactorNumerator() {
        return scaleFactorNumerator;
    }

    /**
     * Mutator for the Scale Factor Numerator
     * @param scaleFactorNumerator The new value for m1
     */
    public void setScaleFactorNumerator(double scaleFactorNumerator) {
        this.scaleFactorNumerator = scaleFactorNumerator;
    }

    /**
     * Accessor for the Scale Factor Denominator
     * @return m2
     */
    public double getScaleFactorDenominator() {
        return scaleFactorDenominator;
    }

    /**
     * Mutator for the Scale Factor Denominator
     * @param scaleFactorDenominator The new value of m2
     */
    public void setScaleFactorDenominator(double scaleFactorDenominator) {
        this.scaleFactorDenominator = scaleFactorDenominator;
    }

    /**
     * Accessor for the X Offset
     * @return t
     */
    public double getXOffset() {
        return xOffset;
    }

    /**
     * Mutator for the X Offset
     * @param xOffset The new value of t
     */
    public void setXOffset(double xOffset) {
        this.xOffset = xOffset;
    }

    /**
     * Accessor for Power
     * @return p
     */
    public double getPower() {
        return power;
    }

    /**
     * Mutator for Power
     * @param power The new value of p
     */
    public void setPower(double power) {
        this.power = power;
    }

    /**
     * Accessor for the Y Offset
     * @return b
     */
    public double getYOffset() {
        return yOffset;
    }

    /**
     * Mutator for the Y Offset
     * @param yOffset The new value of b
     */
    public void setYOffset(double yOffset) {
        this.yOffset = yOffset;
    }

    /**
     * The lower limit of the function
     * @return The lowest possible value
     */
    public double getFloor() {
        return floor;
    }

    /**
     * Set the lower limit
     * @param floor The new lowest value
     */
    public void setFloor(double floor) {
        this.floor = floor;
    }

    /**
     * The upper limit of the function
     * @return The highest value
     */
    public double getCeiling() {
        return ceiling;
    }

    /**
     * Set the new highest value
     * @param ceiling The new highest value
     */
    public void setCeiling(double ceiling) {
        this.ceiling = ceiling;
    }

    /*******************************************************************************************************************
     **************************************** Basic Java Things ********************************************************
     *******************************************************************************************************************/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Calculation that = (Calculation) o;

        return  Double.compare(that.scaleFactorNumerator, scaleFactorNumerator) == 0 &&
                Double.compare(that.scaleFactorDenominator, scaleFactorDenominator) == 0 &&
                Double.compare(that.xOffset, xOffset) == 0 &&
                Double.compare(that.power, power) == 0 &&
                Double.compare(that.yOffset, yOffset) == 0 &&
                Double.compare(that.floor, floor) == 0 &&
                Double.compare(that.ceiling, ceiling) == 0;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[] {scaleFactorNumerator, scaleFactorDenominator, xOffset,
                power, yOffset, floor, ceiling});
    }

    @Override
    public String toString() {
        return (scaleFactorNumerator / scaleFactorDenominator) + "(x + " + xOffset + ")" + "^" + power + " + " + yOffset;
    }
}
