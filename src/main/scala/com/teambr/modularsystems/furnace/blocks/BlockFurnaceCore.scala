package com.teambr.modularsystems.furnace.blocks

import com.teambr.bookshelf.Bookshelf
import com.teambr.bookshelf.common.blocks.traits.DropsItems
import com.teambr.bookshelf.common.tiles.traits.OpensGui
import com.teambr.modularsystems.core.ModularSystems
import com.teambr.modularsystems.core.common.blocks.BaseBlock
import com.teambr.modularsystems.core.common.blocks.traits.CoreStates
import com.teambr.modularsystems.furnace.container.ContainerFurnaceCore
import com.teambr.modularsystems.furnace.gui.GuiFurnaceCore
import com.teambr.modularsystems.furnace.tiles.TileEntityFurnaceCore
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.{ BlockPos, EnumFacing }
import net.minecraft.world.World

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
class BlockFurnaceCore(name: String, isActive: Boolean) extends BaseBlock(Material.rock, name, classOf[TileEntityFurnaceCore])
with OpensGui with CoreStates with DropsItems {

    //Block Methods
    override def getCreativeTab: CreativeTabs = {
        if (isActive)
            null
        else
            ModularSystems.tabModularSystems
    }

    override def breakBlock(world: World, pos: BlockPos, state: IBlockState) {
        world.getTileEntity(pos) match {
            case core: TileEntityFurnaceCore => core.deconstructMultiblock()
            case _ =>
        }
        super[DropsItems].breakBlock(world, pos, state)
    }

    override def onBlockActivated(world : World, pos : BlockPos, state : IBlockState, player : EntityPlayer, side : EnumFacing, hitX : Float, hitY : Float, hitZ : Float) : Boolean = {
        world.getTileEntity(pos) match {
            case core: TileEntityFurnaceCore =>
                if (core.wellFormed)
                    player.openGui(Bookshelf, 0, world, pos.getX, pos.getY, pos.getZ)
                else
                    core.setDirty()
            case _ =>
        }
        true
    }

    //OpensGui Methods
    override def getServerGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): AnyRef = {
        new ContainerFurnaceCore(player.inventory, world.getTileEntity(new BlockPos(x, y, z)).asInstanceOf[TileEntityFurnaceCore])
    }

    override def getClientGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): AnyRef = {
        new GuiFurnaceCore(player, world.getTileEntity(new BlockPos(x, y, z)).asInstanceOf[TileEntityFurnaceCore])
    }
}
