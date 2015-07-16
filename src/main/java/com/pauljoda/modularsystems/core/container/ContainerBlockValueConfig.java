package com.pauljoda.modularsystems.core.container;

import com.teambr.bookshelf.inventory.BaseContainer;
import com.teambr.bookshelf.inventory.GenericInventory;
import com.teambr.bookshelf.inventory.IInventoryCallback;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerBlockValueConfig extends BaseContainer {
    public String itemName;
    public Block currentBlock;
    public int meta = -1;

    public ContainerBlockValueConfig(IInventory playerInventory) {
        super(playerInventory, new GenericInventory("inventory.config", true, 1));
        ((GenericInventory)inventory).addCallback(new IInventoryCallback() {
            @Override
            public void onInventoryChanged(IInventory inventory, int slotNumber) {
                if (inventory.getStackInSlot(slotNumber) != null) {
                    itemName = inventory.getStackInSlot(slotNumber).getDisplayName();
                    currentBlock = Block.getBlockFromItem(inventory.getStackInSlot(slotNumber).getItem());
                    meta = inventory.getStackInSlot(slotNumber).getItemDamage();
                }
            }
        });

        addSlotToContainer(new Slot(inventory, 0, 8, 8));

        addPlayerInventorySlots(8, 120);
    }

    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        transferStackInSlot(player, 0);
    }
}
