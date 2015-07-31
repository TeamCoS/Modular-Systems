package com.pauljoda.modularsystems.storage.gui;

import com.pauljoda.modularsystems.core.ModularSystems;
import com.pauljoda.modularsystems.storage.container.ContainerStorageCore;
import com.pauljoda.modularsystems.storage.tiles.TileStorageCore;
import com.teambr.bookshelf.client.gui.GuiBase;
import com.teambr.bookshelf.client.gui.component.BaseComponent;
import com.teambr.bookshelf.client.gui.component.control.GuiComponentButton;
import com.teambr.bookshelf.client.gui.component.control.GuiComponentScrollBar;
import com.teambr.bookshelf.client.gui.component.control.GuiComponentTextBox;
import com.teambr.bookshelf.client.gui.component.control.GuiComponentTexturedButton;
import com.teambr.bookshelf.client.gui.component.display.GuiComponentText;
import com.teambr.bookshelf.client.gui.component.display.GuiTabCollection;
import com.teambr.bookshelf.helpers.GuiHelper;
import com.teambr.bookshelf.manager.PacketManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/23/2015
 */
public class GuiStorageCore extends GuiBase<ContainerStorageCore> {
    private boolean isInMainArea;
    private float currentScroll;

    public TileStorageCore core;

    private GuiComponentTextBox textBox;
    private GuiComponentScrollBar scrollBar;

    /**
     * Constructor for All Guis
     *
     * @param container Inventory Container
     */
    public GuiStorageCore(ContainerStorageCore container, TileStorageCore tile) {
        super(container, 250, 220, tile.getInventoryName());
        title.setXPos(8);
        core = tile;
        addRightTabs(rightTabs);
        addComponents();
    }

    @Override
    public void addComponents() {
        if(core != null) {
            components.add(textBox = new GuiComponentTextBox(95, 5, 150, 16) {
                @Override
                public void fieldUpdated(String value) {
                    inventory.keyTyped(value);
                }
            });

            textBox.getTextField().setVisible(core.hasSearchUpgrade());

            components.add(scrollBar = new GuiComponentScrollBar(227, 26, 108) {
                @Override
                public void onScroll(float position) {
                    inventory.scrollTo(position);
                    currentScroll = position;
                }
            });

            if(core.hasSortingUpgrade()) {
                components.add(new GuiComponentButton(3, 26, 20, 20, "S") {
                    @Override
                    public void doAction() {
                        core.sortInventory();
                        PacketManager.updateTileWithClientInfo(core);
                    }

                    @Override
                    public List<String> getDynamicToolTip(int mouseX, int mouseY) {
                        return Collections.singletonList(StatCollector.translateToLocal("inventory.storageCore.sort"));
                    }

                });
            }

            if(core.hasCraftingUpgrade()) {
                components.add(new GuiComponentTexturedButton(228, 147, 76, 247, 9, 9, 11, 11) {
                    @Override
                    public void doAction() {
                        clearCraftingGrid();
                    }

                    @Override
                    public List<String> getDynamicToolTip(int mouseX, int mouseY) {
                        return Collections.singletonList(StatCollector.translateToLocal("inventory.storageCore.clear"));
                    }
                });
            }
        }
    }

    public void clearCraftingGrid() {
        inventory.clearCraftingGrid();
        PacketManager.updateTileWithClientInfo(core);
        for(int i = 0; i < this.mc.thePlayer.inventory.getSizeInventory(); i++) {
            this.mc.playerController.sendSlotPacket(this.mc.thePlayer.inventory.getStackInSlot(i), i);
        }
    }

    @Override
    protected void keyTyped(char letter, int keyCode) {
        if(textBox != null && textBox.getTextField() != null && !textBox.getTextField().textboxKeyTyped(letter, keyCode)) {
            super.keyTyped(letter, keyCode);
        } else if(textBox != null && textBox.getTextField() != null){
            this.inventory.keyTyped(textBox.getValue());
        } else
            super.keyTyped(letter, keyCode);
    }

    protected void mouseClicked(int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);
        if(par3 == 2 && core != null && core.hasSortingUpgrade()) {
            core.sortInventory();
            PacketManager.updateTileWithClientInfo(core);
            GuiHelper.playButtonSound();
        }
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        int scrollDirection = Mouse.getEventDWheel();
        if(scrollDirection != 0 && inventory.storage.getInventoryRowCount() > 6) {
            //Since NEI messes with stuff, lets only scroll if its not there or we are outside the main area
            if(ModularSystems.nei != null && isInMainArea)
                return;

            int rowsAboveDisplay = this.inventory.storage.getInventoryRowCount() - 6;
            if(scrollDirection > 0)
                scrollDirection = 1;
            else
                scrollDirection = -1;

            this.currentScroll = (float)((double)this.currentScroll - (double)scrollDirection / (double)rowsAboveDisplay);

            if(currentScroll < 0.0F)
                currentScroll = 0.0F;
            else if(currentScroll > 1.0F)
                currentScroll = 1.0F;

            this.inventory.scrollTo(this.currentScroll);
            this.scrollBar.setPosition(this.currentScroll);
            updateScreen();
        }
        if(textBox != null && textBox.getTextField() != null && textBox.getTextField().isFocused())
            Keyboard.enableRepeatEvents(true);
        else
            Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void drawScreen(int x, int y, float f) {
        super.drawScreen(x, y, f);
        isInMainArea = GuiHelper.isInBounds(x, y, guiLeft + 35, guiTop + 17, guiLeft + 220, guiTop + 215);
    }

    /**
     * Adds the tabs to the right. Overwrite this if you want tabs on your GUI
     * @param tabs List of tabs, put GuiTabs in
     */
    @Override
    public void addRightTabs(GuiTabCollection tabs) {
        if(core != null) {
            List<BaseComponent> children = new ArrayList<>();
            children.add(new GuiComponentText("Information", 26, 6, 0xFFCC00));
            children.add(new GuiComponentText("Size: ", 10, 23, 0xFFFFFF));
            children.add(new GuiComponentText(String.valueOf(core.getSizeInventory()) + " Slots", 20, 33, 0x777777));
            tabs.addTab(children, 100, 100, new Color(77, 75, 196), new ItemStack(Items.book, 1));
        }
    }
}
