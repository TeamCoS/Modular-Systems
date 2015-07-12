package com.pauljoda.modularsystems.power.gui;

import com.pauljoda.modularsystems.core.tiles.AbstractCore;
import com.teambr.bookshelf.Bookshelf;
import com.teambr.bookshelf.client.gui.GuiBase;
import com.teambr.bookshelf.client.gui.component.BaseComponent;
import com.teambr.bookshelf.client.gui.component.display.GuiTabCollection;
import com.teambr.bookshelf.client.gui.component.listeners.IMouseEventListener;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GuiPowerBase<C extends Container> extends GuiBase<C> {

    protected AbstractCore core;


    public GuiPowerBase(C container, AbstractCore core, int width, int height, String name) {
        super(container, width, height, name);

        this.core = core;
    }

    @Override
    public void drawGuiContainerForegroundLayer(int x, int y) {
        super.drawGuiContainerForegroundLayer(x, y);
        rightTabs.getTabs().get(0).setIcon(new ItemStack(core.getWorldObj().getBlock(core.xCoord, core.yCoord, core.zCoord)));
    }

    @Override
    public void addComponents() {

    }

    /*
     * Side Tabs
     */
    @Override
    public void addRightTabs(GuiTabCollection tabs) {
        List<BaseComponent> coreTab = new ArrayList<>();
        tabs.addTab(coreTab, 95, 100, new Color(100, 150, 150), new ItemStack(Blocks.furnace));
        tabs.addTab(coreTab, 95, 100, new Color(255, 0, 0), new ItemStack(Blocks.anvil));
        tabs.getTabs().get(0).setMouseEventListener(new IMouseEventListener() {
            @Override
            public void onMouseDown(BaseComponent baseComponent, int i, int i1, int i2) {

                if(core != null)
                    Minecraft.getMinecraft().thePlayer.openGui(Bookshelf.instance, 0, core.getWorldObj(),
                            core.xCoord, core.yCoord, core.zCoord);
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
