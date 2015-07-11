package com.pauljoda.modularsystems.power.gui;

import com.pauljoda.modularsystems.power.container.ContainerLiquidsPower;
import com.pauljoda.modularsystems.power.tiles.TileLiquidsPower;
import com.teambr.bookshelf.client.gui.component.display.GuiComponentArrow;
import com.teambr.bookshelf.client.gui.component.display.GuiComponentPowerBar;
import net.minecraft.entity.player.InventoryPlayer;

import java.awt.*;
import java.util.ArrayList;

public class GuiLiquidsPower extends GuiPowerBase<ContainerLiquidsPower> {

    protected TileLiquidsPower tileEntity;

    public GuiLiquidsPower(InventoryPlayer inventory, TileLiquidsPower tileEntity) {
        super(new ContainerLiquidsPower(inventory, tileEntity), tileEntity.getCore(), 200, 165, "inventory.liquidspower.title");

        this.tileEntity = tileEntity;
    }

    @Override
    public void addComponents() {
        GuiComponentPowerBar powerBar = new GuiComponentPowerBar(30, 18, 18, 60, new Color(255, 0, 0)) {
            @Override
            public int getEnergyPercent(int scale) {
                return tileEntity.getEnergyStored(null) * scale / tileEntity.getMaxEnergyStored(null);
            }
        };
        components.add(powerBar);

        GuiComponentPowerBar liquidBar = new GuiComponentPowerBar(152, 18, 18, 60, new Color(255, 0, 0)) {
            @Override
            public int getEnergyPercent(int scale) {
                return tileEntity.tank == null ? 0 : tileEntity.tank.getFluidAmount() * scale / tileEntity.tank.getCapacity();
            }
        };
        components.add(liquidBar);

        components.add(new GuiComponentArrow(84, 40) {
            @Override
            public int getCurrentProgress() {
                return 0;
            }
        });
    }

    @Override
    public void drawGuiContainerForegroundLayer(int x, int y) {
        super.drawGuiContainerForegroundLayer(x, y);

        ArrayList<String> toolTipPower = new ArrayList<>();
        toolTipPower.add(tileEntity.getEnergyStored(null) + " / " + tileEntity.getMaxEnergyStored(null));
        components.get(0).setToolTip(toolTipPower);

        ArrayList<String> toolTipLiquid = new ArrayList<>();
        toolTipLiquid.add(tileEntity.tank.getFluidAmount() + " / " + tileEntity.tank.getCapacity());
        components.get(1).setToolTip(toolTipLiquid);
    }
}
