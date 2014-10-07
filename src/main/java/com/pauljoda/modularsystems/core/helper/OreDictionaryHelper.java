package com.pauljoda.modularsystems.core.helper;

import com.pauljoda.modularsystems.core.crafting.OreProcessingRecipies;
import com.pauljoda.modularsystems.core.managers.ItemManager;
import com.pauljoda.modularsystems.oreprocessing.items.ItemDust;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictionaryHelper
{
    public static void initDustsAsNeeded()
    {
        if(OreDictionary.getOres("dustIron").isEmpty())
        {
            ItemManager.ironDust = new ItemDust("modularsystems:ironDust");
            GameRegistry.registerItem(ItemManager.ironDust, "ironDust");
            OreDictionary.registerOre("dustIron", ItemManager.ironDust);
            GameRegistry.addSmelting(ItemManager.ironDust, new ItemStack(Items.iron_ingot, 1), 0.5F);
        }

        if(OreDictionary.getOres("dustGold").isEmpty())
        {
            ItemManager.goldDust = new ItemDust("modularsystems:goldDust");
            GameRegistry.registerItem(ItemManager.goldDust, "goldDust");
            OreDictionary.registerOre("dustGold", ItemManager.goldDust);
            GameRegistry.addSmelting(ItemManager.goldDust, new ItemStack(Items.gold_ingot, 1), 0.7F);
        }
    }

    public static ItemStack getOreOutput(ItemStack input)
    {
        ItemStack output = null;
        if(OreDictionary.getOreIDs(input).length > 0)
        {
            int oreId = OreDictionary.getOreIDs(input)[0];
            String oreName = OreDictionary.getOreName(oreId);
            if(oreName.contains("ore"))
            {
                String baseName = oreName.split("ore", 50)[1];
                String dustName = "dust".concat(baseName);
                if (!OreDictionary.getOres(dustName).isEmpty())
                {
                    output = OreDictionary.getOres(dustName).get(0).copy();
                    output.stackSize++;
                }
            }
        }
        return output;
    }

    public static void initOreProcessingRecipes()
    {
        String[] oresDefined = OreDictionary.getOreNames();
        for(int i = 0; i < oresDefined.length; i++)
        {
            if(!OreDictionary.getOres(oresDefined[i]).isEmpty() && oresDefined[i].contains("ore"))
            {
                ItemStack input = OreDictionary.getOres(oresDefined[i]).get(0);
                if(getOreOutput(input) != null)
                    OreProcessingRecipies.addOreProcessingRecipe(input, getOreOutput(input));
            }
        }
    }
}
