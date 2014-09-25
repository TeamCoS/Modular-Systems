package com.pauljoda.modularsystems.enchanting.items;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;

import com.pauljoda.modularsystems.core.ModularSystems;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemEnchantingUpgrade extends Item {

	private Enchantment enchantment;
	public ItemEnchantingUpgrade(String unlocalizedName, Enchantment enchant)
	{
		super();
		enchantment = enchant;
		this.setUnlocalizedName("enchantmentUpgrade" + unlocalizedName);
		this.setCreativeTab(ModularSystems.tabModularSystems);
		this.setMaxStackSize(1);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		this.itemIcon = iconRegister.registerIcon("modularsystems:" + (this.getUnlocalizedName().substring(5)));
	}
	
	public Enchantment getEnchantment()
	{
		return enchantment;
	}
}
