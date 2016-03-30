package com.teambr.modularsystems.core.common.blocks

import java.util.Random
import javax.annotation.Nonnull

import com.teambr.bookshelf.Bookshelf
import com.teambr.bookshelf.client.TextureManager
import com.teambr.bookshelf.collections.ConnectedTextures
import com.teambr.bookshelf.loadables.{CreatesTextures, ILoadActionProvider}
import com.teambr.modularsystems.core.client.models.BakedProxyModel
import com.teambr.modularsystems.core.common.tiles.{AbstractCore, TileIOExpansion, TileProxy}
import com.teambr.modularsystems.core.lib.Reference
import com.teambr.modularsystems.crusher.tiles.TileCrusherExpansion
import com.teambr.modularsystems.power.tiles.TileBankBase
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.properties.IProperty
import net.minecraft.block.state.{BlockStateContainer, IBlockState}
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.tileentity.TileEntity
import net.minecraft.util._
import net.minecraft.util.math.BlockPos
import net.minecraft.world.{IBlockAccess, World}
import net.minecraftforge.client.event.ModelBakeEvent
import net.minecraftforge.common.property.{ExtendedBlockState, IUnlistedProperty}
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

import scala.collection.mutable.ArrayBuffer

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
class BlockProxy(name: String, tileEntity: Class[_ <: TileEntity]) extends BaseBlock(Material.rock, name, tileEntity)
        with ILoadActionProvider with CreatesTextures {

    override def getCreativeTab: CreativeTabs = {
        null
    }

    override def getItemDropped(state: IBlockState, rand: Random, fortune: Int): Item = {
        null
    }

    override def breakBlock(world: World, pos: BlockPos, state: IBlockState) {
        world.getTileEntity(pos) match {
            case tile: TileProxy =>
                if (tile.getCore.isDefined) {
                    world.getTileEntity(tile.getCore.get.getPos).asInstanceOf[AbstractCore].deconstructMultiblock()
                }
                val block: Block = tile.getStoredBlock
                val meta: Int = tile.metaData

                if (world.isAirBlock(pos) && !tile.isInstanceOf[TileBankBase] && !tile.isInstanceOf[TileCrusherExpansion] && !tile.isInstanceOf[TileIOExpansion]) {
                    val f: Float = world.rand.nextFloat * 0.8F + 0.1F
                    val f1: Float = world.rand.nextFloat * 0.8F + 0.1F
                    val f2: Float = world.rand.nextFloat * 0.8F + 0.1F
                    val entityitem: EntityItem = new EntityItem(world, pos.getX.toDouble + f, pos.getY.toDouble + f1, pos.getZ.toDouble + f2, new ItemStack(block, 1, meta))
                    world.spawnEntityInWorld(entityitem)
                }
                world.notifyNeighborsOfStateChange(pos, this)

            case _ =>
        }
        super.breakBlock(world, pos, state)
    }

    override def onBlockActivated(world: World, pos: BlockPos, state: IBlockState, player: EntityPlayer,
                                  hand: EnumHand, heldItem: ItemStack, side:
                                  EnumFacing, hitX: Float, hitY: Float, hitZ: Float) : Boolean = {
        world.getTileEntity(pos) match {
            case tile: TileProxy =>
                if (tile.getCore.isDefined) {
                    player.openGui(Bookshelf, 0, world,
                        tile.getCore.get.getPos.getX, tile.getCore.get.getPos.getY, tile.getCore.get.getPos.getZ)
                }
        }
        true
    }

    override def createBlockState() : BlockStateContainer = {
        val listed = new Array[IProperty[_]](0)
        val unlisted = new Array[IUnlistedProperty[_]](0)
        new ExtendedBlockState(this, listed, unlisted)
    }

    override def getExtendedState(state : IBlockState, world : IBlockAccess, pos : BlockPos) : IBlockState = {
        new ProxyState(world.getTileEntity(pos).asInstanceOf[TileProxy], state.getBlock)
    }


    /**
      * Used to convert the meta to state
      *
      * @param meta The meta
      * @return
      */
    override def getStateFromMeta(meta : Int) : IBlockState = {
        getDefaultState
    }

    /**
      * Called to convert state from meta
      *
      * @param state The state
      * @return
      */
    override def getMetaFromState(state : IBlockState) = {
        0
    }

    override def getRenderType(state : IBlockState) : EnumBlockRenderType = EnumBlockRenderType.MODEL

    override def isOpaqueCube(state : IBlockState) : Boolean =
        true

    @SideOnly(Side.CLIENT)
    override def isTranslucent(state : IBlockState) : Boolean =
        false

    override def canRenderInLayer(layer : BlockRenderLayer) : Boolean =
        layer == BlockRenderLayer.CUTOUT || layer == BlockRenderLayer.TRANSLUCENT

    /**
      * Performs the action at the given event
      *
      * @param event The event being called from
      * @param isClient True if only on client side, false (default) for server side
      */
    def performLoadAction(@Nonnull event: AnyRef, isClient : Boolean = false) : Unit = {
        event match  {
            case modelBake : ModelBakeEvent =>
                modelBake.getModelRegistry.putObject(
                    new ModelResourceLocation(getUnlocalizedName.split("tile.")(1), "normal"), new BakedProxyModel)
            case _ =>
        }
    }

    /**
      * Used to define the strings needed
      */
    override def getTexturesToStitch: ArrayBuffer[String] = ArrayBuffer(NoCornersTextureLocation, AntiCornersTextureLocation,
        CornersTextureLocation, HorizontalTextureLocation, VerticalTextureLocation, Reference.MOD_ID + ":blocks/furnaceOverlay",
        Reference.MOD_ID + ":blocks/crusherOverlay")

    // Methods to move textures to lower class, handle others here
    def NoCornersTextureLocation   : String = "modularsystems:blocks/border"
    def AntiCornersTextureLocation : String = "modularsystems:blocks/border_anti_corners"
    def CornersTextureLocation     : String = "modularsystems:blocks/border_corners"
    def HorizontalTextureLocation  : String = "modularsystems:blocks/border_horizontal"
    def VerticalTextureLocation    : String = "modularsystems:blocks/border_vertical"

    @SideOnly(Side.CLIENT)
    lazy val connectedTextures = new ConnectedTextures(TextureManager.getTexture(NoCornersTextureLocation),
        TextureManager.getTexture(AntiCornersTextureLocation), TextureManager.getTexture(CornersTextureLocation),
        TextureManager.getTexture(HorizontalTextureLocation), TextureManager.getTexture(VerticalTextureLocation))

    /**
      * Used to get the connected textures object for this block
      *
      * @return
      */
    @SideOnly(Side.CLIENT)
    def getConnectedTextures : ConnectedTextures = if(connectedTextures != null) connectedTextures else {
        new ConnectedTextures(TextureManager.getTexture(NoCornersTextureLocation),
            TextureManager.getTexture(AntiCornersTextureLocation), TextureManager.getTexture(CornersTextureLocation),
            TextureManager.getTexture(HorizontalTextureLocation), TextureManager.getTexture(VerticalTextureLocation))
    }

    /**
      * Used to check if we are able to connect textures with the block
      *
      * @return True if can connect
      */
    def canTextureConnect(world : IBlockAccess, pos : BlockPos, otherPos : BlockPos) : Boolean = {
        world.getTileEntity(pos) match {
            case us : TileProxy =>
                us.getCore match {
                    case Some(core) =>
                        world.getTileEntity(otherPos) match {
                            case proxy : TileProxy =>
                                proxy.getCore.isDefined && proxy.getCore.get == core
                            case _ => false
                        }
                    case _ => false
                }
            case _ => false
        }
    }

    /**
      * Kinds long, but the way to get the connection array for the face
      */
    def getConnectionArrayForFace(world: IBlockAccess, pos: BlockPos, facing: EnumFacing): Array[Boolean] = {
        val connections = new Array[Boolean](16)
        if (world.isAirBlock(pos.offset(facing)) || (!world.getBlockState(pos.offset(facing)).getBlock.isOpaqueCube(world.getBlockState(pos.offset(facing))) &&
                !canTextureConnect(world, pos, pos.offset(facing)))) {
            facing match {
                case EnumFacing.UP =>
                    connections(0) = canTextureConnect(world, pos, pos.add(-1, 0, -1))
                    connections(1) = canTextureConnect(world, pos, pos.add(0, 0, -1))
                    connections(2) = canTextureConnect(world, pos, pos.add(1, 0, -1))
                    connections(3) = canTextureConnect(world, pos, pos.add(-1, 0, 0))
                    connections(4) = canTextureConnect(world, pos, pos.add(1, 0, 0))
                    connections(5) = canTextureConnect(world, pos, pos.add(-1, 0, 1))
                    connections(6) = canTextureConnect(world, pos, pos.add(0, 0, 1))
                    connections(7) = canTextureConnect(world, pos, pos.add(1, 0, 1))
                    return connections
                case EnumFacing.DOWN =>
                    connections(0) = canTextureConnect(world, pos, pos.add(-1, 0, 1))
                    connections(1) = canTextureConnect(world, pos, pos.add(0, 0, 1))
                    connections(2) = canTextureConnect(world, pos, pos.add(1, 0, 1))
                    connections(3) = canTextureConnect(world, pos, pos.add(-1, 0, 0))
                    connections(4) = canTextureConnect(world, pos, pos.add(1, 0, 0))
                    connections(5) = canTextureConnect(world, pos, pos.add(-1, 0, -1))
                    connections(6) = canTextureConnect(world, pos, pos.add(0, 0, -1))
                    connections(7) = canTextureConnect(world, pos, pos.add(1, 0, -1))
                    return connections
                case EnumFacing.NORTH =>
                    connections(0) = canTextureConnect(world, pos, pos.add(1, 1, 0))
                    connections(1) = canTextureConnect(world, pos, pos.add(0, 1, 0))
                    connections(2) = canTextureConnect(world, pos, pos.add(-1, 1, 0))
                    connections(3) = canTextureConnect(world, pos, pos.add(1, 0, 0))
                    connections(4) = canTextureConnect(world, pos, pos.add(-1, 0, 0))
                    connections(5) = canTextureConnect(world, pos, pos.add(1, -1, 0))
                    connections(6) = canTextureConnect(world, pos, pos.add(0, -1, 0))
                    connections(7) = canTextureConnect(world, pos, pos.add(-1, -1, 0))
                    return connections
                case EnumFacing.SOUTH =>
                    connections(0) = canTextureConnect(world, pos, pos.add(-1, 1, 0))
                    connections(1) = canTextureConnect(world, pos, pos.add(0, 1, 0))
                    connections(2) = canTextureConnect(world, pos, pos.add(1, 1, 0))
                    connections(3) = canTextureConnect(world, pos, pos.add(-1, 0, 0))
                    connections(4) = canTextureConnect(world, pos, pos.add(1, 0, 0))
                    connections(5) = canTextureConnect(world, pos, pos.add(-1, -1, 0))
                    connections(6) = canTextureConnect(world, pos, pos.add(0, -1, 0))
                    connections(7) = canTextureConnect(world, pos, pos.add(1, -1, 0))
                    return connections
                case EnumFacing.WEST =>
                    connections(0) = canTextureConnect(world, pos, pos.add(0, 1, -1))
                    connections(1) = canTextureConnect(world, pos, pos.add(0, 1, 0))
                    connections(2) = canTextureConnect(world, pos, pos.add(0, 1, 1))
                    connections(3) = canTextureConnect(world, pos, pos.add(0, 0, -1))
                    connections(4) = canTextureConnect(world, pos, pos.add(0, 0, 1))
                    connections(5) = canTextureConnect(world, pos, pos.add(0, -1, -1))
                    connections(6) = canTextureConnect(world, pos, pos.add(0, -1, 0))
                    connections(7) = canTextureConnect(world, pos, pos.add(0, -1, 1))
                    return connections
                case EnumFacing.EAST =>
                    connections(0) = canTextureConnect(world, pos, pos.add(0, 1, 1))
                    connections(1) = canTextureConnect(world, pos, pos.add(0, 1, 0))
                    connections(2) = canTextureConnect(world, pos, pos.add(0, 1, -1))
                    connections(3) = canTextureConnect(world, pos, pos.add(0, 0, 1))
                    connections(4) = canTextureConnect(world, pos, pos.add(0, 0, -1))
                    connections(5) = canTextureConnect(world, pos, pos.add(0, -1, 1))
                    connections(6) = canTextureConnect(world, pos, pos.add(0, -1, 0))
                    connections(7) = canTextureConnect(world, pos, pos.add(0, -1, -1))
                    return connections
                case _ => return connections
            }
        }
        connections
    }
}

