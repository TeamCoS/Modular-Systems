package com.pauljoda.modularsystems.generator.gui;

import com.pauljoda.modularsystems.core.gui.GuiCoreBase;
import com.pauljoda.modularsystems.core.registries.ConfigRegistry;
import com.pauljoda.modularsystems.generator.container.ContainerGenerator;
import com.pauljoda.modularsystems.generator.tiles.TileGeneratorCore;
import com.teambr.bookshelf.client.gui.component.BaseComponent;
import com.teambr.bookshelf.client.gui.component.display.GuiComponentFlame;
import com.teambr.bookshelf.client.gui.component.display.GuiComponentPowerBar;
import com.teambr.bookshelf.client.gui.component.display.GuiComponentText;
import com.teambr.bookshelf.client.gui.component.display.GuiTabCollection;
import com.teambr.bookshelf.collections.Location;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GuiGenerator extends GuiCoreBase<ContainerGenerator> {

    protected TileGeneratorCore core;
    protected Location tileLocation;

    public GuiGenerator(InventoryPlayer player, TileGeneratorCore tileEntity) {
        super(new ContainerGenerator(player, tileEntity), tileEntity, 175, 165, "inventory.generator.title");
        this.core = tileEntity;
        tileLocation = core.getLocation();
        addRightTabs(rightTabs);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        core = (TileGeneratorCore) core.getWorldObj().getTileEntity(core.xCoord, core.yCoord, core.zCoord);
        super.drawGuiContainerBackgroundLayer(f, i, j);
    }

    @Override
    public void addComponents() {
        components.add(new GuiComponentFlame(78, 40) {
            @Override
            public int getCurrentBurn() {
                return core.isBurning() ? core.getBurnTimeRemainingScaled(14) : 0;
            }

            @Override
            public List<String> getDynamicToolTip(int x, int y) {
                return Collections.singletonList(core.getValues().burnTime + " ticks left");
            }
        });

        components.add(new GuiComponentPowerBar(110, 20, 18, 60, new Color(255, 0, 0)) {
            @Override
            public int getEnergyPercent(int scale) {
                return core.getEnergyStored(null) * scale / core.getMaxEnergyStored(null);
            }
        });

        components.add(new GuiComponentText("+", 55, 24));
        components.add(new GuiComponentText("-", 55, 64));
    }

    @Override
    public void addRightTabs(GuiTabCollection tabs) {
        if (core != null) {
            List<BaseComponent> furnaceInfoSpeed = new ArrayList<>();
            furnaceInfoSpeed.add(new GuiComponentText("Information", 26, 6, 0xFFCC00));
            furnaceInfoSpeed.add(new GuiComponentText("Speed: ", 5, 23, 0xFFFFFF));
            furnaceInfoSpeed.add(new GuiComponentText(String.format("%.2f", (-1 *(((core.getValues().getSpeed() + 200) / 200) - 1)) != 0 ? ((-1 *(((core.getValues().getSpeed() + 200) / 200) - 1)) * 100) : 0.00) + "%", 15, 33, (-1 * (((core.getValues().getSpeed() + 200) / 200) - 1)) > 0 ? 0x5CE62E : (-1 * (((core.getValues().getSpeed() + 200) / 200) - 1)) == 0 ? 0x000000 : 0xE62E00));
            furnaceInfoSpeed.add(new GuiComponentText("Efficiency: ", 5, 48, 0xFFFFFF));
            furnaceInfoSpeed.add(new GuiComponentText(String.format("%.2f", core.getValues().getEfficiency() != 0 ? core.getValues().getEfficiency() : 0.00)  + " ticks", 15, 58, core.getValues().getEfficiency() > 0 ? 0x5CE62E : core.getValues().getEfficiency() == 0 ? 0x000000 : 0xE62E00));
            furnaceInfoSpeed.add(new GuiComponentText("Multiplicity: ", 5, 73, 0xFFFFFF));
            furnaceInfoSpeed.add(new GuiComponentText((int) (core.getValues().getMultiplicity() + 1) + "x", 15, 83, core.getValues().getMultiplicity() > 0 ? 0x5CE62E : 0x000000));
            tabs.addTab(furnaceInfoSpeed, 95, 100, new Color(150, 112, 50), new ItemStack(Items.book));
            List<BaseComponent> gen = new ArrayList<>();
            gen.add(new GuiComponentText("Generation", 26, 6, 0xFFCC00));
            gen.add(new GuiComponentText("RF/t: ", 5, 25, 0xFFFFFF));
            gen.add(new GuiComponentText(Long.toString(Math.round(ConfigRegistry.rfPower * core.getValues().getMultiplicity() *
                    (core.getValues().getSpeed() * -1))), 40, 25, 0x5CE62E));
            tabs.addTab(gen, 95, 100, new Color(70, 108, 150), new ItemStack(Items.blaze_powder));

        }
    }
}
