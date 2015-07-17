package com.pauljoda.modularsystems.core.registries;

import com.teambr.bookshelf.helpers.LogHelper;
import com.teambr.bookshelf.util.JsonUtils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.*;

@SuppressWarnings({"unused", "unchecked"})
public class CrusherRecipeRegistry {
    public static CrusherRecipeRegistry INSTANCE = new CrusherRecipeRegistry();

    protected ArrayList<ShapelessOreRecipe> crusherRecipes;

    public CrusherRecipeRegistry() {
        crusherRecipes = new ArrayList<>();
    }

    /**
     * Add the values
     */
    public void init() {
        LogHelper.info("Creating Dynamic Crusher Recipe List");

        String[] oreDict = OreDictionary.getOreNames();

        for (String anOreDict1 : oreDict) {
            if (anOreDict1.startsWith("dust")) {
                String ore = anOreDict1.replaceFirst("dust", "");
                if (OreDictionary.doesOreNameExist("ore" + ore)) {
                    List<ItemStack> outputDust = OreDictionary.getOres(anOreDict1);
                    if(outputDust.size() > 0 && !outputDust.isEmpty()) {
                        switch ("ore" + ore) {
                            case "oreRedstone":
                                crusherRecipes.add(new ShapelessOreRecipe(new ItemStack(Items.redstone, 8), "oreRedstone"));
                                break;
                            case "oreLapis":
                                crusherRecipes.add(new ShapelessOreRecipe(new ItemStack(Items.dye, 8, 4), "oreLapis"));
                                break;
                            default:
                                crusherRecipes.add(new ShapelessOreRecipe(new ItemStack(outputDust.get(0).getItem(), 2, outputDust.get(0).getItemDamage()), "ore" + ore));
                        }
                    }
                }
            } else if (anOreDict1.startsWith("ingot")) { //Ingot to Dust
                String ingot = anOreDict1.replaceFirst("ingot", "");
                if (OreDictionary.doesOreNameExist("dust" + ingot)) {
                    List<ItemStack> outputDust = OreDictionary.getOres("dust" + ingot);
                    if(outputDust.size() > 0 && !outputDust.isEmpty())
                        crusherRecipes.add(new ShapelessOreRecipe(new ItemStack(outputDust.get(0).getItem(), 1, outputDust.get(0).getItemDamage()), anOreDict1));
                }
            }
        }

        //misc recipes
        crusherRecipes.add(new ShapelessOreRecipe(new ItemStack(Items.blaze_powder, 4), getOreDict(new ItemStack(Items.blaze_rod))));
        crusherRecipes.add(new ShapelessOreRecipe(new ItemStack(Blocks.sand), "cobblestone"));
        crusherRecipes.add(new ShapelessOreRecipe(new ItemStack(Items.dye, 6, 15), getOreDict(new ItemStack(Items.bone))));
        crusherRecipes.add(new ShapelessOreRecipe(new ItemStack(Items.quartz, 4), "oreQuartz"));
        crusherRecipes.add(new ShapelessOreRecipe(new ItemStack(Items.clay_ball, 4), getOreDict(new ItemStack(Blocks.clay))));
        crusherRecipes.add(new ShapelessOreRecipe(new ItemStack(Items.diamond, 2), "oreDiamond"));
        crusherRecipes.add(new ShapelessOreRecipe(new ItemStack(Items.emerald, 2), "oreEmerald"));
        crusherRecipes.add(new ShapelessOreRecipe(new ItemStack(Items.glowstone_dust, 4), "glowstone"));
        crusherRecipes.add(new ShapelessOreRecipe(new ItemStack(Items.coal, 4), "oreCoal"));
        crusherRecipes.add(new ShapelessOreRecipe(new ItemStack(Items.string, 4), "blockCloth"));
        crusherRecipes.add(new ShapelessOreRecipe(new ItemStack(Blocks.sand), "blockGlass"));

        LogHelper.info("Finished adding " + crusherRecipes.size() + " Crusher Recipes");
    }

    private String getOreDict(ItemStack itemstack) {
        int[] registered = OreDictionary.getOreIDs(itemstack);
        if (registered.length > 0)
            return OreDictionary.getOreName(registered[0]);
        else {
            OreDictionary.registerOre(itemstack.getUnlocalizedName(), itemstack);
            return itemstack.getUnlocalizedName();
        }
    }

    @SuppressWarnings("unchecked")
    public ItemStack getOutput(ItemStack itemStack) {
        for (ShapelessOreRecipe crusherRecipe : crusherRecipes) {
            List<Object> list = crusherRecipe.getInput();
            for (Object items : list) {
                ArrayList<OreDictionary> o = (ArrayList<OreDictionary>) items;
                for (Object item : o) {
                    ItemStack itemIn = (ItemStack) item;
                    if (itemIn.getItemDamage() == 32767) {
                        if (itemIn.getItem() == itemStack.getItem())
                            return crusherRecipe.getRecipeOutput();
                    }
                    else if (itemIn.isItemEqual(itemStack))
                        return crusherRecipe.getRecipeOutput();
                }
            }
        }
        return null;
    }

    public boolean isItemValid(ItemStack itemStack) {
        return getOutput(itemStack) != null;
    }

    public Map<ItemStack, ItemStack> getCrusherInputList() {
        HashMap<ItemStack, ItemStack> listRecipes = new HashMap<>();

        for (ShapelessOreRecipe crusherRecipe : crusherRecipes) {
            List<Object> list = crusherRecipe.getInput();
            for (Object items : list) {
                ArrayList<OreDictionary> o = (ArrayList<OreDictionary>) items;
                for (Object item : o) {
                    ItemStack itemIn = (ItemStack) item;
                    listRecipes.put(itemIn, crusherRecipe.getRecipeOutput());
                }
            }
        }
        return listRecipes;
    }
}
