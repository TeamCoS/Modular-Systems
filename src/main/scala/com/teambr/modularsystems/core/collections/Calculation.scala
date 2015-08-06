package com.teambr.modularsystems.core.collections

import java.util

/**
 * This file was created for Modular-Systems
 *
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis <pauljoda>
 * @since August 05, 2015
 *
 *
 * Create a calculation object using the given parameters
 *
 * A function will be created that will allow user to define behaviors
 *
 * The function relates to:
 * y = (m / m1)(x + t) power p + b
 *
 * The floor and ceiling are used to define the range this should not break from
 */
class Calculation(m : Double, m1 : Double, t : Double, p : Double, b : Double, f : Double, c : Double) {
    val scaleFactorNumerator = m
    val scaleFactorDenominator = if (m1 != 0) m1 else 1
    val xOffset = t
    val power = p
    val yOffset = b
    val floor = f
    val ceiling = c

    /**
     * Used to create a new instance, copying the values of the other
     * @param calculation The other
     */
    def this(calculation : Calculation) {
        this(calculation.scaleFactorNumerator, calculation.scaleFactorDenominator, calculation.xOffset, calculation.power, calculation.yOffset, calculation.floor, calculation.ceiling)
    }

    /**
     * Calculate the result using the defined function
     * @param x The blocks count or 'x' in the function
     * @return F(x)
     */
    def F(x : Int) : Double =
        Math.max(floor, Math.min(ceiling, ((scaleFactorNumerator / scaleFactorDenominator) * Math.pow(x + xOffset, power)) + yOffset))

    /**
     * Calculate the result using the defined function
     * @param x The blocks count or 'x' in the function
     * @return F(x)
     */
    def F_NoClamp(x : Int) : Double =
        ((scaleFactorNumerator / scaleFactorDenominator) * Math.pow(x + xOffset, power)) + yOffset

    override def hashCode : Int =
         util.Arrays.hashCode(Array[Double](scaleFactorNumerator, scaleFactorDenominator, xOffset, power, yOffset, floor, ceiling))

    override def toString : String =
         (scaleFactorNumerator / scaleFactorDenominator) + "(x + " + xOffset + ")" + "^" + power + " + " + yOffset

}
