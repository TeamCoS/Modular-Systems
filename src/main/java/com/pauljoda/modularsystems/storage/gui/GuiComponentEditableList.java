package com.pauljoda.modularsystems.storage.gui;

import com.teambr.bookshelf.client.gui.component.BaseComponent;
import com.teambr.bookshelf.client.gui.component.NinePatchRenderer;
import com.teambr.bookshelf.client.gui.component.control.GuiComponentButton;
import com.teambr.bookshelf.client.gui.component.control.GuiComponentScrollBar;
import com.teambr.bookshelf.client.gui.component.control.GuiComponentTextBox;
import com.teambr.bookshelf.helpers.GuiHelper;
import com.teambr.bookshelf.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
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
    protected int currentScroll;

    protected GuiComponentTextBox inputBox;
    protected GuiComponentButton saveButton;
    protected GuiComponentScrollBar scrollBar;

    protected T selected;

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
     * Called when the user selects to delete something, it is then up to you to remove things
     * @param listFrom What we are removing from
     * @param valueDeleted The value being deleted
     */
    public abstract void valueDeleted(List<T> listFrom, T valueDeleted);

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
                currentScroll = currentPosition = (int)(position * (this.getHeight() - 2));
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
        int selectionY = mouseY - yPos - 20;
        selectionY -= selectionY % 20;

        if(GuiHelper.isInBounds(mouseX, mouseY, 0, 60, 115, 160)) {
            if(list.size() > 0) {
                int test = (currentScroll) / (list.size()) + selectionY / 20;
                if(list.size() > test) {
                    selected = list.get(test);
                   GuiHelper.playButtonSound();
                }
            }
        }
        mouseY -= 60;
        if(GuiHelper.isInBounds(mouseX, mouseY, 115, selectionY, 130, selectionY + 20)) {
            if(selected != null) {
                valueDeleted(list, selected);
                GuiHelper.playButtonSound();
            }
        }
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
        if(keyCode == 28) {
            valueSaved(list, inputBox.getValue());
            inputBox.getTextField().setText("");
            GuiHelper.playButtonSound();
        } else
            inputBox.keyTyped(letter, keyCode);
    }

    @Override
    public void render(int guiLeft, int guiTop) {
        GL11.glPushMatrix();

        RenderUtils.bindGuiComponentsSheet();
        RenderUtils.prepareRenderState();

        renderer.render(this, xPos - 1, yPos + 20, 120, 100, new Color(100, 100, 100));
        RenderUtils.setColor(new Color(255, 255, 255));

        inputBox.render(guiLeft, guiTop);
        saveButton.render(guiLeft, guiTop);
        scrollBar.render(guiLeft, guiTop);

        if(list.size() > 0) {
            GL11.glTranslated(xPos + 3, yPos + 23, 0);

            int renderY = 0;
            int currentElement = currentScroll / (list.size());
            for (int i = currentElement; i < list.size(); i++) {
                if(selected != null && selected.equals(list.get(i)))
                    renderSelection(renderY);
                Minecraft.getMinecraft().fontRenderer.drawString(this.convertObjectToString(list.get(i)), 1, renderY + 3, 0xBBBBBB);
                RenderUtils.restoreRenderState();
                renderY += 20;
                if (renderY >= 81)
                    break; //outside where I feel we should render. Just leave
            }
        }

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

    private void renderSelection(int selectionY) {
        GL11.glPushMatrix();

        Minecraft mc = Minecraft.getMinecraft();
        int scale = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight).getScaleFactor();
        GL11.glLineWidth(scale * 1.3F);

        GL11.glTranslated(-3, -1, 5);
        RenderUtils.setColor(new Color(235, 235, 235));

        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2d(0, selectionY);
        GL11.glVertex2d(117, selectionY);
        GL11.glVertex2d(117, selectionY);
        GL11.glVertex2d(117, selectionY + 17);
        GL11.glVertex2d(117, selectionY + 17);
        GL11.glVertex2d(0, selectionY + 17);
        GL11.glVertex2d(1, selectionY + 17);
        GL11.glVertex2d(1, selectionY);

        RenderUtils.setColor(new Color(255, 10, 10));
        GL11.glVertex2d(99, selectionY + 3);
        GL11.glVertex2d(109, selectionY + 13);

        GL11.glVertex2d(99, selectionY + 13);
        GL11.glVertex2d(109, selectionY + 3);
        GL11.glEnd();

        RenderUtils.setColor(new Color(255, 255, 255));
        GL11.glPopMatrix();
    }
}
