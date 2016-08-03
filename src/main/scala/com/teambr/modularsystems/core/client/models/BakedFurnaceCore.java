package com.teambr.modularsystems.core.client.models;

import com.google.common.collect.ImmutableMap;
import com.teambr.bookshelf.client.TextureManager;
import com.teambr.bookshelf.common.blocks.properties.Properties;
import com.teambr.modularsystems.core.common.blocks.traits.CoreStates$;
import com.teambr.modularsystems.core.lib.Reference;
import com.teambr.modularsystems.core.managers.BlockManager;
import com.teambr.modularsystems.furnace.blocks.BlockFurnaceCore;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
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
public class BakedFurnaceCore implements IBakedModel, IPerspectiveAwareModel {

    // The model resource location, reflect items to this if you want it to use this model, no need to register the model itself
    public static final ModelResourceLocation MODEL_RESOURCE_LOCATION_NORMAL =
            new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID(), "furnaceCore"), "normal");

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
    protected BlockFurnaceCore coreBlock;

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        return Pair.of(this, transforms.get(cameraTransformType).getMatrix());
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
        EnumFacing facingDir = EnumFacing.WEST;
        boolean isActive = false;
        if(state != null && side != null) {
            isActive = state.getValue(CoreStates$.MODULE$.PROPERTY_ACTIVE());
            facingDir = state.getValue(Properties.FOUR_WAY());
        }

        coreBlock = BlockManager.furnaceCore();

        ArrayList<BakedQuad> quads = new ArrayList<>();

        // Reference values
        BlockPartFace face = new BlockPartFace(null, 0, "", new BlockFaceUV(new float[] {0.0F, 0.0F, 16.0F, 16.0F}, 0));

        IBlockState storedState =
                isActive ?
                        Blocks.LIT_FURNACE.getDefaultState().withProperty(BlockFurnace.FACING, facingDir) :
                        Blocks.FURNACE.getDefaultState().withProperty(BlockFurnace.FACING, facingDir);

        IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(storedState);

        // Add Furnace Model
        quads.addAll(model.getQuads(storedState, side, rand));

        // Reference values
        ModelRotation rot = lookUpRotationForFace((side == null ? EnumFacing.SOUTH : side));

        // Connected Textures Border
        quads.add(faceBakery.makeBakedQuad(new Vector3f(0.0F, 0.0F, 16.0045F), new Vector3f(16.0F, 16.0F, 16.0045F),
                face,
                TextureManager.getTexture("modularsystems:blocks/border_corners"), EnumFacing.SOUTH, rot, null, true, true));

        return quads;
    }

    private ModelRotation lookUpRotationForFace(EnumFacing face) {
        switch(face) {
            case UP :
                return ModelRotation.X90_Y0;
            case DOWN :
                return ModelRotation.X270_Y0;
            case NORTH :
                return ModelRotation.X0_Y180;
            case EAST :
                return ModelRotation.X0_Y270;
            case SOUTH :
                return ModelRotation.X0_Y0;
            case WEST :
                return ModelRotation.X0_Y90;
            default :
                return ModelRotation.X0_Y0;
        }
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
                .getModelForState(Blocks.FURNACE.getDefaultState()).getParticleTexture();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.NONE;
    }
}

