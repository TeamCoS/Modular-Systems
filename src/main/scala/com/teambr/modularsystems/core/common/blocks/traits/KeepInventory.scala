package com.teambr.modularsystems.core.common.blocks.traits

import com.teambr.bookshelf.common.blocks.traits.DropsItems
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{ Item, ItemStack }
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockPos
import net.minecraft.world.World

import scala.util.Random

trait KeepInventory extends Block with DropsItems {

    /**
     * Override this is you want to do a specific tag write to the item
     * @return True if you did something to it, otherwise we will use defaults
     */
    def manualOverride(tile : TileEntity, stack : ItemStack, tag : NBTTagCompound) : Boolean = false

    override def onBlockHarvested(world: World, pos: BlockPos, state: IBlockState, player: EntityPlayer): Unit = {
        if (!player.capabilities.isCreativeMode) {
            world.getTileEntity(pos) match {
                case tile: TileEntity =>
                    val item = new ItemStack(Item.getItemFromBlock(state.getBlock), 1)
                    val tag = new NBTTagCompound
                    if (!manualOverride(tile, item, tag)) //If the manual override doesn't do anything, just write the whole tag
                        tile.writeToNBT(tag)
                    item.setTagCompound(tag) //Set the tile's tag to the stack
                    dropItem(world, item, pos) //Drop it
                case _ =>
            }
        } else {
            super[DropsItems].breakBlock(world, pos, state)
        }
    }

    override def onBlockPlacedBy(world: World, pos: BlockPos, state: IBlockState, placer: EntityLivingBase, stack:
    ItemStack): Unit = {
        if(stack.hasTagCompound && !world.isRemote) { //If there is a tag and is on the server
            world.getTileEntity(pos).readFromNBT(stack.getTagCompound) //Set the tag
            world.getTileEntity(pos).setPos(pos) //Set the saved tag to here
            world.markBlockForUpdate(pos) //Mark for update to client
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
