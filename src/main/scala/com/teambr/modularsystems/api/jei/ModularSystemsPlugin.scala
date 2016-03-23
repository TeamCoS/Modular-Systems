package com.teambr.modularsystems.api.jei

import java.awt.Rectangle

import com.teambr.bookshelf.client.gui.GuiBase
import mezz.jei.api._
import mezz.jei.api.gui.IAdvancedGuiHandler

/**
  * Created by Dyonovan on 1/18/2016.
  */
object ModularSystemsPlugin {
    var jeiHelpers: IJeiHelpers = _
}

@JEIPlugin
class ModularSystemsPlugin extends IModPlugin {

    override def register(registry: IModRegistry): Unit = {

       // registry.addRecipeCategories(new CrusherRecipeCatagory)
       // registry.addRecipeHandlers(new CrusherRecipeHandler)

       // registry.addRecipes(CrusherRecipeMaker.getRecipes)

      //  ModularSystemsPlugin.jeiHelpers.getItemBlacklist.addItemToBlacklist(new ItemStack(BlockManager.proxy))

        // Register Tab holder
        registry.addAdvancedGuiHandlers(new IAdvancedGuiHandler[GuiBase[_]] {
            override def getGuiContainerClass: Class[GuiBase[_]] = classOf[GuiBase[_]]

            override def getGuiExtraAreas(t: GuiBase[_]): java.util.List[Rectangle] = t.getCoveredAreas
        })
    }

    override def onRuntimeAvailable(jeiRuntime: IJeiRuntime): Unit = {}
}
