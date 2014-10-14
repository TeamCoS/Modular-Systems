package com.pauljoda.modularsystems.storage.gui;

import com.pauljoda.modularsystems.core.ModularSystems;
import com.pauljoda.modularsystems.core.gui.StatisticsPanel;
import com.pauljoda.modularsystems.core.gui.TexturedButton;
import com.pauljoda.modularsystems.core.helper.VersionHelper;
import com.pauljoda.modularsystems.core.lib.GuiColor;
import com.pauljoda.modularsystems.core.lib.Reference;
import com.pauljoda.modularsystems.core.lib.Strings;
import com.pauljoda.modularsystems.core.network.StorageSortPacket;
import com.pauljoda.modularsystems.storage.containers.ContainerModularStorage;
import com.pauljoda.modularsystems.storage.tiles.TileEntityStorageCore;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@SideOnly(Side.CLIENT)
public class GuiModularStorage extends GuiContainer {

    private boolean needsScrollBars = true;
    private float currentScroll;
    private boolean isScrolling;
    private boolean wasClicking;
    private boolean isInMainArea;
    public List itemList = new ArrayList();
    public static TileEntityStorageCore core;
    private ContainerModularStorage containerModularStorage;
    private InventoryPlayer inventory;
    protected final EntityPlayer thePlayer;
    private boolean armorViewable;
    private boolean hasCrafting;
    private StatisticsPanel statsPanel;
    private boolean toggleSort = false;

    private static final ResourceLocation textureLocation = new ResourceLocation("modularsystems:textures/modular_storage_gui.png");
    private static final ResourceLocation craftingLocation = new ResourceLocation("modularsystems:textures/craftingSector.png");

    public GuiModularStorage(InventoryPlayer inventoryPlayer, TileEntityStorageCore tileEntity, EntityPlayer player, boolean hasArmorViewable, boolean hasCraftingVisable)
    {
        super(new ContainerModularStorage(inventoryPlayer, tileEntity, player, hasArmorViewable, hasCraftingVisable));
        this.core = tileEntity;
        this.inventory = inventoryPlayer;
        this.armorViewable = hasArmorViewable;
        this.hasCrafting = hasCraftingVisable;
        this.thePlayer = player;
        this.xSize = 289;
        this.ySize = 221;
        this.allowUserInput = true;
        statsPanel = new StatisticsPanel(0, 0);

        statsPanel.addNode(240, 224, GuiColor.BLACK + String.valueOf(core.inventoryRows * 11));

    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.containerModularStorage = ((ContainerModularStorage)this.inventorySlots);
        for (int i = 0; i < this.core.inventoryRows * 11; ++i)
        {
            ItemStack is = this.core.getStackInSlot(i);

            this.itemList.add(is);
        }
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        if(containerModularStorage.storageCore.hasSpecificUpgrade(Reference.SORTING_STORAGE_EXPANSION))
        {
            this.buttonList.add(new TexturedButton(0, x + xSize - 34, y + 5, 10, 10, 10, 0));
            this.buttonList.add(new TexturedButton(1, x + xSize - 46, y + 5, 10, 10, 20, 0));
        }
    }

    @Override
    public void actionPerformed(GuiButton button)
    {
        switch(button.id)
        {
            case 0 :
                StorageSortPacket packet = new StorageSortPacket(0);
                ModularSystems.packetPipeline.sendToServer(packet);
                break;
            case 1 :
                StorageSortPacket packet1 = new StorageSortPacket(1);
                ModularSystems.packetPipeline.sendToServer(packet1);
                break;
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

    @Override
    public void handleMouseInput()
    {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();
        if (i != 0 && this.needsScrollBars())
        {
            if(ModularSystems.nei != null && this.isInMainArea)
                return;

            int j = this.containerModularStorage.storageCore.inventoryRows - 6;

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

            this.containerModularStorage.scrollTo(this.currentScroll);
            updateScreen();
        }
    }

    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);
        if(par3 == 2 && containerModularStorage.storageCore.hasSpecificUpgrade(Reference.SORTING_STORAGE_EXPANSION))
        {
            if(toggleSort)
            {
                StorageSortPacket packet = new StorageSortPacket(0);
                ModularSystems.packetPipeline.sendToServer(packet);
                toggleSort = false;
            }
            else
            {
                StorageSortPacket packet1 = new StorageSortPacket(1);
                ModularSystems.packetPipeline.sendToServer(packet1);
                toggleSort = true;
            }

        }
    }
    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        super.drawScreen(par1, par2, par3);

        boolean flag = Mouse.isButtonDown(0);
        int k = this.guiLeft;
        int l = this.guiTop;
        int i1 = k + 270;
        int j1 = l + 18;
        int k1 = i1 + 12;
        int l1 = j1 + 112;

        if((par1 < k + 60) || (par1 > k + 60 && par1 < i1 && par2 < j1) || (par1 > k + 60 && par1 < i1 && par2 > l + 229) || (par1 > k1))
            this.isInMainArea = false;
        else
            this.isInMainArea = true;

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

                this.containerModularStorage.scrollTo(this.currentScroll);
            }
        }
        this.updateScreen();

        if(VersionHelper.getResult() == VersionHelper.OUTDATED)
        {
            int var5 = (this.width - xSize) / 2;
            int var6 = (this.height - this.ySize) / 2;
            if(par1 >= 60 + var5 && par2 >= 0 + var6 && par1 <= 76 + var5 && par2 <= 16 + var6)
            {
                List temp = Arrays.asList(Strings.UPDATE_TOOLTIP);
                drawHoveringText(temp, par1, par2, fontRendererObj);
            }
        }
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
        String invTitle = "Modular Storage";
        fontRendererObj.drawString(invTitle, (229 / 2 - fontRendererObj.getStringWidth(invTitle) / 2) + 60, 6, 4210752);
        fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 86, 130, 4210752);

        statsPanel.drawText(fontRendererObj);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(textureLocation);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        this.drawTexturedModalRect(x + 60, y, 0, 0, 229, ySize);
        if(this.armorViewable)
            this.drawTexturedModalRect(x + 254, y + 138, 230, 161, 26, 79);

        int i1 = this.guiLeft + 270;
        int k = this.guiTop + 18;
        int l = k + 112;
        if(this.needsScrollBars())
            this.drawTexturedModalRect(i1, k + (int)((float)(l - k - 17) * this.currentScroll), 232, 241, 12, 15);
        else
            this.drawTexturedModalRect(i1, k + (int)((float)(l - k - 17) * this.currentScroll), 244, 241, 12, 15);

        this.mc.getTextureManager().bindTexture(craftingLocation);
        if(this.hasCrafting)
            this.drawTexturedModalRect(x, y + 112, 0, 0, 83, 109);

        statsPanel.drawBackground(this.mc.getTextureManager(), width, height, xSize, ySize);
    }
}
