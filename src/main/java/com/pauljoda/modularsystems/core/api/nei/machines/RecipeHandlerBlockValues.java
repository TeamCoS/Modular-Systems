package com.pauljoda.modularsystems.core.api.nei.machines;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.pauljoda.modularsystems.core.collections.BlockValues;
import com.pauljoda.modularsystems.core.gui.GuiComponentGraph;
import com.pauljoda.modularsystems.core.lib.Reference;
import com.pauljoda.modularsystems.core.registries.BlockValueRegistry;
import com.teambr.bookshelf.helpers.BlockHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RecipeHandlerBlockValues extends TemplateRecipeHandler {

    /**
     * Our recipe for the Block Values
     */
    public class RecipeBlockValue extends CachedRecipe {
        public PositionedStack block; //The block to display
        public BlockValues values; //The values for the said block
        public GuiComponentGraph speed, efficiency, multiplicity; //The graphs we want to display

        public RecipeBlockValue(ItemStack input) {
            this.block = new PositionedStack(new ItemStack(input.getItem(), 1, input.getItemDamage() > -1 ? input.getItemDamage() : 0), 75, 0);

            values = BlockValueRegistry.INSTANCE.getBlockValues(Block.getBlockFromItem(input.getItem()), input.getItemDamage());

            speed = new GuiComponentGraph(15, 30, 40, 30, values.getSpeedFunction());
            efficiency = new GuiComponentGraph(70, 30, 40, 30, values.getEfficiencyFunction());
            multiplicity = new GuiComponentGraph(125, 30, 40, 30, values.getMultiplicityFunction());
        }

        @Override
        public PositionedStack getResult() {
            return getBlockMeta() != null ? getBlockMeta().get(((cycleticks / 48) % getBlockMeta().size())) : this.block;
        }

        private List<PositionedStack> getBlockMeta() {
            if(BlockValueRegistry.INSTANCE.values.get(BlockHelper.getBlockString(Block.getBlockFromItem(this.block.item.getItem()), this.block.item.getItemDamage())) == null) {
                List<PositionedStack> list = new ArrayList<>();
                Block theBlock = Block.getBlockFromItem(block.item.getItem());
                ArrayList<ItemStack> subBlocks = new ArrayList<>();
                theBlock.getSubBlocks(block.item.getItem(), theBlock.getCreativeTabToDisplayOn(), subBlocks);
                for(ItemStack newBlock : subBlocks) {
                    list.add(new PositionedStack(newBlock, 75, 0));
                }
                return list;
            }
            return null;
        }
    }

    /**
     * Since we want to show a lot, lets just use 1 per page
     */
    @Override
    public int recipiesPerPage() { return 2; }

    /**
     * This will define the area to load all current blocks
     */
    @Override
    public void loadTransferRects () {
        transferRects.add(new RecipeTransferRect(new Rectangle(10, 30, 150, 35), "blockValues"));
    }

    /**
     * This is used when we want to render the extras
     * @param recipe The id, get the cachedrecipe from arecipes
     */
    @Override
    public void drawForeground(int recipe) {
        RecipeBlockValue r = (RecipeBlockValue) arecipes.get(recipe);
        r.speed.renderOverlay(0, 0);
        r.efficiency.renderOverlay(0, 0);
        r.multiplicity.renderOverlay(0, 0);

        r.speed.render(0, 0);
        r.efficiency.render(0, 0);
        r.multiplicity.render(0, 0);

        Minecraft.getMinecraft().fontRenderer.drawString(StatCollector.translateToLocal("inventory.blockValuesConfig.speed"), 5, 20, 0x000000);
        Minecraft.getMinecraft().fontRenderer.drawString(StatCollector.translateToLocal("inventory.blockValuesConfig.efficiency"), 60, 20, 0x000000);
        Minecraft.getMinecraft().fontRenderer.drawString(StatCollector.translateToLocal("inventory.blockValuesConfig.multiplicity"), 115, 20, 0x000000);

        RenderHelper.enableGUIStandardItemLighting();
    }

    /**
     * Set the background texture
     */
    @Override
    public String getGuiTexture() {
        return Reference.MOD_ID + ":textures/gui/nei/blockValues.png";
    }

    /**
     * The name to display at the top
     */
    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("modularsystems.nei.blockValues");
    }

    @Override
    public TemplateRecipeHandler newInstance() {
        return super.newInstance();
    }

    /**
     * Called when you press 'U' or right click on an item
     * @param ingredient The stack that we want to see
     */
    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        Block block = Block.getBlockFromItem(ingredient.getItem());
        if(BlockValueRegistry.INSTANCE.isBlockRegistered(block, ingredient.getItemDamage())) {
            RecipeBlockValue recipe = new RecipeBlockValue(ingredient);
            this.arecipes.add(recipe);
        }
    }

    /**
     * Called when NEI is loading usage recipes
     * @param outputID The id, for us blockValues
     * @param results Something to do with the thing being sent
     */
    @Override
    public void loadCraftingRecipes(String outputID, Object... results) {
        if (outputID.equals("blockValues") && this.getClass() == RecipeHandlerBlockValues.class) {
            Set<String> blocks = BlockValueRegistry.INSTANCE.values.keySet();
            for(String str : blocks) {
                if(BlockHelper.getBlockFromString(str).getFirst() != null)
                    this.arecipes.add(new RecipeBlockValue(new ItemStack(BlockHelper.getBlockFromString(str).getFirst(), 1, BlockHelper.getBlockFromString(str).getSecond())));
            }
        } else
            super.loadCraftingRecipes(outputID, results);
    }
}
