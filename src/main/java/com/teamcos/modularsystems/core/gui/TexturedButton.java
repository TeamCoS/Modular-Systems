package com.teamcos.modularsystems.core.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TexturedButton extends GuiButton {
	int u;
	int v;

	private static final ResourceLocation buttonLocation = new ResourceLocation("modularsystems:textures/buttons.png");

	/**
	 * Creates a Textured button from our assets
	 * @param par1 id
	 * @param par2 xPos
	 * @param par3 yPos
	 * @param x width
	 * @param y height
	 * @param u u
	 * @param v v
	 */
	public TexturedButton(int par1, int par2, int par3, int x, int y, int u, int v) {
		super(par1, par2, par3, x, y, "");
		this.u = u;
		this.v = v;
	}

	public void drawButton(Minecraft par1Minecraft, int par2, int par3) {
		super.drawButton(par1Minecraft, par2, par3);
		if (this.visible) {
			par1Minecraft.getTextureManager().bindTexture(buttonLocation);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			boolean flag = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;

			int v1 = this.v;
			if (flag) {
				v1 += this.height;
			}

			this.drawTexturedModalRect(this.xPosition, this.yPosition, u, v1, this.width, this.height);
		}
	}
}


