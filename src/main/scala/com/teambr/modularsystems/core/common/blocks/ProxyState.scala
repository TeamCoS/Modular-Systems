package com.teambr.modularsystems.core.common.blocks

import java.util

import com.google.common.collect.ImmutableMap
import com.teambr.modularsystems.core.tiles.TileProxy
import net.minecraft.block.Block
import net.minecraft.block.properties.IProperty
import net.minecraft.block.state.IBlockState

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
class ProxyState(val tile : TileProxy, val block : Block) extends IBlockState {
    override def getBlock : Block = block
    override def withProperty(property : IProperty, value : Comparable[_]) : IBlockState = null
    override def getValue(property : IProperty) : Comparable[_] = null
    override def cycleProperty(property : IProperty) : IBlockState = null
    override def getPropertyNames : util.Collection[_] = null
    override def getProperties : ImmutableMap[_, _] = null
}
