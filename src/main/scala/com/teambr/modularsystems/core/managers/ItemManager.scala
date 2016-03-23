package com.teambr.modularsystems.core.managers

import com.teambr.modularsystems.core.common.items.BaseItem
import net.minecraft.init.Items
import net.minecraft.item.{ ItemStack, Item }
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.oredict.OreDictionary

/**
 * Modular-Systems
 * Created by Dyonovan on 05/08/15
 */
object ItemManager {

    var dustGold = new BaseItem("dustGold", 64)
    var dustIron = new BaseItem("dustIron", 64)

    def preInit(): Unit = {
    }

    def init(): Unit = {
        OreDictionary.getOres("dustIron").isEmpty match {
            case true =>
                registerItem(dustGold, "dustGold", "dustGold")
                GameRegistry.addSmelting(dustIron, new ItemStack(Items.iron_ingot), 1.0F)
                registerItem(dustIron, "dustIron", "dustIron")
                GameRegistry.addSmelting(dustGold, new ItemStack(Items.gold_ingot), 2.0F)
            case _ =>
        }
    }

    /**
     * Helper method to register items
     * @param item The item to register
     * @param name The name of the item
     * @param oreDict The ore dict tag
     */
    private def registerItem(item: Item, name: String, oreDict: String) {
        GameRegistry.registerItem(item, name)
        if (oreDict != null) OreDictionary.registerOre(oreDict, item)
    }

    private def registerItem(item: Item, name: String) {
        registerItem(item, name, null)
    }

}
