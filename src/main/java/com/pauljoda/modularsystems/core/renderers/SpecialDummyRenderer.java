package com.pauljoda.modularsystems.core.renderers;

import com.pauljoda.modularsystems.core.managers.BlockManager;
import com.pauljoda.modularsystems.power.blocks.BlockPower;
import com.teambr.bookshelf.client.ClientProxy;
import com.teambr.bookshelf.common.blocks.BaseBlock;
import com.teambr.bookshelf.util.RenderUtils;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.common.Loader;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class SpecialDummyRenderer implements ISimpleBlockRenderingHandler {
    public static int renderID;

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        RenderUtils.setColor(getBackground(block));
        RenderUtils.render3DInventory((BaseBlock) block, Blocks.iron_block.getIcon(0, 0), renderer);
        if (block .getUnlocalizedName().equalsIgnoreCase(BlockManager.supplierRF.getUnlocalizedName()) ||
                (Loader.isModLoaded("IC2") && block.getUnlocalizedName().equalsIgnoreCase(BlockManager.supplierIC2.getUnlocalizedName())))
            RenderUtils.render3DInventory((BaseBlock) block, ((BlockPower) block).providerIcon, renderer);
        else {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

            Tessellator tessellator = Tessellator.instance;
            IIcon icon = ((BlockPower) block).bankIcon;
            block.setBlockBoundsForItemRender();
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, -1F);
            renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, icon);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, icon);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(-1F, 0.0F, 0.0F);
            renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, icon);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, icon);
            tessellator.draw();

            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        }

        if(((BaseBlock)block).getBlockTextures().getOverlay() != null)
            RenderUtils.render3DInventory((BaseBlock) block, ((BaseBlock)block).getBlockTextures().getOverlay(), renderer);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {

        if(ClientProxy.renderPass == 0) {
            Color color = getBackground(block);
            renderer.renderStandardBlockWithColorMultiplier(Blocks.iron_block, x, y, z, color.getRed() / 255F,
                    color.getGreen() / 255F, color.getBlue() / 255F);
            if (block .getUnlocalizedName().equalsIgnoreCase(BlockManager.supplierRF.getUnlocalizedName()) ||
                    (Loader.isModLoaded("IC2") && block.getUnlocalizedName().equalsIgnoreCase(BlockManager.supplierIC2.getUnlocalizedName())))
                renderer.renderBlockUsingTexture(Blocks.cobblestone, x, y, z, ((BlockPower) block).providerIcon);
            else {
                Tessellator.instance.setColorOpaque_F(1.0F, 1.0F, 1.0F);
                renderer.renderFaceXNeg(block, x, y, z, ((BlockPower) block).bankIcon);
                renderer.renderFaceXPos(block, x, y, z, ((BlockPower) block).bankIcon);
                renderer.renderFaceZNeg(block, x, y, z, ((BlockPower) block).bankIcon);
                renderer.renderFaceZPos(block, x, y, z, ((BlockPower) block).bankIcon);
            }
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

    protected Color getBackground(Block tile) {
        if(tile.getUnlocalizedName().equalsIgnoreCase(BlockManager.powerSolids.getUnlocalizedName()))
            return new Color(74, 57, 14);
        else if(tile.getUnlocalizedName().equalsIgnoreCase(BlockManager.powerRF.getUnlocalizedName()) ||
                tile.getUnlocalizedName().equalsIgnoreCase(BlockManager.supplierRF.getUnlocalizedName()))
            return new Color(174, 0, 36);
        else if(tile.getUnlocalizedName().equalsIgnoreCase(BlockManager.powerLiquids.getUnlocalizedName()))
            return new Color(33, 80, 69);
        else if(Loader.isModLoaded("IC2") && (tile.getUnlocalizedName().equalsIgnoreCase(BlockManager.powerIC2.getUnlocalizedName()) ||
                tile.getUnlocalizedName().equalsIgnoreCase(BlockManager.supplierIC2.getUnlocalizedName())))
            return new Color(255, 255, 255);
        else if(tile.getUnlocalizedName().equalsIgnoreCase(BlockManager.powerMana.getUnlocalizedName()))
            return new Color(58, 214, 214);
        return new Color(255, 255, 255, 0);
    }
}