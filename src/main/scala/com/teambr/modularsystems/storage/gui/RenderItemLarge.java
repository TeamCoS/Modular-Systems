package com.teambr.modularsystems.storage.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GLSync;

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
}
