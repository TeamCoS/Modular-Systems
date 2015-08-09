package com.teambr.modularsystems.crusher.blocks

import java.util.Random

import com.teambr.modularsystems.core.ModularSystems
import com.teambr.modularsystems.core.common.blocks.BlockProxy
import com.teambr.modularsystems.core.common.tiles.TileProxy
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockPos
import net.minecraft.world.World

/**
 * This file was created for Modular-Systems
 *
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Dyonovan
 * @since August 09, 2015
 */
class BlockCrusherExpansion(name: String, tileEntity: Class[_ <: TileEntity]) extends BlockProxy(name, tileEntity) {

    override def getCreativeTab: CreativeTabs = {
        ModularSystems.tabModularSystems
    }

    override def breakBlock(world: World, pos: BlockPos, state: IBlockState): Unit = {
        val core = world.getTileEntity(pos).asInstanceOf[TileProxy].getCore
        if (core.isDefined)
            core.get.deconstructMultiblock()
    }

    override def getItemDropped(state: IBlockState, rand: Random, fortune: Int): Item = {
        Item.getItemFromBlock(this)
    }
}
