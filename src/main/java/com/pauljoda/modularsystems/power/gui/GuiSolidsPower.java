package com.pauljoda.modularsystems.power.gui;

import com.pauljoda.modularsystems.core.tiles.AbstractCore;
import com.pauljoda.modularsystems.power.container.ContainerSolidsPower;
import com.pauljoda.modularsystems.power.tiles.TileSolidsPower;
import com.teambr.bookshelf.Bookshelf;
import com.teambr.bookshelf.client.gui.GuiBase;
import com.teambr.bookshelf.client.gui.component.BaseComponent;
import com.teambr.bookshelf.client.gui.component.display.GuiComponentPowerBar;
import com.teambr.bookshelf.client.gui.component.display.GuiTabCollection;
import com.teambr.bookshelf.client.gui.component.listeners.IMouseEventListener;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GuiSolidsPower extends GuiPowerBase<ContainerSolidsPower> {

   protected TileSolidsPower tileEntity;

    public GuiSolidsPower(InventoryPlayer player, TileSolidsPower tileEntity) {
        super(new ContainerSolidsPower(player, tileEntity), tileEntity.getCore(), 200, 165, "inventory.solidspower.title");

        this.tileEntity = tileEntity;
    }

    @Override
    public void drawGuiContainerForegroundLayer(int x, int y) {
        super.drawGuiContainerForegroundLayer(x, y);

        ArrayList<String> toolTip = new ArrayList<>();
        toolTip.add(tileEntity.getEnergyStored(null) + " / " + tileEntity.getMaxEnergyStored(null));
        components.get(0).setToolTip(toolTip);
    }

    @Override
    public void addComponents() {
        GuiComponentPowerBar powerBar = new GuiComponentPowerBar(8, 8, 18, 74, new Color(255, 0, 0)) {
            @Override
            public int getEnergyPercent(int scale) {
                return tileEntity.getEnergyStored(null) * 74 / tileEntity.getMaxEnergyStored(null);
            }
        };

        ArrayList<String> toolTip = new ArrayList<>();
        toolTip.add("");
        powerBar.setToolTip(toolTip);

        components.add(powerBar);
    }
}
