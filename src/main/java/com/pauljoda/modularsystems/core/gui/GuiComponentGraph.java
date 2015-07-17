package com.pauljoda.modularsystems.core.gui;

import com.pauljoda.modularsystems.core.calculations.Calculation;
import com.teambr.bookshelf.client.gui.component.BaseComponent;
import com.teambr.bookshelf.client.gui.component.NinePatchRenderer;
import com.teambr.bookshelf.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class GuiComponentGraph extends BaseComponent {
    protected static final int u = 0;
    protected static final int v = 80;

    protected int width;
    protected int height;

    protected Calculation equation;
    protected int xMax;

    NinePatchRenderer renderer = new NinePatchRenderer(u, v, 3);

    public GuiComponentGraph(int x, int y, int w, int h, Calculation calc) {
        super(x, y);
        width = w;
        height = h;
        equation = calc;
        adjustXAxis();
    }

    @Override
    public void initialize() {}

    @Override
    public void render(int guiLeft, int guiTop) {
        GL11.glPushMatrix();

        GL11.glTranslated(xPos, yPos, 0);

        RenderUtils.bindGuiComponentsSheet();

        renderer.render(this, 0, 0, width, height);
        drawLine(1, convertYToGraph(0), width - 1, convertYToGraph(0), new Color(100, 100, 100, 50));
        for(int x = 1; x <= xMax; x++)
            drawLine(convertXToGraph(x - 1), convertYToGraph(equation.F(x - 1)), convertXToGraph(x), convertYToGraph(equation.F(x)), new Color(0, 0, 0));


        //Render ceiling
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        GL11.glPushMatrix();
        GL11.glScaled(0.5, 0.5, 1);
        GL11.glTranslated(-fontRenderer.getStringWidth(String.valueOf(equation.getCeiling())) - 3, 0, 0);
        fontRenderer.drawString(String.valueOf(equation.getCeiling()), 0, 0, 0x000000);
        GL11.glPopMatrix();

        //Render Floor
        GL11.glPushMatrix();
        GL11.glScaled(0.5, 0.5, 1);
        GL11.glTranslated(-fontRenderer.getStringWidth(String.valueOf(equation.getFloor())) - 3, (height * 2) - 10, 0);
        fontRenderer.drawString(String.valueOf(equation.getFloor()), 0, 0, 0x000000);
        GL11.glPopMatrix();

        //Render X
        GL11.glPushMatrix();
        GL11.glScaled(0.5, 0.5, 1);
        GL11.glTranslated((width * 2) - fontRenderer.getStringWidth(String.valueOf(xMax)), (height * 2), 0);
        fontRenderer.drawString(String.valueOf(xMax), 0, 0, 0x000000);
        GL11.glPopMatrix();


        GL11.glPopMatrix();
    }

    @Override
    public void renderOverlay(int guiLeft, int guiTop) {}

    private double convertXToGraph(double x) {
        return (x * (width - 2)) / xMax;
    }

    private double convertYToGraph(double y) {
        return Math.max(1, Math.min(height - 2, ((equation.getCeiling() - y) * (height - 2)) / Math.abs(equation.getFloor() - equation.getCeiling())));
    }

    private void adjustXAxis() {
        int x;
        for(x = 0; x <= 1000; x++) {
            if(equation.F_NoClamp(x) > equation.getCeiling())
                break;
            else if(equation.F_NoClamp(x) < equation.getFloor())
                break;
        }
        xMax = x;
    }

    public Calculation getEquation() {
        return equation;
    }

    public void setEquation(Calculation other) {
        this.equation = other;
        adjustXAxis();
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public static void drawLine(double x1, double y1, double x2, double y2, Color color) {
        GL11.glPushMatrix();
        Minecraft mc = Minecraft.getMinecraft();
        int scale = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight).getScaleFactor();
        RenderUtils.setColor(color);
        GL11.glLineWidth(scale * 1.3F);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2d(x1, y1);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
        GL11.glPopMatrix();
    }
}
