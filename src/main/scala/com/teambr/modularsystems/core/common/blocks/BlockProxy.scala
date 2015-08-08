package com.teambr.modularsystems.core.common.blocks

import com.teambr.bookshelf.Bookshelf
import com.teambr.modularsystems.core.tiles.{AbstractCore, TileProxy}
import net.minecraft.block.material.Material
import net.minecraft.block.properties.IProperty
import net.minecraft.block.state.{BlockState, IBlockState}
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.{EnumFacing, BlockPos, EnumWorldBlockLayer}
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
class BlockProxy extends BaseBlock(Material.rock, "proxy", classOf[TileProxy]) {

    override def getCreativeTab: CreativeTabs = {
        null
    }

    override def breakBlock(world: World, pos: BlockPos, state: IBlockState) {
        world.getTileEntity(pos) match {
            case tile: TileProxy =>
                if (tile.getCore.isDefined) {
                    world.getTileEntity(tile.getCore.get.getPos).asInstanceOf[AbstractCore].deconstructMultiblock()
                }
            case _ =>
        }
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

    override def canRenderInLayer(layer : EnumWorldBlockLayer) : Boolean =
        layer == EnumWorldBlockLayer.CUTOUT || layer == EnumWorldBlockLayer.TRANSLUCENT

    @SideOnly(Side.CLIENT)
    override def getBlockLayer : EnumWorldBlockLayer = EnumWorldBlockLayer.CUTOUT

}

