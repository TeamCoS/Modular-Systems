package com.pauljoda.modularsystems.core.api.nei.machines;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.pauljoda.modularsystems.core.collections.BlockValues;
import com.pauljoda.modularsystems.core.gui.GuiComponentGraph;
import com.pauljoda.modularsystems.core.lib.Reference;
import com.pauljoda.modularsystems.core.registries.BlockValueRegistry;
import com.teambr.bookshelf.helpers.BlockHelper;
import com.teambr.bookshelf.util.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.awt.*;
import java.util.Set;

public class RecipeHandlerBlockValues extends TemplateRecipeHandler {

    public class RecipeBlockValue extends CachedRecipe {
        public PositionedStack block;
        public BlockValues values;
        public GuiComponentGraph speed, efficiency, multiplicity;

        public RecipeBlockValue(ItemStack input) {
            this.block = new PositionedStack(new ItemStack(input.getItem(), 1, input.getItemDamage() > -1 ? input.getItemDamage() : 0), 75, 5);

            values = BlockValueRegistry.INSTANCE.getBlockValues(Block.getBlockFromItem(input.getItem()), input.getItemDamage());
            speed = new GuiComponentGraph(15, 40, 40, 30, values.getSpeedFunction());
            efficiency = new GuiComponentGraph(70, 40, 40, 30, values.getEfficiencyFunction());
            multiplicity = new GuiComponentGraph(125, 40, 40, 30, values.getMultiplicityFunction());
        }

        @Override
        public PositionedStack getResult() {
            return block;
        }
    }

    @Override
    public int recipiesPerPage() { return 1; }

    @Override
    public void loadTransferRects () {
        transferRects.add(new RecipeTransferRect(new Rectangle(10, 40, 150, 35), "blockValues"));
    }

    @Override
    public void drawForeground(int recipe) {
        RecipeBlockValue r = (RecipeBlockValue) arecipes.get(recipe);
        r.speed.renderOverlay(0, 0);
        r.efficiency.renderOverlay(0, 0);
        r.multiplicity.renderOverlay(0, 0);

        RenderUtils.prepareRenderState();
        r.speed.render(0, 0);
        r.efficiency.render(0, 0);
        r.multiplicity.render(0, 0);

        Minecraft.getMinecraft().fontRenderer.drawString(StatCollector.translateToLocal("inventory.blockValuesConfig.speed"), 5, 30, 0x000000);
        Minecraft.getMinecraft().fontRenderer.drawString(StatCollector.translateToLocal("inventory.blockValuesConfig.efficiency"), 60, 30, 0x000000);
        Minecraft.getMinecraft().fontRenderer.drawString(StatCollector.translateToLocal("inventory.blockValuesConfig.multiplicity"), 115, 30, 0x000000);

        RenderHelper.enableGUIStandardItemLighting();
    }

    @Override
    public String getGuiTexture() {
        return Reference.MOD_ID + ":textures/gui/nei/blockValues.png";
    }

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("modularsystems.nei.blockValues");
    }

    @Override
    public TemplateRecipeHandler newInstance() {
        return super.newInstance();
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        Block block = Block.getBlockFromItem(ingredient.getItem());
        if(BlockValueRegistry.INSTANCE.isBlockRegistered(block, ingredient.getItemDamage())) {
            RecipeBlockValue recipe = new RecipeBlockValue(ingredient);
            this.arecipes.add(recipe);
        }
    }

    @Override
    public void loadCraftingRecipes(String outputID, Object... results) {
        if (outputID.equals("blockValues") && this.getClass() == RecipeHandlerBlockValues.class) {
            Set<String> blocks = BlockValueRegistry.INSTANCE.values.keySet();
            for(String str : blocks) {
                this.arecipes.add(new RecipeBlockValue(new ItemStack(BlockHelper.getBlockFromString(str).getFirst(), 1, BlockHelper.getBlockFromString(str).getSecond())));
            }
        } else
            super.loadCraftingRecipes(outputID, results);
    }
}
