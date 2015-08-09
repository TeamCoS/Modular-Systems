package com.teambr.modularsystems.core.common.tiles

import com.teambr.bookshelf.common.tiles.traits.RedstoneAware
import com.teambr.bookshelf.util.InventoryUtils
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing

/**
 * This file was created for Modular-Systems
 *
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis <pauljoda>
 * @since August 09, 2015
 */
class TileIOExpansion extends TileProxy with RedstoneAware {
    var input : Boolean = true
    var output : Boolean = true
    var auto : Boolean = true

    /*******************************************************************************************************************
      **************************************************  Tile Methods  ************************************************
      ******************************************************************************************************************/

    override def readFromNBT(tagCompound : NBTTagCompound) {
        super.readFromNBT(tagCompound)
        input = tagCompound.getBoolean("Input")
        output = tagCompound.getBoolean("Output")
        auto = tagCompound.getBoolean("Auto")
    }

    override def writeToNBT(tagCompound : NBTTagCompound) {
        super.writeToNBT(tagCompound)
        tagCompound.setBoolean("Input", input)
        tagCompound.setBoolean("Output", output)
        tagCompound.setBoolean("Auto", auto)
    }

    override def onServerTick() : Unit = {
        getCore match {
            case Some(theCore) =>
                if (worldObj != null && worldObj.rand.nextInt (20) == 10 && auto) {
                    if (!isPowered) {
                        for(dir <- EnumFacing.values()) {
                            worldObj.getTileEntity(pos.offset(dir)) match {
                                case tile : IInventory =>
                                    if (!tile.isInstanceOf[TileProxy] && !tile.isInstanceOf[AbstractCore]) {

                                        if (input) {
                                            for (i <- 0 until tile.getSizeInventory) {
                                                if (InventoryUtils.moveItemInto(tile, i, theCore, -1, 64, dir.getOpposite, doMove = true, canStack = true) > 0) {
                                                    worldObj.markBlockForUpdate(theCore.getPos)
                                                    return
                                                }
                                            }
                                        }

                                        if (output) {
                                            for (i <- 0 until tile.getSizeInventory) {
                                                for(s <- theCore.getSlotsForFace(dir)) {
                                                    if (s !=  0 && InventoryUtils.moveItemInto(theCore, s, tile, i, 64, dir.getOpposite, doMove = true, canStack = true) > 0) {
                                                        worldObj.markBlockForUpdate(theCore.getPos)
                                                        return
                                                    }
                                                }
                                            }
                                        }
                                    }
                                case _ =>
                            }
                        }
                    }
                }
            case _ =>
        }
    }

    override def canExtractItem(index : Int, stack : ItemStack, direction : EnumFacing) : Boolean = {
        getCore match {
            case Some(core) => if(!isPowered && output) core.canExtractItem(index, stack, direction) else false
            case _ => false
        }
    }

    override def canInsertItem(index : Int, itemStackIn : ItemStack, direction : EnumFacing) : Boolean = {
        getCore match {
            case Some(core) => if(!isPowered && input) core.canInsertItem(index, itemStackIn, direction) else false
            case _ => false
        }
    }
}
