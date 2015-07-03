package com.pauljoda.modularsystems.core.renderer;

import com.pauljoda.modularsystems.core.blocks.BaseBlock;
import com.pauljoda.modularsystems.core.utils.RenderUtils;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

@SideOnly(Side.CLIENT)
public class BasicBlockRenderer implements ISimpleBlockRenderingHandler {
    public static int renderID;

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        RenderUtils.render3DInventory((BaseBlock)block, metadata, modelId, renderer);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        BaseBlock baseBlock = (BaseBlock)block;

        int meta = world.getBlockMetadata(x, y, z);

        Tessellator.instance.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        Tessellator.instance.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z) - 3);

        renderer.renderFaceZPos(block, x, y, z, baseBlock.getBlockTextures().getSouth(meta, baseBlock.getDefaultRotation()));
        renderer.renderFaceZNeg(block, x, y, z, baseBlock.getBlockTextures().getNorth(meta, baseBlock.getDefaultRotation()));

        renderer.renderFaceXPos(block, x, y, z, baseBlock.getBlockTextures().getEast(meta, baseBlock.getDefaultRotation()));
        renderer.renderFaceXNeg(block, x, y, z, baseBlock.getBlockTextures().getWest(meta, baseBlock.getDefaultRotation()));

        renderer.renderFaceYPos(block, x, y, z, baseBlock.getBlockTextures().getUp(meta, baseBlock.getDefaultRotation()));
        renderer.renderFaceYNeg(block, x, y, z, baseBlock.getBlockTextures().getDown(meta, baseBlock.getDefaultRotation()));

        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return renderID;
    }
}
