package com.pauljoda.modularsystems.core.renderers;

import com.pauljoda.modularsystems.core.managers.BlockManager;
import com.teambr.bookshelf.client.ClientProxy;
import com.teambr.bookshelf.common.blocks.BaseBlock;
import com.teambr.bookshelf.util.RenderUtils;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class SpecialDummyRenderer implements ISimpleBlockRenderingHandler {
    public static int renderID;

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        RenderUtils.setColor(getBackground(block));
        RenderUtils.render3DInventory((BaseBlock) block, Blocks.iron_block.getIcon(0, 0), renderer);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if(((BaseBlock)block).getBlockTextures().getOverlay() != null)
            RenderUtils.render3DInventory((BaseBlock) block, ((BaseBlock)block).getBlockTextures().getOverlay(), renderer);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        //TileBankBase dummy = (TileBankBase)world.getTileEntity(x, y, z);
        if(ClientProxy.renderPass == 0) {
            renderer.renderBlockUsingTexture(Blocks.iron_block, x, y, z, Blocks.iron_block.getIcon(0, 0));
            return true;
        } else if (ClientProxy.renderPass == 1) {
            renderer.renderBlockUsingTexture(Blocks.cobblestone, x, y, z, Blocks.hopper.getIcon(1, 0));
        }
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return renderID;
    }

    protected void drawLevel(float x1, float y1, float z1, float x2, float y2, float z2, Tessellator tess) {
        tess.startDrawingQuads();
        tess.addVertexWithUV(x1, y1, z1, 2 / 255F, 2 / 255F);
        tess.addVertexWithUV(x1, y2, z1, 2 / 255F, 4 / 255F);
        tess.addVertexWithUV(x2, y2, z2, 3 / 255F, 4 / 255F);
        tess.addVertexWithUV(x2, y1, z2, 3 / 255F, 2 / 255F);
        tess.draw();
    }

    protected Color getBackground(Block tile) {
        if(tile.getUnlocalizedName().equalsIgnoreCase(BlockManager.powerSolids.getUnlocalizedName()))
            return new Color(74, 57, 14);
        else if(tile.getUnlocalizedName().equalsIgnoreCase(BlockManager.powerRF.getUnlocalizedName()) ||
                tile.getUnlocalizedName().equalsIgnoreCase(BlockManager.supplierRF.getUnlocalizedName()))
            return new Color(174, 0, 36);
        else if(tile.getUnlocalizedName().equalsIgnoreCase(BlockManager.powerLiquids.getUnlocalizedName()))
            return new Color(33, 80, 69);
        else if(tile.getUnlocalizedName().equalsIgnoreCase(BlockManager.powerIC2.getUnlocalizedName()) ||
                tile.getUnlocalizedName().equalsIgnoreCase(BlockManager.powerIC2.getUnlocalizedName()))
            return new Color(255, 255, 255);
        else if(tile.getUnlocalizedName().equalsIgnoreCase(BlockManager.powerMana.getUnlocalizedName()))
            return new Color(58, 214, 214);
        return new Color(255, 255, 255, 0);
    }
}