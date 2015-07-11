package com.pauljoda.modularsystems.power.gui;

import com.pauljoda.modularsystems.core.tiles.AbstractCore;
import com.pauljoda.modularsystems.power.container.ContainerLiquidsPower;
import com.pauljoda.modularsystems.power.tiles.TileLiquidsPower;
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

public class GuiLiquidsPower extends GuiBase<ContainerLiquidsPower> {

    protected AbstractCore core;
    protected TileLiquidsPower tileEntity;

    public GuiLiquidsPower(InventoryPlayer inventory, TileLiquidsPower tileEntity) {
        super(new ContainerLiquidsPower(inventory, tileEntity), 200, 165, "inventory.liquidspower.title");

        core = tileEntity.getCore();
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

        ArrayList<String> toolTipPower = new ArrayList<>();
        toolTipPower.add("");
        powerBar.setToolTip(toolTipPower);

        components.add(powerBar);

        GuiComponentPowerBar liquidBar = new GuiComponentPowerBar(152, 18, 18, 60, new Color(255, 0, 0)) {
            @Override
            public int getEnergyPercent(int scale) {
                int display = tileEntity.tank == null ? 0 : tileEntity.tank.getFluidAmount() * scale / tileEntity.tank.getCapacity();
                return display;
            }
        };

        ArrayList<String> toolTipLiquid = new ArrayList<>();
        toolTipLiquid.add("");
        liquidBar.setToolTip(toolTipLiquid);

        components.add(liquidBar);
    }

    @Override
    public void drawGuiContainerForegroundLayer(int x, int y) {
        super.drawGuiContainerForegroundLayer(x, y);


        ArrayList<String> toolTipLiquid = new ArrayList<>();
        toolTipLiquid.add(tileEntity.tank.getFluidAmount() + " / " + tileEntity.tank.getCapacity());
        components.get(1).setToolTip(toolTipLiquid);

        rightTabs.getTabs().get(0).setIcon(new ItemStack(core.getWorldObj().getBlock(core.xCoord, core.yCoord, core.zCoord)));

    }

    /*
     * Side Tabs
     */
    @Override
    public void addRightTabs(GuiTabCollection tabs) {
        List<BaseComponent> coreTab = new ArrayList<>();
        tabs.addTab(coreTab, 95, 100, new Color(100, 150, 150), new ItemStack(Blocks.furnace), false);
        tabs.getTabs().get(0).setMouseEventListener(new IMouseEventListener() {
            @Override
            public void onMouseDown(BaseComponent baseComponent, int i, int i1, int i2) {

                if (tileEntity.getCore() != null)
                    Minecraft.getMinecraft().thePlayer.openGui(Bookshelf.instance, 0, tileEntity.getWorldObj(), tileEntity.getCore().xCoord, tileEntity.getCore().yCoord, tileEntity.getCore().zCoord);
            }

            @Override
            public void onMouseUp(BaseComponent baseComponent, int i, int i1, int i2) {

            }

            @Override
            public void onMouseDrag(BaseComponent baseComponent, int i, int i1, int i2, long l) {

            }
        });
    }
}
