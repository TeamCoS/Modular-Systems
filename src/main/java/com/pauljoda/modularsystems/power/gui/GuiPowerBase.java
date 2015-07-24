package com.pauljoda.modularsystems.power.gui;

import com.pauljoda.modularsystems.core.network.OpenContainerPacket;
import com.pauljoda.modularsystems.core.tiles.AbstractCore;
import com.pauljoda.modularsystems.power.tiles.TileBankBase;
import com.teambr.bookshelf.client.gui.GuiBase;
import com.teambr.bookshelf.client.gui.component.BaseComponent;
import com.teambr.bookshelf.client.gui.component.control.GuiComponentSetNumber;
import com.teambr.bookshelf.client.gui.component.display.GuiComponentText;
import com.teambr.bookshelf.client.gui.component.display.GuiTabCollection;
import com.teambr.bookshelf.client.gui.component.listeners.IMouseEventListener;
import com.teambr.bookshelf.manager.PacketManager;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GuiPowerBase<C extends Container> extends GuiBase<C> {

    protected TileBankBase tileEntity;
    protected AbstractCore core;


    public GuiPowerBase(C container, TileBankBase tileEntity, int width, int height, String name) {
        super(container, width, height, name);

        this.tileEntity = tileEntity;
        this.core = tileEntity.getCore();
        addRightTabs(rightTabs);
    }

    @Override
    public void drawGuiContainerForegroundLayer(int x, int y) {
        super.drawGuiContainerForegroundLayer(x, y);

        if (tileEntity.getCore() != null) {
            core = (AbstractCore) core.getWorldObj().getTileEntity(core.xCoord, core.yCoord, core.zCoord);
            rightTabs.getTabs().get(0).setIcon(new ItemStack(core.getWorldObj().getBlock(
                    core.xCoord, core.yCoord, core.zCoord)));
        }
    }

    @Override
    public void addComponents() {

    }

    /*
     * Side Tabs
     */
    @Override
    public void addRightTabs(GuiTabCollection tabs) {
        if (tileEntity != null && core != null) {
            //Core Gui Tab
            List<BaseComponent> coreTab = new ArrayList<>();
            tabs.addTab(coreTab, 95, 100, new Color(100, 150, 150), new ItemStack(Blocks.furnace));

            //Priority Tab
            List<BaseComponent> priorityTab = new ArrayList<>();
            priorityTab.add(new GuiComponentSetNumber(26, 25, 40, tileEntity.getPriority(), 0, 100) {
                @Override
                public void setValue(int value) {
                    tileEntity.setPriority(value);
                    PacketManager.updateTileWithClientInfo(
                            tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord));
                }
            });
            priorityTab.add(new GuiComponentText("Fuel Priority", 22, 7));
            tabs.addTab(priorityTab, 95, 55, new Color(255, 68, 51), new ItemStack(Blocks.anvil));
            tabs.getTabs().get(0).setToolTip(Collections.singletonList("Core Gui"));
            tabs.getTabs().get(1).setToolTip(Collections.singletonList("Fuel Priority"));

            //Link Core tab to core Gui
            tabs.getTabs().get(0).setMouseEventListener(new IMouseEventListener() {
                @Override
                public void onMouseDown(BaseComponent baseComponent, int i, int i1, int i2) {

                    if (tileEntity.getCore() != null) {
                        com.pauljoda.modularsystems.core.managers.PacketManager.net.sendToServer(
                                new OpenContainerPacket.UpdateMessage(core.xCoord, core.yCoord, core.zCoord));
                    }
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
}
