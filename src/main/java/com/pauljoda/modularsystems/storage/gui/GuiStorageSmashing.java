package com.pauljoda.modularsystems.storage.gui;

import com.pauljoda.modularsystems.storage.container.ContainerStorageSmashing;
import com.teambr.bookshelf.client.gui.GuiBase;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/27/2015
 */
public class GuiStorageSmashing extends GuiBase<ContainerStorageSmashing> {
    /**
     * Constructor for All Guis
     *
     * @param container Inventory Container
     */
    public GuiStorageSmashing(ContainerStorageSmashing container) {
        super(container, 175, 165, "inventory.storageSmashing.title");
    }

    @Override
    public void addComponents() {}
}
