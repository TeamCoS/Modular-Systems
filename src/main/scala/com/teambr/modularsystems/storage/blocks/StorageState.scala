package com.teambr.modularsystems.storage.blocks

import java.util

import com.google.common.collect.ImmutableMap
import net.minecraft.block.Block
import net.minecraft.block.properties.IProperty
import net.minecraft.block.state.IBlockState
import net.minecraft.util.BlockPos
import net.minecraft.world.IBlockAccess

/**
 * This file was created for Modular-Systems
 *
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis <pauljoda>
 * @since August 08, 2015
 */
class StorageState(val connected : Boolean, val pos : BlockPos,  val world : IBlockAccess, val block : Block) extends IBlockState {
    override def getBlock : Block = block
    override def withProperty(property : IProperty, value : Comparable[_]) : IBlockState = null
    override def getValue(property : IProperty) : Comparable[_] = null
    override def cycleProperty(property : IProperty) : IBlockState = null
    override def getPropertyNames : util.Collection[_] = null
    override def getProperties : ImmutableMap[_, _] = null
}
