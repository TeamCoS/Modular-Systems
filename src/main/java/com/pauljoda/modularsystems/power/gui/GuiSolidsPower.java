package com.pauljoda.modularsystems.power.gui;

import com.pauljoda.modularsystems.core.tiles.AbstractCore;
import com.pauljoda.modularsystems.power.container.ContainerSolidsPower;
import com.pauljoda.modularsystems.power.tiles.TileSolidsPower;
import com.teambr.bookshelf.client.gui.GuiBase;
import com.teambr.bookshelf.client.gui.component.display.GuiComponentPowerBar;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiSolidsPower extends GuiBase<ContainerSolidsPower> {

    protected AbstractCore core;
    protected TileSolidsPower tileEntity;

    public GuiSolidsPower(InventoryPlayer player, TileSolidsPower tileEntity) {
        super(new ContainerSolidsPower(player, tileEntity), 200, 165, "inventory.solidspower.title");

        core = tileEntity.getCore();
        this.tileEntity = tileEntity;
    }

    @Override
    public void addComponents() {
        components.add(new GuiComponentPowerBar(10, 10) {
            @Override
            public int getEnergyPercent() {
                return tileEntity.energySolids.getEnergyStored() * 72 / tileEntity.energySolids.getMaxEnergyStored();
            }
        });
    }
}
