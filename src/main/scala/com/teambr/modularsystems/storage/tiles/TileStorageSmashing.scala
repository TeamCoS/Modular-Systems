package com.teambr.modularsystems.storage.tiles

import java.util

import com.teambr.bookshelf.collections.Location
import com.teambr.bookshelf.common.blocks.properties.PropertyRotation
import com.teambr.bookshelf.common.tiles.traits.{ RedstoneAware, Inventory }
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemStack
import net.minecraft.nbt.{ NBTTagCompound, NBTTagList }
import net.minecraft.util.EnumFacing

/**
 * This file was created for Modular-Systems
 *
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis pauljoda
 * @since August 14, 2015
 */
class TileStorageSmashing extends TileEntityStorageExpansion with Inventory with RedstoneAware {
    /**
     * Called after this has been added to a network
     */
    override def addedToNetwork() : Unit = {}

    /**
     * Called right before this is removed from a network
     */
    override def removedFromNetwork() : Unit = {
        val itemStack : ItemStack = getStackInSlot(0)

        if (itemStack != null && itemStack.stackSize > 0) {
            val rx : Float = worldObj.rand.nextFloat * 0.8F + 0.1F
            val ry : Float = worldObj.rand.nextFloat * 0.8F + 0.1F
            val rz : Float = worldObj.rand.nextFloat * 0.8F + 0.1F
            val entityItem : EntityItem = new EntityItem(worldObj, pos.getX + rx, pos.getY + ry, pos.getZ + rz, new ItemStack(itemStack.getItem, itemStack.stackSize, itemStack.getItemDamage))
            if (itemStack.hasTagCompound) entityItem.getEntityItem.setTagCompound(itemStack.getTagCompound.copy.asInstanceOf[NBTTagCompound])
            val factor : Float = 0.05F
            entityItem.motionX = worldObj.rand.nextGaussian * factor
            entityItem.motionY = worldObj.rand.nextGaussian * factor + 0.2F
            entityItem.motionZ = worldObj.rand.nextGaussian * factor
            worldObj.spawnEntityInWorld(entityItem)
            itemStack.stackSize = 0
        }
    }

    override def update() : Unit = {
        super.update()
        if (!worldObj.isRemote && !isPowered && getCore != null && getStackInSlot(0) != null && worldObj.rand.nextInt(20) == 0) {

            val dir : EnumFacing = worldObj.getBlockState(pos).getValue(PropertyRotation.SIX_WAY.getProperty).asInstanceOf[EnumFacing]
            val blockBreakLocation = new Location(pos)
            blockBreakLocation.travel(dir)

            if (!worldObj.isAirBlock(blockBreakLocation.asBlockPos)) {

                val toBreak : Block = worldObj.getBlockState(blockBreakLocation.asBlockPos).getBlock
                if (toBreak.getMaterial == Material.water || toBreak.getMaterial == Material.lava) return
                val harvestLevel : Int = getStackInSlot(0).getItem.getHarvestLevel(getStackInSlot(0), "pickaxe")

                //Can we harvest this?
                if (toBreak.getHarvestLevel(worldObj.getBlockState(blockBreakLocation.asBlockPos)) <= harvestLevel) {
                    var fortune : Int = 0
                    val silkTouch : Boolean = toBreak.canSilkHarvest(worldObj, blockBreakLocation.asBlockPos, worldObj.getBlockState(blockBreakLocation.asBlockPos), null)
                    var hasSilkTouch : Boolean = false
                    val enchantList : NBTTagList = getStackInSlot(0).getEnchantmentTagList

                    //Check for enchants
                    if (enchantList != null) {
                        for(i <- 0 until enchantList.tagCount()) {
                            val tag = enchantList.getCompoundTagAt(i)
                            if (tag.hasKey("id") && tag.getInteger("id") == 35) fortune = tag.getInteger("lvl")
                            else if (tag.hasKey("id") && tag.getInteger("id") == 33) hasSilkTouch = true
                        }
                    }

                    //Build Drop List
                    val itemStacks : util.ArrayList[ItemStack] = new util.ArrayList[ItemStack]
                    if (silkTouch && hasSilkTouch) {
                        itemStacks.add(new ItemStack(toBreak, 1, worldObj.getBlockState(blockBreakLocation.asBlockPos).getBlock.getMetaFromState(worldObj.getBlockState(blockBreakLocation.asBlockPos))))
                    }
                    else {
                        itemStacks.addAll(toBreak.getDrops(worldObj, blockBreakLocation.asBlockPos, worldObj.getBlockState(blockBreakLocation.asBlockPos), fortune))
                    }

                    //Spawn Drop List
                    for (i <- 0 until itemStacks.size()) {
                        val itemStack = itemStacks.get(i)
                        if (itemStack != null && itemStack.stackSize > 0) {
                            val rx : Float = worldObj.rand.nextFloat * 0.8F + 0.1F
                            val ry : Float = worldObj.rand.nextFloat * 0.8F + 0.1F
                            val rz : Float = worldObj.rand.nextFloat * 0.8F + 0.1F
                            val entityItem : EntityItem = new EntityItem(worldObj, blockBreakLocation.x + rx, blockBreakLocation.y + ry, blockBreakLocation.z + rz, new ItemStack(itemStack.getItem, itemStack.stackSize, itemStack.getItemDamage))
                            if (itemStack.hasTagCompound) entityItem.getEntityItem.setTagCompound(itemStack.getTagCompound.copy.asInstanceOf[NBTTagCompound])
                            val factor : Float = 0.05F
                            entityItem.motionX = worldObj.rand.nextGaussian * factor
                            entityItem.motionY = worldObj.rand.nextGaussian * factor + 0.2F
                            entityItem.motionZ = worldObj.rand.nextGaussian * factor
                            worldObj.spawnEntityInWorld(entityItem)
                            itemStack.stackSize = 0
                        }
                    }

                    //Destroy Block
                    worldObj.destroyBlock(blockBreakLocation.asBlockPos, false)
                    getStackInSlot(0).setItemDamage(getStackInSlot(0).getItemDamage + 1)
                    if (getStackInSlot(0).getItemDamage > getStackInSlot(0).getMaxDamage) setInventorySlotContents(0, null)
                }
            }
        }
    }

    override def markDirty() : Unit = {
        super[TileEntityStorageExpansion].markDirty()
        super[Inventory].markDirty()
    }

    override def readFromNBT(tag : NBTTagCompound) : Unit = {
        super[TileEntityStorageExpansion].readFromNBT(tag)
        super[Inventory].readFromNBT(tag)
    }

    override def writeToNBT(tag : NBTTagCompound) : Unit = {
        super[TileEntityStorageExpansion].writeToNBT(tag)
        super[Inventory].writeToNBT(tag)
    }

    /*******************************************************************************************************************
      ***************************************** Inventory Methods ******************************************************
      *******************************************************************************************************************/

    override var inventoryName : String = "inventory.storageSmashing.title"

    override def hasCustomName() : Boolean = true

    override def initialSize : Int = 1
}
