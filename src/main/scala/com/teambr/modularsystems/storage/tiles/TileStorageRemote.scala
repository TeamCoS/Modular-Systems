package com.teambr.modularsystems.storage.tiles

import com.teambr.bookshelf.common.tiles.traits.{UpdatingTile, Inventory}
import com.teambr.modularsystems.storage.items.ItemStorageRemote
import net.minecraft.item.ItemStack
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
class TileStorageRemote extends TileEntityStorageExpansion with Inventory with UpdatingTile {

    val maxDistance = 15

    override def onServerTick(): Unit = {
        if (getCore.isDefined && getStackInSlot(0) != null && getStackInSlot(1) == null) {
            val tag = getStackInSlot(0).getTagCompound
            tag.setLong("Core", getPos.toLong)
            getStackInSlot(0).setTagCompound(tag)

            setInventorySlotContents(1, getStackInSlot(0).copy)
            getStackInSlot(1).setItemDamage(getStackInSlot(0).getItemDamage)
            setInventorySlotContents(0, null)
            worldObj.markBlockForUpdate(pos)
        }
    }

    /**
     * Called after this has been added to a network
     */
    override def addedToNetwork(): Unit = { }

    /**
     * Called right before this is removed from a network
     */
    override def removedFromNetwork(): Unit = { }

    override var inventoryName: String = _

    override def hasCustomName(): Boolean = false

    override def initialSize: Int = 2

    override def isItemValidForSlot(slot: Int, stack: ItemStack): Boolean = {
        slot == 0 && stack.getItem.isInstanceOf[ItemStorageRemote]
    }

    override def readFromNBT(tag: NBTTagCompound): Unit = {
        super[Inventory].readFromNBT(tag)
        super[TileEntityStorageExpansion].readFromNBT(tag)
    }

    override def writeToNBT(tag: NBTTagCompound): Unit = {
        super[Inventory].writeToNBT(tag)
        super[TileEntityStorageExpansion].writeToNBT(tag)
    }

    override def markDirty(): Unit = {
        super[Inventory].markDirty()
        super[TileEntityStorageExpansion].markDirty()
    }
}
