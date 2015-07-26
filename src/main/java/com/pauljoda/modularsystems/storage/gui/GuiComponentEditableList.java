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

import java.util.List;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/25/2015
 */
public abstract class GuiComponentEditableList<T> extends BaseComponent {

    protected int width = 120;
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

    @Override
    public void initialize() {
        renderer = new NinePatchRenderer(0, 80, 3);

        inputBox = new GuiComponentTextBox(0, 0, 100, 16) {
            @Override
            public void fieldUpdated(String value) {}
        };

        saveButton = new GuiComponentButton(105, 0, 15, 16, StatCollector.translateToLocal("inventory.blockValuesConfig.save")) {
            @Override
            public void doAction() {
                valueSaved(list, inputBox.getValue());
                inputBox.getTextField().setText("");
                GuiHelper.playButtonSound();
            }
        };

        scrollBar = new GuiComponentScrollBar(110, 20, 100) {
            @Override
            public void onScroll(float position) {
                currentPosition = (int)position * list.size();
            }
        };
    }

    @Override
    public void render(int guiLeft, int guiTop) {
        GL11.glPushMatrix();

        GL11.glTranslated(xPos, yPos, 0);
        RenderUtils.bindGuiComponentsSheet();
        RenderUtils.prepareRenderState();

        renderer.render(this, 0, 20, 100, 100);
        inputBox.render(guiLeft, guiTop);
        saveButton.render(guiLeft, guiTop);
        scrollBar.render(guiLeft, guiTop);

        RenderUtils.restoreRenderState();
        GL11.glPopMatrix();
    }

    @Override
    public void renderOverlay(int guiLeft, int guiTop) {}

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
