package com.pauljoda.modularsystems.crusher.gui;

import com.pauljoda.modularsystems.crusher.container.ContainerCrusher;
import com.pauljoda.modularsystems.crusher.tiles.TileCrusherCore;
import com.pauljoda.modularsystems.furnace.tiles.TileEntityFurnaceCore;
import com.teambr.bookshelf.client.gui.GuiBase;
import com.teambr.bookshelf.client.gui.component.BaseComponent;
import com.teambr.bookshelf.client.gui.component.display.GuiComponentText;
import com.teambr.bookshelf.client.gui.component.display.GuiTabCollection;
import com.teambr.bookshelf.collections.Location;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GuiCrusher extends GuiBase<ContainerCrusher> {

    protected TileCrusherCore core;
    protected Location tileLocation;

    public GuiCrusher(InventoryPlayer inventory, TileCrusherCore tileEntity) {
        super(new ContainerCrusher(inventory, tileEntity), 175, 165, "inventory.crusher.title");

        this.core = tileEntity;
        tileLocation = core.getLocation();
        addRightTabs(rightTabs);
    }

    @Override
    public void addComponents() {

    }

    @Override
    public void addRightTabs(GuiTabCollection tabs) {
        if (core != null) {
            List<BaseComponent> crusherInfoSpeed = new ArrayList<>();
            crusherInfoSpeed.add(new GuiComponentText("Information", 26, 6, 0xFFCC00));
            crusherInfoSpeed.add(new GuiComponentText("Speed: ", 5, 23, 0xFFFFFF));
            crusherInfoSpeed.add(new GuiComponentText(String.format("%.2f", (-1 * (((core.getValues().getSpeed() + 200) / 200) - 1)) != 0 ? ((-1 * (((core.getValues().getSpeed() + 200) / 200) - 1)) * 100) : 0.00) + "%", 15, 33, (-1 * (((core.getValues().getSpeed() + 200) / 200) - 1)) > 0 ? 0x5CE62E : (-1 * (((core.getValues().getSpeed() + 200) / 200) - 1)) == 0 ? 0x000000 : 0xE62E00));
            crusherInfoSpeed.add(new GuiComponentText("Efficiency: ", 5, 48, 0xFFFFFF));
            crusherInfoSpeed.add(new GuiComponentText(String.format("%.2f", (((core.getValues().getEfficiency() + 200) / 200) - 1) != 0 ? ((((core.getValues().getEfficiency() + 200) / 200) - 1) * 100) : 0.00) + "%", 15, 58, (((core.getValues().getEfficiency() + 200) / 200) - 1) > 0 ? 0x5CE62E : (((core.getValues().getEfficiency() + 200) / 200) - 1) == 0 ? 0x000000 : 0xE62E00));
            crusherInfoSpeed.add(new GuiComponentText("Multiplicity: ", 5, 73, 0xFFFFFF));
            crusherInfoSpeed.add(new GuiComponentText((core.getValues().getMultiplicity() + 1) + "x", 15, 83, core.getValues().getMultiplicity() > 0 ? 0x5CE62E : 0x000000));
            tabs.addTab(crusherInfoSpeed, 95, 100, new Color(100, 150, 150), new ItemStack(Items.book));
        }
    }
}
