package com.pauljoda.modularsystems.core.renderers;

import com.pauljoda.modularsystems.power.tiles.TilePowerBase;
import com.teambr.bookshelf.util.RenderUtils;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

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


        float level = Math.max(3 / 16F, ((TilePowerBase)tile).getPowerLevelScaled(10) + 3 / 16F);

        drawLevel(7 / 16F, 3 / 16F, -0.001F, 9 / 16F, level, -0.001F, tess);
        drawLevel(-0.001F, 3 / 16F, 7 / 16F, -0.001F, level, 9 / 16F, tess);
        drawLevel( 1.001F, 3 / 16F, 7 / 16F,  1.001F, level, 9 / 16F, tess);
        drawLevel(7 / 16F, 3 / 16F,  1.001F, 9 / 16F, level,  1.001F, tess);


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
}
