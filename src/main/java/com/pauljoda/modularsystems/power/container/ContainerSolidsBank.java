package com.pauljoda.modularsystems.power.container;

import com.pauljoda.modularsystems.power.tiles.TileBankSolids;
import com.teambr.bookshelf.inventory.BaseContainer;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerSolidsBank extends BaseContainer {
    public ContainerSolidsBank(InventoryPlayer playerInventory, TileBankSolids tileEntity) {
        super(playerInventory, tileEntity);
        addInventoryGrid(8, 20, 9);
        addPlayerInventorySlots(8, 84);
    }
}
