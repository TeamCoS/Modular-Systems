package com.pauljoda.modularsystems.power.gui;

import com.pauljoda.modularsystems.power.container.ContainerManaBank;
import com.pauljoda.modularsystems.power.tiles.TileManaBank;
import com.teambr.bookshelf.client.gui.component.display.GuiComponentPowerBar;

import java.awt.*;
import java.util.ArrayList;

public class GuiManaBank extends GuiPowerBase<ContainerManaBank> {

    protected TileManaBank tileEntity;

    public GuiManaBank(TileManaBank tileEntity) {
        super(new ContainerManaBank(), tileEntity, 140, 120, "inventory.manapower.title");

        this.tileEntity = tileEntity;
    }

    @Override
    public void drawGuiContainerForegroundLayer(int x, int y) {
        super.drawGuiContainerForegroundLayer(x, y);

        ArrayList<String> toolTipPower = new ArrayList<>();
        toolTipPower.add(tileEntity.getEnergyStored(null) + " / " + tileEntity.getMaxEnergyStored(null));
        components.get(1).setToolTip(toolTipPower);
    }

    @Override
    public void addComponents() {
        components.add(new GuiComponentPowerBar(56, 23, 18, 74, new Color(255, 0, 0)) {
            @Override
            public int getEnergyPercent(int scale) {
                return tileEntity.getEnergyStored(null) * scale / tileEntity.getMaxEnergyStored(null);
            }
        });
    }
}
