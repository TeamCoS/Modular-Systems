package com.teamcos.modularsystems.enchanting.container;

import com.teamcos.modularsystems.core.helper.EnchantHelper;
import com.teamcos.modularsystems.enchanting.tiles.TileEntityEnchantmentAlter;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemStack;

public class ContainerModularEnchanting extends Container {

	public TileEntityEnchantmentAlter alter;

	public ContainerModularEnchanting(InventoryPlayer playerInventory, TileEntityEnchantmentAlter tileEntityAlter, final EntityPlayer thePlayer)
	{
		alter = tileEntityAlter;
		addSlotToContainer(new Slot(alter, 0, 24, 24));
		bindPlayerInventory(playerInventory);
	}
	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
						48 + j * 18, 145 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventoryPlayer, i, 48 + i * 18, 203));
		}
	}


	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		return true;
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
			if (slot < 1) {
				if (!this.mergeItemStack(stackInSlot, 1, 37, true)) {
					return null;
				}
			}
			//places it into the tileEntity if possible since its in the player inventory
			else if (!this.mergeItemStack(stackInSlot, 0, 1, false)) {
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
	
	public void addEnchantment(int enchantId, int level, EntityPlayer player) 
	{
		if(alter.inv[0] != null && (player.experienceLevel >= EnchantHelper.getScaledValue(level, alter) || player.capabilities.isCreativeMode))
		{
			Enchantment enchantment = Enchantment.enchantmentsList[enchantId];

            if(alter.inv[0].getItem() instanceof ItemBook)
            {
                alter.inv[0].func_150996_a(Items.enchanted_book);
                Items.enchanted_book.addEnchantment(alter.inv[0], new EnchantmentData(enchantId, level));
                if(!player.capabilities.isCreativeMode)
                    player.addExperienceLevel(-(EnchantHelper.getScaledValue(level, alter)));
                return;
            }

			alter.inv[0].addEnchantment(enchantment, level);
			if(!player.capabilities.isCreativeMode)
				player.addExperienceLevel(-(EnchantHelper.getScaledValue(level, alter)));
		}
	}

}
