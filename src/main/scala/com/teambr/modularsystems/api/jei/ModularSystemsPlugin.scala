package com.teambr.modularsystems.api.jei

import java.awt.Rectangle

import com.teambr.bookshelf.client.gui.GuiBase
import com.teambr.modularsystems.api.jei.modularcrusher.{CrusherRecipeCatagory, CrusherRecipeHandler, CrusherRecipeMaker}
import com.teambr.modularsystems.api.jei.transfer.CraftingTransferHandler
import com.teambr.modularsystems.core.managers.BlockManager
import mezz.jei.api._
import mezz.jei.api.gui.IAdvancedGuiHandler
import net.minecraft.item.ItemStack

/**
  * Created by Dyonovan on 1/18/2016.
  */
object ModularSystemsPlugin {
    var jeiHelpers: IJeiHelpers = _
}

@JEIPlugin
class ModularSystemsPlugin extends IModPlugin {

    override def register(registry: IModRegistry): Unit = {

        ModularSystemsPlugin.jeiHelpers = registry.getJeiHelpers

        registry.addRecipeCategories(new CrusherRecipeCatagory)
        registry.addRecipeHandlers(new CrusherRecipeHandler)

        registry.addRecipes(CrusherRecipeMaker.getRecipes)

        registry.getRecipeTransferRegistry.addRecipeTransferHandler(new CraftingTransferHandler)

        ModularSystemsPlugin.jeiHelpers.getItemBlacklist.addItemToBlacklist(new ItemStack(BlockManager.proxy))

        // Register Tab holder
        registry.addAdvancedGuiHandlers(new IAdvancedGuiHandler[GuiBase[_]] {
            override def getGuiContainerClass: Class[GuiBase[_]] = classOf[GuiBase[_]]

            override def getGuiExtraAreas(t: GuiBase[_]): java.util.List[Rectangle] = t.getCoveredAreas
        })
    }

    override def onRuntimeAvailable(jeiRuntime: IJeiRuntime): Unit = {}
}
