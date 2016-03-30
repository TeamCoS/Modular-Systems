package com.teambr.modularsystems.core.managers

import com.teambr.modularsystems.core.lib.Reference
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.ModelLoader

/**
 * Modular-Systems
 * Created by Dyonovan on 05/08/15
 */
object ItemRenderManager {

    def registerItemRenderer(): Unit = {
        registerItem(ItemManager.dustGold)
        registerItem(ItemManager.dustIron)
    }

    def registerItem(item: Item): Unit = {
        Minecraft.getMinecraft.getRenderItem.getItemModelMesher.register(item, 0,
            new ModelResourceLocation(item.getUnlocalizedName.substring(5), "inventory"))
        ModelLoader.setCustomModelResourceLocation(item, 0,
            new ModelResourceLocation(item.getUnlocalizedName.substring(5), "inventory"))
    }

    def registerBlockModel(block : Block, name : String, variants : String, meta : Int = 0) : Unit = {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block),
            meta, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, name), variants))
    }
}
