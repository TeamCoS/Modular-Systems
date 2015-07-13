package com.pauljoda.modularsystems.core.registries;

import com.teambr.bookshelf.helpers.LogHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class CrusherRecipeRegistry {
    public static CrusherRecipeRegistry INSTANCE = new CrusherRecipeRegistry();

    protected HashMap<ItemStack, ItemStack> crusherRecipes;

    public CrusherRecipeRegistry() {
        crusherRecipes = new LinkedHashMap<>();
    }

    /**
     * Add the values
     */
    public void init() {
        LogHelper.info("Creating Dynamic Crusher Recipe List");

        String[] oreDict = OreDictionary.getOreNames();

        //Ore to Dust
        for (int i = 0; i < oreDict.length; i++) {
            if (oreDict[i].startsWith("dust")) {
                String ore = oreDict[i].replaceFirst("dust", "");
                if (OreDictionary.doesOreNameExist("ore" + ore)) {
                    List<ItemStack> inputOre =  OreDictionary.getOres("ore" + ore);
                    List<ItemStack> outputDust = OreDictionary.getOres(oreDict[i]);

                    for (int j = 0; j < inputOre.size(); j++) {
                        switch (inputOre.get(j).getUnlocalizedName()) {
                            case "tile.oreRedstone":
                                crusherRecipes.put(inputOre.get(j), new ItemStack(outputDust.get(0).getItem(), 8, outputDust.get(0).getItemDamage()));
                                break;
                            case "tile.oreLapis":
                                crusherRecipes.put(inputOre.get(j), new ItemStack(outputDust.get(0).getItem(), 8, outputDust.get(0).getItemDamage()));
                                break;
                            default:
                                crusherRecipes.put(inputOre.get(j), new ItemStack(outputDust.get(0).getItem(), 2, outputDust.get(0).getItemDamage()));
                        }
                    }
                }
            }
        }

        //Ingot to dust
        for (int i = 0; i < oreDict.length; i++) {
            if (oreDict[i].startsWith("ingot")) {
                String dust = oreDict[i].replaceFirst("ingot", "");
                if (OreDictionary.doesOreNameExist("dust" + dust)) {
                    List<ItemStack> inputOre =  OreDictionary.getOres(oreDict[i]);
                    List<ItemStack> outputDust = OreDictionary.getOres("dust" + dust);

                    for (int j = 0; j < inputOre.size(); j++) {
                        crusherRecipes.put(inputOre.get(j), new ItemStack(outputDust.get(0).getItem(), 2, outputDust.get(0).getItemDamage()));
                    }
                }
            }
        }

        //misc recipes
        crusherRecipes.put(new ItemStack(Items.blaze_rod), new ItemStack(Items.blaze_powder, 4));
        crusherRecipes.put(new ItemStack(Blocks.cobblestone), new ItemStack(Blocks.sand));
        crusherRecipes.put(new ItemStack(Items.bone), new ItemStack(Items.dye, 6, 15));
        crusherRecipes.put(new ItemStack(Blocks.quartz_ore), new ItemStack(Items.quartz, 4));
        crusherRecipes.put(new ItemStack(Blocks.clay), new ItemStack(Items.clay_ball, 4));
        crusherRecipes.put(new ItemStack(Blocks.diamond_ore), new ItemStack(Items.diamond, 2));
        crusherRecipes.put(new ItemStack(Blocks.emerald_ore), new ItemStack(Items.emerald, 2));
        crusherRecipes.put(new ItemStack(Blocks.glowstone), new ItemStack(Items.glowstone_dust, 4));
        crusherRecipes.put(new ItemStack(Blocks.coal_ore), new ItemStack(Items.coal, 4));
        for (int i = 0; i < 16; i++)
            crusherRecipes.put(new ItemStack(Blocks.wool, 1, i), new ItemStack(Items.string, 4));

        LogHelper.info("Finished adding " + crusherRecipes.size() + " Crusher Recipes");
    }

    /**
     *
     * @param itemStack to check
     * @return boolean
     */
    public boolean isItemValid(ItemStack itemStack) {
        return crusherRecipes.containsKey(new ItemStack(itemStack.getItem(), 1, itemStack.getItemDamage()));
    }

    /**
     *
     * @param itemStack
     * @return output Itemstack or Null
     */
    public ItemStack getCrusherOutput(ItemStack itemStack) {
        return crusherRecipes.get(new ItemStack(itemStack.getItem(), 1, itemStack.getItemDamage()));
    }
}
