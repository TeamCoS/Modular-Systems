package com.pauljoda.modularsystems.core.gui;

import com.pauljoda.modularsystems.core.container.ContainerBlockValueConfig;
import com.teambr.bookshelf.client.gui.GuiBase;
import com.teambr.bookshelf.client.gui.component.control.GuiComponentSetNumber;
import com.teambr.bookshelf.client.gui.component.display.GuiComponentText;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.StatCollector;

import java.util.Arrays;
import java.util.List;

public class GuiBlockValueConfig extends GuiBase<ContainerBlockValueConfig> {

    protected GuiComponentSetNumber m1, m2, t, p, b, f, c;
    /**
     * Constructor for All Guis
     *
     * @param container Inventory Container
     * @param width     XSize
     * @param height    YSize
     * @param name      The inventory title
     */
    public GuiBlockValueConfig(ContainerBlockValueConfig container, int width, int height, String name) {
        super(container, width, height, name);
    }

    @Override
    public void initGui() {
        super.initGui();
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        this.buttonList.add(new GuiButton(0, x + 170, y + 145, 60, 20, StatCollector.translateToLocal("inventory.blockValuesConfig.speed")));
        this.buttonList.add(new GuiButton(1, x + 235, y + 145, 60, 20, StatCollector.translateToLocal("inventory.blockValuesConfig.efficiency")));
        this.buttonList.add(new GuiButton(2, x + 170, y + 175, 60, 20, StatCollector.translateToLocal("inventory.blockValuesConfig.multiplicity")));
        this.buttonList.add(new GuiButton(3, x + 235, y + 175, 60, 20, StatCollector.translateToLocal("inventory.blockValuesConfig.save")));
    }

    @Override
    public void actionPerformed(GuiButton button) {
        switch (button.id) {

        }
    }

    @Override
    public void addComponents() {
        //Formula
        components.add(new GuiComponentText("inventory.blockValuesConfig.formula", 175, 125));

        //Scale Numerator
        components.add(new GuiComponentText("inventory.blockValuesConfig.scaleFactorNumerator", 8, 30) {
            @Override
            public List<String> getDynamicToolTip(int x, int y) {
                return Arrays.asList(StatCollector.translateToLocal("inventory.blockValuesConfig.scaleFactorNumerator.toolTip"));
            }
        });
        components.add(m1 = new GuiComponentSetNumber(30, 27, 40, 0, -100, 100) {
            @Override
            public void setValue(int i) {
                numbersChanged();
            }
        });

        //Scale Denominator
        components.add(new GuiComponentText("inventory.blockValuesConfig.scaleDenominator", 8, 50) {
            @Override
            public List<String> getDynamicToolTip(int x, int y) {
                return Arrays.asList(StatCollector.translateToLocal("inventory.blockValuesConfig.scaleDenominator.toolTip"));
            }
        });
        components.add(m2 = new GuiComponentSetNumber(30, 47, 40, 0, -100, 100) {
            @Override
            public void setValue(int i) {
                numbersChanged();
            }
        });

        //X Offset
        components.add(new GuiComponentText("inventory.blockValuesConfig.xOffset", 8, 70) {
            @Override
            public List<String> getDynamicToolTip(int x, int y) {
                return Arrays.asList(StatCollector.translateToLocal("inventory.blockValuesConfig.xOffset.toolTip"));
            }
        });
        components.add(t = new GuiComponentSetNumber(30, 67, 40, 0, -100, 100) {
            @Override
            public void setValue(int i) {
                numbersChanged();
            }
        });
    }

    protected void numbersChanged() {

    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        if(inventory.itemName != null) {
            this.title.setText(inventory.itemName);
            this.title.setXPos(xSize / 2);
            this.title.setXPos(this.title.getXPos() - (Minecraft.getMinecraft().fontRenderer.getStringWidth(this.title.getText()) / 2));
        }
        super.drawGuiContainerForegroundLayer(x, y);
    }
}
