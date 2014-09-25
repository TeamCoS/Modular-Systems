package com.pauljoda.modularsystems.furnace.gui;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import com.pauljoda.modularsystems.core.gui.StatisticsPanel;
import com.pauljoda.modularsystems.core.helper.VersionHelper;
import com.pauljoda.modularsystems.core.lib.GuiColor;
import com.pauljoda.modularsystems.core.lib.Strings;
import com.pauljoda.modularsystems.furnace.containers.ContainerModularFurnace;
import com.pauljoda.modularsystems.furnace.tiles.TileEntityFurnaceCore;

public class GuiModularFurnace extends GuiContainer
{
	private TileEntityFurnaceCore tileEntity;
	private StatisticsPanel statsPanel;
	private static final ResourceLocation background = new ResourceLocation("textures/gui/container/furnace.png");

	public GuiModularFurnace(InventoryPlayer playerInventory, TileEntityFurnaceCore tileEntity)
	{
		super(new ContainerModularFurnace(playerInventory, tileEntity));

		this.tileEntity = tileEntity;
		statsPanel = new StatisticsPanel(-60, 0);
		
		final double speedValue = tileEntity.getSpeed() / 8;
		String speedText = String.format("%.1f", speedValue) + "x";

		final double efficiencyValue = tileEntity.getScaledEfficiency();
		String efficiencyText = String.format("%.1f", efficiencyValue) + "x";

		final int multiplicity = tileEntity.smeltingMultiplier;
		String multiplicityText = String.valueOf(multiplicity) + "x";
		
		statsPanel.addNode(240, 240, GuiColor.RED + speedText);
		statsPanel.addNode(224, 240, GuiColor.GRAY + efficiencyText);
		statsPanel.addNode(208, 240, GuiColor.GREEN + multiplicityText);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		final String invTitle = "Modular Furnace";

		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;

		fontRendererObj.drawString(invTitle, xSize / 2 - fontRendererObj.getStringWidth(invTitle) / 2, 6, 4210752);

		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);

		statsPanel.drawText(fontRendererObj);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1f, 1f, 1f, 1f);

		this.mc.getTextureManager().bindTexture(background);

		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);


		int i1;

		if(tileEntity.isBurning())
		{
			i1 = tileEntity.getBurnTimeRemainingScaled(12);
			drawTexturedModalRect(x + 56, y + 36 + 12 - i1, 176, 13 - i1, 14, i1 + 2);

		}
		i1 = tileEntity.getCookProgressScaled(24);
		drawTexturedModalRect(x + 79, y + 34, 176, 14, i1 + 1, 16);

		statsPanel.drawBackground(this.mc.getTextureManager(), width, height, xSize, ySize);
	}

	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		super.drawScreen(par1, par2, par3);
		if(VersionHelper.getResult() == VersionHelper.OUTDATED)
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
