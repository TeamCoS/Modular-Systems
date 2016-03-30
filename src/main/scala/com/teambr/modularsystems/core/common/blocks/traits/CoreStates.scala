package com.teambr.modularsystems.core.common.blocks.traits

import com.teambr.bookshelf.common.blocks.properties.Properties
import com.teambr.modularsystems.core.common.tiles.AbstractCore
import net.minecraft.block.Block
import net.minecraft.block.properties.{IProperty, PropertyBool}
import net.minecraft.block.state.{BlockStateContainer, IBlockState}
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.math.{BlockPos, MathHelper}
import net.minecraft.util.{BlockRenderLayer, EnumBlockRenderType, EnumFacing}
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
 * @author Dyonovan
 * @since August 07, 2015
 */
object CoreStates {
    lazy val PROPERTY_ACTIVE = PropertyBool.create("is_active")
}
trait CoreStates extends Block {

    /**
     * Called when the block is placed, we check which way the player is facing and put our value as the opposite of that
     */
    override def onBlockPlaced(world : World, blockPos : BlockPos, facing : EnumFacing, hitX : Float, hitY : Float, hitZ : Float, meta : Int, placer : EntityLivingBase) : IBlockState = {
        val playerFacingDirection = if (placer == null) 0 else MathHelper.floor_double((placer.rotationYaw / 90.0F) + 0.5D) & 3
        val enumFacing = EnumFacing.getHorizontal(playerFacingDirection).getOpposite
        this.getDefaultState.withProperty(Properties.FOUR_WAY, enumFacing)
    }

    /**
     * Used to say what our block state is
     */
    override def createBlockState() : BlockStateContainer = {
        val listed : Array[IProperty[_]] = Array(Properties.FOUR_WAY, CoreStates.PROPERTY_ACTIVE)
        val unlisted = new Array[IUnlistedProperty[_]](0)
        new ExtendedBlockState(this, listed, unlisted)
    }

    override def getExtendedState(state : IBlockState, world : IBlockAccess, pos : BlockPos) : IBlockState = {
        world.getTileEntity(pos) match {
            case core : AbstractCore =>
                state.withProperty (Properties.FOUR_WAY, world.getBlockState (pos).getValue (Properties.FOUR_WAY))
                        .withProperty(CoreStates.PROPERTY_ACTIVE, core.isBurning.asInstanceOf[java.lang.Boolean])
            case _ =>
                state.withProperty (Properties.FOUR_WAY, world.getBlockState (pos).getValue (Properties.FOUR_WAY))
                    .withProperty(CoreStates.PROPERTY_ACTIVE, true.asInstanceOf[java.lang.Boolean])
        }
    }

    /**
     * Used to convert the meta to state
      *
      * @param meta The meta
     * @return
     */
    override def getStateFromMeta(meta : Int) : IBlockState = {
        var facing = meta & 5
        if(facing == EnumFacing.DOWN.ordinal() || facing == EnumFacing.UP.ordinal())
            facing = EnumFacing.SOUTH.ordinal()
        getDefaultState.withProperty(Properties.FOUR_WAY, facing)
                .withProperty(CoreStates.PROPERTY_ACTIVE, if((Integer.valueOf(meta & 15) >> 2) == 1)
                    true.asInstanceOf[java.lang.Boolean] else false.asInstanceOf[java.lang.Boolean])
    }

    /**
     * Called to convert state from meta
      *
      * @param state The state
     * @return
     */
    override def getMetaFromState(state : IBlockState) = {
        val b0 : Byte = 0
        var i : Int = b0 | state.getValue(Properties.FOUR_WAY).getIndex
        i |= (if(state.getValue(CoreStates.PROPERTY_ACTIVE).asInstanceOf[Boolean]) 1 else 0 ) << 2
        i
    }

    override def getRenderType(state : IBlockState) : EnumBlockRenderType = EnumBlockRenderType.MODEL

    override def isOpaqueCube(state : IBlockState) : Boolean = false

    @SideOnly(Side.CLIENT)
    override def isTranslucent(state : IBlockState) : Boolean = true

    @SideOnly(Side.CLIENT)
    override def getBlockLayer : BlockRenderLayer = BlockRenderLayer.CUTOUT
}
