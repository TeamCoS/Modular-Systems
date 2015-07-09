package com.pauljoda.modularsystems.core.renderers;

import com.teambr.bookshelf.client.ClientProxy;
import com.teambr.bookshelf.common.blocks.BaseBlock;
import com.teambr.bookshelf.util.RenderUtils;
import com.pauljoda.modularsystems.core.tiles.AbstractDummy;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class BlockDummyRenderer implements ISimpleBlockRenderingHandler {
    public static int renderID;

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        RenderUtils.render3DInventory((BaseBlock) block, renderer);
        if(((BaseBlock)block).getBlockTextures().getOverlay() != null)
            RenderUtils.render3DInventory((BaseBlock) block, ((BaseBlock)block).getBlockTextures().getOverlay(), renderer);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        AbstractDummy dummy = (AbstractDummy)world.getTileEntity(x, y, z);
        if(ClientProxy.renderPass == 0) {
            renderer.renderBlockUsingTexture(dummy.getStoredBlock(), x, y, z, dummy.getStoredBlock().getIcon(0, dummy.getMetadata()));
            GL11.glEnable(GL11.GL_BLEND);
            renderer.renderBlockUsingTexture(Blocks.cobblestone, x, y, z, ((BaseBlock) block).getBlockTextures().getOverlay());
            GL11.glDisable(GL11.GL_BLEND);
            return true;
        }
        else if(ClientProxy.renderPass == 1 && ((BaseBlock) block).getBlockTextures().getOverlay() != null) {
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
}