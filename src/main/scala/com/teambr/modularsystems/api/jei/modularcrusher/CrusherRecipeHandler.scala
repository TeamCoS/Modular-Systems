package com.teambr.modularsystems.api.jei.modularcrusher

import com.teambr.modularsystems.core.lib.Reference
import mezz.jei.api.recipe.{IRecipeWrapper, IRecipeHandler}

/**
  * Created by Dyonovan on 1/18/2016.
  */
class CrusherRecipeHandler extends IRecipeHandler[CrusherRecipeJEI] {

    override def getRecipeWrapper(recipe: CrusherRecipeJEI): IRecipeWrapper = recipe

    override def getRecipeCategoryUid: String = Reference.MOD_ID + ":modularCrusher"

    override def isRecipeValid(recipe: CrusherRecipeJEI): Boolean = true

    override def getRecipeClass: Class[CrusherRecipeJEI] = classOf[CrusherRecipeJEI]
}
