package com.pauljoda.modularsystems.core.renderers;

import com.dyonovan.brlib.client.ClientProxy;
import com.dyonovan.brlib.common.blocks.BaseBlock;
import com.dyonovan.brlib.util.RenderUtils;
import com.pauljoda.modularsystems.core.tiles.AbstractDummy;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;

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
            return true;
        }
        else if(ClientProxy.renderPass == 1 && ((BaseBlock) block).getBlockTextures().getOverlay() != null)
            renderer.renderBlockUsingTexture(Blocks.cobblestone, x, y, z, ((BaseBlock)block).getBlockTextures().getOverlay());
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