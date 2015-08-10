package com.teambr.modularsystems.power.tiles

import com.teambr.bookshelf.common.tiles.traits.{UpdatingTile, Inventory}
import com.teambr.modularsystems.core.providers.FuelProvider.FuelProviderType
import com.teambr.modularsystems.core.registries.FluidFuelValues
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraftforge.fluids._

/**
 * This file was created for Modular-Systems
 *
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Dyonovan
 * @since August 08, 2015
 */
class TileBankLiquids extends TileBankBase with IFluidHandler with Inventory with UpdatingTile {

    final val BUCKET_IN = 0
    final val BUCKET_OUT = 1

    var tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 20)

    override def onServerTick(): Unit = {
        if (coolDown >= 0) coolDown += 1
        if (coolDown >= 20) coolDown = 0

        if (getStackInSlot(BUCKET_IN) != null &&
                FluidContainerRegistry.isFilledContainer(getStackInSlot(BUCKET_IN)) &&
                FluidFuelValues.getFluidFuelValue(FluidContainerRegistry.getFluidForFilledItem(getStackInSlot(BUCKET_IN)).getFluid.getName) > 0 &&
                tank.getCapacity >= tank.getFluidAmount + FluidContainerRegistry.getContainerCapacity(getStackInSlot(BUCKET_IN))) {

            if (getStackInSlot(BUCKET_OUT) == null ||
                    getStackInSlot(BUCKET_OUT).isItemEqual(FluidContainerRegistry.drainFluidContainer(getStackInSlot(BUCKET_IN))) ||
                    getStackInSlot(BUCKET_OUT).getMaxStackSize > getStackInSlot(BUCKET_OUT).stackSize) {

                if (tank.getFluid != null || FluidContainerRegistry.getFluidForFilledItem(getStackInSlot(BUCKET_IN)).getFluid != null ||
                        tank.getFluid.getFluid == FluidContainerRegistry.getFluidForFilledItem(getStackInSlot(BUCKET_IN)).getFluid) {

                    fill(null, FluidContainerRegistry.getFluidForFilledItem(getStackInSlot(BUCKET_IN)), doFill = true)
                }

                //return Empty Container
                if (getStackInSlot(BUCKET_OUT) == null)
                    setInventorySlotContents(BUCKET_OUT, FluidContainerRegistry.drainFluidContainer(getStackInSlot(BUCKET_IN)))
                else
                    getStackInSlot(BUCKET_OUT).stackSize += 1

                decrStackSize(BUCKET_IN, 1)
                worldObj.markBlockForUpdate(pos)
            }
        } else if (getStackInSlot(BUCKET_IN) != null && FluidContainerRegistry.isEmptyContainer(getStackInSlot(BUCKET_IN))
                && tank.getFluid != null && tank.getFluidAmount > FluidContainerRegistry.getContainerCapacity(getStackInSlot(BUCKET_IN))) {
            if (getStackInSlot(BUCKET_OUT) == null) {

                //return full container
                setInventorySlotContents(BUCKET_OUT, FluidContainerRegistry.fillFluidContainer(tank.getFluid, getStackInSlot(BUCKET_IN)))

                drain(null, FluidContainerRegistry.BUCKET_VOLUME, doDrain = true)

                //clear input slot
                decrStackSize(BUCKET_IN, 1)
                worldObj.markBlockForUpdate(pos)
            }
        }
    }

    /**
     * Used to scale the current power level
     * @param scale The scale to move to
     * @return A number from 0 - { @param scale} for current level
     */
    override def getPowerLevelScaled(scale: Int): Double = tank.getFluidAmount * scale / tank.getCapacity

    /*******************************************************************************************************************
      **************************************** Fuel Provider Functions *************************************************
      ******************************************************************************************************************/

    override def consume(): Double = {
        if (tank.getFluid != null && tank.getFluidAmount >= 100) {
            val fluid = tank.getFluid
            val value = FluidFuelValues.getFluidFuelValue(fluid.getFluid.getName)
            val actual = tank.drain(100, true)
            worldObj.markBlockForUpdate(pos)
            return Math.round(actual.amount.toFloat / FluidContainerRegistry.BUCKET_VOLUME * value)
        }
        0
    }

    override def `type`(): FuelProviderType = FuelProviderType.LIQUID

    override def fuelProvided(): Double = {
        if (tank.getFluid != null && tank.getFluidAmount >= 100) {
            val fluid = tank.getFluid
            val value = FluidFuelValues.getFluidFuelValue(fluid.getFluid.getName)
            val actual = tank.drain(100, true)
            worldObj.markBlockForUpdate(pos)
            return Math.round(actual.amount.toFloat / FluidContainerRegistry.BUCKET_VOLUME * value)
        }
        0
    }

    override def canProvide: Boolean = tank.getFluid != null && tank.getFluidAmount >= 100

    /** *****************************************************************************************************************
      * ****************************************** FluidTank Methods ****************************************************
      * *****************************************************************************************************************/

    override def drain(from: EnumFacing, resource: FluidStack, doDrain: Boolean): FluidStack = {
        if (resource == null || !resource.isFluidEqual(tank.getFluid)) {
            return null
        }
        tank.drain(resource.amount, doDrain)
    }

    override def drain(from: EnumFacing, maxDrain: Int, doDrain: Boolean): FluidStack = tank.drain(maxDrain, doDrain)

    override def canFill(from: EnumFacing, fluid: Fluid): Boolean = FluidFuelValues.getFluidFuelValue(fluid.getName) > 0

    override def canDrain(from: EnumFacing, fluid: Fluid): Boolean = false

    override def fill(from: EnumFacing, resource: FluidStack, doFill: Boolean): Int = {
        if (FluidFuelValues.getFluidFuelValue(resource.getFluid.getName) > 0) {
            val amount = tank.fill(resource, doFill)
            worldObj.markBlockForUpdate(pos)
            return amount
        }
        0
    }

    override def getTankInfo(from: EnumFacing): Array[FluidTankInfo] = Array(tank.getInfo)

    /** *****************************************************************************************************************
      * ************************************* Inventory Methods *********************************************************
      * *****************************************************************************************************************/

    override var inventoryName: String = _

    override def initialSize: Int = 2

    /** *****************************************************************************************************************
      * ****************************************** Tile Methods *********************************************************
      * *****************************************************************************************************************/

    override def readFromNBT(tag: NBTTagCompound): Unit = {
        super[TileBankBase].readFromNBT(tag)
        super[Inventory].readFromNBT(tag)
        tank.readFromNBT(tag)
    }

    override def writeToNBT(tag: NBTTagCompound) {
        super[TileBankBase].writeToNBT(tag)
        super[Inventory].writeToNBT(tag)
        tank.writeToNBT(tag)
    }

    override def markDirty(): Unit = {
        super[TileBankBase].markDirty()
        super[Inventory].markDirty()
    }
}
