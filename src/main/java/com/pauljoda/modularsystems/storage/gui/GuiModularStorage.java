package com.pauljoda.modularsystems.storage.gui;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import com.pauljoda.modularsystems.core.gui.StatisticsPanel;
import com.pauljoda.modularsystems.core.lib.Strings;
import com.pauljoda.modularsystems.core.util.VersionChecking;
import com.pauljoda.modularsystems.storage.containers.ContainerModularStorage;
import com.pauljoda.modularsystems.storage.tiles.TileEntityStorageCore;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class GuiModularStorage extends GuiContainer {
	private TileEntityStorageCore core;
	private InventoryPlayer inventory;
    protected final EntityPlayer thePlayer;
    private StatisticsPanel statsPanel;

    private static final ResourceLocation textureLocation = new ResourceLocation("modularsystems:textures/modular_storage_gui.png");
   
    public GuiModularStorage(InventoryPlayer inventoryPlayer, TileEntityStorageCore tileEntity, EntityPlayer player) 
	{
    	super(new ContainerModularStorage(inventoryPlayer, tileEntity));
		this.core = tileEntity;
		this.inventory = inventoryPlayer;
		this.thePlayer = player;
		this.xSize = 211;
		this.ySize = 221;
		
		statsPanel = new StatisticsPanel(-60, 0);
	}
    
    @Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) 
	{

		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;

		String invTitle = "Modular Storage";
		fontRendererObj.drawString(invTitle, xSize / 2 - fontRendererObj.getStringWidth(invTitle) / 2, 6, 4210752);
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 26, 130, 4210752);
		
		statsPanel.drawText(fontRendererObj);
	}
    
    @Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(textureLocation);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		
		statsPanel.drawBackground(this.mc.getTextureManager(), width, height, xSize, ySize);
	}
    
    @Override
	public void drawScreen(int par1, int par2, float par3)
	{
		super.drawScreen(par1, par2, par3);
		if(VersionChecking.getResult() == VersionChecking.OUTDATED)
		{
			int var5 = (this.width - this.xSize) / 2;
			int var6 = (this.height - this.ySize) / 2;
			if(par1 >= 0 + var5 && par2 >= 0 + var6 && par1 <= 16 + var5 && par2 <= 16 + var6) 
			{
				List temp = Arrays.asList(Strings.UPDATE_TOOLTIP);
				drawHoveringText(temp, par1, par2, fontRendererObj); 
			}
		}
	}
}
