package com.pauljoda.modularsystems.power.gui;

import com.pauljoda.modularsystems.power.container.ContainerSolidsBank;
import com.pauljoda.modularsystems.power.tiles.TileSolidsBank;
import com.teambr.bookshelf.client.gui.component.display.GuiComponentPowerBar;
import net.minecraft.entity.player.InventoryPlayer;

import java.awt.*;
import java.util.ArrayList;

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
