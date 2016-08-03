package com.teambr.modularsystems.storage.blocks

import com.teambr.bookshelf.Bookshelf
import com.teambr.bookshelf.common.blocks.BlockConnectedTextures
import com.teambr.modularsystems.core.ModularSystems
import com.teambr.modularsystems.core.lib.Reference
import com.teambr.modularsystems.storage.tiles.{TileStorageExpansion, TileStorageCore}
import net.minecraft.block.BlockContainer
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.{EnumFacing, EnumHand}
import net.minecraft.util.math.BlockPos
import net.minecraft.world.{IBlockAccess, World}

/**
  * This file was created for Modular-Systems
  *
  * Modular-Systems is licensed under the
  * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
  * http://creativecommons.org/licenses/by-nc-sa/4.0/
  *
  * @author Paul Davis "pauljoda"
  * @since 3/28/2016
  */
class BlockStorageExpansion(name : String, tileEntity : Class[_ <: TileEntity], material : Material = Material.WOOD)
        extends BlockContainer(material) with BlockConnectedTextures {

    setCreativeTab(ModularSystems.tabModularSystems)
    setUnlocalizedName(Reference.MOD_ID + ":" + name)
    setHardness(1.5F)

    override def isClear: Boolean = false
    override def NoCornersTextureLocation: String = Reference.MOD_ID + ":blocks/storage/" + name + "/noCorners"
    override def CornersTextureLocation: String = Reference.MOD_ID + ":blocks/storage/" + name + "/corners"
    override def VerticalTextureLocation: String = Reference.MOD_ID + ":blocks/storage/" + name + "/vertical"
    override def AntiCornersTextureLocation: String = Reference.MOD_ID + ":blocks/storage/" + name + "/antiCorners"
    override def HorizontalTextureLocation: String = Reference.MOD_ID + ":blocks/storage/" + name + "/horizontal"

    override def createNewTileEntity(world: World, meta: Int): TileEntity = {
        if (tileEntity != null) tileEntity.newInstance() else null
    }

    override def breakBlock(world: World, pos: BlockPos, state: IBlockState) {
        world.getTileEntity(pos).asInstanceOf[TileStorageExpansion].removeFromNetwork(true)
        super.breakBlock(world, pos, state)
    }

    /**
      * Used to check if we are able to connect textures with the block
      *
      * @return True if can connect
      */
    override def canTextureConnect(world : IBlockAccess, pos : BlockPos, otherPos : BlockPos) : Boolean = {
        // Yo dawg, I heard you like match statements
        world.getTileEntity(pos) match {
            case expansion : TileStorageExpansion =>
                expansion.getCore match {
                    case core : TileStorageCore =>
                        world.getTileEntity(otherPos) match {
                            case expansionOther : TileStorageExpansion =>
                                expansionOther.getCore match {
                                    case otherCore : TileStorageCore =>
                                        otherCore == core
                                    case _ => false
                                }
                            case _ => false
                        }
                    case _ => false
                }
            case _ => false
        }
    }


    /**
      * Called when the block is activated
      *
      * If you want to override this but still call it, make sure you call
      *      super[OpensGui].onBlockActivated(...)
      */
    override def onBlockActivated(world : World, pos : BlockPos, state : IBlockState, player : EntityPlayer, hand: EnumHand, heldItem: ItemStack, side : EnumFacing, hitX : Float, hitY : Float, hitZ : Float) : Boolean = {
        super.onBlockActivated(world, pos, state, player, hand, heldItem, side, hitX, hitY, hitZ)

        world.getTileEntity(pos) match {
            case expansion : TileStorageExpansion =>
                expansion.getCore match {
                    case core : TileStorageCore =>
                        player.openGui(Bookshelf, 0, world, core.getPos.getX, core.getPos.getY, core.getPos.getZ)
                        true
                    case _ => false
                }
            case _ => false
        }
    }
}
