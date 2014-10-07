package com.pauljoda.modularsystems.furnace.gui;

import com.pauljoda.modularsystems.core.gui.GuiBase;
import com.pauljoda.modularsystems.core.gui.StatisticsPanel;
import com.pauljoda.modularsystems.core.lib.GuiColor;
import com.pauljoda.modularsystems.furnace.containers.ContainerModularFurnaceCrafter;
import com.pauljoda.modularsystems.furnace.tiles.TileEntityFurnaceCore;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
public class GuiModularFurnaceEnabled extends GuiBase
{
    private TileEntityFurnaceCore tileEntity;
    private StatisticsPanel statsPanel;

    private static final ResourceLocation field_110410_t = new ResourceLocation("modularsystems:textures/furnace_crafting.png");

    public GuiModularFurnaceEnabled(ContainerModularFurnaceCrafter container, TileEntityFurnaceCore tileEntity)
    {
        super(container, 112, 47, 133, 61, 175, 165);
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
        final String invTitle = " Modular Furnace";

        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        fontRendererObj.drawString(invTitle, xSize / 2 - fontRendererObj.getStringWidth(invTitle) / 20, 6, 4210752);

        fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);

        statsPanel.drawText(fontRendererObj);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1f, 1f, 1f, 1f);

        this.mc.getTextureManager().bindTexture(field_110410_t);

        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        int i1;

        if(tileEntity.isBurning())
        {
            i1 = tileEntity.getBurnTimeRemainingScaled(12);
            drawTexturedModalRect(x + 87, y + 49 + 12 - i1, 176, 12 - i1, 14, i1 + 2);
        }

        i1 = tileEntity.getCookProgressScaled(24);
        drawTexturedModalRect(x + 111, y + 46, 176, 14, i1 + 1, 16);

        statsPanel.drawBackground(this.mc.getTextureManager(), width, height, xSize, ySize);
    }
}
