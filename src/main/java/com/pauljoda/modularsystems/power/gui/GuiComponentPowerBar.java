package com.pauljoda.modularsystems.power.gui;

import com.teambr.bookshelf.client.gui.component.BaseComponent;
import com.teambr.bookshelf.util.RenderUtils;
import org.lwjgl.opengl.GL11;

public abstract class GuiComponentPowerBar extends BaseComponent {

    protected static final int u = 0;
    protected static final int v = 80;

    public GuiComponentPowerBar(int x, int y) {
        super(x, y);
    }

    public abstract int getEnergyPercent();

    @Override
    public void initialize() {

    }

    @Override
    public void render(int guiLeft, int guiTop) {
        GL11.glPushMatrix();

        GL11.glTranslated(guiLeft + xPos, guiTop + yPos, 0);
        RenderUtils.bindGuiComponentsSheet();

        drawTexturedModalRect(0, 0, u, v, getWidth(), getHeight());
        drawTexturedModalRect(0, 0, 19, v + (getEnergyPercent() - 100), getWidth(), getEnergyPercent());

        GL11.glPopMatrix();


    }

    @Override
    public void renderOverlay(int i, int i1) {

    }

    @Override
    public int getWidth() {
        return 18;
    }

    @Override
    public int getHeight() {
        return 74;
    }
}
