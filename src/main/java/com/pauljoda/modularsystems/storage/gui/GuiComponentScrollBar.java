package com.pauljoda.modularsystems.storage.gui;

import com.teambr.bookshelf.client.gui.component.BaseComponent;
import com.teambr.bookshelf.client.gui.component.NinePatchRenderer;
import com.teambr.bookshelf.util.RenderUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/24/2015
 */
public abstract class GuiComponentScrollBar extends BaseComponent {

    protected int height;
    protected int currentPosition;
    protected int maxRange;

    protected boolean isMoving;

    protected int u = 78;
    protected int v = 0;
    NinePatchRenderer renderer;


    public GuiComponentScrollBar(int x, int y, int height) {
        super(x, y);
        this.height = height;
        this.currentPosition = 0;
        maxRange = (height - 2 - 15);
    }

    /**
     * Called when the scroll box has moved.
     * @param position The position, 0 - 1 of how far along it is
     */
    public abstract void onScroll(float position);

    /**
     * Set the position of the box
     * @param position The position, 0 - 1 of how far along it is
     */
    public void setPosition(float position) {
        this.currentPosition = (int) (maxRange * position);
    }

    /**
     * Called when the mouse is pressed
     * @param mouseX Mouse X Position
     * @param y Mouse Y Position
     * @param button Mouse Button
     */
    public void mouseDown(int mouseX, int y, int button) {
        isMoving = true;
        currentPosition = (y - yPos) - 7;
        if(currentPosition > maxRange)
            currentPosition = maxRange;
        else if(currentPosition < 0)
            currentPosition = 0;
        onScroll((float) currentPosition / maxRange);
    }

    /**
     * Called when the user drags the component
     * @param x Mouse X Position
     * @param y Mouse Y Position
     * @param button Mouse Button
     * @param time How long
     */
    public void mouseDrag(int x, int y, int button, long time) {
        currentPosition = (y - yPos) - 7;
        if(currentPosition > maxRange)
            currentPosition = maxRange;
        else if(currentPosition < 0)
            currentPosition = 0;
        onScroll((float) currentPosition / maxRange);
    }

    /**
     * Called when the mouse button is over the component and released
     * @param x Mouse X Position
     * @param y Mouse Y Position
     * @param button Mouse Button
     */
    public void mouseUp(int x, int y, int button) {
        isMoving = false;
        onScroll((float) currentPosition / maxRange);
    }

    @Override
    public void initialize() {
        isMoving = false;
        renderer = new NinePatchRenderer(0, 80, 3);
    }

    @Override
    public void render(int guiLeft, int guiTop) {
        if(currentPosition > maxRange)
            currentPosition = maxRange;

        GL11.glPushMatrix();

        GL11.glTranslated(xPos, yPos, 0);
        RenderUtils.prepareRenderState();

        renderer.render(this, 0, 0, 14, height);

        GL11.glTranslated(1, currentPosition + 1, 0);
        RenderUtils.bindGuiComponentsSheet();
        if(isMoving && !Mouse.isButtonDown(0))
            isMoving = false;
        drawTexturedModalRect(0, 0, isMoving ? u + 12 : u, v, 12, 15);

        RenderUtils.restoreRenderState();
        GL11.glPopMatrix();
    }

    @Override
    public void renderOverlay(int guiLeft, int guiTop) {}

    @Override
    public int getWidth() {
        return 14;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
