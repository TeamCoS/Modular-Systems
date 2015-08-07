package com.teambr.modularsystems.furnace.blocks

import com.teambr.bookshelf.collections.CubeTextures
import com.teambr.bookshelf.common.blocks.traits.{BlockBakeable, DropsItems, FourWayRotation}
import com.teambr.bookshelf.common.tiles.traits.OpensGui
import com.teambr.modularsystems.core.ModularSystems
import com.teambr.modularsystems.core.common.blocks.BaseBlock
import com.teambr.modularsystems.core.common.blocks.traits.ActiveStates
import com.teambr.modularsystems.core.lib.Reference
import com.teambr.modularsystems.furnace.tiles.TileEntityFurnaceCore
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation
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
class BlockFurnaceCore(name: String, var isActive: Boolean) extends BaseBlock(Material.rock, name, classOf[TileEntityFurnaceCore])
with OpensGui with FourWayRotation with BlockBakeable with DropsItems with ActiveStates {



    override def blockName: String = name

    override def MODID: String = Reference.MOD_ID

    override def getCreativeTab: CreativeTabs = {
        if (!isActive)
            ModularSystems.tabModularSystems
        else
            null
    }

    override def getDisplayTextures(state: IBlockState): CubeTextures = {
        val map = Minecraft.getMinecraft.getTextureMapBlocks
        var textures: CubeTextures = null
        if (state.getValue(PROPERTY_ACTIVE).asInstanceOf[Boolean]) {
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

    override def getServerGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): AnyRef = ???

    override def getClientGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): AnyRef = ???

}
