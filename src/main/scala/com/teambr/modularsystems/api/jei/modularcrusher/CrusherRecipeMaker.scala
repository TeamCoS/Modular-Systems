package com.teambr.modularsystems.api.jei.modularcrusher

import java.util

import com.teambr.modularsystems.core.registries.CrusherRecipeRegistry

/**
  * Created by Dyonovan on 1/18/2016.
  */
object CrusherRecipeMaker {

    def getRecipes: java.util.List[CrusherRecipeJEI] = {
        val recipes = new util.ArrayList[CrusherRecipeJEI]()
        val crusher = CrusherRecipeRegistry.getRecipes

        for (i <- 0 until crusher.size()) {
            recipes.add(new CrusherRecipeJEI(crusher.get(i).input, crusher.get(i).output, crusher.get(i).secondary))
        }
        recipes
    }
}
