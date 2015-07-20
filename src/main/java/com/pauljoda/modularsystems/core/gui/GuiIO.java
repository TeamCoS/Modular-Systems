package com.pauljoda.modularsystems.core.gui;

import com.pauljoda.modularsystems.core.tiles.DummyIO;
import com.teambr.bookshelf.client.gui.GuiBase;
import com.teambr.bookshelf.client.gui.component.control.GuiComponentCheckBox;
import com.teambr.bookshelf.inventory.ContainerGeneric;
import com.teambr.bookshelf.manager.PacketManager;

/**
 * The GUI class for the I/O ports
 */
public class GuiIO extends GuiBase<ContainerGeneric> {
    protected DummyIO dummy;

    /**
     * Creates the GUI
     * @param d The dummy tile
     * @param container Some container, doesn't really need anything fancy
     * @param width The width of the GUI
     * @param height The height of the GUI
     * @param name The title of the GUI
     */
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
