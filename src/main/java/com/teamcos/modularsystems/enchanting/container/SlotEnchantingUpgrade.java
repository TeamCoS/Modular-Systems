package com.teamcos.modularsystems.enchanting.container;

import com.teamcos.modularsystems.enchanting.tiles.TileEntityEnchantmentAlter;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotEnchantingUpgrade extends Slot {

	TileEntityEnchantmentAlter alter;
	public SlotEnchantingUpgrade(IInventory inventory, int par2, int par3, int par4, TileEntityEnchantmentAlter tile)
	{
		super(inventory, par2, par3, par4);
		alter = tile;
	}
	
	public int getSlotStackLimit()
    {
        return 1;
    }

	public boolean isItemValid(ItemStack par1ItemStack)
    {
		return alter.canPlaceUpgrade(par1ItemStack);
    }
}
