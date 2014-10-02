package com.pauljoda.modularsystems.core.helper;

import com.pauljoda.modularsystems.enchanting.gui.GuiElementEnchantmentAdjuster;
import com.pauljoda.modularsystems.enchanting.tiles.TileEntityEnchantmentAlter;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemStack;

import java.util.*;

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

		Map validEnchants = getValidEnchantments(itemstack);
		List<Enchantment> list = new ArrayList<Enchantment>();
		List<Enchantment> l = alter.getEnchantsUnlocked();
        Random r = new Random();
		if(validEnchants != null && !validEnchants.isEmpty())
		{
			for(int i = 0; i < l.size(); i++)
			{
				if(validEnchants.containsKey(Integer.valueOf(l.get(i).effectId)))
					list.add(l.get(i));
			}
            while(list.size() > 7)
                list.remove(r.nextInt(list.size()));
		}
		if(itemstack.getItem() instanceof ItemBook)
		{
			while(list.size() > 7)
				list.remove(r.nextInt(list.size()));
		}
		return list;
	}

    public static Map getValidEnchantments(ItemStack itemstack)
    {
        HashMap map = null;
        Enchantment[] enchantList = Enchantment.enchantmentsList;
        for(int i = 0; i < enchantList.length; i++)
        {
            Enchantment temp = enchantList[i];
            if(temp == null) continue;
            if(temp.canApply(itemstack) || ((itemstack.getItem() == Items.book) && temp.isAllowedOnBooks()))
            {
                if (map == null)
                {
                    map = new HashMap();
                }

                map.put(Integer.valueOf(temp.effectId), new EnchantmentData(temp, 1));
            }
        }


        return map;
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
