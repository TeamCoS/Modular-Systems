package com.teambr.modularsystems.power.tiles

import com.teambr.modularsystems.core.providers.FuelProvider
import com.teambr.modularsystems.core.common.tiles.TileProxy
import net.minecraft.nbt.NBTTagCompound

/**
 * This file was created for Modular-Systems
 *
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Dyonovan
 * @since August 07, 2015
 */
abstract class TileBankBase extends TileProxy with FuelProvider {

    protected[power] var priority = 0
    protected[power] var coolDown = 0

    /**
     * Used to scale the current power level
     * @param scale The scale to move to
     * @return A number from 0 - { @param scale} for current level
     */
    def getPowerLevelScaled(scale: Int): Double

    /******************************************************************************************************************
      ******************************************* Tile Methods ********************************************************
      *****************************************************************************************************************/
    override def readFromNBT(tag: NBTTagCompound) : Unit = {
        super.readFromNBT(tag)
        priority = tag.getInteger("priority")
    }

    override def writeToNBT(tag: NBTTagCompound) {
        super.writeToNBT(tag)
        tag.setInteger("priority", priority)
    }

    /*******************************************************************************************************************
      ***************************************** Fuel Provider Functions ************************************************
      ******************************************************************************************************************/
    override def getPriority: Int = {
        priority
    }
}
