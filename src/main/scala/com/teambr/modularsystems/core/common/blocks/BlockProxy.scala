package com.teambr.modularsystems.core.common.blocks

import java.util.Random

import com.teambr.bookshelf.Bookshelf
import com.teambr.modularsystems.core.common.tiles.{AbstractCore, TileProxy}
import com.teambr.modularsystems.power.tiles.TileBankBase
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.properties.IProperty
import net.minecraft.block.state.{BlockState, IBlockState}
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.{BlockPos, EnumFacing, EnumWorldBlockLayer}
import net.minecraft.world.{IBlockAccess, World}
import net.minecraftforge.common.property.{ExtendedBlockState, IUnlistedProperty}
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

/**
 * This file was created for Modular-Systems
 *
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis <pauljoda>
 * @since August 06, 2015
 */
class BlockProxy(name: String, tileEntity: Class[_ <: TileEntity]) extends BaseBlock(Material.rock, name, tileEntity) {

    override def getCreativeTab: CreativeTabs = {
        null
    }

    override def getItemDropped(state: IBlockState, rand: Random, fortune: Int): Item = {
        null
    }

    override def breakBlock(world: World, pos: BlockPos, state: IBlockState) {
        world.getTileEntity(pos) match {
            case tile: TileProxy =>
                if (tile.getCore.isDefined) {
                    world.getTileEntity(tile.getCore.get.getPos).asInstanceOf[AbstractCore].deconstructMultiblock()
                }
                val block: Block = tile.getStoredBlock
                val meta: Int = tile.metaData

                if (world.isAirBlock(pos) && !tile.isInstanceOf[TileBankBase]) {
                    val f: Float = world.rand.nextFloat * 0.8F + 0.1F
                    val f1: Float = world.rand.nextFloat * 0.8F + 0.1F
                    val f2: Float = world.rand.nextFloat * 0.8F + 0.1F
                    val entityitem: EntityItem = new EntityItem(world, pos.getX.toDouble + f, pos.getY.toDouble + f1, pos.getZ.toDouble + f2, new ItemStack(block, 1, meta))
                    world.spawnEntityInWorld(entityitem)
                    world.notifyNeighborsOfStateChange(pos, this)
                }
            case _ =>
        }
        super.breakBlock(world, pos, state)
    }

    override def onBlockActivated(world : World, pos : BlockPos, state : IBlockState, player : EntityPlayer, side : EnumFacing, hitX : Float, hitY : Float, hitZ : Float) : Boolean = {
        world.getTileEntity(pos) match {
            case tile: TileProxy =>
                if (tile.getCore.isDefined) {
                    player.openGui(Bookshelf, 0, world, tile.getCore.get.getPos.getX, tile.getCore.get.getPos.getY, tile.getCore.get.getPos.getZ)
                }
        }
        true
    }

    override def createBlockState() : BlockState = {
        val listed = new Array[IProperty](0)
        val unlisted = new Array[IUnlistedProperty[_]](0)
        new ExtendedBlockState(this, listed, unlisted)
    }

    override def getExtendedState(state : IBlockState, world : IBlockAccess, pos : BlockPos) : IBlockState = {
        new ProxyState(world.getTileEntity(pos).asInstanceOf[TileProxy], state.getBlock)
    }

    override def getRenderType : Int = 3

    override def isOpaqueCube : Boolean =
        false

    @SideOnly(Side.CLIENT)
    override def isTranslucent : Boolean =
        true

    @SideOnly(Side.CLIENT)
    override def getBlockLayer : EnumWorldBlockLayer = EnumWorldBlockLayer.CUTOUT

}

