package com.teambr.modularsystems.core.client.gui;

import com.teambr.bookshelf.client.gui.GuiBase;
import com.teambr.bookshelf.client.gui.GuiColor;
import com.teambr.bookshelf.client.gui.component.control.GuiComponentCheckBox;
import com.teambr.bookshelf.client.gui.component.control.GuiComponentSetNumber;
import com.teambr.bookshelf.client.gui.component.display.GuiComponentText;
import com.teambr.bookshelf.helper.GuiHelper;
import com.teambr.modularsystems.core.collections.Calculation;
import com.teambr.modularsystems.core.common.container.ContainerBlockValueConfig;
import com.teambr.modularsystems.core.network.AddCalculationPacket;
import com.teambr.modularsystems.core.network.DeleteValuesPacket;
import com.teambr.modularsystems.core.network.PacketManager;
import com.teambr.modularsystems.core.registries.BlockValueRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.text.translation.I18n;
import org.lwjgl.input.Keyboard;
import scala.collection.mutable.ArrayBuffer;

import java.util.Collections;
import java.util.List;

/**
 * This file was created for Modular-Systems
 * <p/>
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis "pauljoda"
 * @since 3/24/2016
 */
public class GuiBlockValueConfig extends GuiBase<ContainerBlockValueConfig> {

    protected GuiComponentSetNumber m1, m2, t, p, b, f, c;
    protected GuiComponentGraph graph;
    protected Calculation speed, efficiency, multiplicity;
    protected Block currentBlock;
    protected int meta = -1;
    protected boolean ignoreMeta = true;
    protected MODE currentMode = MODE.SPEED;

    /**
     * Used to hold what mode we are in as well as get their translated name for display
     */
    private enum MODE {
        SPEED("inventory.blockValuesConfig.speed"),
        EFFICIENCY("inventory.blockValuesConfig.efficiency"),
        MULTIPLICITY("inventory.blockValuesConfig.multiplicity");

        private String label;

        MODE(String str) {
            label = I18n.translateToLocal(str);
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

        this.buttonList.add(new GuiButton(0, x + 170, y + 145, 60, 20, I18n.translateToLocal("inventory.blockValuesConfig.speed")));
        this.buttonList.add(new GuiButton(1, x + 235, y + 145, 60, 20, I18n.translateToLocal("inventory.blockValuesConfig.efficiency")));
        this.buttonList.add(new GuiButton(2, x + 170, y + 175, 60, 20, I18n.translateToLocal("inventory.blockValuesConfig.multiplicity")));
        this.buttonList.add(new GuiButton(3, x + 235, y + 175, 60, 20, I18n.translateToLocal("inventory.blockValuesConfig.save")));
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
                    if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                        PacketManager.net.sendToServer(new DeleteValuesPacket(Block.getIdFromBlock(currentBlock), ignoreMeta ? -1 : meta));
                    } else {
                        PacketManager.net.sendToServer(new AddCalculationPacket.CalculationMessage(currentBlock, ignoreMeta ? -1 : meta,
                                speed,
                                efficiency,
                                multiplicity)); //We want the server to know what we've done
                    }
                    tryLoadFunctions();
                }
        }
        if(currentBlock != null) //Set the title to what we are doing now
            this.title().setText(GuiColor.YELLOW + currentMode.getName() + ": " + GuiColor.GRAY + inventory().itemName);
    }

    @Override
    public void addComponents() {
        if(speed != null) {
            //Formula
            components().$plus$eq(new GuiComponentText(I18n.translateToLocal("inventory.blockValuesConfig.formula"), 175, 125));

            //Scale Numerator
            components().$plus$eq(new GuiComponentText(I18n.translateToLocal("inventory.blockValuesConfig.scaleFactorNumerator"), 8, 30) {
                @Override
                public ArrayBuffer<String> getDynamicToolTip(int x, int y) {
                    return new ArrayBuffer<String>().$plus$eq(I18n.translateToLocal("inventory.blockValuesConfig.scaleFactorNumerator.toolTip"));
                }
            });
            components().$plus$eq(m1 = new GuiComponentSetNumber(30, 27, 40, 0, -1000, 1000) {
                @Override
                public void setValue(int i) {
                    numbersChanged();
                }
            });

            //Scale Denominator
            components().$plus$eq(new GuiComponentText(I18n.translateToLocal("inventory.blockValuesConfig.scaleDenominator"), 8, 50) {
                @Override
                public ArrayBuffer<String> getDynamicToolTip(int x, int y) {
                    return new ArrayBuffer<String>().$plus$eq(I18n.translateToLocal("inventory.blockValuesConfig.scaleDenominator.toolTip"));
                }
            });
            components().$plus$eq(m2 = new GuiComponentSetNumber(30, 47, 40, 1, -1000, 1000) {
                @Override
                public void setValue(int i) {
                    numbersChanged();
                }
            });

            //X Offset
            components().$plus$eq(new GuiComponentText(I18n.translateToLocal("inventory.blockValuesConfig.xOffset"), 8, 70) {
                @Override
                public ArrayBuffer<String> getDynamicToolTip(int x, int y) {
                    return new ArrayBuffer<String>().$plus$eq(I18n.translateToLocal("inventory.blockValuesConfig.xOffset.toolTip"));
                }
            });
            components().$plus$eq(t = new GuiComponentSetNumber(30, 67, 40, 0, -1000, 1000) {
                @Override
                public void setValue(int i) {
                    numbersChanged();
                }
            });

            //Power
            components().$plus$eq(new GuiComponentText(I18n.translateToLocal("inventory.blockValuesConfig.power"), 8, 90) {
                @Override
                public ArrayBuffer<String> getDynamicToolTip(int x, int y) {
                    return new ArrayBuffer<String>().$plus$eq(I18n.translateToLocal("inventory.blockValuesConfig.power.toolTip"));
                }
            });
            components().$plus$eq(p = new GuiComponentSetNumber(30, 87, 40, 0, -1000, 1000) {
                @Override
                public void setValue(int i) {
                    numbersChanged();
                }
            });

            //Y Offset
            components().$plus$eq(new GuiComponentText(I18n.translateToLocal("inventory.blockValuesConfig.yOffset"), 80, 30) {
                @Override
                public ArrayBuffer<String> getDynamicToolTip(int x, int y) {
                    return new ArrayBuffer<String>().$plus$eq(I18n.translateToLocal("inventory.blockValuesConfig.yOffset.toolTip"));
                }
            });
            components().$plus$eq(b = new GuiComponentSetNumber(97, 27, 40, 0, -1000, 1000) {
                @Override
                public void setValue(int i) {
                    numbersChanged();
                }
            });

            //Floor
            components().$plus$eq(new GuiComponentText(I18n.translateToLocal("inventory.blockValuesConfig.floor"), 80, 50) {
                @Override
                public ArrayBuffer<String> getDynamicToolTip(int x, int y) {
                    return new ArrayBuffer<String>().$plus$eq(I18n.translateToLocal("inventory.blockValuesConfig.floor.toolTip"));
                }
            });
            components().$plus$eq(f = new GuiComponentSetNumber(97, 47, 40, 0, -1000, 1000) {
                @Override
                public void setValue(int i) {
                    numbersChanged();
                }
            });

            //Ceiling
            components().$plus$eq(new GuiComponentText(I18n.translateToLocal("inventory.blockValuesConfig.ceiling"), 80, 70) {
                @Override
                public ArrayBuffer<String> getDynamicToolTip(int x, int y) {
                    return new ArrayBuffer<String>().$plus$eq(I18n.translateToLocal("inventory.blockValuesConfig.ceiling.toolTip"));
                }
            });
            components().$plus$eq(c = new GuiComponentSetNumber(97, 67, 40, 0, -1000, 1000) {
                @Override
                public void setValue(int i) {
                    numbersChanged();
                }
            });

            components().$plus$eq(new GuiComponentText(I18n.translateToLocal("inventory.blockValuesConfig.ceiling"), 80, 70) {
                @Override
                public ArrayBuffer<String> getDynamicToolTip(int x, int y) {
                    return new ArrayBuffer<String>().$plus$eq(I18n.translateToLocal("inventory.blockValuesConfig.ceiling.toolTip"));
                }
            });
            components().$plus$eq(new GuiComponentSetNumber(-100000, 67, 40, 0, -1000, 1000) {
                @Override
                public void setValue(int i) {
                    numbersChanged();
                }
            });

            //Ignore Meta
            components().$plus$eq(new GuiComponentCheckBox(80, 90, I18n.translateToLocal("inventory.blockValuesConfig.ignoreMeta"), ignoreMeta) {
                @Override
                public void setValue(boolean bool) {
                    ignoreMeta = !bool;
                }

                @Override
                public ArrayBuffer<String> getDynamicToolTip(int x, int y) {
                    return new ArrayBuffer<String>().$plus$eq(I18n.translateToLocal("inventory.blockValuesConfig.ignoreMeta.toolTip"));
                }
            });

            components().$plus$eq(graph = new GuiComponentGraph(170, 20, 124, 99, speed));
        }
    }

    /**
     * Called when a number has been changed. We need to update things
     */
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

        if(!isLoading) {
            current.setScaleFactorNumerator(m1.getValue());
            current.setScaleFactorDenominator(m2.getValue());
            current.setXOffset(t.getValue());
            current.setPower(p.getValue());
            current.setYOffset(b.getValue());
            current.setFloor(f.getValue());
            current.setCeiling(c.getValue());
        }
        graph.setEquation(current);
    }

    /**
     * Tries to load the function for the current block
     */
    boolean isLoading = true;
    protected void tryLoadFunctions() {
        Calculation current;
        isLoading = true;
        if(currentBlock != null && BlockValueRegistry.INSTANCE.isBlockRegistered(currentBlock, ignoreMeta ? -1 : meta)) {

            speed = new Calculation(BlockValueRegistry.INSTANCE.getBlockValues(currentBlock, ignoreMeta ? -1 : meta).getSpeedFunction());
            efficiency = new Calculation(BlockValueRegistry.INSTANCE.getBlockValues(currentBlock, ignoreMeta ? -1 : meta).getEfficiencyFunction());
            multiplicity = new Calculation(BlockValueRegistry.INSTANCE.getBlockValues(currentBlock, ignoreMeta ? -1 : meta).getMultiplicityFunction());

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
        m1.setValue((int) current.getScaleFactorNumerator());
        m2.setValue((int) current.getScaleFactorDenominator());
        t.setValue((int) current.getXOffset());
        p.setValue((int) current.getPower());
        b.setValue((int) current.getYOffset());
        f.setValue((int) current.getFloor());
        c.setValue((int) current.getCeiling());
        graph.setEquation(current);
        isLoading = false;
    }

    /**
     * Creates new functions so we can edit them again and not touch the last
     */
    protected void reloadNewFunctions() {
        speed = new Calculation(0, 0, 0, 0, 0, 0, 0);
        efficiency = new Calculation(0, 0, 0, 0, 0, 0, 0);
        multiplicity = new Calculation(0, 0, 0, 0, 0, 0, 0);
        tryLoadFunctions();
    }

    @Override
    public void drawGuiContainerForegroundLayer(int x, int y) {
        if(currentBlock == null || (inventory().itemName != null && inventory().currentBlock != null && inventory().currentBlock != currentBlock)) {
            this.title().setText(GuiColor.YELLOW + currentMode.getName() + ": " + GuiColor.GRAY + inventory().itemName);
            this.title().setXPos(xSize / 2);
            this.title().setXPos(this.title().getXPos() - (Minecraft.getMinecraft().fontRendererObj.getStringWidth(this.title().label()) / 2));
            currentBlock = inventory().currentBlock;
            meta = ignoreMeta ? -1 : inventory().meta;
            reloadNewFunctions();
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
            this.buttonList.get(3).displayString = I18n.translateToLocal("inventory.blockValuesConfig.delete");
        else
            this.buttonList.get(3).displayString = I18n.translateToLocal("inventory.blockValuesConfig.save");
        super.drawGuiContainerForegroundLayer(x, y);
    }
}
