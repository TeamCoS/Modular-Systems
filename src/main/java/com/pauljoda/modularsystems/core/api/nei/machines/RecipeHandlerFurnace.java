package com.pauljoda.modularsystems.core.api.nei.machines;

import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.pauljoda.modularsystems.core.lib.Reference;
import com.pauljoda.modularsystems.furnace.gui.GuiModularFurnace;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.StatCollector;

import java.awt.*;
import java.util.Arrays;
import java.util.Map;

public class RecipeHandlerFurnace extends TemplateRecipeHandler {

    @Override
    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(74, 23, 24, 18), "furnace", new Object[0]));
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() { return GuiModularFurnace.class; }

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("modularsystems.nei.furnacerecipes");
    }

    @Override
    public TemplateRecipeHandler newInstance() {
        return super.newInstance();
    }

    @Override
    public void loadCraftingRecipes(String outputID, Object... results) {
        if (outputID.equals("furnace") && this.getClass() == RecipeHandlerFurnace.class) {
            Map recipes = FurnaceRecipes.smelting().getSmeltingList();

            for (Object o : recipes.entrySet()) {
                Map.Entry recipe = (Map.Entry) o;
                this.arecipes.add(new SmeltingPair((ItemStack) recipe.getKey(), (ItemStack) recipe.getValue()));
            }
        } else {
            super.loadCraftingRecipes(outputID, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        Map recipes = FurnaceRecipes.smelting().getSmeltingList();

        for (Object o : recipes.entrySet()) {
            Map.Entry recipe = (Map.Entry) o;
            if (NEIServerUtils.areStacksSameType((ItemStack) recipe.getValue(), result)) {
                this.arecipes.add(new SmeltingPair((ItemStack) recipe.getKey(), (ItemStack) recipe.getValue()));
            }
        }
    }

    @Override
    public void loadUsageRecipes(String inputId, Object... ingredients) {
        if(inputId.equals("furnace") && this.getClass() == RecipeHandlerFurnace.class) {
            this.loadCraftingRecipes("furnace", new Object[0]);
        } else {
            super.loadUsageRecipes(inputId, ingredients);
        }

    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        Map recipes = FurnaceRecipes.smelting().getSmeltingList();

        for (Object o : recipes.entrySet()) {
            Map.Entry recipe = (Map.Entry) o;
            if (NEIServerUtils.areStacksSameTypeCrafting((ItemStack) recipe.getKey(), ingredient)) {
                SmeltingPair arecipe = new SmeltingPair((ItemStack) recipe.getKey(), (ItemStack) recipe.getValue());
                arecipe.setIngredientPermutation(Arrays.asList(new PositionedStack[]{arecipe.input}), ingredient);
                this.arecipes.add(arecipe);
            }
        }
    }

    @Override
    public void drawExtras(int recipe) {
        this.drawProgressBar(77, 41, 176, 0, 14, 14, 48, 7);
        this.drawProgressBar(74, 23, 176, 14, 24, 16, 48, 0);
    }

    @Override
    public String getGuiTexture() {
        return Reference.MOD_ID + ":textures/gui/nei/furnace.png";
    }

    public class SmeltingPair extends CachedRecipe {

        public PositionedStack input;
        public PositionedStack output;

        public SmeltingPair(ItemStack input, ItemStack output) {
            this.input = new PositionedStack(input, 51, 24);
            this.output = new PositionedStack(output, 110, 24);
        }

        @Override
        public java.util.List<PositionedStack> getIngredients() {
            return getCycledIngredients(cycleticks / 48, Arrays.asList(new PositionedStack[]{this.input}));
        }

        @Override
        public PositionedStack getResult() {
            return this.output;
        }
    }
}
