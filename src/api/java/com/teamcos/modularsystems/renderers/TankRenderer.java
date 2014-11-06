package com.teamcos.modularsystems.renderers;

import com.teamcos.modularsystems.utilities.helper.BlockSkinRenderHelper;
import com.teamcos.modularsystems.utilities.tiles.TankLogic;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class TankRenderer implements ISimpleBlockRenderingHandler
{
    public static int tankModelID = RenderingRegistry.getNextAvailableRenderId();
    public static int renderPass = 0;

    @Override
    public void renderInventoryBlock (Block block, int metadata, int modelID, RenderBlocks renderer) {
    }

    @Override
    public boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, Block block, int modelID, RenderBlocks renderer)
    {
        if (modelID == tankModelID)
        {
            //Liquid
            if (renderPass == 0)
            {
                TankLogic logic = (TankLogic) world.getTileEntity(x, y, z);
                if (logic.containsFluid())
                {
                    FluidStack liquid = logic.tank.getFluid();
                    renderer.setRenderBounds(0.001, 0.001, 0.001, 0.999, logic.getFluidAmountScaled(), 0.999);
                    Fluid fluid = liquid.getFluid();
                    BlockSkinRenderHelper.renderLiquidBlock(fluid.getStillIcon(), fluid.getStillIcon(), x, y, z, renderer, world, true);

                    renderer.setRenderBounds(0, 0.001, 0.001, 0.999, logic.getFluidAmountScaled(), 0.999);
                }

                if(logic.isFilling()) {
                    if (logic.getDirectionFillingFrom() == ForgeDirection.UP) {
                        Fluid fluid = logic.getFillingLiquid();
                        renderer.setRenderBounds(0.5 - logic.getTransferAmountScaled(), 0.001, 0.5 - logic.getTransferAmountScaled(), 0.5 + logic.getTransferAmountScaled(), 0.999, 0.5 + logic.getTransferAmountScaled());
                        BlockSkinRenderHelper.renderLiquidBlock(fluid.getStillIcon(), fluid.getStillIcon(), x, y, z, renderer, world, true);
                    }
                    else if(logic.renderOffset > 0 && (logic.getDirectionFillingFrom() == ForgeDirection.DOWN || logic.getDirectionFillingFrom() == ForgeDirection.UNKNOWN)){
                        Fluid fluid = logic.getFillingLiquid();
                        double renderMax = 0;
                        if(logic.containsFluid())
                         renderMax= logic.getFluidAmountScaled() + 0.1;
                        if(renderMax > 0.999)
                            renderMax = 0.999;
                        renderer.setRenderBounds(0.5 - logic.getTransferAmountScaled(), 0.001, 0.5 - logic.getTransferAmountScaled(), 0.5 + logic.getTransferAmountScaled(), renderMax, 0.5 + logic.getTransferAmountScaled());
                        BlockSkinRenderHelper.renderLiquidBlock(fluid.getStillIcon(), fluid.getStillIcon(), x, y, z, renderer, world, true);
                    }
                }

            }

            //Block
            else
            {
                renderer.setRenderBounds(0, 0, 0, 1, 1, 1);
                renderer.renderStandardBlock(block, x, y, z);
            }
        }
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory (int modelID)
    {
        return true;
    }

    @Override
    public int getRenderId ()
    {
        return tankModelID;
    }
}