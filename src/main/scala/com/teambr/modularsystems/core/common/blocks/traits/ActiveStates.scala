package com.teambr.modularsystems.core.common.blocks.traits

import com.teambr.bookshelf.common.blocks.traits.BlockBakeable
import net.minecraft.block.Block
import net.minecraft.block.properties.{IProperty, PropertyBool}
import net.minecraft.block.state.{BlockState, IBlockState}
import net.minecraft.util.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraftforge.common.property.{ExtendedBlockState, IUnlistedProperty}

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
trait ActiveStates extends Block with BlockBakeable {

    lazy val PROPERTY_ACTIVE = PropertyBool.create("isActive")

    def isActive: Boolean
    def isActive_=(bool: Boolean): Boolean

    override def createBlockState() : BlockState = {
        val listed = Array[IProperty](PROPERTY_ACTIVE) //Used to create sub variants
        val unListed = new Array[IUnlistedProperty[_]] (0) //Things we want, but don't need displayed
        new ExtendedBlockState(this, listed, unListed)
    }

    override def getExtendedState(state : IBlockState, world : IBlockAccess, pos : BlockPos) : IBlockState = {
        state.withProperty(PROPERTY_ACTIVE, isActive)
    }

    /**
     * Used to convert the meta to state
     * @param meta The meta
     * @return
     */
    override def getStateFromMeta(meta : Int) : IBlockState = getDefaultState.withProperty(PROPERTY_ACTIVE, java.lang.Boolean.valueOf(meta > 0))

    /**
     * Called to convert state from meta
     * @param state The state
     * @return
     */
    override def getMetaFromState(state : IBlockState) = if(state.getValue(PROPERTY_ACTIVE).asInstanceOf[Boolean]) 1 else 0


    override def getAllPossibleStates: Array[IBlockState] = {
        Array[IBlockState](getDefaultState.withProperty(PROPERTY_ACTIVE, true),
            getDefaultState.withProperty(PROPERTY_ACTIVE, false))
    }

}
