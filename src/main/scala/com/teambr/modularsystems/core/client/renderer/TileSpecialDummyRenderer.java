package com.teambr.modularsystems.core.client.renderer;

import com.teambr.bookshelf.util.RenderUtils;
import com.teambr.modularsystems.power.tiles.TileBankBase;
import com.teambr.modularsystems.power.tiles.TileBankLiquids;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidTankInfo;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class TileSpecialDummyRenderer extends TileEntitySpecialRenderer {

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float tick, int i) {
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        GL11.glDisable(GL11.GL_CULL_FACE);

        WorldRenderer tess = Tessellator.getInstance().getWorldRenderer();
        GlStateManager.translate((float) x, (float) y, (float) z);
        RenderUtils.bindGuiComponentsSheet();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
        tess.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        tess.setBrightness(15728880);

        float level = (float) Math.max(3 / 16F, (((TileBankBase)tile).getPowerLevelScaled(8) + 4) / 16F);

        if(tile instanceof TileBankLiquids) {
            TileBankLiquids fluidTile = (TileBankLiquids)tile;
            FluidTankInfo info = fluidTile.getTankInfo(EnumFacing.UP)[0];
            if(info != null && info.fluid != null) {
                TextureAtlasSprite fluidTexture = info.fluid.getFluid().getIcon();
                RenderUtils.bindMinecraftBlockSheet();
                float level2 = (float) Math.max(3, (((TileBankBase) tile).getPowerLevelScaled(12) + 4));
                float difference = (fluidTexture.getMaxU() - fluidTexture.getMinU()) / 16;
                float differenceV = (fluidTexture.getMaxV() - fluidTexture.getMinV()) / 16;
                drawLevel(7 / 16F, 4 / 16F, 1.5F / 16F, 9 / 16F, level, 1.5F / 16F, fluidTexture.getMinU() + (4 * difference), fluidTexture.getMaxV() - (4 * differenceV), fluidTexture.getMinU() + (9 * difference), fluidTexture.getMaxV() - (level2 * differenceV), tess);
                drawLevel(1.5F / 16F, 4 / 16F, 7 / 16F, 1.5F / 16F, level, 9 / 16F, fluidTexture.getMinU() + (4 * difference), fluidTexture.getMaxV() - (4 * differenceV), fluidTexture.getMinU() + (9 * difference), fluidTexture.getMaxV() - (level2 * differenceV), tess);
                drawLevel(14.5F / 16F, 4 / 16F, 7 / 16F, 14.5F / 16F, level, 9 / 16F, fluidTexture.getMinU() + (4 * difference), fluidTexture.getMaxV() - (4 * differenceV), fluidTexture.getMinU() + (9 * difference), fluidTexture.getMaxV() - (level2 * differenceV), tess);
                drawLevel(7 / 16F, 4 / 16F, 14.5F / 16F, 9 / 16F, level, 14.5F / 16F, fluidTexture.getMinU() + (4 * difference), fluidTexture.getMaxV() - (4 * differenceV), fluidTexture.getMinU() + (9 * difference), fluidTexture.getMaxV() - (level2 * differenceV), tess);
            }
        } else {
            RenderUtils.setColor(new Color(200, 0, 0));
            drawLevel(7 / 16F, 4 / 16F, 1.5F / 16F, 9 / 16F, level, 1.5F / 16F, tess);
            drawLevel(1.5F / 16F, 4 / 16F, 7 / 16F, 1.5F / 16F, level, 9 / 16F, tess);
            drawLevel(14.5F / 16F, 4 / 16F, 7 / 16F, 14.5F / 16F, level, 9 / 16F, tess);
            drawLevel(7 / 16F, 4 / 16F, 14.5F / 16F, 9 / 16F, level, 14.5F / 16F, tess);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }

        RenderUtils.bindMinecraftBlockSheet();

        GlStateManager.translate((float) -x, (float) -y, (float) -z);

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }

    protected void drawLevel(float x1, float y1, float z1, float x2, float y2, float z2, WorldRenderer tess) {
        tess.startDrawingQuads();
        tess.addVertexWithUV(x1, y1, z1, 2 / 255F, 2 / 255F);
        tess.addVertexWithUV(x1, y2, z1, 2 / 255F, 4 / 255F);
        tess.addVertexWithUV(x2, y2, z2, 3 / 255F, 4 / 255F);
        tess.addVertexWithUV(x2, y1, z2, 3 / 255F, 2 / 255F);
        Tessellator.getInstance().draw();
    }

    protected void drawLevel(float x1, float y1, float z1, float x2, float y2, float z2, float u1, float v1, float u2, float v2, WorldRenderer tess) {
        tess.startDrawingQuads();
        tess.addVertexWithUV(x1, y1, z1, u1, v1);
        tess.addVertexWithUV(x1, y2, z1, u1, v2);
        tess.addVertexWithUV(x2, y2, z2, u2, v2);
        tess.addVertexWithUV(x2, y1, z2, u2, v1);
        Tessellator.getInstance().draw();
    }
}
