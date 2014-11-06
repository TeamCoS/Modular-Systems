package com.teamcos.modularsystems.renderers;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

public class TankItemRenderer implements IItemRenderer
{
    private RenderBlocks renderer;
    public TankItemRenderer()
    {
        renderer = new RenderBlocks();
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
    {
        switch (type)
        {
        case ENTITY:
        {
            render(item, item.getItemDamage());
            break;
        }
        case EQUIPPED:
        {
            render(item, item.getItemDamage());
            break;
        }
        case EQUIPPED_FIRST_PERSON:
        {
            render(item, item.getItemDamage());
            break;
        }
        case INVENTORY:
        {
            render(item, item.getItemDamage());
            break;
        }
        default:
            break;
        }
    }

    public void render(ItemStack stack, int metadata)
    {
        FluidStack liquid = null;
        if (stack.hasTagCompound()) {
            NBTTagCompound liquidTag = stack.getTagCompound().getCompoundTag("Fluid");
            if (liquidTag != null) {
                liquid = FluidStack.loadFluidStackFromNBT(liquidTag);
            }
        }
        boolean fluid = liquid != null;

        Block block = Block.getBlockFromItem(stack.getItem());
        Tessellator tessellator = Tessellator.instance;

        block.setBlockBoundsForItemRender();
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1F, 0.0F);
        if(fluid)
            renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, liquid.getFluid().getStillIcon());
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        if(fluid)
            renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, liquid.getFluid().getStillIcon());
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));

        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1F);
        if(fluid)
            renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, liquid.getFluid().getStillIcon());
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));

        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        if(fluid)
            renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, liquid.getFluid().getStillIcon());
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));

        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(-1F, 0.0F, 0.0F);
        if(fluid)
            renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, liquid.getFluid().getStillIcon());
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));

        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        if(fluid)
            renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, liquid.getFluid().getStillIcon());
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 5, metadata));

        tessellator.draw();

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);

        //Block
        renderer.setRenderBounds(0, 0, 0, 1, 1, 1);
    }
}
