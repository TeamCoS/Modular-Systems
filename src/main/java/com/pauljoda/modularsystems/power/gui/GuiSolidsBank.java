package com.pauljoda.modularsystems.power.gui;

import com.pauljoda.modularsystems.power.container.ContainerSolidsBank;
import com.pauljoda.modularsystems.power.tiles.TileSolidsBankBank;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiSolidsBank extends GuiPowerBase<ContainerSolidsBank> {

   protected TileSolidsBankBank tileEntity;

    public GuiSolidsBank(InventoryPlayer player, TileSolidsBankBank tileEntity) {
        super(new ContainerSolidsBank(player, tileEntity), tileEntity, 200, 165, "inventory.solidspower.title");

        this.tileEntity = tileEntity;
    }

    @Override
    public void addComponents() {

    }
}
