package com.pauljoda.modularsystems.power.gui;

import com.pauljoda.modularsystems.core.tiles.AbstractCore;
import com.pauljoda.modularsystems.power.container.ContainerSolidsPower;
import com.pauljoda.modularsystems.power.tiles.TileSolidsPower;
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

public class GuiSolidsPower extends GuiBase<ContainerSolidsPower> {

    protected AbstractCore core;
    protected TileSolidsPower tileEntity;

    public GuiSolidsPower(InventoryPlayer player, TileSolidsPower tileEntity) {
        super(new ContainerSolidsPower(player, tileEntity), 200, 165, "inventory.solidspower.title");

        core = tileEntity.getCore();
        this.tileEntity = tileEntity;
    }

    @Override
    public void drawGuiContainerForegroundLayer(int x, int y) {
        super.drawGuiContainerForegroundLayer(x, y);

        ArrayList<String> toolTip = new ArrayList<>();
        toolTip.add(tileEntity.getEnergyStored(null) + " / " + tileEntity.getMaxEnergyStored(null));
        components.get(0).setToolTip(toolTip);
    }

    @Override
    public void addComponents() {
        GuiComponentPowerBar powerBar = new GuiComponentPowerBar(8, 8, 18, 74, new Color(255, 255, 255)) {
            @Override
            public int getEnergyPercent(int scale) {
                return tileEntity.getEnergyStored(null) * 74 / tileEntity.getMaxEnergyStored(null);
            }
        };

        ArrayList<String> toolTip = new ArrayList<>();
        toolTip.add("");
        powerBar.setToolTip(toolTip);

        components.add(powerBar);

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
