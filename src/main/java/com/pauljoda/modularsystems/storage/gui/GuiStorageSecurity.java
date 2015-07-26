package com.pauljoda.modularsystems.storage.gui;

import com.pauljoda.modularsystems.storage.tiles.TileStorageSecurity;
import com.teambr.bookshelf.client.gui.GuiBase;
import com.teambr.bookshelf.client.gui.component.control.GuiComponentCheckBox;
import com.teambr.bookshelf.inventory.ContainerGeneric;
import com.teambr.bookshelf.manager.PacketManager;

import java.util.List;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/25/2015
 */
public class GuiStorageSecurity extends GuiBase<ContainerGeneric> {
    protected TileStorageSecurity securityTile;

    public GuiStorageSecurity(TileStorageSecurity tile) {
        super(new ContainerGeneric(), 175, 150, "tile.modularsystems:storageSecurity.name");
        securityTile = tile;
        addComponents();
    }

    @Override
    public void addComponents() {
        if(securityTile != null && securityTile.getCore() != null) {
            components.add(new GuiComponentCheckBox(10, 20, "inventory.storageSecurity.lock", securityTile.getCore().isSecured()) {
                @Override
                public void setValue(boolean bool) {
                    securityTile.getCore().setIsSecured(bool);
                    PacketManager.updateTileWithClientInfo(securityTile.getCore());
                }
            });

            components.add(new GuiComponentEditableList<String>(5, 40, securityTile.getCore().getAllowedPlayers()) {
                @Override
                public void valueSaved(List<String> listToSaveTo, String valuePassed) {

                }
            });
        }
    }
}
