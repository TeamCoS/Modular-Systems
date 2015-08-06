package com.teambr.modularsystems.core.collections

import com.teambr.bookshelf.traits.NBTSavable
import net.minecraft.nbt.NBTTagCompound

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
class StandardValues extends NBTSavable {
    var burnTime = 0
    var currentItemBurnTime = 0
    var cookTime = 0

    var speed = 0.0D
    var efficiency = 0.0D
    var multiplicity = 0

    var isPowered = false

    def resetStructureValues() : Unit = {
        speed = 0.0D
        efficiency = 0.0D
        multiplicity = 0
        burnTime = 0
    }

    override def writeToNBT(tag : NBTTagCompound) : Unit = {
        tag.setInteger("Burn Time", burnTime)
        tag.setInteger("Cook Time", cookTime)
        tag.setInteger("Current Burn", currentItemBurnTime)

        tag.setDouble("Speed", speed)
        tag.setDouble("Efficiency", efficiency)
        tag.setInteger("Multiplicity", multiplicity)

        tag.setBoolean("IsPowered", isPowered)
    }

    override def readFromNBT(tag : NBTTagCompound) : Unit = {
        burnTime = tag.getInteger("Burn Time")
        cookTime = tag.getInteger("Cook Time")
        currentItemBurnTime = tag.getInteger("Current Burn")

        speed = tag.getDouble("Speed")
        efficiency = tag.getDouble("Efficiency")
        multiplicity = tag.getInteger("Multiplicity")

        isPowered = tag.getBoolean("IsPowered")
    }
}
