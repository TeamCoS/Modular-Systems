package com.pauljoda.modularsystems.core.gui;

import com.pauljoda.modularsystems.core.tiles.DummyIO;
import com.teambr.bookshelf.client.gui.GuiBase;
import com.teambr.bookshelf.client.gui.component.control.GuiComponentCheckBox;
import com.teambr.bookshelf.inventory.ContainerGeneric;
import com.teambr.bookshelf.manager.PacketManager;

public class GuiIO extends GuiBase<ContainerGeneric> {
    protected DummyIO dummy;
    public GuiIO(DummyIO d, ContainerGeneric container, int width, int height, String name) {
        super(container, width, height, name);
        dummy = d;
        addComponents();
    }

    @Override
    public void addComponents() {
        if (dummy != null) {
            components.add(new GuiComponentCheckBox(20, 20, "inventory.io.import", dummy.input) {
                @Override
                public void setValue(boolean bool) {
                    dummy.setInput(bool);
                    PacketManager.updateTileWithClientInfo(dummy);
                }
            });

            components.add(new GuiComponentCheckBox(20, 35, "inventory.io.export", dummy.output) {
                @Override
                public void setValue(boolean bool) {
                    dummy.setOutput(bool);
                    PacketManager.updateTileWithClientInfo(dummy);
                }
            });

            components.add(new GuiComponentCheckBox(20, 50, "inventory.io.auto", dummy.auto) {
                @Override
                public void setValue(boolean bool) {
                    dummy.setAuto(bool);
                    PacketManager.updateTileWithClientInfo(dummy);
                }
            });
        }
    }
}
