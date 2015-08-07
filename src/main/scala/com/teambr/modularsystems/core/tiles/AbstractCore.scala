package com.teambr.modularsystems.core.tiles

import java.util

import com.teambr.bookshelf.collections.Location
import com.teambr.bookshelf.common.tiles.traits.{ Inventory, UpdatingTile }
import com.teambr.bookshelf.util.WorldUtils
import com.teambr.modularsystems.core.collections.StandardValues
import com.teambr.modularsystems.core.functions.BlockCountFunction
import com.teambr.modularsystems.core.managers.BlockManager
import net.minecraft.block.Block
import net.minecraft.block.properties.PropertyDirection
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.{ EnumFacing, BlockPos }
import net.minecraft.world.World

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
abstract class AbstractCore extends UpdatingTile with Inventory {
    final val cookSpeed = 200
    final val MAX_SIZE = 100

    val values = new StandardValues
    var corners : (BlockPos, BlockPos) = null
    var isDirty = true
    var wellFormed = false

    /**
     * Used to set the blocks to its active and non-active state
     * @param positiveBurnTime True if active
     * @param world World object
     * @param pos The block position
     */
    def updateBlockState(positiveBurnTime : Boolean, world : World, pos : BlockPos)

    /**
     * Get the output of the recipe
     * @param stack The input
     * @return The output
     */
    def recipe(stack : ItemStack) : ItemStack

    /**
     * Check if this blocks is not allowed in the structure
     * @param block The blocks to check
     * @param meta The meta data of said blocks
     * @return True if it is banned
     */
    def isBlockBanned(block : Block, meta : Int) : Boolean

    /**
     * Take the blocks in this structure and generate the speed etc values
     * @param function The blocks count function
     */
    def generateValues(function : BlockCountFunction)

    /**
     * Gets around that last little bit where the values line up weird and it doesn't convert back
     * @return The block to set when on
     */
    def getOnBlock : Block

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
                    case _ =>
                        val id = Block.getIdFromBlock(worldObj.getBlockState(loc).getBlock)
                        val meta = worldObj.getBlockState(loc).getBlock.getMetaFromState(worldObj.getBlockState(loc))
                        blockCountFunction.addBlock(worldObj.getBlockState(loc).getBlock, meta)

                        worldObj.setBlockState(loc, BlockManager.proxy.getDefaultState)
                        val proxy = worldObj.getTileEntity(loc).asInstanceOf[TileProxy]
                        proxy.setCore(this)
                        proxy.storedBlock = id
                        proxy.metaData = meta
                        worldObj.markBlockForUpdate(loc)
                        worldObj.markBlockRangeForRenderUpdate(loc, loc)
                }
                generateValues(blockCountFunction)
                wellFormed = true
                worldObj.markBlockForUpdate(pos)
            }
        }
    }

    def deconstructMultiblock() : Unit = {
        //Just to be safe...
        if(corners == null)
            return

        values.resetStructureValues()
        val outside = new Location(corners._1).getAllWithinBounds(new Location(corners._2), includeInner = false, includeOuter = true)
        for(i <- 0 until outside.size()) {
            val loc = outside.get(i).asBlockPos

            if (!loc.equals(pos)) { //Not us, so we can do something
                worldObj.getTileEntity(loc) match {
                    case proxy : TileProxy =>
                        val meta = proxy.metaData
                        worldObj.setBlockState(loc, proxy.getStoredBlock.getStateFromMeta(meta))
                        worldObj.markBlockForUpdate(loc)
                    case _ =>
                }
            }
        }
        wellFormed = false
        worldObj.markBlockForUpdate(pos)
    }

    def getCorners : Option[(BlockPos, BlockPos)] = {
        val local = getPos
        val firstCorner = new BlockPos(local)
        val secondCorner = new BlockPos(local)

        val dir : EnumFacing = worldObj.getBlockState(this.pos).getValue(PropertyDirection.create("facing", util.Arrays.asList(EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST))).asInstanceOf[EnumFacing]

        //Move Inside
        firstCorner.offset(dir.getOpposite)
        secondCorner.offset(dir.getOpposite)

        //Get our directions
        val right = WorldUtils.rotateRight(dir)
        val left = WorldUtils.rotateLeft(dir)

        //Get first corner
        //Find side
        while(worldObj.isAirBlock(firstCorner)) {
            firstCorner.offset(right)
            if(pos.distanceSq(firstCorner) > MAX_SIZE)
                return None
        }

        //Pop back inside
        firstCorner.offset(right.getOpposite)

        //Find floor
        while(worldObj.isAirBlock(firstCorner)) {
            firstCorner.offset(EnumFacing.DOWN)
            if(pos.distanceSq(firstCorner) > MAX_SIZE)
                return None
        }

        //Found, so move to physical location
        firstCorner.offset(right)
        firstCorner.offset(dir)

        //Find side
        while(worldObj.isAirBlock(secondCorner)) {
            secondCorner.offset(left)
            if(pos.distanceSq(secondCorner) > MAX_SIZE)
                return None
        }

        //Pop back inside
        secondCorner.offset(left.getOpposite)

        //Move to back
        while(worldObj.isAirBlock(secondCorner)) {
            secondCorner.offset(dir.getOpposite)
            if(pos.distanceSq(secondCorner) > MAX_SIZE)
                return None
        }

        //Found, so move back to physical location
        secondCorner.offset(left)
        secondCorner.offset(dir.getOpposite)

        Some(firstCorner, secondCorner)
    }

    override def markDirty() : Unit = {}

    override def writeToNBT(tag : NBTTagCompound) : Unit = {}
    override def readFromNBT(tag : NBTTagCompound) : Unit = {}

    /*******************************************************************************************************************
      ************************************************* Inventory methods ***********************************************
      *******************************************************************************************************************/
    override def initialSize : Int = 2

    /**
     * Does this inventory has a custom name
     * @return True if there is a name (localized)
     */
    override def hasCustomName(): Boolean = false

}
