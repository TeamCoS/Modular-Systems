package com.pauljoda.modularsystems.enchanting.container;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.util.EnchantmentNameParts;

import com.pauljoda.modularsystems.core.helper.EnchantHelper;
import com.pauljoda.modularsystems.enchanting.tiles.TileEntityEnchantmentAlter;
import com.pauljoda.modularsystems.storage.tiles.TileEntityStorageCore;

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
	
	public void addEnchantment(int enchantId, int level, EntityPlayer player) 
	{
		if(alter.inv[0] != null && (player.experienceLevel >= EnchantHelper.getScaledValue(level, alter) || player.capabilities.isCreativeMode))
		{
			Enchantment enchantment = Enchantment.enchantmentsList[enchantId];
			alter.inv[0].addEnchantment(enchantment, level);
			if(!player.capabilities.isCreativeMode)
				player.addExperienceLevel(-(EnchantHelper.getScaledValue(level, alter)));
		}
	}

}
