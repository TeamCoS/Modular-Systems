package com.pauljoda.modularsystems.storage.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.pauljoda.modularsystems.storage.tiles.TileEntityStorageCore;

public class ContainerModularStorage extends Container {

	private TileEntityStorageCore storageCore;

	public ContainerModularStorage(InventoryPlayer playerInventory, TileEntityStorageCore tileEntityStorageCore)
	{
		storageCore = tileEntityStorageCore;

		for (int i = 0; i < 6; i++)
		{
			for (int j = 0; j < 11; j++)
			{
				addSlotToContainer(new Slot(tileEntityStorageCore, j + i * 11, 8 + j * 18, 18 + i * 18));
			}
		}

		bindPlayerInventory(playerInventory);
	}
	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
						26 + j * 18, 140 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventoryPlayer, i, 26 + i * 18, 198));
		}
	}
	
	@Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
            ItemStack stack = null;
            Slot slotObject = (Slot)this.inventorySlots.get(slot);
            //null checks and checks if the item can be stacked (maxStackSize > 1)
            if (slotObject != null && slotObject.getHasStack()) {
                    ItemStack stackInSlot = slotObject.getStack();
                    stack = stackInSlot.copy();
                    //merges the item into player inventory since its in the tileEntity
                    if (slot < 66) {
                            if (!this.mergeItemStack(stackInSlot, 66, 102, true)) {
                                    return null;
                            }
                    }
                    //places it into the tileEntity is possible since its in the player inventory
                    else if (!this.mergeItemStack(stackInSlot, 0, 65, false)) {
                            return null;
                    }

                    if (stackInSlot.stackSize == 0) {
                            slotObject.putStack(null);
                    } else {
                            slotObject.onSlotChanged();
                    }

                    if (stackInSlot.stackSize == stack.stackSize) {
                            return null;
                    }
                    slotObject.onPickupFromSlot(player, stackInSlot);
            }
            return stack;
    }
	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		// TODO Auto-generated method stub
		return true;
	}
}
