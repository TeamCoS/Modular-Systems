package com.pauljoda.modularsystems.power.gui;

import com.pauljoda.modularsystems.power.container.ContainerSolidsBank;
import com.pauljoda.modularsystems.power.tiles.TileBankSolids;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiSolidsBank extends GuiPowerBase<ContainerSolidsBank> {

   protected TileBankSolids tileEntity;

    public GuiSolidsBank(InventoryPlayer player, TileBankSolids tileEntity) {
        super(new ContainerSolidsBank(player, tileEntity), tileEntity, 175, 165, "inventory.solidspower.title");

        this.tileEntity = tileEntity;
    }

    @Override
    public void addComponents() {}
}
