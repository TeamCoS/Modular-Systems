package com.pauljoda.modularsystems.power.gui;

import com.pauljoda.modularsystems.power.container.ContainerSolidsBank;
import com.pauljoda.modularsystems.power.tiles.TileSolidsBank;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiSolidsBank extends GuiPowerBase<ContainerSolidsBank> {

   protected TileSolidsBank tileEntity;

    public GuiSolidsBank(InventoryPlayer player, TileSolidsBank tileEntity) {
        super(new ContainerSolidsBank(player, tileEntity), tileEntity, 200, 165, "inventory.solidspower.title");

        this.tileEntity = tileEntity;
    }

    @Override
    public void addComponents() {

    }
}
