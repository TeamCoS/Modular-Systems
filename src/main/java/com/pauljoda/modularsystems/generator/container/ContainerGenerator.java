package com.pauljoda.modularsystems.generator.container;

import com.pauljoda.modularsystems.generator.tiles.TileGeneratorCore;
import com.teambr.bookshelf.inventory.BaseContainer;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerGenerator extends BaseContainer {
    public ContainerGenerator(InventoryPlayer playerInventory, TileGeneratorCore tileEntity) {
        super(playerInventory, tileEntity);
    }
}
