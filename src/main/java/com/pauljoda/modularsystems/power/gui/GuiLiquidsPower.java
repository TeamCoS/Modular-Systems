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
        components.add(new GuiComponentPowerBar(19, 18, 18, 60, new Color(255, 0, 0)) {
            @Override
            public int getEnergyPercent(int scale) {
                return tileEntity.tank == null ? 0 : tileEntity.tank.getFluidAmount() * scale / tileEntity.tank.getCapacity();
            }
        });

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

        ArrayList<String> toolTipLiquid = new ArrayList<>();
        toolTipLiquid.add(tileEntity.tank.getFluidAmount() + " / " + tileEntity.tank.getCapacity());
        toolTipLiquid.add(tileEntity.tank.getFluid() != null ? tileEntity.tank.getFluid().getLocalizedName() : "Empty");
        components.get(1).setToolTip(toolTipLiquid);
    }
}
