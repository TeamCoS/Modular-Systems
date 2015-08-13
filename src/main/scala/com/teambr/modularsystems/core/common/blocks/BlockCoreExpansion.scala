package com.teambr.modularsystems.core.common.blocks

import com.teambr.bookshelf.Bookshelf
import com.teambr.bookshelf.common.blocks.traits.KeepInventory
import com.teambr.bookshelf.common.container.ContainerGeneric
import com.teambr.bookshelf.common.tiles.traits.OpensGui
import com.teambr.modularsystems.core.ModularSystems
import com.teambr.modularsystems.core.client.gui.GuiIOExpansion
import com.teambr.modularsystems.core.common.tiles.{ TileIOExpansion, TileProxy }
import com.teambr.modularsystems.core.managers.BlockManager
import com.teambr.modularsystems.power.container.{ ContainerBankLiquids, ContainerBankSolids }
import com.teambr.modularsystems.power.gui.{ GuiBankLiquids, GuiBankRF, GuiBankSolids }
import com.teambr.modularsystems.power.tiles.{ TileBankBase, TileBankLiquids, TileBankRF, TileBankSolids }
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.{ BlockPos, EnumFacing }
import net.minecraft.world.{ IBlockAccess, World }

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
class BlockCoreExpansion(name: String, tileEntity: Class[_ <: TileEntity], blockColor: Int) extends BlockProxy(name, tileEntity)
with KeepInventory with OpensGui {

    override def getCreativeTab: CreativeTabs = {
        ModularSystems.tabModularSystems
    }

    override def manualOverride(tile : TileEntity, stack : ItemStack, tag : NBTTagCompound) : Boolean = {
        tile match {
            case tile : TileProxy =>
                tile.writeToNBT(tag)
                if(tag.hasKey("CoreLocation"))
                    tag.removeTag("CoreLocation")
                true
            case _ => false
        }
    }

    override def onBlockActivated(world: World, pos: BlockPos, state: IBlockState, player: EntityPlayer, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean = {
        if (player.isSneaking) {
            world.getTileEntity(pos) match {
                case tile: TileBankBase =>
                    player.openGui(Bookshelf, 0, world, pos.getX, pos.getY, pos.getZ)
                case tile : TileIOExpansion =>
                    player.openGui(Bookshelf, 0, world, pos.getX, pos.getY, pos.getZ)
                case _ =>
            }
        } else
            super[BlockProxy].onBlockActivated(world, pos, state, player, side, hitX, hitY, hitZ)
        true
    }

    override def breakBlock(world: World, pos: BlockPos, state: IBlockState): Unit = {
        world.getTileEntity(pos) match {
            case tile: TileProxy =>
                val core = tile.getCore
                if (core.isDefined)
                    core.get.deconstructMultiblock()
            case _ =>
        }

        //super[KeepInventory].breakBlock(world, pos, state)
        super[BlockProxy].breakBlock(world, pos, state)
    }

    /* override def getItemDropped(state: IBlockState, rand: Random, fortune: Int): Item = {
         if (tileEntity.isInstanceOf[classOf TileBankBase])
             super[KeepInventory].getItemDropped(state, rand, fortune)
         else
             Item.getItemFromBlock(this)
     }*/

    override def colorMultiplier(worldIn : IBlockAccess, pos : BlockPos, renderPass : Int) : Int = {
        blockColor
    }

    def getTextureForItem(block : BlockCoreExpansion) : TextureAtlasSprite = {
        block match {
            case BlockManager.bankSolids =>
                Minecraft.getMinecraft.getTextureMapBlocks.getTextureExtry ("modularsystems:blocks/solidsOverlay")
            case BlockManager.bankLiquids =>
                Minecraft.getMinecraft.getTextureMapBlocks.getTextureExtry("modularsystems:blocks/liquidsOverlay")
            case BlockManager.bankRF =>
                Minecraft.getMinecraft.getTextureMapBlocks.getTextureExtry("modularsystems:blocks/rfOverlay")
            case BlockManager.crusherExpansion =>
                Minecraft.getMinecraft.getTextureMapBlocks.getTextureExtry("modularsystems:blocks/crusherExpansionOverlay")
            case BlockManager.ioExpansion =>
                Minecraft.getMinecraft.getTextureMapBlocks.getTextureExtry("modularsystems:blocks/ioExpansionOverlay")
            case _ =>
                Minecraft.getMinecraft.getTextureMapBlocks.getTextureExtry ("modularsystems:blocks/solidsOverlay")
        }
    }

    /*******************************************************************************************************************
      ***************************************** IOpensGui Methods ******************************************************
      ******************************************************************************************************************/

    override def getServerGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): AnyRef = {
        world.getBlockState(new BlockPos(x, y, z)).getBlock match {
            case block: BlockManager.bankSolids.type =>
                new ContainerBankSolids(player.inventory, world.getTileEntity(new BlockPos(x, y, z)).asInstanceOf[TileBankSolids])
            case block: BlockManager.bankLiquids.type =>
                new ContainerBankLiquids(player.inventory, world.getTileEntity(new BlockPos(x, y, z)).asInstanceOf[TileBankLiquids])
            case block: BlockManager.bankRF.type =>
                new ContainerGeneric
            case block : BlockManager.ioExpansion.type =>
                new ContainerGeneric
            case _ => null
        }
    }

    override def getClientGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): AnyRef = {
        world.getBlockState(new BlockPos(x, y, z)).getBlock match {
            case block: BlockManager.bankSolids.type =>
                new GuiBankSolids(new ContainerBankSolids(player.inventory, world.getTileEntity(new BlockPos(x, y, z)).asInstanceOf[TileBankSolids]),
                    player, world.getTileEntity(new BlockPos(x, y, z)).asInstanceOf[TileBankSolids])
            case block: BlockManager.bankLiquids.type =>
                new GuiBankLiquids(new ContainerBankLiquids(player.inventory, world.getTileEntity(new BlockPos(x, y, z)).asInstanceOf[TileBankLiquids]),
                    player, world.getTileEntity(new BlockPos(x, y, z)).asInstanceOf[TileBankLiquids])
            case block: BlockManager.bankRF.type =>
                new GuiBankRF(new ContainerGeneric(),
                    player, world.getTileEntity(new BlockPos(x, y, z)).asInstanceOf[TileBankRF])
            case block : BlockManager.ioExpansion.type =>
                new GuiIOExpansion(world.getTileEntity(new BlockPos(x, y, z)).asInstanceOf[TileIOExpansion])
            case _ => null
        }
    }
}
