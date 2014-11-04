package com.teamcos.modularsystems.enchanting.gui;

import org.lwjgl.opengl.GL11;

import com.teamcos.modularsystems.notification.GuiColor;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.ResourceLocation;

public class GuiElementEnchantmentAdjuster extends GuiScreen {

	private int xPos;
	private int yPos;
	public Enchantment enchantment;
	public int level;
	private static final ResourceLocation texture = new ResourceLocation("modularsystems:textures/buttons.png");

	/**
	 * Adds a gui element that has enchantment info
	 * @param x xPosition
	 * @param y yPosition
	 * @param enchantment Enchantment
	 */
	public GuiElementEnchantmentAdjuster(int x, int y, Enchantment enchantment)
	{
		xPos = x;
		yPos = y;
		this.enchantment = enchantment;
	}

	public void drawText(FontRenderer fontRendererObj)
	{		
		fontRendererObj.drawString(GuiColor.GREEN + String.valueOf(level), xPos + 41 - (fontRendererObj.getStringWidth(String.valueOf(level)) / 2), yPos + 5, 4210752);

		String[] enchantmentText = enchantment.getTranslatedName(0).split("enchantment.level.0");
		String enchantment = enchantmentText[0];
		int yMod;
		if(fontRendererObj.getStringWidth(enchantment) > 80 && !enchantment.equals(Enchantment.field_151370_z.getTranslatedName(0).split("enchantment.level.0")[0]))
		{
			yMod = 1;
			fontRendererObj.FONT_HEIGHT = 7;
			fontRendererObj.drawSplitString(enchantment, xPos + 50, yPos + yMod, 80, 4210752);
		}
		else if(enchantment.equals(Enchantment.field_151370_z.getTranslatedName(0).split("enchantment.level.0")[0]))
		{
			yMod = 1;
			fontRendererObj.FONT_HEIGHT = 7;
			fontRendererObj.drawSplitString(enchantment, xPos + 50, yPos + yMod, 70, 4210752);
		}
		else
		{
			yMod = 6;
			fontRendererObj.FONT_HEIGHT = 8;
			fontRendererObj.drawSplitString(enchantment, xPos + 50, yPos + yMod, 80, 4210752);
		}
		
		
	}

	public void drawBackground(TextureManager textureManager, int width, int height, int parentXSize, int parentYSize)
	{
		GL11.glColor4f(1f, 1f, 1f, 1f);

		int x = (width - parentXSize) / 2;
		int y = (height - parentYSize) / 2;

		textureManager.bindTexture(texture);
		drawTexturedModalRect(x + xPos + 48, y + yPos, 16, 80, 80, 16);
	}
}
