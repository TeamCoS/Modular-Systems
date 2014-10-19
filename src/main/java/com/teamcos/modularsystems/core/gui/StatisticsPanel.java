package com.teamcos.modularsystems.core.gui;

import com.teamcos.modularsystems.core.helper.VersionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
@SideOnly(Side.CLIENT)
public class StatisticsPanel extends Gui {

    private int xPos;
    private int yPos;
    private List<StatisticNode> stats;
    private static final ResourceLocation infoPane = new ResourceLocation("modularsystems:textures/stats.png");

    /**
     * Used to create a side panel on all Modular Systems
     * @param x Location on Screen for X
     * @param y Location on Screen for Y
     */
    public StatisticsPanel(int x, int y)
    {
        this.xPos = x;
        this.yPos = y;
        stats = new ArrayList<StatisticNode>();
    }

    public void addNode(int x, int y, String text)
    {
        stats.add(new StatisticNode(x, y, text));
    }

    public void updateNode(int pos, String string)
    {
        stats.get(pos).updateText(string);
    }

    public void drawText(FontRenderer fontRendererObj)
    {
        fontRendererObj.drawString("Stats:",  xPos + 5 , 6, 4210752);
        for(int i = 0; i < stats.size(); i++)
            fontRendererObj.drawString(stats.get(i).text, (xPos + 52) - fontRendererObj.getStringWidth(stats.get(i).text), 21 + (i * 16), 4210752);
    }

    public void drawBackground(TextureManager textureManager, int width, int height, int parentXSize, int parentYSize)
    {
        GL11.glColor4f(1f, 1f, 1f, 1f);

        int x = (width - parentXSize) / 2;
        int y = (height - parentYSize) / 2;

        textureManager.bindTexture(infoPane);
        drawTexturedModalRect(x + xPos, y, 0, 0, 56, 104);
        for(int i = 0; i < stats.size(); i++)
            drawTexturedModalRect(x + (xPos + 3), y + 16 + (i * 16), stats.get(i).iconX, stats.get(i).iconY, 16, 16);

        if(VersionHelper.getResult() == VersionHelper.OUTDATED)
        {
            if(xPos != 0)
                drawTexturedModalRect(x, y, 0, 240, 16, 16);
            else
                drawTexturedModalRect(x + 60, y, 0, 240, 16, 16); //Hard coding instance for modular storage. Sloppy but whatever
        }
    }


    //Abstract Class for Statistics
    public class StatisticNode
    {
        public int iconX;
        public int iconY;
        public String text;
        public StatisticNode(int iconX, int iconY, String text)
        {
            this.iconX = iconX;
            this.iconY = iconY;
            this.text = text;
        }

        public void updateText(String string)
        {
            this.text = string;
        }
    }
}
