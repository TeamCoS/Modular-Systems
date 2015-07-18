package com.pauljoda.modularsystems.core.managers;

import com.pauljoda.modularsystems.core.items.BaseItem;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;

public class ItemManager {

    public static Item dustIron, dustGold;

    public static void init(Boolean state) {
        if (state) {
            registerItem(dustIron = new BaseItem("dustIron", 64), "dustIron", "dustIron");
            registerItem(dustGold = new BaseItem("dustGold", 64), "dustGold", "dustGold");
        } else {
            //Other Items Here
        }
    }

    private static void registerItem(Item item, String name, String oreDict) {
        GameRegistry.registerItem(item, name);
        if (oreDict != null)
            OreDictionary.registerOre(oreDict, item);
    }

    private static void registerItem(Item item, String name) {
        registerItem(item, name, null);
    }
}
