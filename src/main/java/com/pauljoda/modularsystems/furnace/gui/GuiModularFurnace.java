package com.pauljoda.modularsystems.furnace.gui;

import com.pauljoda.modularsystems.furnace.container.ContainerModularFurnace;
import com.pauljoda.modularsystems.furnace.tiles.TileEntityFurnaceCore;
import com.teambr.bookshelf.client.gui.GuiBase;
import com.teambr.bookshelf.client.gui.component.BaseComponent;
import com.teambr.bookshelf.client.gui.component.display.GuiComponentArrow;
import com.teambr.bookshelf.client.gui.component.display.GuiComponentFlame;
import com.teambr.bookshelf.client.gui.component.display.GuiComponentText;
import com.teambr.bookshelf.client.gui.component.display.GuiTabCollection;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GuiModularFurnace extends GuiBase<ContainerModularFurnace> {
    protected TileEntityFurnaceCore core;

    public GuiModularFurnace(InventoryPlayer player, TileEntityFurnaceCore tile) {
        super(new ContainerModularFurnace(player, tile), 175, 165, "inventory.furnace.title");
        this.core = tile;
        addRightTabs(rightTabs);
    }

    @Override
    public void addComponents() {
        //Silly work around to having to super. Oh well
        if (core != null) {
            components.add(new GuiComponentFlame(56, 36) {
                @Override
                public int getCurrentBurn() {
                    return core.isBurning() ? core.getBurnTimeRemainingScaled(14) : 0;
                }
            });

            components.add(new GuiComponentArrow(79, 34) {
                @Override
                public int getCurrentProgress() {
                    return core.getCookProgressScaled(24);
                }
            });
        }
    }

        /**
         * Adds the tabs to the right. Overwrite this if you want tabs on your GUI
         * @param tabs List of tabs, put GuiTabs in
         */
        @Override
        public void addRightTabs(GuiTabCollection tabs) {
            if (core != null) {
                List<BaseComponent> furnaceInfoSpeed = new ArrayList<>();
                furnaceInfoSpeed.add(new GuiComponentText("Information", 26, 6, 0xFFCC00));
                furnaceInfoSpeed.add(new GuiComponentText("Speed: ", 5, 23, 0xFFFFFF));
                furnaceInfoSpeed.add(new GuiComponentText(String.format("%.2f", -(((core.getValues().getSpeed() + 200) / 200) - 1)) + "x", 15, 33, (-(((core.getValues().getSpeed() + 200) / 200) - 1)) > 0 ? 0x19A319 : 0x801A00));
                furnaceInfoSpeed.add(new GuiComponentText("Efficiency: ", 5, 48, 0xFFFFFF));
                furnaceInfoSpeed.add(new GuiComponentText(String.format("%.2f", -(((core.getValues().getEfficiency() + 200) / 200) - 1)) + "x", 15, 58, (-(((core.getValues().getEfficiency() + 200) / 200) - 1)) > 0 ? 0x19A319 : 0x801A00));
                furnaceInfoSpeed.add(new GuiComponentText("Multiplicity: ", 5, 73, 0xFFFFFF));
                furnaceInfoSpeed.add(new GuiComponentText((core.getValues().getMultiplicity() + 1) + "x", 15, 83, core.getValues().getMultiplicity() > 0 ? 0x19A319 : 0x000000));
                tabs.addTab(furnaceInfoSpeed, 95, 100, new Color(100, 150, 150), new ItemStack(Items.book), false);
            }
        }

        /**
         * Add the tabs to the left. Overwrite this if you want tabs on your GUI
         * @param tabs List of tabs, put GuiReverseTabs in
         */
        @Override
        public void addLeftTabs(GuiTabCollection tabs) {}
    }
