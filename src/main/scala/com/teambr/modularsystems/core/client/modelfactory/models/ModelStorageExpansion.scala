package com.teambr.modularsystems.core.client.modelfactory.models

import java.util
import javax.vecmath.Vector3f

import com.teambr.bookshelf.common.blocks.properties.PropertyRotation
import com.teambr.modularsystems.core.client.modelfactory.ModelFactory
import com.teambr.modularsystems.core.collections.ConnectedTextures
import com.teambr.modularsystems.core.managers.BlockManager
import com.teambr.modularsystems.storage.blocks.{ BlockStorageExpansion, StorageState }
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.model._
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.resources.model.{ IBakedModel, ModelRotation }
import net.minecraft.item.ItemStack
import net.minecraft.util.{ EnumWorldBlockLayer, BlockPos, EnumFacing }
import net.minecraft.world.IBlockAccess
import net.minecraftforge.client.MinecraftForgeClient
import net.minecraftforge.client.model.{ ISmartBlockModel, ISmartItemModel }

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
class ModelStorageExpansion extends ISmartBlockModel with ISmartItemModel {
    val faceBakery = new FaceBakery
    var textures : ConnectedTextures =
        new ConnectedTextures(ModelFactory.STORAGE_CONNECTED_TEXTURE_NO_CONNECTIONS,
            ModelFactory.STORAGE_CONNECTED_TEXTURE_ANTI_CORNERS,
            ModelFactory.STORAGE_CONNECTED_TEXTURE_CORNERS,
            ModelFactory.STORAGE_CONNECTED_TEXTURE_HORIZONTAL,
            ModelFactory.STORAGE_CONNECTED_TEXTURE_VERTICAL)
    var block : BlockStorageExpansion = null
    var connected = true
    var world : IBlockAccess = null
    var pos : BlockPos = null

    def this(connect : Boolean, w : IBlockAccess, p : BlockPos, b : BlockStorageExpansion) {
        this()
        block = b
        connected = connect
        world = w
        pos = p
    }

    def drawFace(connections : Array[Boolean], list : java.util.List[BakedQuad], rot : ModelRotation, facing : EnumFacing) {
        val face : BlockPartFace = new BlockPartFace(null, 0, "", new BlockFaceUV(Array[Float](0.0F, 0.0F, 16.0F, 16.0F), 0))
        val scale : Boolean = true

        list.add(faceBakery.makeBakedQuad(new Vector3f(0.0F, 0.0F, 16.0F), new Vector3f(8.0F, 8.0F, 16.0F), face, textures.getTextureForCorner(6, connections), EnumFacing.SOUTH, rot, null, scale, true))
        list.add(faceBakery.makeBakedQuad(new Vector3f(8.0F, 0.0F, 16.0F), new Vector3f(16.0F, 8.0F, 16.0F), face, textures.getTextureForCorner(7, connections), EnumFacing.SOUTH, rot, null, scale, true))
        list.add(faceBakery.makeBakedQuad(new Vector3f(0.0F, 8.0F, 16.0F), new Vector3f(8.0F, 16.0F, 16.0F), face, textures.getTextureForCorner(4, connections), EnumFacing.SOUTH, rot, null, scale, true))
        list.add(faceBakery.makeBakedQuad(new Vector3f(8.0F, 8.0F, 16.0F), new Vector3f(16.0F, 16.0F, 16.0F), face, textures.getTextureForCorner(5, connections), EnumFacing.SOUTH, rot, null, scale, true))

        if(MinecraftForgeClient.getRenderLayer == EnumWorldBlockLayer.CUTOUT || world == null)
            list.add(faceBakery.makeBakedQuad(new Vector3f(-0.01F, -0.01F, -0.01F), new Vector3f(16.01F, 16.01F, 16.01F), face, getOverlayTexture(facing), EnumFacing.SOUTH, rot, null, scale, true))

        if (block != null && block.isTranslucent) {
            list.add(faceBakery.makeBakedQuad(new Vector3f(0.0F, 0.0F, 16.0F), new Vector3f(8.0F, 8.0F, 15.999F), face, textures.getTextureForCorner(6, connections), EnumFacing.NORTH, rot, null, scale, true))
            list.add(faceBakery.makeBakedQuad(new Vector3f(8.0F, 0.0F, 16.0F), new Vector3f(16.0F, 8.0F, 15.999F), face, textures.getTextureForCorner(7, connections), EnumFacing.NORTH, rot, null, scale, true))
            list.add(faceBakery.makeBakedQuad(new Vector3f(0.0F, 8.0F, 16.0F), new Vector3f(8.0F, 16.0F, 15.999F), face, textures.getTextureForCorner(4, connections), EnumFacing.NORTH, rot, null, scale, true))
            list.add(faceBakery.makeBakedQuad(new Vector3f(8.0F, 8.0F, 16.0F), new Vector3f(16.0F, 16.0F, 15.999F), face, textures.getTextureForCorner(5, connections), EnumFacing.NORTH, rot, null, scale, true))
        }
    }

    def getOverlayTexture(facing : EnumFacing) : TextureAtlasSprite = {
        if(!connected)
            return ModelFactory.STORAGE_NOCONNECTION_TEXTURE
        block match {
            case BlockManager.storageBasic =>
                ModelFactory.STORAGE_BASIC_TEXTURE
            case BlockManager.storageCapacity =>
                ModelFactory.STORAGE_CAPACITY_TEXTURE
            case BlockManager.storageCrafting =>
                ModelFactory.STORAGE_CRAFTING_TEXTURE
            case BlockManager.storageRemote =>
                ModelFactory.STORAGE_REMOTE_TEXTURE
            case BlockManager.storageHopping =>
                ModelFactory.STORAGE_HOPPING_TEXTURE
            case BlockManager.storageSearch =>
                ModelFactory.STORAGE_SEARCH_TEXTURE
            case BlockManager.storageSort =>
                ModelFactory.STORAGE_SORT_TEXTURE
            case BlockManager.storageSmashing =>
                if(world != null) {
                    val newFacing = world.getBlockState(pos).getValue(PropertyRotation.SIX_WAY).asInstanceOf[EnumFacing]
                    if (newFacing == facing)
                        return ModelFactory.STORAGE_SMASHING_TEXTURE
                    else if (newFacing == facing.getOpposite)
                        return ModelFactory.STORAGE_BACKIO_TEXTURE
                } else {
                    if(facing == EnumFacing.NORTH)
                        return ModelFactory.STORAGE_SMASHING_TEXTURE
                }
                ModelFactory.STORAGE_CAPACITY_TEXTURE
            case _ =>
                ModelFactory.STORAGE_CAPACITY_TEXTURE
        }
    }

    override def getGeneralQuads : java.util.List[_] = {
        new util.ArrayList[Nothing]()
    }

    override def getFaceQuads(facing : EnumFacing) : java.util.List[_] = {
        val bakedQuads = new util.ArrayList[BakedQuad]()
        if(world != null)
            drawFace(block.getConnectionArrayForFace(world, pos, facing), bakedQuads, lookUpRotationForFace(facing), facing)
        else
            drawFace(Array[Boolean](false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false), bakedQuads, lookUpRotationForFace(facing), facing)
        bakedQuads
    }

    def lookUpRotationForFace(face : EnumFacing) : ModelRotation = {
        face match {
            case EnumFacing.UP =>
                ModelRotation.X90_Y0
            case EnumFacing.DOWN =>
                ModelRotation.X270_Y0
            case EnumFacing.NORTH =>
                ModelRotation.X0_Y180
            case EnumFacing.EAST =>
                ModelRotation.X0_Y270
            case EnumFacing.SOUTH =>
                ModelRotation.X0_Y0
            case EnumFacing.WEST =>
                ModelRotation.X0_Y90
            case _ =>
                ModelRotation.X0_Y0
        }
    }

    override def isAmbientOcclusion: Boolean = true

    override def isGui3d: Boolean = true

    override def isBuiltInRenderer: Boolean = false

    override def handleBlockState(state : IBlockState) : IBakedModel = {
        state match {
            case newState : StorageState =>
                new ModelStorageExpansion(newState.connected, newState.world, newState.pos, newState.getBlock.asInstanceOf[BlockStorageExpansion])
            case _ =>
                new ModelStorageExpansion(true, null, null, null)
        }
    }

    override def handleItemState(stack : ItemStack) : IBakedModel = {
        new ModelStorageExpansion(true, null, null, Block.getBlockFromItem(stack.getItem).asInstanceOf[BlockStorageExpansion])
    }

    val MovedUp = new ItemTransformVec3f(new Vector3f(0.0F, 0.0F, 0.0F), new Vector3f(-0.05F, 0.05F, -0.15F), new Vector3f(-0.5F, -0.5F, -0.5F))
    override def getItemCameraTransforms : ItemCameraTransforms = {
        new ItemCameraTransforms(MovedUp, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT)
    }

    override def getTexture : TextureAtlasSprite = textures.corners
}
