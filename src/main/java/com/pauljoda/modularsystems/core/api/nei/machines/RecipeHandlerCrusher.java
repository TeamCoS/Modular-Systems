package com.pauljoda.modularsystems.core.api.nei.machines;

import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.pauljoda.modularsystems.core.lib.Reference;
import com.pauljoda.modularsystems.core.registries.CrusherRecipeRegistry;
import com.pauljoda.modularsystems.crusher.gui.GuiCrusher;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;

public class RecipeHandlerCrusher extends TemplateRecipeHandler {

    /**
     * The recipe for the crusher
     */
    public class CrushingPair extends CachedRecipe {

        public PositionedStack input; //The input stack
        public PositionedStack output; //The output stack

        public CrushingPair(ItemStack input, ItemStack output) {
            this.input = new PositionedStack(input, 51, 24);
            this.output = new PositionedStack(output, 110, 24);
        }

        @Override
        public List<PositionedStack> getIngredients() {
            return getCycledIngredients(cycleticks / 48, Collections.singletonList(this.input));
        }

        @Override
        public PositionedStack getResult() {
            return this.output;
        }
    }

    /**
     * Used to draw the arrows and such
     */
    @Override
    public void drawExtras(int recipe) {
        this.drawProgressBar(77, 41, 176, 0, 14, 14, 48, 7);
        this.drawProgressBar(74, 23, 176, 14, 24, 16, 48, 0);
    }

    /**
     * Define the texture
     */
    @Override
    public String getGuiTexture() {
        return Reference.MOD_ID + ":textures/gui/nei/furnace.png";
    }

    /**
     * The area we want to define for the "Recipes" area
     */
    @Override
    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(74, 23, 24, 18), "crusher", new Object[0]));
    }

    /**
     * The Gui this handler mimics
     */
    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiCrusher.class;
    }

    /**
     * Sets the display name
     */
    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("modularsystems.nei.crusherrecipes");
    }

    @Override
    public TemplateRecipeHandler newInstance() {
        return super.newInstance();
    }

    /**
     * Loads the crafting using the results
     * @param outputID ID
     * @param results Checking from results
     */
    @Override
    public void loadCraftingRecipes(String outputID, Object... results) {
        if (outputID.equals("crusher") && this.getClass() == RecipeHandlerCrusher.class) {
            Map recipes = CrusherRecipeRegistry.INSTANCE.getCrusherInputList();

            for (Object o : recipes.entrySet()) {
                Entry recipe = (Entry) o;
                this.arecipes.add(new CrushingPair((ItemStack) recipe.getKey(), (ItemStack) recipe.getValue()));
            }
        } else
            super.loadCraftingRecipes(outputID, results);
    }

    /**
     * Checks for crafting using our handler
     * @param result The stack we want to see how is made
     */
    @Override
    public void loadCraftingRecipes(ItemStack result) {
        Map recipes = CrusherRecipeRegistry.INSTANCE.getCrusherInputList();

        for (Object o : recipes.entrySet()) {
            Entry recipe = (Entry) o;
            if (NEIServerUtils.areStacksSameType((ItemStack) recipe.getValue(), result)) {
                this.arecipes.add(new CrushingPair((ItemStack) recipe.getKey(), (ItemStack) recipe.getValue()));
            }
        }
    }

    /**
     * Used to find out the usage of blocks
     * @param input Our ID
     * @param ingredients Inputs, check if our handler makes something out of it
     */
    @Override
    public void loadUsageRecipes(String input, Object... ingredients) {
        if (input.equals("crusher") && this.getClass() == RecipeHandlerCrusher.class)
            this.loadCraftingRecipes("crusher");
        else
            super.loadUsageRecipes(input, ingredients);
    }

    /**
     * Used to find the usage of a single stack
     * @param ingredient The thing to see if we use it
     */
    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        Map recipes = CrusherRecipeRegistry.INSTANCE.getCrusherInputList();

        for (Object o : recipes.entrySet()) {
            Entry recipe = (Entry) o;
            if (NEIServerUtils.areStacksSameTypeCrafting((ItemStack) recipe.getKey(), ingredient)) {
                CrushingPair arecipe = new CrushingPair((ItemStack) recipe.getKey(), (ItemStack) recipe.getValue());
                arecipe.setIngredientPermutation(Collections.singletonList(arecipe.input), ingredient);
                this.arecipes.add(arecipe);
                return; //We should stop since we found our answer
            }
        }
    }
}
