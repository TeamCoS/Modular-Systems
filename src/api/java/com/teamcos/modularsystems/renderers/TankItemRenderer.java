package com.teamcos.modularsystems.renderers;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import org.lwjgl.opengl.GL11;

public class TankItemRenderer implements IItemRenderer
{
    private FluidTank tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 16);
    public RenderBlocks renderBlocks = new RenderBlocks();

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_LIGHTING);
        if (type == ItemRenderType.ENTITY) GL11.glTranslated(-0.5, -0.5, -0.5);
        else if (type == ItemRenderType.INVENTORY) GL11.glTranslated(0, -0.1, 0);

        TankRenderer.EMPTY_FRAME.render();

        NBTTagCompound tag = item.getTagCompound();
        if (tag != null && tag.hasKey("Fluid")) {
            final FluidStack stack = readFluid(tag);
            if (stack != null) {
                final float height = (float)tank.getFluidAmount() / (float)tank.getCapacity();

                Tessellator.instance.startDrawingQuads();
                final IIcon icon = stack.getFluid().getStillIcon();
                Tessellator.instance.setTextureUV(icon.getMinU(), icon.getMinV());
                Tessellator.instance.setColorRGBA(255, 255, 255, 255);
                RenderHelper.renderCubeWithUV(Tessellator.instance, 0.001, 0.001, 0.001, 0.999, height, 0.999, icon.getMinU(), icon.getMinV(), icon.getMaxU(), icon.getMaxV());
                Tessellator.instance.draw();
            }
        }

        GL11.glPopMatrix();
    }

    private FluidStack readFluid(NBTTagCompound tag) {
        synchronized (tank) {
            tank.setFluid(null);
            tank.readFromNBT(tag.getCompoundTag("Fluid"));
            return tank.getFluid();
        }
    }
}
