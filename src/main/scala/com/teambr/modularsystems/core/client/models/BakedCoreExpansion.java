package com.teambr.modularsystems.core.client.models;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.teambr.modularsystems.core.common.blocks.BlockCoreExpansion;
import com.teambr.modularsystems.core.lib.Reference;
import com.teambr.modularsystems.core.managers.BlockManager;
import gnu.trove.map.hash.THashMap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.util.vector.Vector3f;

import javax.vecmath.Matrix4f;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This file was created for Modular-Systems
 * <p/>
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis "pauljoda"
 * @since 3/23/2016
 */
public class BakedCoreExpansion implements IBakedModel, IPerspectiveAwareModel {

    // The model resource location, reflect items to this if you want it to use this model, no need to register the model itself
    public static final ModelResourceLocation MODEL_RESOURCE_LOCATION_NORMAL =
            new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID(), "coreExpansion"), "normal");

    // The face bakery
    protected static final FaceBakery faceBakery = new FaceBakery();

    private static TRSRTransformation get(float tx, float ty, float tz, float ax, float ay, float az, float s) {
        return new TRSRTransformation(
                new javax.vecmath.Vector3f(tx / 16, ty / 16, tz / 16),
                TRSRTransformation.quatFromXYZDegrees(new javax.vecmath.Vector3f(ax, ay, az)),
                new javax.vecmath.Vector3f(s, s, s),
                null);
    }

    private static Map<ItemCameraTransforms.TransformType, TRSRTransformation> transforms = ImmutableMap.<ItemCameraTransforms.TransformType, TRSRTransformation>builder()
            .put(ItemCameraTransforms.TransformType.GUI,                         get(0, 0, 0, 30, 45, 0, 0.625f))
            .put(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND,     get(0, 2.5f, 0, 75, 45, 0, 0.375f))
            .put(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND,      get(0, 2.5f, 0, 75, 45, 0, 0.375f))
            .put(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND,     get(0, 0, 0, 0, 45, 0, 0.4f))
            .put(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND,      get(0, 0, 0, 0, 225, 0, 0.4f))
            .put(ItemCameraTransforms.TransformType.GROUND,                      get(0, 2, 0, 0, 0, 0, 0.25f))
            .put(ItemCameraTransforms.TransformType.HEAD,                        get(0, 0, 0, 0, 0, 0, 1))
            .put(ItemCameraTransforms.TransformType.FIXED,                       get(0, 0, 0, 0, 0, 0, 1))
            .build();

    // Variables
    protected BlockCoreExpansion expansion;
    protected IBakedModel parent;

    public BakedCoreExpansion() {}

    public BakedCoreExpansion(IBakedModel block) {
        parent = block;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        return Pair.of(this, transforms.get(cameraTransformType).getMatrix());
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
        // Should only be for in world stuff
        if(expansion == null && state != null)
            expansion = (BlockCoreExpansion) state.getBlock();

        ArrayList<BakedQuad> quads = new ArrayList<>();

        if(parent != null)
            quads.addAll(parent.getQuads(state, side, rand));

        if(side == null)
            side = EnumFacing.SOUTH;

        BakedQuad bakedQuad = faceBakery.makeBakedQuad(new Vector3f(2.0F, 2.0F, 2.0F), new Vector3f(14.0F, 14.0F, 14.0F),
                new BlockPartFace(null, 0, "", new BlockFaceUV(new float[] {0.0F, 0.0F, 16.0F, 16.0F}, 0)),
                Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry("minecraft:blocks/iron_block"),
                side, ModelRotation.X0_Y0, null, true, true);

        int c = expansion.colorMultiplier();
        int alpha = c >> 24;
        if(alpha == 0)
            alpha = 255;
        int red   = (c >> 16) & 0xFF;
        int green = (c >> 8) & 0xFF;
        int blue  =  c & 0xFF;

        // New Color
        int color = red | green << 8 | blue << 16 | alpha << 24;

        for(int i = 0; i < 4; i++)
            bakedQuad.getVertexData()[i * 7 + 3] = color;

        // Add inside Iron Block
        quads.add(bakedQuad);

        return quads;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return Minecraft.getMinecraft().getBlockRendererDispatcher()
                .getModelForState(Blocks.IRON_BLOCK.getDefaultState()).getParticleTexture();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return BakedCoreExpansionOverride.INSTANCE;
    }

    /**
     * Allows us to base things on the stack itself, kinda nice
     */
    public static final class BakedCoreExpansionOverride extends ItemOverrideList {

        // The instance of this override, you should never need to make a new one
        public static final BakedCoreExpansionOverride INSTANCE = new BakedCoreExpansionOverride();

        /**
         * Constructor, resolved to private because you should never need to make a new one, use INSTANCE
         */
        private BakedCoreExpansionOverride() {
            super(ImmutableList.<ItemOverride>of());
        }

        /**
         * Allows us to change the item model base on the stack data
         * @param originalModel The base model, probably won't be very useful to you
         * @param stack The stack
         * @param world The world
         * @param entity The entity
         * @return The new model to render, try to cache if at all possible
         */
        @Override
        public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {

            if(originalModel instanceof BakedCoreExpansion && Block.getBlockFromItem(stack.getItem()) instanceof BlockCoreExpansion) {
                // Cast the model
                BakedCoreExpansion model = (BakedCoreExpansion) originalModel;

                // Grab the new block, put into the model
                model.expansion = (BlockCoreExpansion) Block.getBlockFromItem(stack.getItem());

                // Return the new model
                return model;
            }

            // Couldn't find it
            return originalModel;
        }
    }
}

