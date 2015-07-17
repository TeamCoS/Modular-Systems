package com.pauljoda.modularsystems.generator.gui;

import com.pauljoda.modularsystems.core.gui.GuiCoreBase;
import com.pauljoda.modularsystems.core.tiles.AbstractCore;
import com.pauljoda.modularsystems.crusher.tiles.TileCrusherCore;
import com.pauljoda.modularsystems.generator.container.ContainerGenerator;
import com.pauljoda.modularsystems.generator.tiles.TileGeneratorCore;
import com.teambr.bookshelf.collections.Location;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiGenerator extends GuiCoreBase<ContainerGenerator> {

    protected TileCrusherCore core;
    protected Location tileLocation;

    public GuiGenerator(InventoryPlayer player, TileGeneratorCore tileEntity) {
        super(new ContainerGenerator(player, tileEntity), tileEntity, 175, 165, "inventory.generator.title");
    }
}
