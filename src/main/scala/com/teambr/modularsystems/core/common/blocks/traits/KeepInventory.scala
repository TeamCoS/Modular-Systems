package com.teambr.modularsystems.core.common.blocks.traits

import java.util

import com.teambr.bookshelf.common.tiles.traits.Inventory
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.nbt.{NBTTagCompound, NBTTagList}
import net.minecraft.util.{BlockPos, EnumFacing}
import net.minecraft.world.World

import scala.util.Random

trait KeepInventory extends Block {

    override def breakBlock(world: World, pos: BlockPos, state: IBlockState): Unit = {
        world.getTileEntity(pos) match {
            case tile: Inventory =>
                val item = new ItemStack(Item.getItemFromBlock(state.getBlock), 1)
                val tag = new NBTTagCompound

                tag.setInteger("Size:" + tile.inventoryName, tile.getSizeInventory())
                val nbttaglist = new NBTTagList
                for (i <- 0 until tile.inventoryContents.size()) {
                    if (tile.inventoryContents.get(i) != null) {
                        val stackTag = new NBTTagCompound
                        stackTag.setByte("Slot:" + tile.inventoryName, i.asInstanceOf[Byte])
                        tile.inventoryContents.get(i).writeToNBT(stackTag)
                        nbttaglist.appendTag(stackTag)
                    }
                }
                tag.setTag("Items:" + tile.inventoryName, nbttaglist)
                item.setTagCompound(tag)

                dropItem(world, item, pos)
            case _ =>
        }
    }

    override def onBlockPlacedBy(world: World, pos: BlockPos, state: IBlockState, placer: EntityLivingBase, stack:
    ItemStack): Unit = {
        world.getTileEntity(pos) match {
            case tile: Inventory =>
                if (stack.hasTagCompound) {
                    val nbttaglist = stack.getTagCompound.getTagList("Items:" + tile.inventoryName, 10)
                    if (nbttaglist != null) {
                        if (stack.getTagCompound.hasKey("Size:" + tile.inventoryName)) tile.inventoryContents.
                                setSize(stack.getTagCompound.getInteger("Size:" + tile.inventoryName))
                        for (i <- 0 until nbttaglist.tagCount()) {
                            val stacktag = nbttaglist.getCompoundTagAt(i)
                            val j = stacktag.getByte("Slot:" + tile.inventoryName)
                            if (j >= 0 && j < tile.inventoryContents.size())
                                tile.inventoryContents.set(j, ItemStack.loadItemStackFromNBT(stacktag))
                        }
                    }
                }
            case _ =>
        }
    }

    override def getItemDropped(state: IBlockState, rand: java.util.Random, fortune: Int): Item = {
        null
    }

    private def dropItem(world: World, stack: ItemStack, pos: BlockPos): Unit = {
        val random = new Random
        if (stack != null && stack.stackSize > 0) {
            val rx = random.nextFloat * 0.8F + 0.1F
            val ry = random.nextFloat * 0.8F + 0.1F
            val rz = random.nextFloat * 0.8F + 0.1F

            val itemEntity = new EntityItem(world,
                pos.getX + rx, pos.getY + ry, pos.getZ + rz,
                new ItemStack(stack.getItem, stack.stackSize, stack.getItemDamage))

            if (stack.hasTagCompound)
                itemEntity.getEntityItem.setTagCompound(stack.getTagCompound)

            val factor = 0.05F

            itemEntity.motionX = random.nextGaussian * factor
            itemEntity.motionY = random.nextGaussian * factor + 0.2F
            itemEntity.motionZ = random.nextGaussian * factor
            world.spawnEntityInWorld(itemEntity)

            stack.stackSize = 0
        }
    }
}
