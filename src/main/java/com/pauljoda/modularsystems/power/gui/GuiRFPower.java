package com.pauljoda.modularsystems.power.gui;

import com.pauljoda.modularsystems.core.tiles.AbstractCore;
import com.pauljoda.modularsystems.core.tiles.DummyTile;
import com.pauljoda.modularsystems.power.container.ContainerRFPower;
import com.pauljoda.modularsystems.power.tiles.TileRFPower;
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

public class GuiRFPower extends GuiBase<ContainerRFPower> {

    protected AbstractCore core;
    protected TileRFPower tileEntity;


    public GuiRFPower(InventoryPlayer player, TileRFPower tileEntity) {
        super(new ContainerRFPower(player, tileEntity), 175, 165, "inventory.rfpower.title");

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

                if(tileEntity.getCore() != null)
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
