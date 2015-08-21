package com.pauljoda.modularsystems.core.registries;

import appeng.api.AEApi;
import appeng.api.definitions.IBlocks;
import appeng.api.definitions.IMaterials;
import com.google.gson.reflect.TypeToken;
import com.pauljoda.modularsystems.core.ModularSystems;
import com.pauljoda.modularsystems.core.collections.CrusherRecipes;
import com.teambr.bookshelf.helpers.LogHelper;
import com.teambr.bookshelf.util.JsonUtils;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.io.File;
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

    public ArrayList<CrusherRecipes> crusherRecipes;

    public CrusherRecipeRegistry() {
        crusherRecipes = new ArrayList<>();
    }

    /**
     * Add the values
     */
    public void init() {
        if(!loadFromFile())
            generateDefaults();
        else
            LogHelper.info("Block Values loaded successfully");
    }

    /**
     * Load the values from the file
     * @return True if successful
     */
    public boolean loadFromFile() {
        LogHelper.info("Loading Block Values...");
        crusherRecipes = JsonUtils.readFromJson(new TypeToken<ArrayList<CrusherRecipes>>() { },
                ModularSystems.configFolderLocation + File.separator + "Registries" + File.separator + "crusherRecipes.json");
        return crusherRecipes != null;
    }

    /**
     * Save the current registry to a file
     */
    public void saveToFile() {
        validateList();
        if (!crusherRecipes.isEmpty())
            JsonUtils.writeToJson(crusherRecipes, ModularSystems.configFolderLocation + File.separator + "Registries" + File.separator + "crusherRecipes.json");
    }

    /**
     * Used to generate the default values
     */
    public void generateDefaults() {
        LogHelper.info("Json not found. Creating Dynamic Crusher Recipe List...");
        validateList();

        String[] oreDict = OreDictionary.getOreNames();

        for (String anOreDict1 : oreDict) {
            if (anOreDict1.startsWith("dust")) {
                String ore = anOreDict1.replaceFirst("dust", "");
                if (OreDictionary.doesOreNameExist("ore" + ore)) {
                    List<ItemStack> outputDust = OreDictionary.getOres(anOreDict1);
                    if(outputDust.size() > 0 && !outputDust.isEmpty()) {
                        switch ("ore" + ore) {
                            case "oreRedstone":
                                crusherRecipes.add(new CrusherRecipes("oreRedstone",
                                        getItemStackString(new ItemStack(Items.redstone)), 8));
                                break;
                            case "oreLapis":
                                crusherRecipes.add(new CrusherRecipes("oreLapis",
                                        getItemStackString(new ItemStack(Items.dye, 1, 4)), 8));
                                break;
                            default:
                                crusherRecipes.add(new CrusherRecipes("ore" + ore,
                                        getItemStackString(new ItemStack(outputDust.get(0).getItem(), 1,
                                                outputDust.get(0).getItemDamage())), 2));
                        }
                    }
                }
            } else if (anOreDict1.startsWith("ingot")) { //Ingot to Dust
                String ingot = anOreDict1.replaceFirst("ingot", "");
                if (OreDictionary.doesOreNameExist("dust" + ingot)) {
                    List<ItemStack> outputDust = OreDictionary.getOres("dust" + ingot);
                    if(outputDust.size() > 0 && !outputDust.isEmpty())
                        crusherRecipes.add(new CrusherRecipes(anOreDict1, getItemStackString(
                                new ItemStack(outputDust.get(0).getItem(), 1, outputDust.get(0).getItemDamage())), 1));
                }
            }
        }

        //misc recipes
        crusherRecipes.add(new CrusherRecipes(getOreDict(new ItemStack(Items.blaze_rod)),
                getItemStackString(new ItemStack(Items.blaze_powder)), 4));
        crusherRecipes.add(new CrusherRecipes(getOreDict(new ItemStack(Blocks.cobblestone)),
                getItemStackString(new ItemStack(Blocks.sand)), 1));
        crusherRecipes.add(new CrusherRecipes(getOreDict(new ItemStack(Items.bone)),
                getItemStackString(new ItemStack(Items.dye, 1, 15)), 6));
        crusherRecipes.add(new CrusherRecipes(getOreDict(new ItemStack(Blocks.quartz_ore)),
                getItemStackString(new ItemStack(Items.quartz)), 4));
        crusherRecipes.add(new CrusherRecipes(getOreDict(new ItemStack(Blocks.clay)),
                getItemStackString(new ItemStack(Items.clay_ball)), 4));
        crusherRecipes.add(new CrusherRecipes(getOreDict(new ItemStack(Blocks.diamond_ore)),
                getItemStackString(new ItemStack(Items.diamond)), 2));
        crusherRecipes.add(new CrusherRecipes(getOreDict(new ItemStack(Blocks.emerald_ore)),
                getItemStackString(new ItemStack(Items.emerald)), 2));
        crusherRecipes.add(new CrusherRecipes(getOreDict(new ItemStack(Blocks.glowstone)),
                getItemStackString(new ItemStack(Items.glowstone_dust)), 4));
        crusherRecipes.add(new CrusherRecipes(getOreDict(new ItemStack(Blocks.coal_ore)),
                getItemStackString(new ItemStack(Items.coal, 1, 0)), 4));
        crusherRecipes.add(new CrusherRecipes(getOreDict(new ItemStack(Blocks.wool)),
                getItemStackString(new ItemStack(Items.string)), 4));
        crusherRecipes.add(new CrusherRecipes("blockGlass",
                getItemStackString(new ItemStack(Blocks.sand)), 1));
        crusherRecipes.add(new CrusherRecipes(getOreDict(new ItemStack(Blocks.gravel)),
                getItemStackString(new ItemStack(Items.flint)), 1));
        
        //Mod Specific
        if (Loader.isModLoaded("appliedenergistics2")) {
            IMaterials mats = AEApi.instance().definitions().materials();
            IBlocks blocks = AEApi.instance().definitions().blocks();

            crusherRecipes.add(new CrusherRecipes(getOreDict(blocks.quartzOre().maybeStack(1).get()),
                    getItemStackString(mats.certusQuartzDust().maybeStack(1).get()), 2));
            crusherRecipes.add(new CrusherRecipes(getOreDict(mats.certusQuartzCrystal().maybeStack(1).get()),
                    getItemStackString(mats.certusQuartzDust().maybeStack(1).get()), 1));
            crusherRecipes.add(new CrusherRecipes(getOreDict(mats.certusQuartzCrystalCharged().maybeStack(1).get()),
                    getItemStackString(mats.certusQuartzDust().maybeStack(1).get()), 1));
            crusherRecipes.add(new CrusherRecipes(getOreDict(new ItemStack(Items.quartz)),
                    getItemStackString(mats.netherQuartzDust().maybeStack(1).get()), 1));
            crusherRecipes.add(new CrusherRecipes(getOreDict(mats.fluixCrystal().maybeStack(1).get()),
                    getItemStackString(mats.fluixDust().maybeStack(1).get()), 1));
        }
        
        saveToFile();
        LogHelper.info("Finished adding " + crusherRecipes.size() + " Crusher Recipes");
    }

    /**
     * Get the oreDict tag for an item
     * @param itemstack The stack to try
     * @return The string for this stack or OreDict name
     */
    private String getOreDict(ItemStack itemstack) {
        int[] registered = OreDictionary.getOreIDs(itemstack);
        if (registered.length > 0)
            return OreDictionary.getOreName(registered[0]);
        else {
            return getItemStackString(itemstack);
        }
    }

    /**
     * Get the oreDict tag for an item
     * @param itemstack The stack to find
     * @return The string for this stack or OreDict name
     */
    private boolean checkOreDict(String oreDict, ItemStack itemstack) {
        ArrayList<ItemStack> oreList = OreDictionary.getOres(oreDict);
        for (ItemStack list : oreList) {
            if (list.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
                if (list.getItem() == itemstack.getItem()) return true;
            }
            else if (list.isItemEqual(itemstack)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the output for an input
     */
    public ItemStack getOutput(ItemStack itemStack) {

        for (CrusherRecipes recipe : crusherRecipes) {
            String[] name = recipe.getInput().split(":");
            ItemStack outputStack = getItemStackFromString(recipe.getOutput());
            if (outputStack == null) continue;

            switch (name.length) {
                case 3:
                    ItemStack inputstack = getItemStackFromString(recipe.getInput());
                    if (inputstack == null) break;
                    if (itemStack.isItemEqual(inputstack))
                        return new ItemStack(outputStack.getItem(), recipe.getQty(), outputStack.getItemDamage());
                case 1:
                    if (checkOreDict(recipe.getInput(), itemStack))
                        return new ItemStack(outputStack.getItem(), recipe.getQty(), outputStack.getItemDamage());
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
     * @return Pretty much the whole recipes TODO check if NEI can handle oreDict
     */
    public Map<ItemStack, ItemStack> getCrusherInputList() {
        HashMap<ItemStack, ItemStack> listRecipes = new HashMap<>();

        for (CrusherRecipes recipe : crusherRecipes) {
            String[] name = recipe.getInput().split(":");
            ItemStack output = getItemStackFromString(recipe.getOutput());
            if (output == null) continue;
            switch (name.length) {
                case 3:
                    ItemStack inputStack = getItemStackFromString(recipe.getInput());
                    listRecipes.put(inputStack, new ItemStack(output.getItem(), recipe.getQty(), output.getItemDamage()));
                    break;
                case 1:
                    ArrayList<ItemStack> stacks = OreDictionary.getOres(recipe.getInput());
                    for (ItemStack stack : stacks) {
                        listRecipes.put(stack, new ItemStack(output.getItem(), recipe.getQty(), output.getItemDamage()));
                    }
            }
        }
        return listRecipes;
    }

    /**
     * Make sure the list exists
     */
    private void validateList() {
        if(crusherRecipes == null)
            crusherRecipes = new ArrayList<>();
    }

    private String getItemStackString(ItemStack itemStack) {
        GameRegistry.UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(itemStack.getItem());
        return id.modId + ":" + id.name + ":" + itemStack.getItemDamage();
    }

    private ItemStack getItemStackFromString(String item) {
        String[] name = item.split(":");
        switch(name.length) {
            case 3:
                Item itemFromString = GameRegistry.findItem(name[0], name[1]);
                if (itemFromString != null)
                    return new ItemStack(itemFromString, 1, Integer.valueOf(name[2]));
                else return null;
            default:
                return null;
        }
    }
}
