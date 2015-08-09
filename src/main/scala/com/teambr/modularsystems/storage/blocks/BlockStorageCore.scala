package com.teambr.modularsystems.storage.blocks

import com.teambr.bookshelf.collections.CubeTextures
import com.teambr.bookshelf.common.blocks.traits.{ BlockBakeable, DropsItems, FourWayRotation }
import com.teambr.bookshelf.common.tiles.traits.OpensGui
import com.teambr.modularsystems.core.common.blocks.BaseBlock
import com.teambr.modularsystems.core.lib.Reference
import com.teambr.modularsystems.storage.container.ContainerStorageCore
import com.teambr.modularsystems.storage.gui.GuiStorageCore
import com.teambr.modularsystems.storage.tiles.TileStorageCore
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.{ BlockPos, ResourceLocation }
import net.minecraft.world.World

/**
 * Modular-Systems
 * Created by Dyonovan on 04/08/15
 */
class BlockStorageCore extends BaseBlock(Material.wood, "storageCore", classOf[TileStorageCore])
    with OpensGui with FourWayRotation with BlockBakeable with DropsItems {

    override def MODID: String = Reference.MOD_ID
    override def blockName: String = "storageCore"

    override def breakBlock(world: World, pos: BlockPos, state: IBlockState) {

        world.getTileEntity(pos).asInstanceOf[TileStorageCore].destroyNetwork()
        super[DropsItems].breakBlock(world, pos, state)
    }

    override def getDisplayTextures(state : IBlockState) : CubeTextures = {
        val map = Minecraft.getMinecraft.getTextureMapBlocks
        val textures = new CubeTextures(
            map.getTextureExtry(MODID + ":blocks/" + blockName + "Front"),
            map.getTextureExtry(MODID + ":blocks/" + blockName + "Side"),
            map.getTextureExtry(MODID + ":blocks/" + blockName + "Side"),
            map.getTextureExtry(MODID + ":blocks/" + blockName + "Side"),
            map.getTextureExtry(MODID + ":blocks/" + blockName + "Side"),
            map.getTextureExtry(MODID + ":blocks/" + blockName + "Side")
        )
        textures
    }

    override def registerIcons() : Array[ResourceLocation] = {
        Array[ResourceLocation](new ResourceLocation(MODID + ":blocks/" + blockName + "Side"),
            new ResourceLocation(MODID + ":blocks/" + blockName + "Front"),
            new ResourceLocation(MODID + ":blocks/furnaceOverlay"),
            new ResourceLocation(MODID + ":blocks/crusherOverlay"),
            new ResourceLocation(MODID + ":blocks/crusherFront_off"),
            new ResourceLocation(MODID + ":blocks/crusherFront_On"),
            new ResourceLocation(MODID + ":blocks/solidsOverlay"))
    }

    override def getClientGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): AnyRef =
        new GuiStorageCore(player, world.getTileEntity(new BlockPos(x, y, z)).asInstanceOf[TileStorageCore])

    override def getServerGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): AnyRef =
        new ContainerStorageCore(player.inventory, world.getTileEntity(new BlockPos(x, y, z)).asInstanceOf[TileStorageCore])
}
