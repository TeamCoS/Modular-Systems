package com.pauljoda.modularsystems.core.renderers;

import com.pauljoda.modularsystems.power.tiles.TilePowerBase;
import com.teambr.bookshelf.client.ClientProxy;
import com.teambr.bookshelf.common.blocks.BaseBlock;
import com.teambr.bookshelf.util.RenderUtils;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;

public class SpecialDummyRenderer implements ISimpleBlockRenderingHandler {
    public static int renderID;

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        RenderUtils.render3DInventory((BaseBlock) block, Blocks.iron_block.getIcon(0, 0), renderer);
        if(((BaseBlock)block).getBlockTextures().getOverlay() != null)
            RenderUtils.render3DInventory((BaseBlock) block, ((BaseBlock)block).getBlockTextures().getOverlay(), renderer);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        TilePowerBase dummy = (TilePowerBase)world.getTileEntity(x, y, z);
        if(ClientProxy.renderPass == 0) {
            renderer.renderBlockUsingTexture(Blocks.iron_block, x, y, z, Blocks.iron_block.getIcon(0, 0));
            return true;
        } else if (ClientProxy.renderPass == 1 && dummy.getCore() != null) {
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