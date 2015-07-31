package com.pauljoda.modularsystems.core.gui;

import com.pauljoda.modularsystems.core.tiles.DummyRedstoneInput;
import com.teambr.bookshelf.client.gui.GuiBase;
import com.teambr.bookshelf.client.gui.component.control.GuiComponentSlider;
import com.teambr.bookshelf.inventory.ContainerGeneric;
import com.teambr.bookshelf.manager.PacketManager;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/31/2015
 */
public class GuiRedstoneInput extends GuiBase<ContainerGeneric> {
    protected DummyRedstoneInput dummy;
    /**
     * Constructor for All Guis
     */
    public GuiRedstoneInput(DummyRedstoneInput tile) {
        super(new ContainerGeneric(), 100, 50, "inventory.dummyRedstoneIn.title");
        dummy = tile;
        addComponents();
    }

    @Override
    public void addComponents() {
        if(dummy != null) {
            components.add(new GuiComponentSlider<Integer>(10, 25, 80, GuiComponentSlider.generateNumberList(0, 16), dummy.setLevel) {
                @Override
                public void onValueChanged(Integer value) {
                    dummy.setLevel = value;
                    PacketManager.updateTileWithClientInfo(dummy);
                }
            });
        }
    }
}
