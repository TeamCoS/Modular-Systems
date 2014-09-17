package com.pauljoda.modularsystems.storage.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.pauljoda.modularsystems.core.gui.StatisticsPanel;
import com.pauljoda.modularsystems.core.lib.GuiColor;
import com.pauljoda.modularsystems.core.lib.Strings;
import com.pauljoda.modularsystems.core.util.VersionChecking;
import com.pauljoda.modularsystems.storage.containers.ContainerModularStorage;
import com.pauljoda.modularsystems.storage.tiles.TileEntityStorageCore;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class GuiModularStorage extends GuiContainer {

	private boolean needsScrollBars = true;
	private float currentScroll;
	private boolean isScrolling;
	private boolean wasClicking;
	private boolean mouseClicked;
	private boolean hasScrollBar = true;
	private List itemList = new ArrayList();
	public static TileEntityStorageCore core;
	private ContainerModularStorage chestItems;
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
		this.xSize = 229;
		this.ySize = 221;
		this.allowUserInput = true;
		statsPanel = new StatisticsPanel(-60, 0);
		
		statsPanel.addNode(240, 224, GuiColor.BLACK + String.valueOf(core.inventoryRows * 11));

	}

	@Override
	public void initGui()
	{
		super.initGui();
		this.chestItems = ((ContainerModularStorage)this.inventorySlots);
		for (int i = 0; i < this.core.inventoryRows * 11; ++i)
		{
			ItemStack is = this.core.getStackInSlot(i);

			this.itemList.add(is);
			chestItems.itemList = this.itemList;
		}
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();
	}

	public void onGuiClosed()
	{
		super.onGuiClosed();
	}

	 public void handleMouseInput()
	    {
	        super.handleMouseInput();
	        int i = Mouse.getEventDWheel();

	        if (i != 0 && this.needsScrollBars())
	        {
	            int j = this.chestItems.storageCore.inventoryRows - 6;

	            if (i > 0)
	            {
	                i = 1;
	            }

	            if (i < 0)
	            {
	                i = -1;
	            }

	            this.currentScroll = (float)((double)this.currentScroll - (double)i / (double)j);

	            if (this.currentScroll < 0.0F)
	            {
	                this.currentScroll = 0.0F;
	            }

	            if (this.currentScroll > 1.0F)
	            {
	                this.currentScroll = 1.0F;
	            }

	            this.chestItems.scrollTo(this.currentScroll);
	            updateScreen();
	        }
	    }
	 
	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		boolean flag = Mouse.isButtonDown(0);
		int k = this.guiLeft;
		int l = this.guiTop;
		int i1 = k + 210;
		int j1 = l + 18;
		int k1 = i1 + 12;
		int l1 = j1 + 112;
		if(this.needsScrollBars())
		{
			if (!this.wasClicking && flag && par1 >= i1 && par2 >= j1 && par1 < k1 && par2 < l1)
			{
				this.isScrolling = this.needsScrollBars;
			}

			if (!flag)
			{
				this.isScrolling = false;
			}

			this.wasClicking = flag;

			if (this.isScrolling)
			{
				this.currentScroll = ((float)(par2 - j1) - 7.5F) / ((float)(l1 - j1) - 15.0F);

				if (this.currentScroll < 0.0F)
				{
					this.currentScroll = 0.0F;
				}

				if (this.currentScroll > 1.0F)
				{
					this.currentScroll = 1.0F;
				}

				this.chestItems.scrollTo(this.currentScroll);
			}
		}
		this.updateScreen();

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
		super.drawScreen(par1, par2, par3);
	}

	private boolean needsScrollBars()
	{
		if(core.inventoryRows > 6)
			return true;
		else
			return false;
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

		int i1 = this.guiLeft + 210;
		int k = this.guiTop + 18;
		int l = k + 112;
		if(this.needsScrollBars())
			this.drawTexturedModalRect(i1, k + (int)((float)(l - k - 17) * this.currentScroll), 232, 241, 12, 15);
		else
			this.drawTexturedModalRect(i1, k + (int)((float)(l - k - 17) * this.currentScroll), 244, 241, 12, 15);


		statsPanel.drawBackground(this.mc.getTextureManager(), width, height, xSize, ySize);
	}
}
