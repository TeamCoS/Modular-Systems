package com.teambr.modularsystems.power.tiles

import cofh.api.energy.EnergyStorage
import com.teambr.bookshelf.common.tiles.traits.EnergyHandler
import com.teambr.modularsystems.core.providers.FuelProvider.FuelProviderType
import com.teambr.modularsystems.core.registries.ConfigRegistry
import net.minecraft.nbt.NBTTagCompound

/**
 * This file was created for Modular-Systems
 *
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Dyonovan
 * @since August 10, 2015
 */
class TileBankRF extends TileBankBase with EnergyHandler {

    private val energy = new EnergyStorage(10000)

    /**
     * Used to scale the current power level
      *
      * @param scale The scale to move to
     * @return A number from 0 - { @param scale} for current level
     */
    override def getPowerLevelScaled(scale: Int): Double = energy.getEnergyStored * scale / energy.getMaxEnergyStored

    /*******************************************************************************************************************
      ******************************************** RF Energy Functions *************************************************
      ******************************************************************************************************************/

    override def defaultEnergyStorageSize: Int = 80000

    override def isReceiver: Boolean = true

    override def isProvider: Boolean = false

    /*******************************************************************************************************************
      **************************************** Fuel Provider Functions *************************************************
      ******************************************************************************************************************/

    override def consume(): Double = {
        val actual = energy.extractEnergy(Math.round(ConfigRegistry.rfPower * 200).toInt, false)
        worldObj.notifyBlockUpdate(pos, worldObj.getBlockState(pos), worldObj.getBlockState(pos), 6)
        (actual / (ConfigRegistry.rfPower * 200)) * 200
    }

    override def `type`(): FuelProviderType = FuelProviderType.RF

    override def fuelProvided(): Double = {
        val actual = energy.extractEnergy(Math.round(ConfigRegistry.rfPower * 200).toInt, false)
        (actual / (ConfigRegistry.rfPower * 200)) * 200
    }

    override def canProvide: Boolean = energy.getEnergyStored > 0

    /*******************************************************************************************************************
      ******************************************** Tile Methods ********************************************************
      ******************************************************************************************************************/
    override def readFromNBT (tags: NBTTagCompound) {
        super.readFromNBT(tags)
        energy.readFromNBT(tags)
    }


    override def writeToNBT(tags: NBTTagCompound) {
        super.writeToNBT(tags)
        energy.writeToNBT(tags)
    }
}
