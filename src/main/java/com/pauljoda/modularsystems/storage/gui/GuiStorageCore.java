package com.pauljoda.modularsystems.storage.gui;

import com.pauljoda.modularsystems.core.ModularSystems;
import com.pauljoda.modularsystems.storage.container.ContainerStorageCore;
import com.teambr.bookshelf.client.gui.GuiBase;
import com.teambr.bookshelf.helpers.GuiHelper;
import org.lwjgl.input.Mouse;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/23/2015
 */
public class GuiStorageCore extends GuiBase<ContainerStorageCore> {
    private boolean isInMainArea;
    private float currentScroll;

    /**
     * Constructor for All Guis
     *
     * @param container Inventory Container
     * @param width     XSize
     * @param height    YSize
     * @param name      The inventory title
     */
    public GuiStorageCore(ContainerStorageCore container, int width, int height, String name) {
        super(container, width, height, name);
    }

    @Override
    public void addComponents() {}

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        int scrollDirection = Mouse.getEventDWheel();
        if(scrollDirection != 0 && inventory.storage.getInventoryRowCount() > 6) {
            //Since NEI messes with stuff, lets only scroll if its not there or we are outside the main area
            if(ModularSystems.nei != null && isInMainArea)
                return;

            int rowsAboveDisplay = this.inventory.storage.getInventoryRowCount() - 6;
            if(scrollDirection > 0)
                scrollDirection = 1;
            else
                scrollDirection = -1;

            this.currentScroll = (float)((double)this.currentScroll - (double)scrollDirection / (double)rowsAboveDisplay);

            if(currentScroll < 0.0F)
                currentScroll = 0.0F;
            else if(currentScroll > 1.0F)
                currentScroll = 1.0F;

            this.inventory.scrollTo(this.currentScroll);
            updateScreen();
        }
    }

    @Override
    public void drawScreen(int x, int y, float f) {
        super.drawScreen(x, y, f);
        isInMainArea = GuiHelper.isInBounds(x, y, guiLeft + 25, guiTop + 17, guiLeft + 220, guiTop + 205);
    }
}
