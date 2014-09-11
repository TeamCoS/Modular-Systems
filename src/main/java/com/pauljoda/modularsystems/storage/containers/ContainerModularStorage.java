package com.pauljoda.modularsystems.storage.containers;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.pauljoda.modularsystems.storage.gui.GuiModularStorage;
import com.pauljoda.modularsystems.storage.tiles.TileEntityStorageCore;

public class ContainerModularStorage extends Container {

	public TileEntityStorageCore storageCore;
	public List itemList = new ArrayList();
	public int currentBottomRow = 0;
	public ContainerModularStorage(InventoryPlayer playerInventory, TileEntityStorageCore tileEntityStorageCore)
	{
		storageCore = tileEntityStorageCore;

		for (int i = 0; i < storageCore.inventoryRows; i++)
		{
			for (int j = 0; j < 11; j++)
			{
				if(i < 6)
					addSlotToContainer(new Slot(tileEntityStorageCore, j + i * 11, 8 + j * 18, 18 + i * 18));
				else
					addSlotToContainer(new Slot(tileEntityStorageCore, j + i * 11, -1000, -1000));
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
			if (slot < storageCore.inventoryRows * 11) {
				if (!this.mergeItemStack(stackInSlot, storageCore.inventoryRows * 11, (storageCore.inventoryRows * 11) + 36, true)) {
					return null;
				}
			}
			//places it into the tileEntity if possible since its in the player inventory
			else if (!this.mergeItemStack(stackInSlot, currentBottomRow * 11, (currentBottomRow * 11) + 65, false)) {
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

	public void scrollTo(float index)
	{
		int i = 1;
		int j = (int)((double)(index * (float)i) + 0.5D);

		this.currentBottomRow = j;
		if (j < 0)
		{
			j = 0;
		}

		for(int it = 0; it < this.storageCore.inventoryRows * 11; it++)
		{
			Slot slot = (Slot)this.inventorySlots.get(it);
			slot.xDisplayPosition = -1000;
			slot.yDisplayPosition = -1000;
		}

		for (int f = 0; f < 6; f++)
		{
			for (int s = 0; s < 11; s++)
			{
					Slot slot = (Slot)this.inventorySlots.get((s + (f+j) * 11));
					slot.xDisplayPosition = 8 + s * 18;
					slot.yDisplayPosition = 18 + f * 18;
			}
		}
	}
	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		// TODO Auto-generated method stub
		return true;
	}
}
