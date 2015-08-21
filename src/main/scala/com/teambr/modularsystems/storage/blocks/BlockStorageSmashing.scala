package com.teambr.modularsystems.storage.blocks

import com.teambr.bookshelf.common.blocks.properties.PropertyRotation
import net.minecraft.block.BlockPistonBase
import net.minecraft.block.properties.IProperty
import net.minecraft.block.state.{ BlockState, IBlockState }
import net.minecraft.entity.EntityLivingBase
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.{ BlockPos, EnumFacing }
import net.minecraft.world.World
import net.minecraftforge.common.property.{ ExtendedBlockState, IUnlistedProperty }

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
class BlockStorageSmashing(name: String, tileEntity: Class[_ <: TileEntity]) extends BlockStorageExpansion(name, tileEntity) {

    /**
     * Called when the block is placed, we check which way the player is facing and put our value as the opposite of that
     */
    override def onBlockPlaced(world: World, blockPos: BlockPos, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float, meta: Int, placer: EntityLivingBase): IBlockState = {
        this.getDefaultState.withProperty(PropertyRotation.SIX_WAY, BlockPistonBase.getFacingFromEntity(world, blockPos, placer))
    }

    /**
     * Used to say what our block state is
     */
    override def createBlockState() : BlockState = {
        val listed = new Array[IProperty](1)
        listed(0) = PropertyRotation.SIX_WAY
        val unlisted = new Array[IUnlistedProperty[_]](0)
        new ExtendedBlockState(this, listed, unlisted)
    }

    /**
     * Used to convert the meta to state
     * @param meta The meta
     * @return
     */
    override def getStateFromMeta(meta : Int) : IBlockState = getDefaultState.withProperty(PropertyRotation.SIX_WAY, EnumFacing.getFront(meta))

    /**
     * Called to convert state from meta
     * @param state The state
     * @return
     */
    override def getMetaFromState(state : IBlockState) = state.getValue(PropertyRotation.SIX_WAY).asInstanceOf[EnumFacing].getIndex
}
