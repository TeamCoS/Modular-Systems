package com.pauljoda.modularsystems.core.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.pauljoda.modularsystems.enchanting.gui.GuiElementEnchantmentAdjuster;
import com.pauljoda.modularsystems.enchanting.tiles.TileEntityEnchantmentAlter;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemStack;

public class EnchantHelper {

	/**
	 * Returns a list of seven enchants that the alter can use for display
	 * @param alter Alter Tile Entity
	 * @param itemstack Item to Enchant
	 * @param level Level to enchant
	 * @return
	 */
	public static List<Enchantment> getEnchantsForItemInAlter(TileEntityEnchantmentAlter alter, ItemStack itemstack, int level)
	{
		Map validEnchants = EnchantmentHelper.mapEnchantmentData(level, itemstack);
		List<Enchantment> list = new ArrayList<Enchantment>();
		List<Enchantment> l = alter.getEnchantsUnlocked();
		if(validEnchants != null && !validEnchants.isEmpty())
		{
			for(int i = 0; i < l.size(); i++)
			{
				if(validEnchants.containsKey(Integer.valueOf(l.get(i).effectId)))
					list.add(l.get(i));
			}
		}
		if(itemstack.getItem() instanceof ItemBook)
		{
			Random r = new Random();
			while(list.size() > 7)
				list.remove(r.nextInt(list.size()));
		}
		return list;
	}

	public static int getScaledValue(int level, TileEntityEnchantmentAlter alter)
	{
		int out;
		out = (int) (-(0.1 * Math.pow(alter.countBlocksInRange(2, Blocks.skull), 2)) + 10);
		if(out < 1)
			out = 1;
		return out * level;
	}

	public static int getRequiredLevelFromList(List<GuiElementEnchantmentAdjuster> enchants, TileEntityEnchantmentAlter alter) {
		int output = 0;
		for(int i = 0; i < enchants.size(); i++)
		{
			output += getScaledValue(enchants.get(i).level, alter);
		}
		return output;
	}
}
