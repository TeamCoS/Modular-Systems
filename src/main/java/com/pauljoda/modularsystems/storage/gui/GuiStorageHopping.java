package com.pauljoda.modularsystems.storage.gui;

import com.pauljoda.modularsystems.storage.tiles.TileStorageHopping;
import com.teambr.bookshelf.client.gui.GuiBase;
import com.teambr.bookshelf.client.gui.component.control.GuiComponentSlider;
import com.teambr.bookshelf.inventory.ContainerGeneric;
import com.teambr.bookshelf.manager.PacketManager;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/27/2015
 */
public class GuiStorageHopping extends GuiBase<ContainerGeneric> {
    protected TileStorageHopping hopper;
    /**
     * Constructor for All Guis
     */
    public GuiStorageHopping(TileStorageHopping tile) {
        super(new ContainerGeneric(), 100, 50, "inventory.storageHopping.title");
        hopper = tile;
        addComponents();
    }

    @Override
    public void addComponents() {
        if(hopper != null) {
            components.add(new GuiComponentSlider<Integer>(10, 25, 80, GuiComponentSlider.generateNumberList(0, 10), hopper.range) {
                @Override
                public void onValueChanged(Integer value) {
                    hopper.range = value;
                    PacketManager.updateTileWithClientInfo(hopper);
                }
            });
        }
    }
}
