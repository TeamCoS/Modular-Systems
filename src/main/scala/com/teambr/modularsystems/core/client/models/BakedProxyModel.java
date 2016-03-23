package com.teambr.modularsystems.core.client.models;

import com.google.common.collect.ImmutableMap;
import com.teambr.modularsystems.core.common.blocks.BlockProxy;
import com.teambr.modularsystems.core.common.blocks.ProxyState;
import com.teambr.modularsystems.core.common.tiles.TileProxy;
import com.teambr.modularsystems.core.lib.Reference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
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
 * @since 3/22/2016
 */
public class BakedProxyModel implements IBakedModel, IPerspectiveAwareModel {

    // The model resource location, reflect items to this if you want it to use this model, no need to register the model itself
    public static final ModelResourceLocation MODEL_RESOURCE_LOCATION_NORMAL =
            new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID(), "proxy"), "normal");

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
    protected TileProxy proxyTile;
    protected BlockProxy proxyBlock;

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        return Pair.of(this, transforms.get(cameraTransformType).getMatrix());
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
        if(state != null && side != null && state instanceof ProxyState) {
            ProxyState proxyState = (ProxyState)state;
            proxyTile = proxyState.tile;
            proxyBlock = (BlockProxy) proxyState.block;

            ArrayList<BakedQuad> quads = new ArrayList<>();

            // Null Checks
            if(proxyTile == null || proxyTile.getStoredBlock() == null)
                return new ArrayList<>();

            // Reference values
            BlockPartFace face = new BlockPartFace(null, 0, "", new BlockFaceUV(new float[] {0.0F, 0.0F, 16.0F, 16.0F}, 0));

            if(MinecraftForgeClient.getRenderLayer() == BlockRenderLayer.TRANSLUCENT) {
                // Inside
               quads.add(faceBakery.makeBakedQuad(new Vector3f(0.0F, 0.0F, 16.002F), new Vector3f(16.0F, 16.0F, 16.002F), face,
                        proxyTile.getOverlay(), EnumFacing.SOUTH, lookUpRotationForFace(side), null, true, true));
            } else {
                IBlockState storedState = proxyTile.getStoredBlock().getStateFromMeta(proxyTile.metaData());
                IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(storedState);

                // Set stored block
                if (model != Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getMissingModel())
                    quads.addAll(model.getQuads(storedState, side, rand));

                // Reference values
                ModelRotation rot = lookUpRotationForFace(side);
                boolean[] connections = proxyBlock.getConnectionArrayForFace(proxyTile.getWorld(), proxyTile.getPos(), side);

                // Connected Textures Border
                quads.add(faceBakery.makeBakedQuad(new Vector3f(0.0F, 0.0F, 16.0045F), new Vector3f(8.0F, 8.0F, 16.0045F),
                        new BlockPartFace(null, 0, "", new BlockFaceUV(new float[] {0.0F, 8.0F, 8.0F, 0.0F}, 0)),
                        proxyBlock.getConnectedTextures().getTextureForCorner(2, connections), EnumFacing.SOUTH, rot, null, false, true));

                quads.add(faceBakery.makeBakedQuad(new Vector3f(8.0F, 0.0F, 16.0045F), new Vector3f(16.0F, 8.0F, 16.0045F),
                        new BlockPartFace(null, 0, "", new BlockFaceUV(new float[] {8.0F, 8.0F, 16.0F, 0.0F}, 0)),
                        proxyBlock.getConnectedTextures().getTextureForCorner(3, connections), EnumFacing.SOUTH, rot, null, false, true));


                quads.add(faceBakery.makeBakedQuad(new Vector3f(0.0F, 8.0F, 16.0045F), new Vector3f(8.0F, 16.0F, 16.0045F),
                        new BlockPartFace(null, 0, "", new BlockFaceUV(new float[] {0.0F, 16.0F, 8.0F, 8.0F}, 0)),
                        proxyBlock.getConnectedTextures().getTextureForCorner(0, connections), EnumFacing.SOUTH, rot, null, false, true));


                quads.add(faceBakery.makeBakedQuad(new Vector3f(8.0F, 8.0F, 16.0045F), new Vector3f(16.0F, 16.0F, 16.0045F),
                        new BlockPartFace(null, 0, "", new BlockFaceUV(new float[] {8.0F, 16.0F, 16.0F, 8.0F}, 0)),
                        proxyBlock.getConnectedTextures().getTextureForCorner(1, connections), EnumFacing.SOUTH, rot, null, false, true));
            }

            return quads;
        }

        return new ArrayList<>();
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
        return proxyTile != null ?
                Minecraft.getMinecraft().getBlockRendererDispatcher()
                        .getModelForState(proxyTile.getStoredBlock().getStateFromMeta(proxyTile.metaData())).getParticleTexture() :
                Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return null;
    }
}
