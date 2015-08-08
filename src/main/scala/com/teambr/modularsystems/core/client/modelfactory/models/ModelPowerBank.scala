package com.teambr.modularsystems.core.client.modelfactory.models

import java.util
import javax.vecmath.Vector3f

import com.teambr.modularsystems.core.managers.BlockManager
import com.teambr.modularsystems.power.blocks.BlockPower
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.block.model._
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.resources.model.{ IBakedModel, ModelRotation }
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
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
class ModelPowerBank extends ISmartBlockModel with ISmartItemModel {
    val faceBakery = new FaceBakery

    var block : BlockPower = BlockManager.bankSolids
    var isItem = false

    def this(b : BlockPower, bool : Boolean) {
        this()
        this.block = b
        isItem = bool
    }

    override def getFaceQuads(facing : EnumFacing) : util.List[_] = {
        val bakedQuads = new util.ArrayList[BakedQuad]()
        val uv = new BlockFaceUV(Array[Float](0.0F, 0.0F, 16.0F, 16.0F), 0)
        val face = new BlockPartFace(null, 0, "", uv)

        val scale = true
        val texture = Minecraft.getMinecraft.getTextureMapBlocks.getTextureExtry("minecraft:blocks/iron_block")

        //Iron Block
        bakedQuads.add(faceBakery.makeBakedQuad(new Vector3f(2.0F, 2.0F, 2.0F), new Vector3f(14.0F, 14.0F, 14.0F), face, texture, facing, ModelRotation.X0_Y0, null, scale, true))

        if(isItem) {
            bakedQuads.add(faceBakery.makeBakedQuad(new Vector3f(1.9F, 1.9F, 1.9F), new Vector3f(14.1F, 14.1F, 14.1F), face, block.getTextureForItem(block), facing, ModelRotation.X0_Y0, null, scale, true))
        }

        bakedQuads
    }

    override def getGeneralQuads : util.List[_] = {
        val bakedQuads = new util.ArrayList[BakedQuad]()
        val uv = new BlockFaceUV(Array[Float](0.0F, 0.0F, 16.0F, 16.0F), 0)
        val face = new BlockPartFace(null, -1, "", uv)

        val scale = true
        val texture = Minecraft.getMinecraft.getTextureMapBlocks.getTextureExtry("minecraft:blocks/hopper_top")
        val barTexture =  Minecraft.getMinecraft.getTextureMapBlocks.getTextureExtry("minecraft:blocks/stone")
        for(enumFacing <- EnumFacing.values()) {
            for(modelRot <- List(ModelRotation.X0_Y0, ModelRotation.X0_Y90, ModelRotation.X0_Y180, ModelRotation.X0_Y270)) {
                //The Bar holder
                bakedQuads.add(faceBakery.makeBakedQuad(new Vector3f(6.0F, 3.0F, 1.0F), new Vector3f(10.0F, 4.0F, 2.0F), face, barTexture, enumFacing, modelRot, null, scale, true))
                bakedQuads.add(faceBakery.makeBakedQuad(new Vector3f(6.0F, 12.0F, 1.0F), new Vector3f(10.0F, 13.0F, 2.0F), face, barTexture, enumFacing, modelRot, null, scale, true))
                bakedQuads.add(faceBakery.makeBakedQuad(new Vector3f(6.0F, 3.0F, 1.0F), new Vector3f(7.0F, 13.0F, 2.0F), face, barTexture, enumFacing, modelRot, null, scale, true))
                bakedQuads.add(faceBakery.makeBakedQuad(new Vector3f(9.0F, 3.0F, 1.0F), new Vector3f(10.0F, 13.0F, 2.0F), face, barTexture, enumFacing, modelRot, null, scale, true))
            }
            //Front border
            bakedQuads.add(faceBakery.makeBakedQuad(new Vector3f(0.0F, 0.0F, 0.0F), new Vector3f(16.0F, 2.0F, 2.0F), face, texture, enumFacing, ModelRotation.X0_Y0, null, scale, true))
            bakedQuads.add(faceBakery.makeBakedQuad(new Vector3f(0.0F, 14.0F, 0.0F), new Vector3f(16.0F, 16.0F, 2.0F), face, texture, enumFacing, ModelRotation.X0_Y0, null, scale, true))
            bakedQuads.add(faceBakery.makeBakedQuad(new Vector3f(0.0F, 2.0F, 0.0F), new Vector3f(2.0F, 14.0F, 2.0F), face, texture, enumFacing, ModelRotation.X0_Y0, null, scale, true))
            bakedQuads.add(faceBakery.makeBakedQuad(new Vector3f(14.0F, 2.0F, 0.0F), new Vector3f(16.0F, 14.0F, 2.0F), face, texture, enumFacing, ModelRotation.X0_Y0, null, scale, true))

            //Going backwards
            bakedQuads.add(faceBakery.makeBakedQuad(new Vector3f(0.0F, 0.0F, 2.0F), new Vector3f(2.0F, 2.0F, 14.0F), face, texture, enumFacing, ModelRotation.X0_Y0, null, scale, true))
            bakedQuads.add(faceBakery.makeBakedQuad(new Vector3f(14.0F, 0.0F, 2.0F), new Vector3f(16.0F, 2.0F, 14.0F), face, texture, enumFacing, ModelRotation.X0_Y0, null, scale, true))
            bakedQuads.add(faceBakery.makeBakedQuad(new Vector3f(0.0F, 14.0F, 2.0F), new Vector3f(2.0F, 16.0F, 14.0F), face, texture, enumFacing, ModelRotation.X0_Y0, null, scale, true))
            bakedQuads.add(faceBakery.makeBakedQuad(new Vector3f(14.0F, 14.0F, 2.0F), new Vector3f(16.0F, 16.0F, 14.0F), face, texture, enumFacing, ModelRotation.X0_Y0, null, scale, true))

            //Back border
            bakedQuads.add(faceBakery.makeBakedQuad(new Vector3f(0.0F, 0.0F, 14.0F), new Vector3f(16.0F, 2.0F, 16.0F), face, texture, enumFacing, ModelRotation.X0_Y0, null, scale, true))
            bakedQuads.add(faceBakery.makeBakedQuad(new Vector3f(0.0F, 14.0F, 14.0F), new Vector3f(16.0F, 16.0F, 16.0F), face, texture, enumFacing, ModelRotation.X0_Y0, null, scale, true))
            bakedQuads.add(faceBakery.makeBakedQuad(new Vector3f(0.0F, 2.0F, 14.0F), new Vector3f(2.0F, 14.0F, 16.0F), face, texture, enumFacing, ModelRotation.X0_Y0, null, scale, true))
            bakedQuads.add(faceBakery.makeBakedQuad(new Vector3f(14.0F, 2.0F, 14.0F), new Vector3f(16.0F, 14.0F, 16.0F), face, texture, enumFacing, ModelRotation.X0_Y0, null, scale, true))

        }
        bakedQuads
    }

    override def isAmbientOcclusion: Boolean = true

    override def isGui3d: Boolean = true

    override def isBuiltInRenderer: Boolean = false

    val MovedUp = new ItemTransformVec3f(new Vector3f(0.0F, 0.0F, 0.0F), new Vector3f(-0.05F, 0.05F, -0.15F), new Vector3f(-0.5F, -0.5F, -0.5F))
    override def getItemCameraTransforms : ItemCameraTransforms = {
        new ItemCameraTransforms(MovedUp, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT)
    }

    override def getTexture : TextureAtlasSprite = Minecraft.getMinecraft.getTextureMapBlocks.getAtlasSprite("minecraft:blocks/iron_block")

    override def handleBlockState(state : IBlockState) : IBakedModel = {
        new ModelPowerBank(state.getBlock.asInstanceOf[BlockPower], false)
    }

    override def handleItemState(stack : ItemStack) : IBakedModel = {
        new ModelPowerBank(Block.getBlockFromItem(stack.getItem).asInstanceOf[BlockPower], true)
    }

    /**
     * Converts the vertex information to the int array format expected by BakedQuads.
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @param color RGBA colour format - white for no effect, non-white to tint the face with the specified colour
     * @param textureAtlasSprite the texture to use for the face
     * @param u u-coordinate of the texture (0 - 16) corresponding to [x,y,z]
     * @param v v-coordinate of the texture (0 - 16) corresponding to [x,y,z]
     * @return
     */
    private def vertexToInts(x : Float, y : Float, z : Float, color : Int, textureAtlasSprite: TextureAtlasSprite, u : Float, v : Float) : Array[Int] = {
        Array[Int] (
            java.lang.Float.floatToRawIntBits(x),
            java.lang.Float.floatToRawIntBits(y),
            java.lang.Float.floatToRawIntBits(z),
            color,
            java.lang.Float.floatToRawIntBits(u),
            java.lang.Float.floatToRawIntBits(v),
            0
        )
    }
}
