package com.pauljoda.modularsystems.enchanting.container;

import com.pauljoda.modularsystems.enchanting.tiles.TileEntityEnchantmentAlter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerEnchantmentUpgrades extends Container {

	public TileEntityEnchantmentAlter alter;

	public ContainerEnchantmentUpgrades(InventoryPlayer playerInventory, TileEntityEnchantmentAlter tileEntityAlter, final EntityPlayer thePlayer)
	{
		alter = tileEntityAlter;

		for (int i = 0; i < 3; i++)
		{
			if(i < 2)
			{
				for (int j = 0; j < 9; j++)
				{
					addSlotToContainer(new SlotEnchantingUpgrade(alter, (j + i * 9) + 1, 8 + j * 18, 17 + i * 18, alter));
				}
			}
			else
			{
				for (int j = 0; j < 6; j++)
				{
					addSlotToContainer(new SlotEnchantingUpgrade(alter, (j + i * 9) + 1, 8 + j * 18, 17 + i * 18, alter));
				}
			}
		}

		bindPlayerInventory(playerInventory);
	}
	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
						8 + j * 18, 84 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
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
			if (slot < 25) {
				if (!this.mergeItemStack(stackInSlot, 25, 60, true)) {
					return null;
				}
			}
			//places it into the tileEntity if possible since its in the player inventory
			else if(alter.canPlaceUpgrade(stackInSlot))
			{
				if (!this.mergeItemStack(stackInSlot, 0, 24, false)) {
					return null;
				}
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
		return true;
	}
}