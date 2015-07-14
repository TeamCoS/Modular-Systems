package com.pauljoda.modularsystems.power.gui;

import com.pauljoda.modularsystems.power.container.ContainerSolidsBank;
import com.pauljoda.modularsystems.power.tiles.TileSolidsBank;
import com.teambr.bookshelf.client.gui.component.display.GuiComponentPowerBar;
import net.minecraft.entity.player.InventoryPlayer;

import java.awt.*;
import java.util.ArrayList;

public class GuiSolidsBank extends GuiPowerBase<ContainerSolidsBank> {

   protected TileSolidsBank tileEntity;

    public GuiSolidsBank(InventoryPlayer player, TileSolidsBank tileEntity) {
        super(new ContainerSolidsBank(player, tileEntity), tileEntity, 200, 165, "inventory.solidspower.title");

        this.tileEntity = tileEntity;
    }

    @Override
    public void drawGuiContainerForegroundLayer(int x, int y) {
        super.drawGuiContainerForegroundLayer(x, y);

        ArrayList<String> toolTip = new ArrayList<>();
        toolTip.add(tileEntity.getEnergyStored(null) + " / " + tileEntity.getMaxEnergyStored(null));
        components.get(1).setToolTip(toolTip);
    }

    @Override
    public void addComponents() {
        GuiComponentPowerBar powerBar = new GuiComponentPowerBar(8, 8, 18, 74, new Color(255, 0, 0)) {
            @Override
            public int getEnergyPercent(int scale) {
                return tileEntity.getEnergyStored(null) * scale / tileEntity.getMaxEnergyStored(null);
            }
        };
        components.add(powerBar);
    }
}
