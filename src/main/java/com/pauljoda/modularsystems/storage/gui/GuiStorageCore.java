package com.pauljoda.modularsystems.storage.gui;

import com.pauljoda.modularsystems.storage.container.ContainerStorageCore;
import com.teambr.bookshelf.client.gui.GuiBase;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/23/2015
 */
public class GuiStorageCore extends GuiBase<ContainerStorageCore> {
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
    public void addComponents() {

    }
}
