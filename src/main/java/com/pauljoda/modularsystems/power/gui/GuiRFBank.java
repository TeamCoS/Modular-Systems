package com.pauljoda.modularsystems.power.gui;

import com.pauljoda.modularsystems.power.tiles.TileRFBank;
import com.teambr.bookshelf.client.gui.component.display.GuiComponentPowerBar;
import com.teambr.bookshelf.inventory.ContainerGeneric;

import java.awt.*;
import java.util.*;

public class GuiRFBank extends GuiPowerBase<ContainerGeneric> {

    protected TileRFBank tileEntity;

    public GuiRFBank(TileRFBank tileEntity) {
        super(new ContainerGeneric(), tileEntity, 140, 120, "inventory.rfpower.title");

        this.tileEntity = tileEntity;
    }

    @Override
    public void addComponents() {
        components.add(new GuiComponentPowerBar(56, 23, 18, 74, new Color(255, 0, 0)) {
            @Override
            public int getEnergyPercent(int scale) {
                return tileEntity.getEnergyStored(null) * scale / tileEntity.getMaxEnergyStored(null);
            }

            @Override
            public java.util.List<String> getDynamicToolTip(int x, int y) {
                return Collections.singletonList(tileEntity.getEnergyStored(null) + " / " + tileEntity.getMaxEnergyStored(null));
            }
        });
    }
}
