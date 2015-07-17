package com.pauljoda.modularsystems.core.gui;

import com.pauljoda.modularsystems.core.calculations.Calculation;
import com.pauljoda.modularsystems.core.container.ContainerBlockValueConfig;
import com.pauljoda.modularsystems.core.registries.BlockValueRegistry;
import com.teambr.bookshelf.client.gui.GuiBase;
import com.teambr.bookshelf.client.gui.component.control.GuiComponentCheckBox;
import com.teambr.bookshelf.client.gui.component.control.GuiComponentSetNumber;
import com.teambr.bookshelf.client.gui.component.display.GuiComponentText;
import com.teambr.bookshelf.helpers.GuiHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.StatCollector;

import java.util.Arrays;
import java.util.List;

public class GuiBlockValueConfig extends GuiBase<ContainerBlockValueConfig> {

    protected GuiComponentSetNumber m1, m2, t, p, b, f, c;
    protected GuiComponentGraph graph;
    protected Calculation speed, efficiency, multiplicity;
    protected Block currentBlock;
    protected int meta = -1;
    protected boolean ignoreMeta = true;
    protected MODE currentMode = MODE.SPEED;

    private enum MODE {
        SPEED("inventory.blockValuesConfig.speed"),
        EFFICIENCY("inventory.blockValuesConfig.efficiency"),
        MULTIPLICITY("inventory.blockValuesConfig.multiplicity");

        private String label;

        MODE(String str) {
            label = StatCollector.translateToLocal(str);
        }

        public String getName() {
            return label;
        }
    }

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
        speed = new Calculation(0, 0, 0, 0, 0, 0, 0);
        efficiency = new Calculation(0, 0, 0, 0, 0, 0, 0);
        multiplicity = new Calculation(0, 0, 0, 0, 0, 0, 0);
        addComponents();
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
            case 0 :
                currentMode = MODE.SPEED;
                tryLoadFunctions();
                break;
            case 1 :
                currentMode = MODE.EFFICIENCY;
                tryLoadFunctions();
                break;
            case 2 :
                currentMode = MODE.MULTIPLICITY;
                tryLoadFunctions();
                break;
            case 3 :
                if(currentBlock != null) {
                    BlockValueRegistry.INSTANCE.addBlockValues(currentBlock, ignoreMeta ? -1 : meta,
                            speed,
                            efficiency,
                            multiplicity);
                    tryLoadFunctions();
                }
        }
        if(currentBlock != null)
            this.title.setText(GuiHelper.GuiColor.YELLOW + currentMode.getName() + ": " + GuiHelper.GuiColor.GRAY + inventory.itemName);
    }

    @Override
    public void addComponents() {
        if(speed != null) {
            //Formula
            components.add(new GuiComponentText("inventory.blockValuesConfig.formula", 175, 125));

            //Scale Numerator
            components.add(new GuiComponentText("inventory.blockValuesConfig.scaleFactorNumerator", 8, 30) {
                @Override
                public List<String> getDynamicToolTip(int x, int y) {
                    return Arrays.asList(StatCollector.translateToLocal("inventory.blockValuesConfig.scaleFactorNumerator.toolTip"));
                }
            });
            components.add(m1 = new GuiComponentSetNumber(30, 27, 40, 0, -1000, 1000) {
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
            components.add(m2 = new GuiComponentSetNumber(30, 47, 40, 1, -1000, 1000) {
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
            components.add(t = new GuiComponentSetNumber(30, 67, 40, 0, -1000, 1000) {
                @Override
                public void setValue(int i) {
                    numbersChanged();
                }
            });

            //Power
            components.add(new GuiComponentText("inventory.blockValuesConfig.power", 8, 90) {
                @Override
                public List<String> getDynamicToolTip(int x, int y) {
                    return Arrays.asList(StatCollector.translateToLocal("inventory.blockValuesConfig.power.toolTip"));
                }
            });
            components.add(p = new GuiComponentSetNumber(30, 87, 40, 0, -1000, 1000) {
                @Override
                public void setValue(int i) {
                    numbersChanged();
                }
            });

            //Y Offset
            components.add(new GuiComponentText("inventory.blockValuesConfig.yOffset", 80, 30) {
                @Override
                public List<String> getDynamicToolTip(int x, int y) {
                    return Arrays.asList(StatCollector.translateToLocal("inventory.blockValuesConfig.yOffset.toolTip"));
                }
            });
            components.add(b = new GuiComponentSetNumber(97, 27, 40, 0, -1000, 1000) {
                @Override
                public void setValue(int i) {
                    numbersChanged();
                }
            });

            //Floor
            components.add(new GuiComponentText("inventory.blockValuesConfig.floor", 80, 50) {
                @Override
                public List<String> getDynamicToolTip(int x, int y) {
                    return Arrays.asList(StatCollector.translateToLocal("inventory.blockValuesConfig.floor.toolTip"));
                }
            });
            components.add(f = new GuiComponentSetNumber(97, 47, 40, 0, -1000, 1000) {
                @Override
                public void setValue(int i) {
                    numbersChanged();
                }
            });

            //Ceiling
            components.add(new GuiComponentText("inventory.blockValuesConfig.ceiling", 80, 70) {
                @Override
                public List<String> getDynamicToolTip(int x, int y) {
                    return Arrays.asList(StatCollector.translateToLocal("inventory.blockValuesConfig.ceiling.toolTip"));
                }
            });
            components.add(c = new GuiComponentSetNumber(97, 67, 40, 0, -1000, 1000) {
                @Override
                public void setValue(int i) {
                    numbersChanged();
                }
            });

            components.add(new GuiComponentText("inventory.blockValuesConfig.ceiling", 80, 70) {
                @Override
                public List<String> getDynamicToolTip(int x, int y) {
                    return Arrays.asList(StatCollector.translateToLocal("inventory.blockValuesConfig.ceiling.toolTip"));
                }
            });
            components.add(new GuiComponentSetNumber(-100000, 67, 40, 0, -1000, 1000) {
                @Override
                public void setValue(int i) {
                    numbersChanged();
                }
            });

            //Ignore Meta
            components.add(new GuiComponentCheckBox(80, 90, "inventory.blockValuesConfig.ignoreMeta", ignoreMeta) {
                @Override
                public void setValue(boolean bool) {
                    ignoreMeta = !bool;
                }

                @Override
                public List<String> getDynamicToolTip(int x, int y) {
                    return Arrays.asList(StatCollector.translateToLocal("inventory.blockValuesConfig.ignoreMeta.toolTip"));
                }
            });

            components.add(graph = new GuiComponentGraph(170, 20, 124, 99, speed));
        }
    }

    protected void numbersChanged() {
        Calculation current;
        switch (currentMode) {
            default :
            case SPEED:
                current = speed;
                break;
            case EFFICIENCY:
                current = efficiency;
                break;
            case MULTIPLICITY:
                current = multiplicity;
                break;
        }

        current.setScaleFactorNumerator(m1.getValue());
        current.setScaleFactorDenominator(m2.getValue());
        current.setxOffset(t.getValue());
        current.setPower(p.getValue());
        current.setyOffset(b.getValue());
        current.setFloor(f.getValue());
        current.setCeiling(c.getValue());
        graph.setEquation(current);
    }

    protected void tryLoadFunctions() {
        Calculation current;

        if(currentBlock != null && BlockValueRegistry.INSTANCE.isBlockRegistered(currentBlock, ignoreMeta ? -1 : meta)) {

            speed = BlockValueRegistry.INSTANCE.getBlockValues(currentBlock, ignoreMeta ? -1 : meta).getSpeedFunction();
            efficiency = BlockValueRegistry.INSTANCE.getBlockValues(currentBlock, ignoreMeta ? -1 : meta).getEfficiencyFunction();
            multiplicity = BlockValueRegistry.INSTANCE.getBlockValues(currentBlock, ignoreMeta ? -1 : meta).getMultiplicityFunction();

            switch (currentMode) {
                default :
                case SPEED:
                    current = speed;
                    break;
                case EFFICIENCY:
                    current = efficiency;
                    break;
                case MULTIPLICITY:
                    current = multiplicity;
                    break;
            }
        } else {

            switch (currentMode) {
                default :
                case SPEED:
                    current = speed;
                    break;
                case EFFICIENCY:
                    current = efficiency;
                    break;
                case MULTIPLICITY:
                    current = multiplicity;
                    break;
            }
        }
        m1.setCurrentValue((int) current.getScaleFactorNumerator());
        m2.setCurrentValue((int) current.getScaleFactorDenominator());
        t.setCurrentValue((int) current.getxOffset());
        p.setCurrentValue((int) current.getPower());
        b.setCurrentValue((int) current.getyOffset());
        f.setCurrentValue((int) current.getFloor());
        c.setCurrentValue((int) current.getCeiling());
        graph.setEquation(current);
    }

    protected void reloadNewFunctions() {
        speed = new Calculation(0, 0, 0, 0, 0, 0, 0);
        efficiency = new Calculation(0, 0, 0, 0, 0, 0, 0);
        multiplicity = new Calculation(0, 0, 0, 0, 0, 0, 0);
        tryLoadFunctions();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        if(currentBlock == null || (inventory.itemName != null && inventory.currentBlock != null && inventory.currentBlock != currentBlock)) {
            this.title.setText(GuiHelper.GuiColor.YELLOW + currentMode.getName() + ": " + GuiHelper.GuiColor.GRAY + inventory.itemName);
            this.title.setXPos(xSize / 2);
            this.title.setXPos(this.title.getXPos() - (Minecraft.getMinecraft().fontRenderer.getStringWidth(this.title.getText()) / 2));
            currentBlock = inventory.currentBlock;
            meta = ignoreMeta ? -1 : inventory.meta;
            reloadNewFunctions();
        }
        super.drawGuiContainerForegroundLayer(x, y);
    }
}
