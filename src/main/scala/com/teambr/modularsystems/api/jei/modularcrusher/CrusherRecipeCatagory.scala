package com.teambr.modularsystems.api.jei.modularcrusher

import java.awt.Color

import com.teambr.bookshelf.api.jei.drawables.{GuiComponentArrowJEI, GuiComponentPowerBarJEI, SlotDrawable}
import com.teambr.modularsystems.api.jei.ModularSystemsPlugin.jeiHelpers
import com.teambr.modularsystems.core.lib.Reference
import mezz.jei.api.gui._
import mezz.jei.api.recipe.{IRecipeCategory, IRecipeWrapper}
import net.minecraft.client.Minecraft
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.translation.I18n

/**
  * Created by Dyonovan on 1/18/2016.
  */
class CrusherRecipeCatagory extends IRecipeCategory[CrusherRecipeJEI] {

    val location = new ResourceLocation(Reference.MOD_ID, "textures/gui/jei/background.png")
    val flameLocation = new ResourceLocation("minecraft", "textures/gui/container/furnace.png")
    val arrow = new GuiComponentArrowJEI(49, 11, jeiHelpers)
    val flameDrawable = jeiHelpers.getGuiHelper.createDrawable(flameLocation, 176, 0, 14, 14)
    val flame = jeiHelpers.getGuiHelper.createAnimatedDrawable(flameDrawable, 300, IDrawableAnimated.StartDirection.TOP, true)

    val slotInput = new SlotDrawable(21, 10)
    val slotOutput = new SlotDrawable(86, 10, isLarge = true)
    val slotOutput2 = new SlotDrawable(115, 10, isLarge = true)

    val background = jeiHelpers.getGuiHelper.createDrawable(location, 10, 15, 150, 50)

    override def getBackground: IDrawable = background

    override def setRecipe(recipeLayout: IRecipeLayout, recipeWrapper: CrusherRecipeJEI): Unit = {
        val stacks: IGuiItemStackGroup = recipeLayout.getItemStacks
        stacks.init(0, true, 21, 10)
        stacks.init(1, false, 86, 10)
        stacks.init(2, false, 115, 10)

        recipeWrapper match {
            case crusherRecipeWrapper: CrusherRecipeJEI =>
                recipeLayout.getItemStacks.set(0, crusherRecipeWrapper.getInputs)
                recipeLayout.getItemStacks.set(1, crusherRecipeWrapper.getOutputs.get(0))
                recipeLayout.getItemStacks.set(2, crusherRecipeWrapper.getOutputs.get(1))
                val tip = new ITooltipCallback[ItemStack] {
                    override def onTooltip(slotIndex: Int, input: Boolean, ingredient: ItemStack, tooltip: java.util.List[String]): Unit = {
                        //TODO tooltip for secondary
                    }
                }
                recipeLayout.getItemStacks.addTooltipCallback(tip)
            case _ =>
        }
    }

    override def drawAnimations(minecraft: Minecraft): Unit = {
        arrow.draw(minecraft, 0, 0)
        flame.draw(minecraft, 53, 26)
    }

    override def drawExtras(minecraft: Minecraft): Unit = {
        slotInput.draw(minecraft)
        slotOutput.draw(minecraft)
        slotOutput2.draw(minecraft)
    }

    override def getTitle: String = I18n.translateToLocal("inventory.crusher.title")

    override def getUid: String = Reference.MOD_ID + ":modularCrusher"
}
