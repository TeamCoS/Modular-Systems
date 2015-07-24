package com.pauljoda.modularsystems.generator.container;

import com.pauljoda.modularsystems.generator.tiles.TileGeneratorCore;
import com.teambr.bookshelf.inventory.BaseContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

public class ContainerGenerator extends BaseContainer {
    public ContainerGenerator(InventoryPlayer playerInventory, TileGeneratorCore tileEntity) {
        super(playerInventory, tileEntity);

        //To Charge
        addSlotToContainer(new Slot(tileEntity, 0, 50, 20));
        //to Discharge
        addSlotToContainer(new Slot(tileEntity, 1, 50, 60));

        addPlayerInventorySlots(8, 84);
    }
}
