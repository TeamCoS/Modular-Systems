package com.teamcos.modularsystems.enchanting.gui;

import com.teamcos.modularsystems.core.ModularSystems;
import com.teamcos.modularsystems.core.entity.EntityStaticItem;
import com.teamcos.modularsystems.core.gui.StatisticsPanel;
import com.teamcos.modularsystems.core.gui.TexturedButton;
import com.teamcos.modularsystems.core.helper.EnchantHelper;
import com.teamcos.modularsystems.core.helper.VersionHelper;
import com.teamcos.modularsystems.notification.GuiColor;
import com.teamcos.modularsystems.core.lib.Strings;
import com.teamcos.modularsystems.core.network.EnchantPacket;
import com.teamcos.modularsystems.enchanting.container.ContainerModularEnchanting;
import com.teamcos.modularsystems.enchanting.tiles.TileEntityEnchantmentAlter;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiModularEnchanting extends GuiContainer {

	public TileEntityEnchantmentAlter alter;
	private InventoryPlayer inventory;
	protected final EntityPlayer thePlayer;

	List<GuiElementEnchantmentAdjuster> enchants = new ArrayList<GuiElementEnchantmentAdjuster>();

	EntityStaticItem item;
	ItemRenderer itemRenderer;
	private float roll;
	private float yaw;
	private boolean rollDown;

	private boolean hasItem;
	private ItemStack currentStack;

	private int maxPoints;
	private int pointsToSpend;

	private StatisticsPanel statsPanel;

	private static final ResourceLocation textureLocation = new ResourceLocation("modularsystems:textures/enchanting.png");

	@SideOnly(Side.CLIENT)
	public GuiModularEnchanting(InventoryPlayer inventoryPlayer, TileEntityEnchantmentAlter tileEntity, EntityPlayer player) {
		super(new ContainerModularEnchanting(inventoryPlayer, tileEntity, player));
		alter = tileEntity;
		inventory = inventoryPlayer;
		thePlayer = player;
		this.xSize = 256;
		this.ySize = 226;

        maxPoints = pointsToSpend = alter.getEnchantmentBonus();

		itemRenderer = new ItemRenderer(mc);
		item = new EntityStaticItem(Minecraft.getMinecraft().theWorld, 0, 0, 0, new ItemStack(Blocks.air, 1));

		statsPanel = new StatisticsPanel(-60, 0);
		statsPanel.addNode(240, 208, GuiColor.GREEN + "0");
		statsPanel.addNode(224, 208, GuiColor.BLACK + String.valueOf(maxPoints));

		hasItem = false;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;

		this.buttonList.add(new GuiButton(0, x + 48, y + 22, 48, 20, "Enchant"));
	}

	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		super.drawScreen(par1, par2, par3);

		statsPanel.updateNode(0, GuiColor.GREEN + String.valueOf(EnchantHelper.getRequiredLevelFromList(enchants, alter)));
		statsPanel.updateNode(1, GuiColor.BLACK + String.valueOf(pointsToSpend));

		if(alter.inv[0] == null && hasItem)
			onInventoryChanged();
		else if(alter.inv[0] != null && !hasItem)
			onInventoryChanged();
		else if(alter.inv[0] != null && hasItem && !ItemStack.areItemStacksEqual(currentStack, alter.inv[0]))
			onInventoryChanged();


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

	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2)
	{
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;

		String invTitle = "Modular Enchanting";
		fontRendererObj.drawString(invTitle, xSize / 2 - fontRendererObj.getStringWidth(invTitle) / 2, 6, 4210752);
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 42, 130, 4210752);

		statsPanel.drawText(fontRendererObj);

		for(int i = 0; i < enchants.size(); i++)
			enchants.get(i).drawText(fontRendererObj);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(textureLocation);

		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

		statsPanel.drawBackground(this.mc.getTextureManager(), width, height, xSize, ySize);

		for(int i = 0; i < enchants.size(); i++)
			enchants.get(i).drawBackground(this.mc.getTextureManager(), width, height, xSize, ySize);

		renderItemInSlot();
	}

	@Override
	public void actionPerformed(GuiButton button)
	{
		switch(button.id)
		{
		case 0 :
			if(hasItem)
			{
				if(Minecraft.getMinecraft().thePlayer.experienceLevel > EnchantHelper.getRequiredLevelFromList(enchants, alter) || Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode)
                {
                    for (int i = 0; i < enchants.size(); i++)
                    {
                        if (enchants.get(i).level > 0)
                        {
                            EnchantPacket packet = new EnchantPacket(enchants.get(i).enchantment.effectId, enchants.get(i).level);
                            ModularSystems.packetPipeline.sendToServer(packet);
                        }
                    }
                }
			}
			break;
		default :
			int operation = button.id % 2;
			int enchantLoc = ((button.id / 2) - 1);

            boolean canOperate = true;
            for(int i = 0; i < enchants.size(); i++)
                if (enchantLoc != i && enchants.get(i).level > 0 && alter.inv[0].getItem() instanceof ItemBook)
                    canOperate = false;


			if(operation == 0)
			{
				enchants.get(enchantLoc).level--;
				pointsToSpend++;
				if(pointsToSpend > maxPoints)
					pointsToSpend = maxPoints;
			}
			else if(pointsToSpend > 0 && enchants.get(enchantLoc).level < enchants.get(enchantLoc).enchantment.getMaxLevel() && canOperate)
			{
				enchants.get(enchantLoc).level++;
				pointsToSpend--;
				if(pointsToSpend < 0)
					pointsToSpend = 0;
			}

			if(enchants.get(enchantLoc).level < 0)
				enchants.get(enchantLoc).level = 0;

			break;
		}
	}
	public void renderItemInSlot()
	{
		float x = (width - xSize) / 2;
		float y = (height - ySize) / 2;

		GL11.glPushMatrix();

		GL11.glTranslatef(x + 55, y + 114, 100);

		float scale = 120F;
		GL11.glScalef(-scale, scale, scale);

		RenderHelper.enableStandardItemLighting();

		GL11.glRotatef(180, 0, 0, 1);
		GL11.glRotatef(roll, 1, 0, 0);
		GL11.glRotatef(yaw, 0, 1, 0);

		if(alter.inv[0] != null)
			item.setEntityItemStack(alter.inv[0]);
		else
			item.setEntityItemStack(new ItemStack(Blocks.air, 1));


		RenderManager.instance.renderEntityWithPosYaw(item, 0, 0, 0, 0, 0);

		RenderHelper.disableStandardItemLighting();
		GL11.glPopMatrix();

		yaw += 0.5F;
		if (rollDown) {
			roll -= 0.05F;
			if (roll < -5) {
				rollDown = false;
				roll = -5;
			}
		}else{
			roll += 0.05F;
			if (roll > 25) {
				rollDown = true;
				roll = 25;
			}
		}
		updateScreen();
	}

	public void onInventoryChanged()
	{
        hasItem = !hasItem;


		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		if(alter.inv[0] == null)
		{
			pointsToSpend = maxPoints;
			enchants.clear();
			buttonList.clear();

			this.buttonList.add(new GuiButton(0, x + 48, y + 22, 48, 20, "Enchant"));
		}
		else
		{
			if(!ItemStack.areItemStacksEqual(currentStack, alter.inv[0]))
			{
				pointsToSpend = maxPoints;
				enchants.clear();
				buttonList.clear();

				this.buttonList.add(new GuiButton(0, x + 48, y + 22, 48, 20, "Enchant"));
				if(alter.inv[0].getEnchantmentTagList() == null || alter.inv[0].getEnchantmentTagList().tagCount() == 0)
				{
					List<Enchantment> enchantList = EnchantHelper.getEnchantsForItemInAlter(alter, alter.inv[0], 30);
					for(int i = 0; i < enchantList.size(); i++)
					{
						this.enchants.add(new GuiElementEnchantmentAdjuster(112, 16 + (16 * i), enchantList.get(i)));
						this.buttonList.add(new TexturedButton((i + i) + 2, x + 112, y + 16 +(16 * i), 16, 16, 0, 96));
						this.buttonList.add(new TexturedButton((i + i) + 3, x + 112 + 16, y + 16 + (16 * i), 16, 16, 16, 96));
					}
				}
			}
		}
		currentStack = alter.inv[0];
	}
}
