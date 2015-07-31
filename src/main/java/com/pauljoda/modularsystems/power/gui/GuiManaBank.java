package com.pauljoda.modularsystems.power.gui;

import com.pauljoda.modularsystems.power.tiles.TileBankMana;
import com.teambr.bookshelf.client.gui.component.display.GuiComponentPowerBar;
import com.teambr.bookshelf.inventory.ContainerGeneric;

import java.awt.*;
import java.util.*;
import java.util.List;

public class GuiManaBank extends GuiPowerBase<ContainerGeneric> {

    protected TileBankMana tileEntity;

    public GuiManaBank(TileBankMana tileEntity) {
        super(new ContainerGeneric(), tileEntity, 140, 120, "inventory.manapower.title");

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
            public List<String> getDynamicToolTip(int mouseX, int mouseY) {
                return Collections.singletonList(tileEntity.getEnergyStored(null) + " / " + tileEntity.getMaxEnergyStored(null));
            }
        });
    }
}
