package com.teambr.modularsystems.api.jei

import com.teambr.modularsystems.api.jei.modularcrusher.{CrusherRecipeMaker, CrusherRecipeHandler, CrusherRecipeCatagory}
import mezz.jei.api._

/**
  * Created by Dyonovan on 1/18/2016.
  */
object ModularSystemsPlugin {
    var jeiHelpers: IJeiHelpers = _
}

@JEIPlugin
class ModularSystemsPlugin extends IModPlugin {

    override def onRecipeRegistryAvailable(recipeRegistry: IRecipeRegistry): Unit = { }

    override def register(registry: IModRegistry): Unit = {

        registry.addRecipeCategories(new CrusherRecipeCatagory)
        registry.addRecipeHandlers(new CrusherRecipeHandler)

        registry.addRecipes(CrusherRecipeMaker.getRecipes)
    }

    override def onItemRegistryAvailable(itemRegistry: IItemRegistry): Unit = { }

    override def onJeiHelpersAvailable(jeiHelpers: IJeiHelpers): Unit = ModularSystemsPlugin.jeiHelpers = jeiHelpers
}
