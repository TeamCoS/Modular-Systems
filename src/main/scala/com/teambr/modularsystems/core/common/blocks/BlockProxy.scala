package com.teambr.modularsystems.core.common.blocks

import com.teambr.modularsystems.core.tiles.TileProxy
import net.minecraft.block.material.Material
import net.minecraft.block.properties.IProperty
import net.minecraft.block.state.{BlockState, IBlockState}
import net.minecraft.util.{BlockPos, EnumWorldBlockLayer}
import net.minecraft.world.IBlockAccess
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

