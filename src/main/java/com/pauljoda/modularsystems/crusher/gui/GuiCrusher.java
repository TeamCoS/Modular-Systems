package com.pauljoda.modularsystems.crusher.gui;

import com.pauljoda.modularsystems.core.ModularSystems;
import com.pauljoda.modularsystems.core.gui.GuiCoreBase;
import com.pauljoda.modularsystems.crusher.container.ContainerCrusher;
import com.pauljoda.modularsystems.crusher.tiles.TileCrusherCore;
import com.teambr.bookshelf.client.gui.component.BaseComponent;
import com.teambr.bookshelf.client.gui.component.display.GuiComponentArrow;
import com.teambr.bookshelf.client.gui.component.display.GuiComponentFlame;
import com.teambr.bookshelf.client.gui.component.display.GuiComponentText;
import com.teambr.bookshelf.client.gui.component.display.GuiTabCollection;
import com.teambr.bookshelf.collections.Location;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GuiCrusher extends GuiCoreBase<ContainerCrusher> {

    protected TileCrusherCore core;
    protected Location tileLocation;

    public GuiCrusher(InventoryPlayer player, TileCrusherCore tileEntity) {
        super(new ContainerCrusher(player, tileEntity), tileEntity, 175, 165, "inventory.crusher.title");

        this.core = tileEntity;
        tileLocation = core.getLocation();
        addRightTabs(rightTabs);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        core = (TileCrusherCore) core.getWorldObj().getTileEntity(core.xCoord, core.yCoord, core.zCoord);
        super.drawGuiContainerBackgroundLayer(f, i, j);
    }

    @Override
    public void addComponents() {
        components.add(new GuiComponentFlame(81, 55) {
            @Override
            public int getCurrentBurn() {
                return core.isBurning() ? core.getBurnTimeRemainingScaled(14) : 0;
            }

            @Override
            public List<String> getDynamicToolTip(int x, int y) {
                return Collections.singletonList(core.getValues().burnTime + " ticks left");
            }
        });
        components.add(new GuiComponentArrow(79, 34) {
            @Override
            public int getCurrentProgress() {
                return core.getCookProgressScaled(24);
            }

            @Override
            public List<String> getDynamicToolTip(int mouseX, int mouseY) {
                if(ModularSystems.nei != null)
                    return Collections.singletonList(StatCollector.translateToLocal("inventory.nei.recipes"));
                return null;
            }

            @Override
            public void mouseDown(int mouseX, int mouseY, int button) {
                if(ModularSystems.nei != null)
                    ModularSystems.nei.onArrowClicked(inventory);
            }
        });
    }

    @Override
    public void addRightTabs(GuiTabCollection tabs) {
        if (core != null) {
            List<BaseComponent> crusherInfoSpeed = new ArrayList<>();
            crusherInfoSpeed.add(new GuiComponentText("Information", 26, 6, 0xFFCC00));
            crusherInfoSpeed.add(new GuiComponentText("Speed: ", 5, 23, 0xFFFFFF));
            crusherInfoSpeed.add(new GuiComponentText(String.format("%.2f", (-1 * (((core.getValues().getSpeed() + 200) / 200) - 1)) != 0 ? ((-1 * (((core.getValues().getSpeed() + 200) / 200) - 1)) * 100) : 0.00) + "%", 15, 33, (-1 * (((core.getValues().getSpeed() + 200) / 200) - 1)) > 0 ? 0x5CE62E : (-1 * (((core.getValues().getSpeed() + 200) / 200) - 1)) == 0 ? 0x000000 : 0xE62E00));
            crusherInfoSpeed.add(new GuiComponentText("Efficiency: ", 5, 48, 0xFFFFFF));
            crusherInfoSpeed.add(new GuiComponentText(String.format("%.2f", -1 * (100 - ((1600 + core.getValues().getEfficiency()) / 1600) * 100) != 0 ? -1 * (100 - ((1600 + core.getValues().getEfficiency()) / 1600) * 100) : 0.00)  + "%", 15, 58, -1 * (100 - ((1600 + core.getValues().getEfficiency()) / 1600) * 100) > 0 ? 0x5CE62E : -1 * (100 - ((1600 + core.getValues().getEfficiency()) / 1600) * 100) == 0 ? 0x000000 : 0xE62E00));
            crusherInfoSpeed.add(new GuiComponentText("Multiplicity: ", 5, 73, 0xFFFFFF));
            crusherInfoSpeed.add(new GuiComponentText((int)(core.getValues().getMultiplicity() + 1) + "x", 15, 83, core.getValues().getMultiplicity() > 0 ? 0x5CE62E : 0x000000));
            tabs.addTab(crusherInfoSpeed, 95, 100, new Color(150, 112, 50), new ItemStack(Items.book));
        }
    }
}
