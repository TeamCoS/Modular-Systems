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
                if(mouseX >= xPos + 50 && mouseX < xPos + 60 && mouseY >= yPos && mouseY < yPos + 10) {
                    isSelected = !isSelected;
                    setValue(isSelected);
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

        GL11.glPushMatrix();
        GL11.glTranslated(50, 0, 0);
        drawTexturedModalRect(0, 0, isSelected ? 40 : 48, 0, 8, 8);
        GL11.glPopMatrix();

        //Draw Text
        Minecraft.getMinecraft().fontRenderer.drawString(text, 0, 0, 0x000000);

        GL11.glPopMatrix();
    }

    @Override
    public void renderOverlay(int i, int i1) {}

    @Override
    public int getWidth() {
        return 58;
    }

    @Override
    public int getHeight() {
        return 8;
    }
}
