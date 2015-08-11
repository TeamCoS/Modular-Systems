package com.teambr.modularsystems.storage.tiles

import com.teambr.bookshelf.common.tiles.traits.RedstoneAware
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.{ EnumParticleTypes, AxisAlignedBB }

/**
 * This file was created for Modular-Systems
 *
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis <pauljoda>
 * @since August 10, 2015
 */
class TileStorageHopping extends TileEntityStorageExpansion with RedstoneAware {
    //Used to store the range we wish to pull from
    var range = 3

    /**
     * Called after this has been added to a network
     */
    override def addedToNetwork() : Unit = {}

    /**
     * Called right before this is removed from a network
     */
    override def removedFromNetwork() : Unit = {}

    override def update() : Unit = {
        super.update()
        if(!isPowered)
            hooverItems(range, 0.2D)
    }

    def hooverItems(distance : Double, speed : Double) : Unit = {
        getCore match {
            case Some(theCore) =>
                val entityBox = AxisAlignedBB.fromBounds(pos.getX - distance, pos.getY - distance, pos.getZ - distance,
                    pos.getX + distance + 1, pos.getY + distance + 1, pos.getZ + distance + 1)

                val interestingItems = worldObj.getEntitiesWithinAABB(classOf[EntityItem], entityBox)

                for(s <- 0 until interestingItems.size()) {
                    val entity : EntityItem = interestingItems.get(s).asInstanceOf[EntityItem]
                    val x = pos.getX + 0.05D - entity.posX
                    val y = pos.getY + 0.05D - entity.posY
                    val z = pos.getZ + 0.05D - entity.posZ

                    val howFar = Math.sqrt(x * x + y * y + z * z)

                    if(howFar <= 1.1)
                        onEntityCollidedWithBlock(entity)
                    else {
                        var distancePull = 1.0 - howFar / 15.0
                        worldObj.spawnParticle(EnumParticleTypes.PORTAL, entity.posX, entity.posY - 0.8D, entity.posZ, 0, 0, 0)

                        if(distancePull > 0.0D) {
                            distancePull *= distancePull
                            entity.motionX += x / distance * distancePull * speed
                            entity.motionY += y / distance * distancePull * speed * 4
                            entity.motionZ += z / distance * distancePull * speed
                        }
                    }
                }
            case   _ =>
        }
    }

    def onEntityCollidedWithBlock(entity : Entity): Unit = {
        if(!worldObj.isRemote) {
            entity match {
                case itemEntity : EntityItem if !entity.isDead =>
                    val stack = itemEntity.getEntityItem.copy()
                    tryInsertStack(stack)
                    if (stack.stackSize <= 0)
                        itemEntity.setDead()
                    else
                        itemEntity.setEntityItemStack(stack)
                case _ =>
            }
        }
    }

    def tryInsertStack(stack : ItemStack) : Unit = {
        getCore match {
            case Some(theCore) =>
                for(i <- 0 until theCore.getSizeInventory()) {
                    if(theCore.isItemValidForSlot(i, stack.copy())) {
                        val targetStack = theCore.getStackInSlot(i)
                        if(targetStack == null) {
                            theCore.setInventorySlotContents(i, stack.copy())
                            stack.stackSize = 0
                            markDirty()
                            return
                        }
                        else if(areMergeCandidates(stack, targetStack)) {
                            val space = targetStack.getMaxStackSize
                            - targetStack.stackSize
                            val mergeAmount = Math.min(space, stack.stackSize)
                            val copy = targetStack.copy()
                            copy.stackSize += mergeAmount
                            theCore.setInventorySlotContents(i, copy)
                            stack.stackSize -= mergeAmount
                            markDirty()
                            return
                        }
                    }
                }
            case _ =>
        }
    }

    def areItemAndTagEqual(stackA : ItemStack, stackB : ItemStack) : Boolean = {
        stackA.isItemEqual(stackB) && ItemStack.areItemStackTagsEqual(stackA, stackB)
    }

    def areMergeCandidates(source : ItemStack, target : ItemStack) : Boolean = {
        areItemAndTagEqual(source, target) && target.stackSize < target.getMaxStackSize
    }

    override def readFromNBT(tag : NBTTagCompound) {
        super.readFromNBT(tag)
        range = tag.getInteger("Range")
    }

    override def writeToNBT(tag : NBTTagCompound) {
        super.writeToNBT(tag)
        tag.setInteger("Range", range)
    }
}
