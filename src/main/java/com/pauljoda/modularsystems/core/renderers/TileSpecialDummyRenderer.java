package com.pauljoda.modularsystems.core.renderers;

import com.pauljoda.modularsystems.power.tiles.TileLiquidsPower;
import com.pauljoda.modularsystems.power.tiles.TilePowerBase;
import com.pauljoda.modularsystems.power.tiles.TileRFPower;
import com.pauljoda.modularsystems.power.tiles.TileSolidsPower;
import com.teambr.bookshelf.util.RenderUtils;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidTankInfo;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.awt.*;

public class TileSpecialDummyRenderer extends TileEntitySpecialRenderer {

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float tick) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);

        Tessellator tess = Tessellator.instance;
        tess.addTranslation((float) x, (float) y, (float) z);
        RenderUtils.bindGuiComponentsSheet();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);

        //Color Background
        RenderUtils.setColor(getBackground(tile));
        float min = 2 / 16F - 0.002F;
        float max = 14 / 16F + 0.002F;
        drawLevel(min,  min, -0.001F, max, max, -0.001F, tess);
        drawLevel(-0.001F, min, min, -0.001F, max, max, tess);
        drawLevel(1.001F, min, min, 1.001F, max, max, tess);
        drawLevel(min, min, 1.001F, max, max, 1.001F, tess);
        tess.startDrawingQuads();
        tess.addVertexWithUV(min, 1.001, min, 2 / 255F, 2 / 255F);
        tess.addVertexWithUV(min, 1.001, max, 2 / 255F, 4 / 255F);
        tess.addVertexWithUV(max, 1.001, max, 3 / 255F, 4 / 255F);
        tess.addVertexWithUV(max, 1.001, min, 3 / 255F, 2 / 255F);
        tess.draw();
        tess.startDrawingQuads();
        tess.addVertexWithUV(min, -0.001, min, 2 / 255F, 2 / 255F);
        tess.addVertexWithUV(min, -0.001, max, 2 / 255F, 4 / 255F);
        tess.addVertexWithUV(max, -0.001, max, 3 / 255F, 4 / 255F);
        tess.addVertexWithUV(max, -0.001, min, 3 / 255F, 2 / 255F);
        tess.draw();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        RenderUtils.setColor(new Color(57, 57, 57));
        drawLevel(6 / 16F, 3 / 16F, -0.002F, 10 / 16F,  13 / 16F,  -0.002F, tess);
        drawLevel(-0.002F, 3 / 16F, 6 / 16F,  -0.002F,  13 / 16F, 10 / 16F, tess);
        drawLevel( 1.002F, 3 / 16F, 6 / 16F,   1.002F,  13 / 16F, 10 / 16F, tess);
        drawLevel(6 / 16F, 3 / 16F,  1.002F, 10 / 16F,  13 / 16F,   1.002F, tess);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        float level = Math.max(3 / 16F, (((TilePowerBase)tile).getPowerLevelScaled(8) + 4) / 16F);

        if(tile instanceof TileLiquidsPower) {
            TileLiquidsPower fluidTile = (TileLiquidsPower)tile;
            FluidTankInfo info = fluidTile.getTankInfo(ForgeDirection.UNKNOWN)[0];
            if(info != null && info.fluid != null) {
                IIcon fluidTexture = info.fluid.getFluid().getIcon();
                RenderUtils.bindMinecraftBlockSheet();
                float level2 = Math.max(3, (((TilePowerBase) tile).getPowerLevelScaled(12) + 4));
                float difference = (fluidTexture.getMaxU() - fluidTexture.getMinU()) / 16;
                float differenceV = (fluidTexture.getMaxV() - fluidTexture.getMinV()) / 16;
                drawLevel(7 / 16F, 4 / 16F, -0.003F, 9 / 16F, level, -0.003F, fluidTexture.getMinU() + (4 * difference), fluidTexture.getMaxV() - (4 * differenceV), fluidTexture.getMinU() + (9 * difference), fluidTexture.getMaxV() - (level2 * differenceV), tess);
                drawLevel(-0.003F, 4 / 16F, 7 / 16F, -0.003F, level, 9 / 16F, fluidTexture.getMinU() + (4 * difference), fluidTexture.getMaxV() - (4 * differenceV), fluidTexture.getMinU() + (9 * difference), fluidTexture.getMaxV() - (level2 * differenceV), tess);
                drawLevel(1.003F, 4 / 16F, 7 / 16F, 1.003F, level, 9 / 16F, fluidTexture.getMinU() + (4 * difference), fluidTexture.getMaxV() - (4 * differenceV), fluidTexture.getMinU() + (9 * difference), fluidTexture.getMaxV() - (level2 * differenceV), tess);
                drawLevel(7 / 16F, 4 / 16F, 1.003F, 9 / 16F, level, 1.003F, fluidTexture.getMinU() + (4 * difference), fluidTexture.getMaxV() - (4 * differenceV), fluidTexture.getMinU() + (9 * difference), fluidTexture.getMaxV() - (level2 * differenceV), tess);
            }
        } else {
            RenderUtils.setColor(new Color(200, 0, 0));
            drawLevel(7 / 16F, 4 / 16F, -0.003F, 9 / 16F, level, -0.003F, tess);
            drawLevel(-0.003F, 4 / 16F, 7 / 16F, -0.003F, level, 9 / 16F, tess);
            drawLevel(1.003F, 4 / 16F, 7 / 16F, 1.003F, level, 9 / 16F, tess);
            drawLevel(7 / 16F, 4 / 16F, 1.003F, 9 / 16F, level, 1.003F, tess);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }

        RenderUtils.bindMinecraftBlockSheet();
        tess.addTranslation((float) -x, (float) -y, (float) -z);

        GL11.glEnable(GL11.GL_CULL_FACE);
        RenderUtils.restoreRenderState();
        GL11.glPopMatrix();
    }

    protected void drawLevel(float x1, float y1, float z1, float x2, float y2, float z2, Tessellator tess) {
        tess.startDrawingQuads();
        tess.addVertexWithUV(x1, y1, z1, 2 / 255F, 2 / 255F);
        tess.addVertexWithUV(x1, y2, z1, 2 / 255F, 4 / 255F);
        tess.addVertexWithUV(x2, y2, z2, 3 / 255F, 4 / 255F);
        tess.addVertexWithUV(x2, y1, z2, 3 / 255F, 2 / 255F);
        tess.draw();
    }

    protected void drawLevel(float x1, float y1, float z1, float x2, float y2, float z2, float u1, float v1, float u2, float v2, Tessellator tess) {
        tess.startDrawingQuads();
        tess.addVertexWithUV(x1, y1, z1, u1, v1);
        tess.addVertexWithUV(x1, y2, z1, u1, v2);
        tess.addVertexWithUV(x2, y2, z2, u2, v2);
        tess.addVertexWithUV(x2, y1, z2, u2, v1);
        tess.draw();
    }

    protected Color getBackground(TileEntity tile) {
        if(tile instanceof TileSolidsPower)
            return new Color(74, 57, 14, 100);
        else if(tile instanceof TileRFPower)
            return new Color(174, 0, 36, 100);
        else if(tile instanceof TileLiquidsPower)
            return new Color(33, 80, 69, 100);
        return new Color(255, 255, 255, 0);
    }
}
