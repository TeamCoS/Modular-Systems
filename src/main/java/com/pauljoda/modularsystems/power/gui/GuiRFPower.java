package com.pauljoda.modularsystems.power.gui;

import com.pauljoda.modularsystems.core.tiles.AbstractCore;
import com.pauljoda.modularsystems.power.container.ContainerRFPower;
import com.pauljoda.modularsystems.power.tiles.TileRFPower;
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

public class GuiRFPower extends GuiPowerBase<ContainerRFPower, TileRFPower> {

    protected TileRFPower tileEntity;

    public GuiRFPower(InventoryPlayer player, TileRFPower tileEntity) {
        super(new ContainerRFPower(), tileEntity, 130, 120, "inventory.rfpower.title");

        this.tileEntity = tileEntity;
    }

    @Override
    public void drawGuiContainerForegroundLayer(int x, int y) {
        super.drawGuiContainerForegroundLayer(x, y);

        ArrayList<String> toolTipPower = new ArrayList<>();
        toolTipPower.add(tileEntity.getEnergyStored(null) + " / " + tileEntity.getMaxEnergyStored(null));
        components.get(0).setToolTip(toolTipPower);

        rightTabs.getTabs().get(0).setIcon(new ItemStack(core.getWorldObj().getBlock(core.xCoord, core.yCoord, core.zCoord)));
    }

    @Override
    public void addComponents() {
        components.add(new GuiComponentPowerBar(56, 23, 18, 74, new Color(255, 0, 0)) {
            @Override
            public int getEnergyPercent(int scale) {
                return tileEntity.getEnergyStored(null) * 74 / tileEntity.getMaxEnergyStored(null);
            }
        });
    }


}
