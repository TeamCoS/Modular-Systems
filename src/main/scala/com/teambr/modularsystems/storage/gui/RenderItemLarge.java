package com.teambr.modularsystems.storage.gui;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GLSync;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * This file was created for Modular-Systems
 * <p/>
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis "pauljoda"
 * @since 3/28/2016
 */
public class RenderItemLarge extends RenderItem {

    public static RenderItemLarge INSTANCE = new RenderItemLarge(
            Minecraft.getMinecraft().getTextureManager(),
            Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager(),
            Minecraft.getMinecraft().getItemColors());

    public RenderItemLarge(TextureManager textureManager, ModelManager modelManager, ItemColors itemColors) {
        super(textureManager, modelManager, itemColors);
    }

    /**
     * Renders the stack size and/or damage bar for the given ItemStack.
     */
    @Override
    public void renderItemOverlayIntoGUI(FontRenderer fr, ItemStack stack, int xPosition, int yPosition, String text) {
        boolean hasUnicode = fr.getUnicodeFlag();
        fr.setUnicodeFlag(true);
        if(stack != null && yPosition < 140) {
            String s = String.valueOf(stack.stackSize);
            long value = Long.parseLong(s);
            if (value > 999999999) // Billion
                s = String.format("%.0f", Math.floor(value / 1000000000)) + "B";
            else if (value > 999999) // Million
                s = String.format("%.0f", Math.floor(value / 10000000)) + "M";
            else if (value > 999) // Thousand
                s = String.format("%.0f", Math.floor(value / 1000)) + "K";

            super.renderItemOverlayIntoGUI(fr, stack, xPosition, yPosition, stack.stackSize == 1 ? null : s);
            fr.setUnicodeFlag(hasUnicode);
        } else {
            fr.setUnicodeFlag(hasUnicode);
            super.renderItemOverlayIntoGUI(fr, stack, xPosition, yPosition, text);
        }
    }

    @Override
    public IBakedModel getItemModelWithOverrides(ItemStack stack, World world, EntityLivingBase entity) {
        IBakedModel ibakedmodel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(stack);
        return ibakedmodel.getOverrides().handleItemState(ibakedmodel, stack, world, entity);
    }
}
