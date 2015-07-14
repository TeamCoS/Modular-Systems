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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class RecipeHandlerCrusher extends TemplateRecipeHandler {

    public RecipeHandlerCrusher() { }

    @Override
    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(74, 23, 24, 18), "crusher", new Object[0]));
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() { return GuiCrusher.class; }

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("modularsystems.nei.crusherrecipes");
    }

    @Override
    public TemplateRecipeHandler newInstance() {
        return super.newInstance();
    }

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

    @Override
    public void loadUsageRecipes(String input, Object... ingredients) {
        if (input.equals("crusher") && this.getClass() == RecipeHandlerCrusher.class)
            this.loadCraftingRecipes("crusher", new Object[0]);
        else
            super.loadUsageRecipes(input, ingredients);
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        Map recipes = CrusherRecipeRegistry.INSTANCE.getCrusherInputList();
        Iterator iterator = recipes.entrySet().iterator();

        while(iterator.hasNext()) {
            Entry recipe = (Entry)iterator.next();
            if (NEIServerUtils.areStacksSameTypeCrafting((ItemStack) recipe.getKey(), ingredient)) {
                RecipeHandlerCrusher.CrushingPair arecipe = new RecipeHandlerCrusher.CrushingPair((ItemStack) recipe.getKey(), (ItemStack) recipe.getValue());
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

    public class CrushingPair extends CachedRecipe {

        public PositionedStack input;
        public PositionedStack output;

        public CrushingPair(ItemStack input, ItemStack output) {
            this.input = new PositionedStack(input, 51, 24);
            this.output = new PositionedStack(output, 110, 24);
        }

        @Override
        public List<PositionedStack> getIngredients() {
            return getCycledIngredients(cycleticks / 48, Arrays.asList(new PositionedStack[]{this.input}));
        }

        @Override
        public PositionedStack getResult() {
            return this.output;
        }
    }
}
