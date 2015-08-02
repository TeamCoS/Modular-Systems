package com.pauljoda.modularsystems.storage.gui;

import com.pauljoda.modularsystems.storage.container.ContainerStorageRemote;
import com.teambr.bookshelf.client.gui.GuiBase;

/**
 * Modular-Systems
 * Created by Dyonovan on 01/08/15
 */
public class GuiStorageRemote extends GuiBase<ContainerStorageRemote> {

    public GuiStorageRemote(ContainerStorageRemote container) {
        super(container, 175, 165, "inventory.storageRemote.title");
    }

    @Override
    public void addComponents() {

    }
}
