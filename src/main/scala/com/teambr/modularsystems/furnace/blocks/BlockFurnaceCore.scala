package com.teambr.modularsystems.furnace.blocks

import com.teambr.bookshelf.collections.CubeTextures
import com.teambr.bookshelf.common.blocks.traits.{BlockBakeable, DropsItems, FourWayRotation}
import com.teambr.bookshelf.common.tiles.traits.OpensGui
import com.teambr.modularsystems.core.ModularSystems
import com.teambr.modularsystems.core.common.blocks.BaseBlock
import com.teambr.modularsystems.core.lib.Reference
import com.teambr.modularsystems.furnace.container.ContainerFurnaceCore
import com.teambr.modularsystems.furnace.gui.GuiFurnaceCore
import com.teambr.modularsystems.furnace.tiles.TileEntityFurnaceCore
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.{BlockPos, ResourceLocation}
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
        with OpensGui with FourWayRotation with BlockBakeable with DropsItems {

    override def blockName: String = name
    override def MODID: String = Reference.MOD_ID

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

    //BlockBakeable Methods
    override def getDisplayTextures(state: IBlockState): CubeTextures = {
        val map = Minecraft.getMinecraft.getTextureMapBlocks
        var textures: CubeTextures = null
        if (isActive) {
            textures = new CubeTextures(
                map.getTextureExtry("minecraft:blocks/furnace_front_on"),
                map.getTextureExtry("minecraft:blocks/furnace_side"),
                map.getTextureExtry("minecraft:blocks/furnace_side"),
                map.getTextureExtry("minecraft:blocks/furnace_side"),
                map.getTextureExtry("minecraft:blocks/furnace_top"),
                map.getTextureExtry("minecraft:blocks/furnace_top"))
        } else {
            textures = new CubeTextures(
                map.getTextureExtry("minecraft:blocks/furnace_front_off"),
                map.getTextureExtry("minecraft:blocks/furnace_side"),
                map.getTextureExtry("minecraft:blocks/furnace_side"),
                map.getTextureExtry("minecraft:blocks/furnace_side"),
                map.getTextureExtry("minecraft:blocks/furnace_top"),
                map.getTextureExtry("minecraft:blocks/furnace_top"))
        }
        textures
    }

    override def registerIcons(): Array[ResourceLocation] = {
        Array[ResourceLocation](new ResourceLocation("minecraft:blocks/furnace_front_off"),
            new ResourceLocation("minecraft:blocks/furnace_side"),
            new ResourceLocation("minecraft:blocks/furnace_top"),
            new ResourceLocation("minecraft:blocks/furnace_front_on"))
    }

    //OpensGui Methods
    override def getServerGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): AnyRef =
        new ContainerFurnaceCore(player.inventory, world.getTileEntity(new BlockPos(x, y, z)).asInstanceOf[TileEntityFurnaceCore])

    override def getClientGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): AnyRef =
        new GuiFurnaceCore(player, world.getTileEntity(new BlockPos(x, y, z)).asInstanceOf[TileEntityFurnaceCore])
}
