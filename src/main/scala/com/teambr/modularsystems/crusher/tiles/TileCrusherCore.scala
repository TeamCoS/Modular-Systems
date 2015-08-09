package com.teambr.modularsystems.crusher.tiles

import com.teambr.bookshelf.collections.Location
import com.teambr.bookshelf.helper.BlockHelper
import com.teambr.modularsystems.core.common.blocks.BlockProxy
import com.teambr.modularsystems.core.common.tiles.AbstractCore
import com.teambr.modularsystems.core.functions.BlockCountFunction
import com.teambr.modularsystems.core.managers.BlockManager
import com.teambr.modularsystems.core.registries.{BlockValueRegistry, CrusherRecipeRegistry, FurnaceBannedBlocks}
import net.minecraft.block.Block
import net.minecraft.inventory.Container
import net.minecraft.item.ItemStack

import scala.util.Random

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
class TileCrusherCore extends AbstractCore {

    override def smeltItem() {
        val smeltCount : (Int, Int) = smeltCountAndSmeltSize
        if (smeltCount != null && smeltCount._2 > 0) {
            val recipeResult = recipeCrusher(getStackInSlot(0))
            decrStackSize(0, smeltCount._1)
            if (getStackInSlot(1) == null) {
                val recipeOutput = recipeResult._1.copy
                recipeOutput.stackSize = smeltCount._2
                setInventorySlotContents(1, recipeOutput)
            }
            else {
                getStackInSlot(1).stackSize += smeltCount._2
            }
            if (getCrusherExtraCount != 0 && recipeResult._2 != null) {
                var extraCount = 0
                for (i <- 0 until values.multiplicity.toInt + 1) {
                    val random = Random.nextInt(100)
                    if (getCrusherExtraCount >=  random) {
                        extraCount += 1
                    }
                }
                if (getStackInSlot(2) == null && extraCount > 0) {
                    val extraOutput = recipeResult._2.copy
                    extraOutput.stackSize = extraCount
                    setInventorySlotContents(2, extraOutput)
                } else if (extraCount > 0) {
                    getStackInSlot(2).stackSize += extraCount
                }
            }
        }
    }

    override def canSmelt(input : ItemStack, result : ItemStack, output : ItemStack) : Boolean = {
        val recipeResults = recipeCrusher(input)
        if (recipeResults != null) {
            val crusherOutput = recipeResults._1
            val crusherExtra = recipeResults._2
            if (input == null || result == crusherOutput)
                return false
            else if (output == null && crusherExtra != null)
                return true
            else if (getStackInSlot(2) != null || !crusherExtra.isItemEqual(getStackInSlot(2)))
                return false
            else if (!output.isItemEqual(result))
                return false
            else {
                //The size below would be if the smeltingMultiplier = 1
                //If the smelting multiplier is > 1,
                //there is no guarantee that all potential operations will be completed.
                val minStackSize: Int = output.stackSize + result.stackSize
                if (getCrusherExtraCount == 0 || crusherExtra == null || getStackInSlot(2) == null) {
                    return minStackSize <= getInventoryStackLimit && minStackSize <= result.getMaxStackSize
                } else {
                    val extraMinStackSize = crusherExtra.stackSize + getStackInSlot(2).stackSize
                    return extraMinStackSize <= getInventoryStackLimit() && extraMinStackSize <= getStackInSlot(2).getMaxStackSize &&
                            minStackSize <= getInventoryStackLimit && minStackSize <= result.getMaxStackSize
                }
            }
        }
        false
    }

    def getCrusherExtraCount: Int = {
        val coords = new Location(corners._1).getAllWithinBounds(new Location(corners._2), includeInner = false, includeOuter = true)
        var count = 0
        for (i <- 0 until coords.size()) {
            val coord = coords.get(i)
            val block: Block = worldObj.getBlockState(coord.asBlockPos).getBlock
            if (block != null) {
                if (block == BlockManager.crusherExpansion) count += 1
            }
        }
        count * 10
    }

    /**
     * Get the output of the recipe
     * @param stack The input
     * @return The output
     */
    override def recipe(stack: ItemStack): ItemStack = if (recipeCrusher(stack) == null) null else recipeCrusher(stack)._1

    def recipeCrusher(stack: ItemStack): (ItemStack, ItemStack) = {
        CrusherRecipeRegistry.getOutput(stack).orNull
    }

    /**
     * Take the blocks in this structure and generate the speed etc values
     * @param function The blocks count function
     */
    override def generateValues(function: BlockCountFunction): Unit = {
        for (i <- function.getBlockIds) {
            val t = (BlockHelper.getBlockFromString(i)._1, BlockHelper.getBlockFromString(i)._2)

            if (BlockValueRegistry.isBlockRegistered(t._1, t._2)) {
                values.speed += BlockValueRegistry.getSpeedValue(t._1, t._2, function.getBlockCount(t._1, t._2))
                values.efficiency += BlockValueRegistry.getEfficiencyValue(t._1, t._2, function.getBlockCount(t._1, t._2))
                values.multiplicity += BlockValueRegistry.getMultiplicityValue(t._1, t._2, function.getBlockCount(t._1, t._2))
            }
        }

        for (i <- function.getMaterialStrings) {
            if (BlockValueRegistry.isMaterialRegistered(i)) {
                values.speed += BlockValueRegistry.getSpeedValueMaterial(i, function.getMaterialCount(i))
                values.efficiency += BlockValueRegistry.getEfficiencyValueMaterial(i, function.getMaterialCount(i))
                values.multiplicity += BlockValueRegistry.getMultiplicityValueMaterial(i, function.getMaterialCount(i))
            }
        }
    }

    /**
     * Check if this blocks is not allowed in the structure
     * @param block The blocks to check
     * @param meta The meta data of said blocks
     * @return True if it is banned
     */
    override def isBlockBanned(block: Block, meta: Int): Boolean = {
        if (block.isInstanceOf[BlockProxy])
            false
        else
            FurnaceBannedBlocks.isBlockBanned(block, meta)
    }

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
    override def getRedstoneOutput: Int = Container.calcRedstoneFromInventory(this)

    override var inventoryName: String = "inventory.crusher.title"

    override def initialSize : Int = 3
}
