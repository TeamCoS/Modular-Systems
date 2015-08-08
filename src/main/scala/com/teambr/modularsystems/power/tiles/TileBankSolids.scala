package com.teambr.modularsystems.power.tiles

import com.teambr.bookshelf.common.tiles.traits.Inventory
import com.teambr.modularsystems.core.providers.FuelProvider.FuelProviderType
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntityFurnace

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
class TileBankSolids extends TileBankBase with Inventory {

    /**
     * Used to scale the current power level
     * @param scale The scale to move to
     * @return A number from 0 - { @param scale} for current level
     */
    override def getPowerLevelScaled(scale: Int): Double = getFuelCount * scale / getSizeInventory()

    /**
     * Used to count how many slots have things in them
     * @return How many slots have an item
     */
    private def getFuelCount: Int = {
        var count = 0
        for (i <- 0 until getSizeInventory()) {
            if (getStackInSlot(i) != null && getStackInSlot(i).stackSize > 0)
                count += 1
        }
        count
    }

    /**
     * Helper Method to consume solid fuels
     */
    private def consumeFuel(simulate: Boolean): Int = {
        for (i<- 0 until getSizeInventory()) {
            if (getStackInSlot(i) != null && getStackInSlot(i).stackSize > 0 && TileEntityFurnace.isItemFuel(getStackInSlot(i))) {
                val burnValue = TileEntityFurnace.getItemBurnTime(getStackInSlot(i))
                if (!simulate) {
                    decrStackSize(i, 1)
                    worldObj.markBlockForUpdate(pos)
                }
                burnValue
            }
        }
        0
    }

    /*******************************************************************************************************************
      **************************************** Fuel Provider Functions *************************************************
      ******************************************************************************************************************/
    override def canProvide: Boolean = consumeFuel(true) > 0

    override def fuelProvided(): Double = consumeFuel(true)

    override def consume(): Double = consumeFuel(false)

    override def `type`(): FuelProviderType = FuelProviderType.ITEM

    /*******************************************************************************************************************
      ************************************** Inventory Methods *********************************************************
      ******************************************************************************************************************/

    override def initialSize: Int = 27

    override var inventoryName: String = "inventory.solidspower.title"

    override def readFromNBT(tag: NBTTagCompound) : Unit = {
        super[TileBankBase].readFromNBT(tag)
        super[Inventory].readFromNBT(tag)
    }

    override def writeToNBT(tag: NBTTagCompound) {
        super[TileBankBase].writeToNBT(tag)
        super[Inventory].writeToNBT(tag)
    }

    override def markDirty(): Unit = {
        super[TileBankBase].markDirty()
        super[Inventory].markDirty()
    }
}
