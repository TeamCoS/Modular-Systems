package com.pauljoda.modularsystems.storage.gui;

import com.pauljoda.modularsystems.storage.container.ContainerStorageIO;
import com.pauljoda.modularsystems.storage.tiles.TileStorageIO;
import com.teambr.bookshelf.client.gui.GuiBase;
import com.teambr.bookshelf.client.gui.component.control.GuiComponentCheckBox;
import com.teambr.bookshelf.manager.PacketManager;
import net.minecraft.util.StatCollector;

import java.util.Collections;
import java.util.List;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/29/2015
 */
public class GuiStorageIO extends GuiBase<ContainerStorageIO> {
    protected TileStorageIO io;
    /**
     * Constructor for All Guis
     *
     * @param container Inventory Container
     */
    public GuiStorageIO(ContainerStorageIO container, TileStorageIO tile) {
        super(container, 175, 180, "inventory.storageIO.title");
        io = tile;
        addComponents();
    }

    @Override
    public void addComponents() {
        if(io != null) {
            components.add(new GuiComponentCheckBox(8, 15, "inventory.storageIO.canImport", io.isCanImport()) {
                @Override
                public void setValue(boolean bool) {
                    io.setCanImport(bool);
                    PacketManager.updateTileWithClientInfo(io);
                }

                @Override
                public List<String> getDynamicToolTip(int x, int y) {
                    return Collections.singletonList(StatCollector.translateToLocal("inventory.storageIO.canImport.tip"));
                }
            });
            components.add(new GuiComponentCheckBox(8, 25, "inventory.storageIO.importDamage", io.isImportDamage()) {
                @Override
                public void setValue(boolean bool) {
                    io.setImportDamage(true);
                    PacketManager.updateTileWithClientInfo(io);
                }

                @Override
                public List<String> getDynamicToolTip(int x, int y) {
                    return Collections.singletonList(StatCollector.translateToLocal("inventory.storageIO.importDamage.tip"));
                }
            });
            components.add(new GuiComponentCheckBox(75, 15, "inventory.storageIO.importOre", io.isImportOreDict()) {
                @Override
                public void setValue(boolean bool) {
                    io.setImportOreDict(bool);
                    PacketManager.updateTileWithClientInfo(io);
                }

                @Override
                public List<String> getDynamicToolTip(int x, int y) {
                    return Collections.singletonList(StatCollector.translateToLocal("inventory.storageIO.importOre.tip"));
                }
            });

            components.add(new GuiComponentCheckBox(8, 58, "inventory.storageIO.canExport", io.isCanExport()) {
                @Override
                public void setValue(boolean bool) {
                    io.setCanExport(bool);
                    PacketManager.updateTileWithClientInfo(io);
                }

                @Override
                public List<String> getDynamicToolTip(int x, int y) {
                    return Collections.singletonList(StatCollector.translateToLocal("inventory.storageIO.canExport.tip"));
                }
            });
            components.add(new GuiComponentCheckBox(8, 68, "inventory.storageIO.exportDamage", io.isExportDamage()) {
                @Override
                public void setValue(boolean bool) {
                    io.setExportDamage(bool);
                    PacketManager.updateTileWithClientInfo(io);
                }

                @Override
                public List<String> getDynamicToolTip(int x, int y) {
                    return Collections.singletonList(StatCollector.translateToLocal("inventory.storageIO.exportDamage.tip"));
                }
            });
            components.add(new GuiComponentCheckBox(75, 58, "inventory.storageIO.exportOre", io.isExportOreDict()) {
                @Override
                public void setValue(boolean bool) {
                    io.setExportOreDict(bool);
                    PacketManager.updateTileWithClientInfo(io);
                }

                @Override
                public List<String> getDynamicToolTip(int x, int y) {
                    return Collections.singletonList(StatCollector.translateToLocal("inventory.storageIO.exportOre.tip"));
                }
            });
        }
    }
}
