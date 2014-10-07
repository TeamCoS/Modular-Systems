package com.pauljoda.modularsystems.api.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.ItemList;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.pauljoda.modularsystems.core.crafting.OreProcessingRecipies;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RecipeHandlerOreProcessing extends TemplateRecipeHandler
{

    public class CachedOreProcessingRecipe extends CachedRecipe
    {
        public PositionedStack input;
        public PositionedStack output;

        public CachedOreProcessingRecipe(OreProcessingRecipies.OreProcessingRecipe recipe)
        {
            this.input = new PositionedStack(recipe.input, 50, 7);
            this.output = new PositionedStack(recipe.output, 109, 24);
        }

        @Override
        public java.util.List<PositionedStack> getIngredients() {
            return getCycledIngredients(cycleticks / 48, Arrays.asList(input));
        }
        @Override
        public PositionedStack getIngredient ()
        {
            return this.input;
        }

        @Override
        public PositionedStack getResult ()
        {
            return this.output;
        }


        @Override
        public PositionedStack getOtherStack() {
            return afuels.get((cycleticks / 48) % afuels.size()).stack;
        }
    }

    public static class FuelPair
    {
        public FuelPair(ItemStack ingred, int burnTime) {
            this.stack = new PositionedStack(ingred, 50, 43, false);
            this.burnTime = burnTime;
        }

        public PositionedStack stack;
        public int burnTime;
    }

    public static ArrayList<FuelPair> afuels;
    public static HashSet<Block> efuels;

    @Override
    public String getGuiTexture()
    {
        return "modularsystems:textures/neiOreProcessing.png";
    }

    @Override
    public String getRecipeName()
    {
        return "Modular Ore Processing";
    }

    public String getRecipeID ()
    {
        return "modularsystems.oreprocessing";
    }

    @Override
    public void loadTransferRects ()
    {
        transferRects.add(new RecipeTransferRect(new Rectangle(50, 23, 18, 18), "fuel"));
        transferRects.add(new RecipeTransferRect(new Rectangle(74, 23, 24, 18), getRecipeID()));
    }

    @Override
    public void drawBackground (int recipe)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiDraw.changeTexture(this.getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 0, 0, 160, 65);
    }

    @Override
    public void drawExtras (int recipe)
    {
        drawProgressBar(51, 27, 160, 0, 14, 14, 48, 7);
        drawProgressBar(74, 25, 160, 14, 24, 16, 48, 0);
    }

    private static Set<Item> excludedFuels() {
        Set<Item> efuels = new HashSet<Item>();
        efuels.add(Item.getItemFromBlock(Blocks.brown_mushroom));
        efuels.add(Item.getItemFromBlock(Blocks.red_mushroom));
        efuels.add(Item.getItemFromBlock(Blocks.standing_sign));
        efuels.add(Item.getItemFromBlock(Blocks.wall_sign));
        efuels.add(Item.getItemFromBlock(Blocks.wooden_door));
        efuels.add(Item.getItemFromBlock(Blocks.trapped_chest));
        return efuels;
    }

    private static void findFuels() {
        afuels = new ArrayList<FuelPair>();
        Set<Item> efuels = excludedFuels();
        for (ItemStack item : ItemList.items)
            if (!efuels.contains(item.getItem())) {
                int burnTime = TileEntityFurnace.getItemBurnTime(item);
                if (burnTime > 0)
                    afuels.add(new FuelPair(item.copy(), burnTime));
            }
    }

    @Override
    public TemplateRecipeHandler newInstance() {
        if (afuels == null)
            findFuels();
        return super.newInstance();
    }

    @Override
    public void loadCraftingRecipes (String outputId, Object... results)
    {
        if (outputId.equals(this.getRecipeID()))
        {
            for (OreProcessingRecipies.OreProcessingRecipe recipe : OreProcessingRecipies.recipes)
            {
                this.arecipes.add(new CachedOreProcessingRecipe(recipe));
            }
        }
        else
        {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes (ItemStack result)
    {
        for (OreProcessingRecipies.OreProcessingRecipe re : OreProcessingRecipies.recipes)
        {
            if (NEIServerUtils.areStacksSameTypeCrafting(re.output, result))
            {
                this.arecipes.add(new CachedOreProcessingRecipe(re));
            }
        }
    }

    @Override
    public void loadUsageRecipes (ItemStack ingred)
    {
        for (OreProcessingRecipies.OreProcessingRecipe recipe : OreProcessingRecipies.recipes)
        {
            if (NEIServerUtils.areStacksSameTypeCrafting(recipe.input, ingred))
            {
                this.arecipes.add(new CachedOreProcessingRecipe(recipe));
            }
        }
    }
}
