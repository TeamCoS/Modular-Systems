package com.pauljoda.modularsystems.power.gui;

import com.pauljoda.modularsystems.core.tiles.AbstractCore;
import com.pauljoda.modularsystems.power.container.ContainerRFPower;
import com.pauljoda.modularsystems.power.tiles.TileRFPower;
import com.teambr.bookshelf.client.gui.GuiBase;
import com.teambr.bookshelf.client.gui.component.display.GuiComponentPowerBar;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiRFPower extends GuiBase<ContainerRFPower> {

    protected AbstractCore core;
    protected TileRFPower tileEntity;


    public GuiRFPower(InventoryPlayer player, TileRFPower tileEntity) {
        super(new ContainerRFPower(player, tileEntity), 130, 120, "inventory.rfpower.title");

        core = tileEntity.getCore();
        this.tileEntity = tileEntity;
    }

    @Override
    public void addComponents() {
        components.add(new GuiComponentPowerBar(56, 23) {
            @Override
            public int getEnergyPercent() {
                return tileEntity.energyRF.getEnergyStored() * 72 / tileEntity.energyRF.getMaxEnergyStored();
            }
        });
    }
}
