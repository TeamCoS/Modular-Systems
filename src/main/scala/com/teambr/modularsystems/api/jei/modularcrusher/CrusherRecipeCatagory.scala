package com.teambr.modularsystems.api.jei.modularcrusher

import com.teambr.modularsystems.api.jei.ModularSystemsPlugin
import com.teambr.modularsystems.core.lib.Reference
import mezz.jei.api.gui.{IDrawableAnimated, IGuiItemStackGroup, IDrawable, IRecipeLayout}
import mezz.jei.api.recipe.{IRecipeWrapper, IRecipeCategory}
import net.minecraft.client.Minecraft
import net.minecraft.util.{ResourceLocation, StatCollector}

/**
  * Created by Dyonovan on 1/18/2016.
  */
class CrusherRecipeCatagory extends IRecipeCategory {

    val location = new ResourceLocation(Reference.MOD_ID, "textures/gui/nei/crusher.png")
    val arrow = ModularSystemsPlugin.jeiHelpers.getGuiHelper.createAnimatedDrawable(
        ModularSystemsPlugin.jeiHelpers.getGuiHelper.createDrawable(location, 176, 14, 24, 17), 75, IDrawableAnimated.StartDirection.LEFT, false)
    val power = ModularSystemsPlugin.jeiHelpers.getGuiHelper.createAnimatedDrawable(
        ModularSystemsPlugin.jeiHelpers.getGuiHelper.createDrawable(location, 175, 31, 12, 45), 75, IDrawableAnimated.StartDirection.TOP, true)

    override def getBackground: IDrawable = { ModularSystemsPlugin.jeiHelpers.getGuiHelper.createDrawable(location, 10, 15, 150, 50) }

    override def setRecipe(recipeLayout: IRecipeLayout, recipeWrapper: IRecipeWrapper): Unit = {
        val stacks: IGuiItemStackGroup = recipeLayout.getItemStacks
        stacks.init(0, true, 31, 20)
        stacks.init(1, false, 96, 20)
        stacks.init(2, false, 125, 20)

        recipeWrapper match {
            case crusherRecipeWrapper: CrusherRecipeJEI =>
                recipeLayout.getItemStacks.set(0, crusherRecipeWrapper.getInputs)
                recipeLayout.getItemStacks.set(1, crusherRecipeWrapper.getOutputs.get(0))
                recipeLayout.getItemStacks.set(2, crusherRecipeWrapper.getOutputs.get(1))
            case _ =>
        }
    }

    override def drawAnimations(minecraft: Minecraft): Unit = { }

    override def drawExtras(minecraft: Minecraft): Unit = { }

    override def getTitle: String = StatCollector.translateToLocal("inventory.crusher.title")

    override def getUid: String = Reference.MOD_ID + ":modularCrusher"
}
