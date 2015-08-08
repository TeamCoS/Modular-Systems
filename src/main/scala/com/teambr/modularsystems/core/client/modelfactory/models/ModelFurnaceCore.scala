package com.teambr.modularsystems.core.client.modelfactory.models

import java.util
import javax.vecmath.Vector3f

import com.teambr.bookshelf.common.blocks.properties.PropertyRotation
import com.teambr.modularsystems.core.common.blocks.traits.CoreStates
import net.minecraft.block.BlockFurnace
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.block.model._
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.resources.model.{ IBakedModel, ModelRotation }
import net.minecraft.init.Blocks
import net.minecraft.util.EnumFacing
import net.minecraftforge.client.model.ISmartBlockModel

/**
 * This file was created for Modular-Systems
 *
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis <pauljoda>
 * @since August 07, 2015
 */
class ModelFurnaceCore extends ISmartBlockModel {
    val faceBakery = new FaceBakery
    var facingDirection = EnumFacing.NORTH
    val modelRot = ModelRotation.X0_Y0
    var isActive = false

    def this(rotation : EnumFacing, active : Boolean) = {
        this()
        facingDirection = rotation
        isActive = active
    }
    override def getFaceQuads(facing : EnumFacing) : util.List[_] = {
        val bakedQuads = new util.ArrayList[BakedQuad]()
        addBorderAndOverlay(facing, bakedQuads)
        bakedQuads
    }

    private def addBorderAndOverlay(facing : EnumFacing, bakedQuad: util.ArrayList[BakedQuad]) : Unit = {
        val uv = new BlockFaceUV(Array[Float](0.0F, 0.0F, 16.0F, 16.0F), 0)
        val face = new BlockPartFace(null, 0, "", uv)

        val scale = true
        val hopperTop = Minecraft.getMinecraft.getTextureMapBlocks.getTextureExtry("minecraft:blocks/hopper_top")

        facing match {
            case EnumFacing.UP =>
                bakedQuad.add(faceBakery.makeBakedQuad(new Vector3f(0.0F, 16.03F, 0.0F), new Vector3f(16.0F, 16.03F, 2.0F), face, hopperTop, EnumFacing.UP, modelRot, null, scale, true))
                bakedQuad.add(faceBakery.makeBakedQuad(new Vector3f(0.0F, 16.03F, 14.0F), new Vector3f(16.0F, 16.03F, 16.0F), face, hopperTop, EnumFacing.UP, modelRot, null, scale, true))
                bakedQuad.add(faceBakery.makeBakedQuad(new Vector3f(0.0F, 16.03F, 0.0F), new Vector3f(2.0F, 16.03F, 16.0F), face, hopperTop, EnumFacing.UP, modelRot, null, scale, true))
                bakedQuad.add(faceBakery.makeBakedQuad(new Vector3f(14.0F, 16.03F, 0.0F), new Vector3f(16.0F, 16.03F, 16.0F), face, hopperTop, EnumFacing.UP, modelRot, null, scale, true))
            case EnumFacing.DOWN =>
                bakedQuad.add(faceBakery.makeBakedQuad(new Vector3f(0.0F, -0.03F, 0.0F), new Vector3f(16.0F, -0.03F, 2.0F), face, hopperTop, EnumFacing.DOWN, modelRot, null, scale, true))
                bakedQuad.add(faceBakery.makeBakedQuad(new Vector3f(0.0F, -0.03F, 14.0F), new Vector3f(16.0F, -0.03F, 16.0F), face, hopperTop, EnumFacing.DOWN, modelRot, null, scale, true))
                bakedQuad.add(faceBakery.makeBakedQuad(new Vector3f(0.0F, -0.03F, 0.0F), new Vector3f(2.0F, -0.03F, 16.0F), face, hopperTop, EnumFacing.DOWN, modelRot, null, scale, true))
                bakedQuad.add(faceBakery.makeBakedQuad(new Vector3f(14.0F, -0.03F, 0.0F), new Vector3f(16.0F, -0.03F, 16.0F), face, hopperTop, EnumFacing.DOWN, modelRot, null, scale, true))
            case EnumFacing.NORTH =>
                bakedQuad.add(faceBakery.makeBakedQuad(new Vector3f(0.0F, 0.0F, -0.03F), new Vector3f(16.0F, 2.0F, -0.03F), face, hopperTop, EnumFacing.NORTH, modelRot, null, scale, true))
                bakedQuad.add(faceBakery.makeBakedQuad(new Vector3f(0.0F, 14.0F, -0.03F), new Vector3f(16.0F, 16.0F, -0.03F), face, hopperTop, EnumFacing.NORTH, modelRot, null, scale, true))
                bakedQuad.add(faceBakery.makeBakedQuad(new Vector3f(0.0F, 0.0F, -0.03F), new Vector3f(2.0F, 16.0F, -0.03F), face, hopperTop, EnumFacing.NORTH, modelRot, null, scale, true))
                bakedQuad.add(faceBakery.makeBakedQuad(new Vector3f(14.0F, 0.0F, -0.03F), new Vector3f(16.0F, 16.0F, -0.03F), face, hopperTop, EnumFacing.NORTH, modelRot, null, scale, true))
            case EnumFacing.SOUTH =>
                bakedQuad.add(faceBakery.makeBakedQuad(new Vector3f(0.0F, 0.0F, 16.03F), new Vector3f(16.0F, 2.0F, 16.03F), face, hopperTop, EnumFacing.SOUTH, modelRot, null, scale, true))
                bakedQuad.add(faceBakery.makeBakedQuad(new Vector3f(0.0F, 14.0F, 16.03F), new Vector3f(16.0F, 16.0F, 16.03F), face, hopperTop, EnumFacing.SOUTH, modelRot, null, scale, true))
                bakedQuad.add(faceBakery.makeBakedQuad(new Vector3f(0.0F, 0.0F, 16.03F), new Vector3f(2.0F, 16.0F, 16.03F), face, hopperTop, EnumFacing.SOUTH, modelRot, null, scale, true))
                bakedQuad.add(faceBakery.makeBakedQuad(new Vector3f(14.0F, 0.0F, 16.03F), new Vector3f(16.0F, 16.0F, 16.03F), face, hopperTop, EnumFacing.SOUTH, modelRot, null, scale, true))
            case EnumFacing.EAST =>
                bakedQuad.add(faceBakery.makeBakedQuad(new Vector3f(16.03F, 0.0F, 0.0F), new Vector3f(16.03F, 2.0F, 16.0F), face, hopperTop, EnumFacing.EAST, modelRot, null, scale, true))
                bakedQuad.add(faceBakery.makeBakedQuad(new Vector3f(16.03F, 14.0F, 0.0F), new Vector3f(16.03F, 16.0F, 16.0F), face, hopperTop, EnumFacing.EAST, modelRot, null, scale, true))
                bakedQuad.add(faceBakery.makeBakedQuad(new Vector3f(16.03F, 0.0F, 0.0F), new Vector3f(16.03F, 16.0F, 2.0F), face, hopperTop, EnumFacing.EAST, modelRot, null, scale, true))
                bakedQuad.add(faceBakery.makeBakedQuad(new Vector3f(16.03F, 0.0F, 14.0F), new Vector3f(16.03F, 16.0F, 16.0F), face, hopperTop, EnumFacing.EAST, modelRot, null, scale, true))
            case EnumFacing.WEST =>
                bakedQuad.add(faceBakery.makeBakedQuad(new Vector3f(-0.03F, 0.0F, 0.0F), new Vector3f(-0.03F, 2.0F, 16.0F), face, hopperTop, EnumFacing.WEST, modelRot, null, scale, true))
                bakedQuad.add(faceBakery.makeBakedQuad(new Vector3f(-0.03F, 14.0F, 0.0F), new Vector3f(-0.03F, 16.0F, 16.0F), face, hopperTop, EnumFacing.WEST, modelRot, null, scale, true))
                bakedQuad.add(faceBakery.makeBakedQuad(new Vector3f(-0.03F, 0.0F, 0.0F), new Vector3f(-0.03F, 16.0F, 2.0F), face, hopperTop, EnumFacing.WEST, modelRot, null, scale, true))
                bakedQuad.add(faceBakery.makeBakedQuad(new Vector3f(-0.03F, 0.0F, 14.0F), new Vector3f(-0.03F, 16.0F, 16.0F), face, hopperTop, EnumFacing.WEST, modelRot, null, scale, true))
        }
    }

    override def getGeneralQuads : util.List[_] = {
        val blockRendererDispatcher = Minecraft.getMinecraft.getBlockRendererDispatcher
        val blockModelShapes = blockRendererDispatcher.getBlockModelShapes

        val copiedModel : IBakedModel =
        if(!isActive)
            blockModelShapes.getModelForState(Blocks.furnace.getDefaultState.withProperty(BlockFurnace.FACING, facingDirection))
        else
            blockModelShapes.getModelForState(Blocks.lit_furnace.getDefaultState.withProperty(BlockFurnace.FACING, facingDirection))

        val returnVals = new util.ArrayList[BakedQuad]()
        val otherGeneralQuads = copiedModel.getGeneralQuads
        for(i <- 0 until otherGeneralQuads.size()) {
            returnVals.add(otherGeneralQuads.get(i).asInstanceOf[BakedQuad])
        }

        for(facing <- EnumFacing.values()) {
            val facingQuads = copiedModel.getFaceQuads(facing)
            for(i <- 0 until facingQuads.size()) {
                returnVals.add(facingQuads.get(i).asInstanceOf[BakedQuad])
            }
        }
        returnVals
    }

    override def isAmbientOcclusion: Boolean = true

    override def isGui3d: Boolean = true

    override def isBuiltInRenderer: Boolean = false

    override def handleBlockState(state : IBlockState) : IBakedModel = {
        new ModelFurnaceCore(state.getValue(PropertyRotation.FOUR_WAY.getProperty).asInstanceOf[EnumFacing],
            state.getValue(state.getBlock.asInstanceOf[CoreStates].PROPERTY_ACTIVE).asInstanceOf[Boolean])
    }

    val MovedUp = new ItemTransformVec3f(new Vector3f(0.0F, 0.0F, 0.0F), new Vector3f(-0.05F, 0.05F, -0.15F), new Vector3f(-0.5F, -0.5F, -0.5F))
    override def getItemCameraTransforms : ItemCameraTransforms = {
        new ItemCameraTransforms(MovedUp, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT)
    }

    override def getTexture : TextureAtlasSprite = Minecraft.getMinecraft.getTextureMapBlocks.getAtlasSprite("minecraft:blocks/furnace_front_off")
}
