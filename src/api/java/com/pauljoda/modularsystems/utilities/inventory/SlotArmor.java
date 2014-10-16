package com.pauljoda.modularsystems.utilities.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class SlotArmor extends Slot {

	 /**
     * The armor type that can be placed on that slot, it uses the same values of armorType field on ItemArmor.
     */
    final int armorType;

   
    /**
     * The Player
     */
    final EntityPlayer player;
    
	public SlotArmor(IInventory playerInventory, int par3, int par4, int par5, int par6, EntityPlayer thePlayer) {
		super(playerInventory, par3, par4, par5);
		player = thePlayer;
		armorType = par6;
	}

	/**
     * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the case
     * of armor slots)
     */
    public int getSlotStackLimit()
    {
        return 1;
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    public boolean isItemValid(ItemStack par1ItemStack)
    {
        Item item = (par1ItemStack == null ? null : par1ItemStack.getItem());
        return item != null && item.isValidArmor(par1ItemStack, armorType, player);
    }
    
    @Override
    public IIcon getBackgroundIconIndex()
    {
        return ItemArmor.func_94602_b(this.armorType);
    }
}
