package com.pauljoda.modularsystems.core.managers;

import com.pauljoda.modularsystems.core.items.BaseItem;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Used to register our items
 */
public class ItemManager {

    public static Item dustIron, dustGold;

    public static void init() {
        //We need to register the iron and gold dust
        if (!OreDictionary.doesOreNameExist("dustIron")) {
            registerItem(dustIron = new BaseItem("dustIron", 64), "dustIron", "dustIron");
            registerItem(dustGold = new BaseItem("dustGold", 64), "dustGold", "dustGold");
        }
    }

    /**
     * Helper method to register items
     * @param item The item to register
     * @param name The name of the item
     * @param oreDict The ore dict tag
     */
    private static void registerItem(Item item, String name, String oreDict) {
        GameRegistry.registerItem(item, name);
        if (oreDict != null)
            OreDictionary.registerOre(oreDict, item);
    }

    private static void registerItem(Item item, String name) {
        registerItem(item, name, null);
    }
}
