package com.teamcos.modularsystems.core.gui;

import com.teamcos.modularsystems.core.ModularSystems;
import com.teamcos.modularsystems.core.helper.VersionHelper;
import com.teamcos.modularsystems.core.lib.Strings;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

import java.util.Arrays;


public class GuiBase extends GuiContainer
{
    private int arrowXMin;
    private int arrowXMax;
    private int arrowYMin;
    private int arrowYMax;
    private Container parent;

    public GuiBase(Container container,int xMin, int yMin, int xMax, int yMax, int xSize, int ySize)
    {
        super(container);
        this.parent = container;
        this.arrowXMin = xMin;
        this.arrowXMax = xMax;
        this.arrowYMin = yMin;
        this.arrowYMax = yMax;
        this.xSize = xSize;
        this.ySize = ySize;
    }

    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        super.drawScreen(par1, par2, par3);
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        if(isInBounds(par1, par2, var5 + arrowXMin, var6 + arrowYMin, var5 + arrowXMax, var6 + arrowYMax) && ModularSystems.nei != null)
        {
            renderToolTip(par1, par2, "Recipes");
        }

        if(VersionHelper.getResult() == VersionHelper.OUTDATED)
        {
            if(par1 >= 0 + var5 && par2 >= 0 + var6 && par1 <= 16 + var5 && par2 <= 16 + var6)
            {
                renderToolTip(par1, par2, Strings.UPDATE_TOOLTIP);
            }
        }
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;

        if(isInBounds(par1, par2, var5 + arrowXMin, var6 + arrowYMin, var5 + arrowXMax, var6 + arrowYMax) && ModularSystems.nei != null)
        {
            ModularSystems.nei.onArrowClicked(parent);
        }
    }
    public boolean isInBounds(int x, int y, int a, int b, int c, int d)
    {
        return (x >= a && x <= c && y >= b && y <=d);
    }
    public void renderToolTip(int x, int y, String string)
    {
        java.util.List temp = Arrays.asList(string);
        drawHoveringText(temp, x, y, fontRendererObj);
    }
    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_){}
}
