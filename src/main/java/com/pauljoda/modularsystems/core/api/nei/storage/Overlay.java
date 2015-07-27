package com.pauljoda.modularsystems.core.api.nei.storage;

import codechicken.nei.recipe.DefaultOverlayHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;

public class Overlay extends DefaultOverlayHandler {
    private int offsetX;
    private int offsetY;
    public Overlay(int x, int y) {
        super(x, y);
        this.offsetX = x;
        this.offsetY = y;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public boolean canMoveFrom(Slot slot, GuiContainer gui) {
        return true;
    }
}
