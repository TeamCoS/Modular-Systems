package com.pauljoda.modularsystems.core.registries;

import appeng.api.AEApi;
import appeng.api.definitions.IBlocks;
import appeng.api.definitions.IMaterials;
import com.teambr.bookshelf.helpers.LogHelper;
import cpw.mods.fml.common.Loader;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unused", "unchecked"})
/**
 * The recipes for the crusher are stored here
 */
public class CrusherRecipeRegistry {
    public static CrusherRecipeRegistry INSTANCE = new CrusherRecipeRegistry();

    public ArrayList<ShapelessOreRecipe> crusherRecipes;

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
        crusherRecipes.add(new ShapelessOreRecipe(new ItemStack(Items.flint), getOreDict(new ItemStack(Blocks.gravel))));
        //Mod Specific
        if (Loader.isModLoaded("appliedenergistics2")) {
            IMaterials mats = AEApi.instance().definitions().materials();
            crusherRecipes.add(new ShapelessOreRecipe(mats.certusQuartzDust().maybeStack(2).get(),
                    "oreCertusQuartz"));
            crusherRecipes.add(new ShapelessOreRecipe(mats.certusQuartzDust().maybeStack(1).get(),
                    "crystalCertusQuartz"));
            crusherRecipes.add(new ShapelessOreRecipe(mats.certusQuartzDust().maybeStack(1).get(),
                    "crystalChargedCertusQuartz"));
            crusherRecipes.add(new ShapelessOreRecipe(mats.netherQuartzDust().maybeStack(1).get(),
                    "gemQuartz"));
            crusherRecipes.add(new ShapelessOreRecipe(mats.fluixDust().maybeStack(1).get(),
                    "crystalFluix"));
        }

        LogHelper.info("Finished adding " + crusherRecipes.size() + " Crusher Recipes");
    }

    /**
     * Get the oreDict tag for an item
     * @param itemstack The stack to try
     * @return The string for this stack
     */
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
    /**
     * Get the output for an input
     */
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

    /**
     * Checks if the item is a valid item for this registry
     * @param itemStack The stack to test
     * @return True if an output exists
     */
    public boolean isItemValid(ItemStack itemStack) {
        return getOutput(itemStack) != null;
    }

    /**
     * Get the inputs for crusher
     * @return Pretty much the whole recipes
     */
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
