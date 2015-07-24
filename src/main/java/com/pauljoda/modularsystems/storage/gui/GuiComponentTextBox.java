package com.pauljoda.modularsystems.storage.gui;

import com.teambr.bookshelf.client.gui.component.BaseComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.opengl.GL11;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/23/2015
 */
public abstract class GuiComponentTextBox extends BaseComponent {

    protected GuiTextField textField;
    protected int width, height;

    /**
     * Constructor for the text field
     * @param x The X Position of the field
     * @param y The Y Position of the field
     * @param width The width
     * @param height The height
     */
    public GuiComponentTextBox(int x, int y, int width, int height) {
        super(x, y);
        textField = new GuiTextField(Minecraft.getMinecraft().fontRenderer, x, y, width, height);
        this.width = width;
        this.height = height;
    }

    /**
     * Called when the value has changed
     * @param value The current text in the field
     */
    public abstract void fieldUpdated(String value);

    /**
     * Get the current text
     * @return What is in the field
     */
    public String getValue() {
        return textField.getText();
    }

    /**
     * Set the value in the field
     * @param str The new string
     */
    public void setValue(String str) {
        textField.setText(str);
    }

    /**
     * Get the text field
     * @return The text field (vanilla version)
     */
    public GuiTextField getTextField() {
        return textField;
    }

    @Override
    public void initialize() {}

    /**
     * Called when the mouse is pressed
     * @param mouseX Mouse X Position
     * @param y Mouse Y Position
     * @param button Mouse Button
     */
    public void mouseDown(int mouseX, int y, int button) {
        textField.mouseClicked(mouseX, y, button);
        if(button == 1 && textField.isFocused()) {
            textField.setText("");
            fieldUpdated(textField.getText());
        }
    }

    /**
     * Used when a key is pressed
     * @param letter The letter
     * @param keyCode The code
     */
    public void keyTyped(char letter, int keyCode) {
        if(textField.isFocused()) {
            textField.textboxKeyTyped(letter, keyCode);
            fieldUpdated(textField.getText());
        }
    }

    @Override
    public void render(int guiLeft, int guiTop) {
        GL11.glPushMatrix();

        textField.drawTextBox();

        GL11.glDisable(GL11.GL_ALPHA_TEST);

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

    @Override
    public boolean isMouseOver(int mouseX, int mouseY) {
        return true;
    }
}
