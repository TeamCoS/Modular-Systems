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
 */
class BlockValues(c1 : Calculation, c2 : Calculation, c3 : Calculation) {
    var speedFunction = new Calculation(c1)
    var efficiencyFunction = new Calculation(c2)
    var multiplicityFunction = new Calculation(c3)

    override def hashCode : Int =
         util.Arrays.hashCode(Array[AnyRef](speedFunction, efficiencyFunction, multiplicityFunction))
}
