package com.pauljoda.modularsystems.storage.gui;

import com.teambr.bookshelf.client.gui.component.BaseComponent;
import com.teambr.bookshelf.client.gui.component.NinePatchRenderer;
import com.teambr.bookshelf.client.gui.component.control.GuiComponentButton;
import com.teambr.bookshelf.client.gui.component.control.GuiComponentScrollBar;
import com.teambr.bookshelf.client.gui.component.control.GuiComponentTextBox;
import com.teambr.bookshelf.helpers.GuiHelper;
import com.teambr.bookshelf.util.RenderUtils;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/25/2015
 */
public abstract class GuiComponentEditableList<T> extends BaseComponent {

    protected int width = 135;
    protected int height = 120;

    protected List<T> list;
    protected int currentPosition;

    protected GuiComponentTextBox inputBox;
    protected GuiComponentButton saveButton;
    protected GuiComponentScrollBar scrollBar;

    NinePatchRenderer renderer;

    public GuiComponentEditableList(int x, int y, List<T> starting) {
        super(x, y);
        list = starting;
    }

    /**
     * Called when the user selects to save the current input
     *
     * Convert the string to what you need here and save it to the list
     *
     * @param listToSaveTo What you should save the new entry into
     * @param valuePassed What was in the text field when the user pressed save
     */
    public abstract void valueSaved(List<T> listToSaveTo, String valuePassed);

    /**
     * This is where you should convert the object into what you want to display
     *
     * toString() is not always what you want, so here you can convert things
     * @param object The object to convert to string
     * @return What you want to display
     */
    public abstract String convertObjectToString(T object);

    @Override
    public void initialize() {
        renderer = new NinePatchRenderer(0, 80, 3);

        inputBox = new GuiComponentTextBox(xPos, yPos, 100, 16) {
            @Override
            public void fieldUpdated(String value) {}
        };

        saveButton = new GuiComponentButton(xPos + 105, yPos - 3, 30, 20, StatCollector.translateToLocal("inventory.blockValuesConfig.save")) {
            @Override
            public void doAction() {
                valueSaved(list, inputBox.getValue());
                inputBox.getTextField().setText("");
                GuiHelper.playButtonSound();
            }
        };

        scrollBar = new GuiComponentScrollBar(xPos + 121, yPos + 20, 100) {
            @Override
            public void onScroll(float position) {
                currentPosition = (int)position * list.size();
            }
        };
    }

    /**
     * Called when the mouse is pressed
     * @param mouseX Mouse X Position
     * @param mouseY Mouse Y Position
     * @param button Mouse Button
     */
    public void mouseDown(int mouseX, int mouseY, int button) {
        inputBox.mouseDown(mouseX, mouseY, button);
        if(scrollBar.isMouseOver(mouseX, mouseY))
            scrollBar.mouseDown(mouseX, mouseY, button);
        if(saveButton.isMouseOver(mouseX, mouseY))
            saveButton.mouseDown(mouseX, mouseY, button);
    }

    /**
     * Called when the user drags the component
     * @param x Mouse X Position
     * @param y Mouse Y Position
     * @param button Mouse Button
     * @param time How long
     */
    public void mouseDrag(int x, int y, int button, long time) {
        inputBox.mouseDrag(x, y, button, time);
        if(scrollBar.isMouseOver(x, y))
            scrollBar.mouseDrag(x, y, button, time);
        if(saveButton.isMouseOver(x, y))
            saveButton.mouseDrag(x, y, button, time);
    }

    /**
     * Called when the mouse button is over the component and released
     * @param x Mouse X Position
     * @param y Mouse Y Position
     * @param button Mouse Button
     */
    public void mouseUp(int x, int y, int button) {
        inputBox.mouseUp(x, y, button);
        if(scrollBar.isMouseOver(x, y))
            scrollBar.mouseUp(x, y, button);
        if(saveButton.isMouseOver(x, y))
            saveButton.mouseUp(x, y, button);
    }

    /**
     * Used when a key is pressed
     * @param letter The letter
     * @param keyCode The code
     */
    public void keyTyped(char letter, int keyCode) {
        inputBox.keyTyped(letter, keyCode);
    }

    @Override
    public void render(int guiLeft, int guiTop) {
        GL11.glPushMatrix();

        RenderUtils.bindGuiComponentsSheet();
        RenderUtils.prepareRenderState();

        renderer.render(this, xPos -1, yPos + 20, 120, 100, new Color(100, 100, 100));
        inputBox.render(guiLeft, guiTop);
        saveButton.render(guiLeft, guiTop);
        scrollBar.render(guiLeft, guiTop);

        RenderUtils.restoreRenderState();
        GL11.glPopMatrix();
    }

    @Override
    public void renderOverlay(int guiLeft, int guiTop) {
        GL11.glPushMatrix();

        RenderUtils.bindGuiComponentsSheet();
        RenderUtils.prepareRenderState();

        inputBox.renderOverlay(guiLeft, guiTop);
        saveButton.renderOverlay(guiLeft, guiTop);
        scrollBar.renderOverlay(guiLeft, guiTop);

        RenderUtils.restoreRenderState();
        GL11.glPopMatrix();
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public GuiComponentScrollBar getScrollBar() {
        return scrollBar;
    }

    public GuiComponentTextBox getInputBox() {
        return inputBox;
    }
}
