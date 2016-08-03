package com.teambr.modularsystems.core.common.tiles

import java.util
import java.util.Collections

import com.teambr.bookshelf.collections.Location
import com.teambr.bookshelf.common.blocks.properties.Properties
import com.teambr.bookshelf.common.tiles.traits.{InventorySided, UpdatingTile}
import com.teambr.bookshelf.util.WorldUtils
import com.teambr.modularsystems.core.collections.StandardValues
import com.teambr.modularsystems.core.common.blocks.traits.CoreStates
import com.teambr.modularsystems.core.functions.BlockCountFunction
import com.teambr.modularsystems.core.managers.BlockManager
import com.teambr.modularsystems.core.providers.FuelProvider
import com.teambr.modularsystems.crusher.tiles.TileCrusherExpansion
import com.teambr.modularsystems.power.tiles.TileBankBase
import net.minecraft.block.Block
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

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
abstract class AbstractCore extends UpdatingTile with InventorySided {
    final val cookSpeed = 200
    final val MAX_SIZE = 100

    val values = new StandardValues
    var corners : (BlockPos, BlockPos) = null
    var isDirty = true
    var wellFormed = false

    /**
     * Get the output of the recipe
      *
      * @param stack The input
     * @return The output
     */
    def recipe(stack : ItemStack) : ItemStack

    /**
     * Check if this blocks is not allowed in the structure
      *
      * @param block The blocks to check
     * @param meta The meta data of said blocks
     * @return True if it is banned
     */
    def isBlockBanned(block : Block, meta : Int) : Boolean

    /**
     * Take the blocks in this structure and generate the speed etc values
      *
      * @param function The blocks count function
     */
    def generateValues(function : BlockCountFunction)

    /**
     * Used to output the redstone single from this structure
     *
     * Use a range from 0 - 16.
     *
     * 0 Usually means that there is nothing in the tile, so take that for lowest level. Like the generator has no energy while
     * 16 is usually the flip side of that. Output 16 when it is totally full and not less
     *
     * @return int range 0 - 16
     */
    def getRedstoneOutput : Int

    /*******************************************************************************************************************
      *******************************************  Multiblock Methods  **************************************************
      *******************************************************************************************************************/
    def updateMultiblock() : Boolean = {
        if (isDirty) {
            if (isWellFormed) {
                buildMultiblock()
            }
            else {
                deconstructMultiblock()
            }
            isDirty = false
        }
        wellFormed
    }

    def isWellFormed : Boolean = {
        wellFormed = false

        //Weird Bug
        if(worldObj.isAirBlock(getPos))
            return false

        getCorners match {
            case Some(corner) =>
                corners = corner
                val outside = new Location(corners._1).getAllWithinBounds(new Location(corners._2), includeInner = false, includeOuter = true)
                val inside = new Location(corners._1).getAllWithinBounds(new Location(corners._2), includeInner = true, includeOuter = false)

                for(i <- 0 until outside.size()) {
                    val location = outside.get(i)
                    //This is us
                    if(location.equals(new Location(pos.getX, pos.getY, pos.getZ))) {
                        //Continue
                    }
                    //val block = worldObj.getBlockState(location.asBlockPos).getBlock
                    if(worldObj.isAirBlock(location.asBlockPos) ||
                            isBlockBanned(worldObj.getBlockState(location.asBlockPos).getBlock, getBlockMetadata)) {
                        return false
                    }
                }

                //Tells if it's against a wall
                if(inside.size <= 0)
                    return false

                for(i <- 0 until inside.size()) {
                    val location = inside.get(i)
                    if(!worldObj.isAirBlock(location.asBlockPos))
                        return false
                }

                wellFormed = true
                true
            case _ => wellFormed
        }
    }

    def buildMultiblock() : Unit = {
        //Just to be safe, though it should never happen
        if(corners == null)
            return

        val outside = new Location(corners._1).getAllWithinBounds(new Location(corners._2), includeInner = false, includeOuter = true)
        val blockCountFunction = new BlockCountFunction
        for(i <- 0 until outside.size()) {
            val loc = outside.get(i).asBlockPos

            if(!loc.equals(pos)) { //Not us, so we can do something
                worldObj.getTileEntity(loc) match {
                    case tile : TileProxy =>
                        tile.setCore(this)
                        worldObj.notifyBlockUpdate(loc, worldObj.getBlockState(loc), worldObj.getBlockState(loc), 3)
                    case _ =>
                        val id = Block.getIdFromBlock(worldObj.getBlockState(loc).getBlock)
                        val meta = worldObj.getBlockState(loc).getBlock.getMetaFromState(worldObj.getBlockState(loc))
                        blockCountFunction.addBlock(worldObj.getBlockState(loc).getBlock, meta)

                        worldObj.setBlockState(loc, BlockManager.proxy.getDefaultState)
                        val proxy = worldObj.getTileEntity(loc).asInstanceOf[TileProxy]
                        proxy.setCore(this)
                        proxy.storedBlock = id
                        proxy.metaData = meta
                        worldObj.notifyBlockUpdate(loc, worldObj.getBlockState(loc), worldObj.getBlockState(loc), 3)
                }
            }
        }
        generateValues(blockCountFunction)
        wellFormed = true
        worldObj.notifyBlockUpdate(pos, worldObj.getBlockState(pos), worldObj.getBlockState(pos), 3)
        worldObj.markBlockRangeForRenderUpdate(corners._1, corners._2)
    }

    def deconstructMultiblock() : Unit = {
        //Just to be safe...
        if(corners == null)
            return

        values.resetStructureValues()
        val outside = new Location(corners._1).getAllWithinBounds(new Location(corners._2), includeInner = false, includeOuter = true)
        for(i <- 0 until outside.size()) {
            val loc = outside.get(i).asBlockPos

            if (!loc.equals(pos) && !worldObj.isAirBlock(loc)) { //Not us, so we can do something
                worldObj.getTileEntity(loc) match {
                    case proxy : TileProxy if !proxy.isInstanceOf[TileBankBase] && !proxy.isInstanceOf[TileCrusherExpansion] && !proxy.isInstanceOf[TileIOExpansion] =>
                        val meta = proxy.metaData
                        worldObj.setBlockState(loc, proxy.getStoredBlock.getStateFromMeta(meta))
                        worldObj.notifyBlockUpdate(loc, worldObj.getBlockState(loc), worldObj.getBlockState(loc), 3)
                    case tile : TileBankBase =>
                        tile.coreLocation = None
                        worldObj.notifyBlockUpdate(loc, worldObj.getBlockState(loc), worldObj.getBlockState(loc), 3)
                    case tile : TileCrusherExpansion =>
                        tile.coreLocation = None
                        worldObj.notifyBlockUpdate(loc, worldObj.getBlockState(loc), worldObj.getBlockState(loc), 3)
                    case tile : TileIOExpansion =>
                        tile.coreLocation = None
                        worldObj.notifyBlockUpdate(loc, worldObj.getBlockState(loc), worldObj.getBlockState(loc), 3)
                    case _ =>
                }
            }
        }
        wellFormed = false
        worldObj.notifyBlockUpdate(pos, worldObj.getBlockState(pos), worldObj.getBlockState(pos), 3)
    }

    def getCorners : Option[(BlockPos, BlockPos)] = {
        val local = getPos
        var firstCorner = new BlockPos(local)
        var secondCorner = new BlockPos(local)

        val dir : EnumFacing = worldObj.getBlockState(this.pos)
                .getValue(Properties.FOUR_WAY)

        //Move Inside
        firstCorner = firstCorner.offset(dir.getOpposite)
        secondCorner = secondCorner.offset(dir.getOpposite)

        //Get our directions
        val right = WorldUtils.rotateRight(dir)
        val left = WorldUtils.rotateLeft(dir)

        //Get first corner
        //Find side
        while(worldObj.isAirBlock(firstCorner)) {
            firstCorner = firstCorner.offset(right)
            if(pos.distanceSq(firstCorner) > MAX_SIZE)
                return None
        }

        //Pop back inside
        firstCorner = firstCorner.offset(right.getOpposite)

        //Find floor
        while(worldObj.isAirBlock(firstCorner)) {
            firstCorner = firstCorner.offset(EnumFacing.DOWN)
            if(pos.distanceSq(firstCorner) > MAX_SIZE)
                return None
        }

        //Found, so move to physical location
        firstCorner = firstCorner.offset(right)
        firstCorner = firstCorner.offset(dir)

        //Find side
        while(worldObj.isAirBlock(secondCorner)) {
            secondCorner = secondCorner.offset(left)
            if(pos.distanceSq(secondCorner) > MAX_SIZE)
                return None
        }

        //Pop back inside
        secondCorner = secondCorner.offset(left.getOpposite)

        //Move to back
        while(worldObj.isAirBlock(secondCorner)) {
            secondCorner = secondCorner.offset(dir.getOpposite)
            if(pos.distanceSq(secondCorner) > MAX_SIZE)
                return None
        }

        secondCorner = secondCorner.offset(dir)

        //Move UP
        while(worldObj.isAirBlock(secondCorner)) {
            secondCorner = secondCorner.offset(EnumFacing.UP)
            if(pos.distanceSq(secondCorner) > MAX_SIZE)
                return None
        }

        //Found, so move back to physical location
        secondCorner = secondCorner.offset(left)
        secondCorner = secondCorner.offset(dir.getOpposite)

        Some(firstCorner, secondCorner)
    }

    /*******************************************************************************************************************
      ************************************************  Furnace Methods  ***********************************************
      ******************************************************************************************************************/

    var wasBurning = false
    protected def doWork() : Unit = {
        var didWork : Boolean = false
        wasBurning = this.values.burnTime > 0
        if (!worldObj.isRemote) {
            if (this.values.burnTime > 0) {
                this.values.burnTime = values.burnTime - 1
                worldObj.notifyBlockUpdate(pos, worldObj.getBlockState(pos), worldObj.getBlockState(pos), 6)
            }
            if (canSmelt(getStackInSlot(0), recipe(getStackInSlot(0)), getStackInSlot(1)) && !values.isPowered) {
                if (corners == null)  corners = getCorners match {
                    case Some(v) => v
                    case _ => null
                }

                if (corners == null) {
                    markDirty()
                    return
                }
                val providers : util.List[FuelProvider] = getFuelProviders(new Location(corners._1).getAllWithinBounds(new Location(corners._2), includeInner = false, includeOuter = true))
                if (this.values.burnTime <= 0 && !providers.isEmpty) {
                    val scaledBurnTime : Int = getAdjustedBurnTime(providers.get(0).consume)
                    this.values.burnTime = scaledBurnTime
                    this.values.currentItemBurnTime = this.values.burnTime
                    cook
                    // Started Burn
                    worldObj.setBlockState(pos, worldObj.getBlockState(pos).withProperty(CoreStates.PROPERTY_ACTIVE,
                        (values.burnTime > 0).asInstanceOf[java.lang.Boolean]))
                    worldObj.notifyBlockUpdate(pos, worldObj.getBlockState(pos), worldObj.getBlockState(pos), 3)
                }
                else if (isBurning) {
                    didWork = cook
                }
                else {
                    this.values.cookTime = 0
                    this.values.burnTime = 0
                    // Back to not burning
                    worldObj.setBlockState(pos, worldObj.getBlockState(pos).withProperty(CoreStates.PROPERTY_ACTIVE,
                        (values.burnTime > 0).asInstanceOf[java.lang.Boolean]))
                    worldObj.notifyBlockUpdate(pos, worldObj.getBlockState(pos), worldObj.getBlockState(pos), 3)
                }
            }
            else if (this.values.burnTime <= 0 && wasBurning) {
                this.values.cookTime = 0
                // Can't burn
                worldObj.setBlockState(pos, worldObj.getBlockState(pos).withProperty(CoreStates.PROPERTY_ACTIVE,
                    (values.burnTime > 0).asInstanceOf[java.lang.Boolean]))
                worldObj.notifyBlockUpdate(pos, worldObj.getBlockState(pos), worldObj.getBlockState(pos), 3)
            }
            if (didWork) {
                worldObj.setBlockState(pos, worldObj.getBlockState(pos).withProperty(CoreStates.PROPERTY_ACTIVE,
                    (values.burnTime > 0).asInstanceOf[java.lang.Boolean]))
                worldObj.notifyBlockUpdate(pos, worldObj.getBlockState(pos), worldObj.getBlockState(pos), 6)
            }
        }
    }

    protected def getFuelProviders(coords : util.List[Location]) : util.ArrayList[FuelProvider] = {
        val providers = new util.ArrayList[FuelProvider]
        for (i <- 0 until coords.size()) {
            val coord = coords.get(i)
            val te : TileEntity = worldObj.getTileEntity(coord.asBlockPos)
            if (te != null) {
                te match {
                    case provider1 : FuelProvider if provider1.canProvide =>
                        providers.add(provider1)
                    case _ =>
                }
            }
        }
        Collections.sort(providers, new FuelProvider.FuelSorter)
        providers
    }

    private def cook : Boolean = {
        this.values.cookTime += 1
        if (this.values.cookTime >= getAdjustedCookTime) {
            this.values.cookTime = 0
            this.smeltItem()
            true
        }
        else {
            false
        }
    }

    def canSmelt(input : ItemStack, result : ItemStack, output : ItemStack) : Boolean = {
        if (input == null || result == null)
            false
        else if (output == null)
            true
        else if (!output.isItemEqual(result))
            false
        else {
            //The size below would be if the smeltingMultiplier = 1
            //If the smelting multiplier is > 1,
            //there is no guarantee that all potential operations will be completed.
            val minStackSize : Int = output.stackSize + result.stackSize
            minStackSize <= getInventoryStackLimit && minStackSize <= result.getMaxStackSize
        }
    }

    def smeltItem() {
        val smeltCount : (Int, Int) = smeltCountAndSmeltSize
        if (smeltCount != null && smeltCount._2 > 0) {
            var recipeResult : ItemStack = recipe(getStackInSlot(0))
            decrStackSize(0, smeltCount._1)
            //getStackInSlot(0).stackSize -= smeltCount._1
            if (getStackInSlot(1) == null) {
                recipeResult = recipeResult.copy
                recipeResult.stackSize = smeltCount._2
                setInventorySlotContents(1, recipeResult)
            }
            else {
                getStackInSlot(1).stackSize += smeltCount._2
            }
        }
    }

    def smeltCountAndSmeltSize : (Int, Int) = {
        var input : ItemStack = getStackInSlot(0)
        if (input == null) {
            return null
        }
        var output : ItemStack = getStackInSlot(1)
        val recipeResult : ItemStack = recipe(input)
        if (recipeResult == null && output != null && !output.isItemEqual(recipeResult)) {
            return null
        }
        else if (output == null) {
            output = recipeResult.copy
            output.stackSize = 0
        }
        input = input.copy
        val recipeStackSize : Int = if (recipeResult.stackSize > 0) recipeResult.stackSize else 1
        val outMax : Int = if (getInventoryStackLimit < output.getMaxStackSize) output.getMaxStackSize else getInventoryStackLimit
        val outAvailable : Int = outMax - output.stackSize
        var avail : Int = if (values.multiplicity + 1 < input.stackSize) values.multiplicity.toInt + 1 else input.stackSize
        var count : Int = recipeStackSize * avail
        if (count > outAvailable) {
            avail = outAvailable / recipeStackSize
            count = avail * recipeStackSize
        }
        (avail, count)
    }

    def isBurning : Boolean =
        values.burnTime > 0

    def getAdjustedBurnTime(fuelValue : Double) : Int = {
        var scaledTicks : Double = ((1600 + values.efficiency) / 1600) * fuelValue
        scaledTicks = scaledTicks / (values.multiplicity + 1)
        Math.max(scaledTicks.round.toInt, 5)
    }

    private def getAdjustedCookTime : Double =
        Math.max(cookSpeed + values.speed, 1)

    @SideOnly(Side.CLIENT) def getCookProgressScaled(scaleVal : Int) : Int =
        ((this.values.cookTime * scaleVal) / Math.max(getAdjustedCookTime, 0.001)).toInt

    @SideOnly(Side.CLIENT) def getBurnTimeRemainingScaled(scaleVal : Int) : Int =
        ((this.values.burnTime * scaleVal) / Math.max(this.values.currentItemBurnTime, 0.001)).toInt


    override def markDirty() : Unit = {
        super[TileEntity].markDirty()
        super[InventorySided].markDirty()
    }

    /******************************************************************************************************************
      **************************************************  Tile Methods  ************************************************
      ******************************************************************************************************************/

    override def onServerTick() : Unit = {
        if(updateMultiblock())
            doWork()
    }

    def setDirty() : Unit = isDirty = true

    override def writeToNBT(tag : NBTTagCompound) : NBTTagCompound = {
        super[TileEntity].writeToNBT(tag)
        super[InventorySided].writeToNBT(tag)
        values.writeToNBT(tag)
        tag.setBoolean("IsDirty", isDirty)
        tag.setBoolean("WellFormed", wellFormed)

        if(corners != null) {
            tag.setLong("First", corners._1.toLong)
            tag.setLong("Second", corners._2.toLong)
        }
        tag
    }

    override def readFromNBT(tag : NBTTagCompound) : Unit = {
        super[TileEntity].readFromNBT(tag)
        super[InventorySided].readFromNBT(tag)
        values.readFromNBT(tag)
        isDirty = tag.getBoolean("IsDirty")
        wellFormed = tag.getBoolean("WellFormed")

        corners = (BlockPos.fromLong(tag.getLong("First")), BlockPos.fromLong(tag.getLong("Second")))
    }

    /*******************************************************************************************************************
      ************************************************* Inventory methods ***********************************************
      *******************************************************************************************************************/

    def getSlotsForFace(side: EnumFacing): Array[Int] = {
        Array(0, 1)
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side. Args: slot, item,
     * side
     */
    override def canInsertItem(index : Int, itemStackIn : ItemStack, direction : EnumFacing) : Boolean = {
        if(index == 0) {
            return recipe(itemStackIn) != null
        }
        false
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side. Args: slot, item,
     * side
     */
    override def canExtractItem(index : Int, stack : ItemStack, direction : EnumFacing) : Boolean = index == 1

    /**
     * Used to define if an item is valid for a slot
      *
      * @param index The slot id
     * @param stack The stack to check
     * @return True if you can put this there
     */
    override def isItemValidForSlot(index: Int, stack: ItemStack): Boolean = index == 0 &&
            (stack != null && recipe(stack) != null)

    override def initialSize : Int = 2
}
