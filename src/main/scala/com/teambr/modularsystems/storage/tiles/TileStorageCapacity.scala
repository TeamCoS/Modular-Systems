package com.teambr.modularsystems.storage.tiles

import java.util.Random

import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound

/**
 * Modular-Systems
 * Created by Dyonovan on 06/08/15
 */
class TileStorageCapacity extends TileEntityStorageExpansion {

    override def addedToNetwork(): Unit = {
        super.addedToNetwork()
        getCore.get.addInventorySlots(11)
        worldObj.markBlockForUpdate(getCore.get.getPos)
    }

    override def removedFromNetwork(): Unit = {
        super.removedFromNetwork()
        getCore match {
            case Some(theCore) =>
            val itemStacks = theCore.removeInventorySlots(11)
            for (i <- itemStacks.indices) {
                val rand: Random = new Random
                if (itemStacks(i) != null && itemStacks(i).stackSize > 0) {
                    val rx: Float = rand.nextFloat * 0.8F + 0.1F
                    val ry: Float = rand.nextFloat * 0.8F + 0.1F
                    val rz: Float = rand.nextFloat * 0.8F + 0.1F
                    val entityItem: EntityItem = new EntityItem(worldObj, pos.getX + rx, pos.getY + ry, pos.getZ + rz,
                        new ItemStack(itemStacks(i).getItem, itemStacks(i).stackSize, itemStacks(i).getItemDamage))
                    if (itemStacks(i).hasTagCompound) entityItem.getEntityItem.setTagCompound(itemStacks(i).
                            getTagCompound.copy.asInstanceOf[NBTTagCompound])
                    val factor: Float = 0.05F
                    entityItem.motionX = rand.nextGaussian * factor
                    entityItem.motionY = rand.nextGaussian * factor + 0.2F
                    entityItem.motionZ = rand.nextGaussian * factor
                    worldObj.spawnEntityInWorld(entityItem)
                    itemStacks(i).stackSize = 0
                }
            }
            worldObj.markBlockForUpdate(getCore.get.getPos)
            case _ =>
        }
    }
}
