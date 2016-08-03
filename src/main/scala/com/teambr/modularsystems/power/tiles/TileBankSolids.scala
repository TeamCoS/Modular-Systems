package com.teambr.modularsystems.power.tiles

import com.teambr.bookshelf.common.tiles.traits.Inventory
import com.teambr.modularsystems.core.providers.FuelProvider.FuelProviderType
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntityFurnace
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.items.CapabilityItemHandler

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
      *
      * @param scale The scale to move to
      * @return A number from 0 - { @param scale} for current level
      */
    override def getPowerLevelScaled(scale: Int): Double = getFuelCount * scale / 27

    /**
      * Used to count how many slots have things in them
      *
      * @return How many slots have an item
      */
    private def getFuelCount: Int = {
        var count = 0
        for (i <- 0 until getSizeInventory) {
            if (getStackInSlot(i) != null && getStackInSlot(i).stackSize > 0)
                count += 1
        }
        count
    }

    /**
      * Helper Method to consume solid fuels
      */
    private def consumeFuel(simulate: Boolean): Int = {
        for (i<- 0 until getSizeInventory) {
            if (getStackInSlot(i) != null && getStackInSlot(i).stackSize > 0 && TileEntityFurnace.isItemFuel(getStackInSlot(i))) {
                val burnValue = TileEntityFurnace.getItemBurnTime(getStackInSlot(i))
                if (!simulate) {
                    decrStackSize(i, 1)
                    worldObj.notifyBlockUpdate(pos, worldObj.getBlockState(pos), worldObj.getBlockState(pos), 6)
                }
                return burnValue
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


    override def hasCapability(capability: Capability[_], facing : EnumFacing) = {
        capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
    }

    override def getCapability[T](capability: Capability[T], facing: EnumFacing): T = {
        super[Inventory].getCapability(capability, facing)
    }

    override def getSizeInventory : Int = {
        super[Inventory].getSizeInventory
    }

    override def getStackInSlot(index : Int) : ItemStack = {
        super[Inventory].getStackInSlot(index)
    }

    override def decrStackSize(index : Int, count : Int) : ItemStack = {
        super[Inventory].decrStackSize(index, count)
    }

    override def setInventorySlotContents(index : Int, stack : ItemStack) : Unit = {
        super[Inventory].setInventorySlotContents(index, stack)
    }

    override def getInventoryStackLimit : Int = {
        super[Inventory].getInventoryStackLimit
    }

    override def isUseableByPlayer(player : EntityPlayer) : Boolean = {
        super[Inventory].isUseableByPlayer(player)
    }

    override def isItemValidForSlot(index : Int, stack : ItemStack) : Boolean = {
        TileEntityFurnace.getItemBurnTime(stack) > 0
    }

    override def clear() : Unit = {
        super[Inventory].clear()
    }

    override def getSlotsForFace(side : EnumFacing) : Array[Int] = {
        val array = new Array[Int](27)
        for(i <- 0 to 27)
            array(i) = i
        array
    }

    override def canExtractItem(index : Int, stack : ItemStack, direction : EnumFacing) : Boolean = {
        true
    }

    override def canInsertItem(index : Int, itemStackIn : ItemStack, direction : EnumFacing) : Boolean = {
        TileEntityFurnace.getItemBurnTime(itemStackIn) > 0
    }

    override def removeStackFromSlot(index: Int): ItemStack = {
        super[Inventory].removeStackFromSlot(index)
    }

    override def readFromNBT(tag: NBTTagCompound) : Unit = {
        super[TileBankBase].readFromNBT(tag)
        super[Inventory].readFromNBT(tag)
    }

    override def writeToNBT(tag: NBTTagCompound) : NBTTagCompound = {
        super[TileBankBase].writeToNBT(tag)
        super[Inventory].writeToNBT(tag)
        tag
    }

    override def markDirty(): Unit = {
        super[TileBankBase].markDirty()
        super[Inventory].markDirty()
    }
}
