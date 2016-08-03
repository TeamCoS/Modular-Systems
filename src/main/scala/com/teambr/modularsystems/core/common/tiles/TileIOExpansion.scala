package com.teambr.modularsystems.core.common.tiles

import com.teambr.bookshelf.common.tiles.traits.RedstoneAware
import com.teambr.bookshelf.util.InventoryUtils
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
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

    override def writeToNBT(tagCompound : NBTTagCompound) : NBTTagCompound = {
        super.writeToNBT(tagCompound)
        tagCompound.setBoolean("Input", input)
        tagCompound.setBoolean("Output", output)
        tagCompound.setBoolean("Auto", auto)
        tagCompound
    }

    override def onServerTick() : Unit = {
        getCore match {
            case Some(theCore) if auto =>
                if (worldObj != null && worldObj.rand.nextInt(20) == 10) {
                    if (!isPowered) {
                        for(dir <- EnumFacing.values()) {
                            worldObj.getTileEntity(pos.offset(dir)) match {
                                case tile : TileEntity =>
                                    if (!tile.isInstanceOf[TileProxy] && !tile.isInstanceOf[AbstractCore]) {
                                        if(output) {
                                            for(i <- 1 until theCore.getSlots()) {
                                                InventoryUtils.moveItemInto(theCore, i, tile, -1, 64, dir.getOpposite,
                                                    doMove = true, checkSidedSource = false)
                                            }
                                        }
                                        if(input)
                                            InventoryUtils.moveItemInto(tile, -1, theCore, 0, 64, dir,
                                                doMove = true, checkSidedTarget = false)
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
