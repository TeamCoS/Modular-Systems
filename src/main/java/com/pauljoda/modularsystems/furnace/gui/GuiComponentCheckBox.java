package com.pauljoda.modularsystems.furnace.gui;

import com.teambr.bookshelf.client.gui.component.BaseComponent;
import com.teambr.bookshelf.client.gui.component.listeners.IMouseEventListener;
import com.teambr.bookshelf.util.RenderUtils;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public abstract class GuiComponentCheckBox extends BaseComponent {

    private String text;
    private boolean isSelected;

    public GuiComponentCheckBox(int x, int y, String label, boolean startValue) {
        super(x, y);
        text = label;
        isSelected = startValue;
    }

    public abstract void setValue(boolean bool);

    @Override
    public void initialize() {
        setMouseEventListener(new IMouseEventListener() {
            @Override
            public void onMouseDown(BaseComponent component, int mouseX, int mouseY, int button) {
                if(mouseX > xPos + 50 && mouseX < xPos - 60 && mouseY > yPos && mouseY < yPos + 10) {
                    isSelected = !isSelected;
                }
            }

            @Override
            public void onMouseUp(BaseComponent component, int mouseX, int mouseY, int button) {

            }

            @Override
            public void onMouseDrag(BaseComponent baseComponent, int i, int i1, int i2, long l) {

            }
        });
    }

    @Override
    public void render(int guiLeft, int guiTop) {
        GL11.glPushMatrix();

        GL11.glTranslated(guiLeft + xPos, guiTop + yPos, 0);
        RenderUtils.bindGuiComponentsSheet();

        //Draw Text
        Minecraft.getMinecraft().fontRenderer.drawString(text, 0, 0, 0x000000);

        //Render corners
        GL11.glPushMatrix();
        GL11.glTranslated(50, 0, 0);
        drawTexturedModalRect(0, 0, 40, 0, 1, 1);
        drawTexturedModalRect(0, 10, 40, 2, 1, 1);
        drawTexturedModalRect(10, 0, 42, 0, 1, 1);
        drawTexturedModalRect(10, 10, 42, 2, 1, 1);
        GL11.glPopMatrix();

        //Render top
        GL11.glPushMatrix();
        GL11.glTranslated(51, 0, 0);
        GL11.glScaled(8, 1, 1);
        drawTexturedModalRect(0, 0, 41, 0, 1, 1);
        GL11.glPopMatrix();

        //Render Bottom
        GL11.glPushMatrix();
        GL11.glTranslated(51, 10, 0);
        GL11.glScaled(8, 1, 1);
        drawTexturedModalRect(0, 0, 41, 2, 1, 1);
        GL11.glPopMatrix();

        GL11.glPopMatrix();
    }

    @Override
    public void renderOverlay(int i, int i1) {}

    @Override
    public int getWidth() {
        return 60;
    }

    @Override
    public int getHeight() {
        return 10;
    }
}
