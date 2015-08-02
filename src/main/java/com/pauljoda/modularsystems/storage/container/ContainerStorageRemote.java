package com.pauljoda.modularsystems.storage.container;

import com.pauljoda.modularsystems.storage.tiles.TileStorageRemote;
import com.teambr.bookshelf.inventory.BaseContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.SlotFurnace;

/**
 * Modular-Systems
 * Created by Dyonovan on 01/08/15
 */
public class ContainerStorageRemote extends BaseContainer {
    public ContainerStorageRemote(InventoryPlayer playerInventory, TileStorageRemote tileEntity) {
        super(playerInventory, tileEntity);
        addSlotToContainer(new RestrictedSlot(tileEntity, 0, 56, 35));
        addSlotToContainer(new SlotFurnace(playerInventory.player, tileEntity, 1, 116, 35));
        addPlayerInventorySlots(84);
    }
}
