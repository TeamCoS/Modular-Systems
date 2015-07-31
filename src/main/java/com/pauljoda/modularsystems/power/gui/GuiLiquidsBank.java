package com.pauljoda.modularsystems.power.gui;

import com.pauljoda.modularsystems.power.container.ContainerLiquidsBank;
import com.pauljoda.modularsystems.power.tiles.TileBankLiquid;
import com.teambr.bookshelf.client.gui.component.display.GuiComponentArrow;
import com.teambr.bookshelf.client.gui.component.display.GuiComponentFluidTank;
import com.teambr.bookshelf.helpers.GuiHelper;
import net.minecraft.entity.player.InventoryPlayer;

import java.util.ArrayList;
import java.util.List;

public class GuiLiquidsBank extends GuiPowerBase<ContainerLiquidsBank> {

    protected TileBankLiquid tileEntity;

    public GuiLiquidsBank(InventoryPlayer inventory, TileBankLiquid tileEntity) {
        super(new ContainerLiquidsBank(inventory, tileEntity), tileEntity, 175, 165, "inventory.liquidspower.title");
        this.tileEntity = tileEntity;
        addComponents();
    }

    @Override
    public void addComponents() {
        if(tileEntity != null) {
            components.add(new GuiComponentFluidTank(80, 18, 75, 60, tileEntity.tank) {
                @Override
                public List<String> getDynamicToolTip(int mouseX, int mouseY) {
                    ArrayList<String> toolTipLiquid = new ArrayList<>();
                    toolTipLiquid.add(tileEntity.tank.getFluid() != null ? GuiHelper.GuiColor.YELLOW + tileEntity.tank.getFluid().getLocalizedName() : "Empty");
                    toolTipLiquid.add(tileEntity.tank.getFluidAmount() + " / " + tileEntity.tank.getCapacity() + "mb");
                    return toolTipLiquid;
                }
            });

            components.add(new GuiComponentArrow(48, 20) {
                @Override
                public int getCurrentProgress() {
                    return 0;
                }
            });
        }
    }
}
