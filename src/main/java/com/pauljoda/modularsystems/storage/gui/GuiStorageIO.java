package com.pauljoda.modularsystems.storage.gui;

import com.pauljoda.modularsystems.core.managers.BlockManager;
import com.pauljoda.modularsystems.storage.container.ContainerStorageIO;
import com.pauljoda.modularsystems.storage.tiles.TileStorageIO;
import com.teambr.bookshelf.client.gui.GuiBase;
import com.teambr.bookshelf.client.gui.component.BaseComponent;
import com.teambr.bookshelf.client.gui.component.control.GuiComponentButton;
import com.teambr.bookshelf.client.gui.component.control.GuiComponentCheckBox;
import com.teambr.bookshelf.client.gui.component.display.GuiComponentText;
import com.teambr.bookshelf.client.gui.component.display.GuiTabCollection;
import com.teambr.bookshelf.manager.PacketManager;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.awt.*;
import java.util.ArrayList;
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
        addRightTabs(rightTabs);
    }

    @Override
    public void addComponents() {
        components.add(new GuiComponentText("inventory.storageIO.import", 8, 25));
        components.add(new GuiComponentText("inventory.storageIO.export", 8, 68));
    }

    @Override
    public void addRightTabs(GuiTabCollection tabs) {
        if(this.io != null) {
            List<BaseComponent> component = new ArrayList<>();
            component.add(new GuiComponentText("inventory.storageIO.importControls", 26, 6, 0xFFCC00));
            component.add(new GuiComponentCheckBox(6, 30, "inventory.storageIO.canImport", io.isCanImport()) {
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
            component.add(new GuiComponentCheckBox(6, 40, "inventory.storageIO.importDamage", io.isImportDamage()) {
                @Override
                public void setValue(boolean bool) {
                    io.setImportDamage(bool);
                    PacketManager.updateTileWithClientInfo(io);
                }

                @Override
                public List<String> getDynamicToolTip(int x, int y) {
                    return Collections.singletonList(StatCollector.translateToLocal("inventory.storageIO.importDamage.tip"));
                }
            });
            component.add(new GuiComponentCheckBox(6, 50, "inventory.storageIO.importNBT", io.isImportNBT()) {
                @Override
                public void setValue(boolean bool) {
                    io.setImportNBT(bool);
                    PacketManager.updateTileWithClientInfo(io);
                }

                @Override
                public List<String> getDynamicToolTip(int x, int y) {
                    return Collections.singletonList(StatCollector.translateToLocal("inventory.storageIO.importNBT.tip"));
                }
            });
            component.add(new GuiComponentCheckBox(6, 60, "inventory.storageIO.importOre", io.isImportOreDict()) {
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
            component.add(new GuiComponentText("inventory.storageIO.listMode", 68, 30, 0x000000));
            component.add(new GuiComponentButton(71, 40, 40, 20, io.whiteListImport() ? "inventory.storageIO.white" : "inventory.storageIO.black") {
                @Override
                public void doAction() {
                    io.setWhiteListImport(!io.whiteListImport());
                    this.setText(io.whiteListImport() ? StatCollector.translateToLocal("inventory.storageIO.white") : StatCollector.translateToLocal("inventory.storageIO.black"));
                    PacketManager.updateTileWithClientInfo(io);
                }
            });
            tabs.addTab(component, 120, 85, new Color(61, 55, 255), new ItemStack(BlockManager.storageCore));
            component = new ArrayList<>();

            component.add(new GuiComponentText("inventory.storageIO.exportControls", 26, 6, 0xFFCC00));
            component.add(new GuiComponentCheckBox(6, 30, "inventory.storageIO.canExport", io.isCanExport()) {
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
            component.add(new GuiComponentCheckBox(6, 40, "inventory.storageIO.exportDamage", io.isExportDamage()) {
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
            component.add(new GuiComponentCheckBox(6, 50, "inventory.storageIO.exportNBT", io.isExportNBT()) {
                @Override
                public void setValue(boolean bool) {
                    io.setExportNBT(bool);
                    PacketManager.updateTileWithClientInfo(io);
                }

                @Override
                public List<String> getDynamicToolTip(int x, int y) {
                    return Collections.singletonList(StatCollector.translateToLocal("inventory.storageIO.exportNBT.tip"));
                }
            });
            component.add(new GuiComponentCheckBox(6, 60, "inventory.storageIO.exportOre", io.isExportOreDict()) {
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
            component.add(new GuiComponentText("inventory.storageIO.listMode", 68, 30, 0x000000));
            component.add(new GuiComponentButton(71, 40, 40, 20, io.whiteListExport() ? "inventory.storageIO.white" : "inventory.storageIO.black") {
                @Override
                public void doAction() {
                    io.setWhiteListExport(!io.whiteListExport());
                    this.setText(io.whiteListExport() ? StatCollector.translateToLocal("inventory.storageIO.white") : StatCollector.translateToLocal("inventory.storageIO.black"));
                    PacketManager.updateTileWithClientInfo(io);
                }
            });
            tabs.addTab(component, 120, 80, new Color(255, 86, 49), new ItemStack(Blocks.piston));
        }
    }
}
