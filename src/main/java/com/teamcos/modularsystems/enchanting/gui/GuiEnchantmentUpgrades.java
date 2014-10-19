package com.teamcos.modularsystems.enchanting.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import com.teamcos.modularsystems.enchanting.container.ContainerEnchantmentUpgrades;
import com.teamcos.modularsystems.enchanting.tiles.TileEntityEnchantmentAlter;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GuiEnchantmentUpgrades extends GuiContainer {

	public TileEntityEnchantmentAlter alter;
	private InventoryPlayer inventory;
	protected final EntityPlayer thePlayer;
	
	private static final ResourceLocation textureLocation = new ResourceLocation("modularsystems:textures/enchantingUpgrades.png");

	@SideOnly(Side.CLIENT)
	public GuiEnchantmentUpgrades(InventoryPlayer inventoryPlayer, TileEntityEnchantmentAlter tileEntity, EntityPlayer player) {
		super(new ContainerEnchantmentUpgrades(inventoryPlayer, tileEntity, player));
		alter = tileEntity;
		inventory = inventoryPlayer;
		thePlayer = player;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) 
	{
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;

		String invTitle = "Modular Enchanting Upgrades";
		fontRendererObj.drawString(invTitle, xSize / 2 - fontRendererObj.getStringWidth(invTitle) / 2, 6, 4210752);
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 7, 74, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(textureLocation);

		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}
}
