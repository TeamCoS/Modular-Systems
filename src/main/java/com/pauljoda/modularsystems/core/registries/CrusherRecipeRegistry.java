package com.pauljoda.modularsystems.core.registries;

import com.teambr.bookshelf.helpers.LogHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@SuppressWarnings("unused")
public class CrusherRecipeRegistry {
    public static CrusherRecipeRegistry INSTANCE = new CrusherRecipeRegistry();

    protected HashMap<ItemStack, ItemStack> crusherRecipes;
    protected ArrayList<ShapelessOreRecipe> newCrusherRecipes;

    public CrusherRecipeRegistry() {
        crusherRecipes = new LinkedHashMap<>();
        newCrusherRecipes = new ArrayList<>();
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

                    switch ("ore" + ore) {
                        case "oreRedstone":
                            newCrusherRecipes.add(new ShapelessOreRecipe(new ItemStack(Items.redstone, 8), Blocks.redstone_ore));
                            break;
                        case "oreLapis":
                            newCrusherRecipes.add(new ShapelessOreRecipe(new ItemStack(Items.dye, 8, 4), Blocks.lapis_ore));
                            break;
                        default:
                            newCrusherRecipes.add(new ShapelessOreRecipe(new ItemStack(outputDust.get(0).getItem(), 2, outputDust.get(0).getItemDamage()), "ore" + ore));
                    }
                }
            } else if (anOreDict1.startsWith("ingot")) { //Ingot to Dust
                String ingot = anOreDict1.replaceFirst("ingot", "");
                if (OreDictionary.doesOreNameExist("dust" + ingot)) {
                    List<ItemStack> outputDust = OreDictionary.getOres("dust" + ingot);

                    newCrusherRecipes.add(new ShapelessOreRecipe(new ItemStack(outputDust.get(0).getItem(), 1, outputDust.get(0).getItemDamage()), anOreDict1));
                }
            }
        }

        //misc recipes
        newCrusherRecipes.add(new ShapelessOreRecipe(new ItemStack(Items.blaze_powder, 4), new ItemStack(Items.blaze_rod)));
        newCrusherRecipes.add(new ShapelessOreRecipe(new ItemStack(Blocks.sand), new ItemStack(Blocks.cobblestone)));
        newCrusherRecipes.add(new ShapelessOreRecipe(new ItemStack(Items.dye, 6, 15), new ItemStack(Items.bone)));
        newCrusherRecipes.add(new ShapelessOreRecipe(new ItemStack(Items.quartz, 4), new ItemStack(Blocks.quartz_ore)));
        newCrusherRecipes.add(new ShapelessOreRecipe(new ItemStack(Items.clay_ball, 4), new ItemStack(Blocks.clay)));
        newCrusherRecipes.add(new ShapelessOreRecipe(new ItemStack(Items.diamond, 2), new ItemStack(Blocks.diamond_ore)));
        newCrusherRecipes.add(new ShapelessOreRecipe(new ItemStack(Items.emerald, 2), new ItemStack(Blocks.emerald_ore)));
        newCrusherRecipes.add(new ShapelessOreRecipe(new ItemStack(Items.glowstone_dust, 4), new ItemStack(Blocks.glowstone)));
        newCrusherRecipes.add(new ShapelessOreRecipe(new ItemStack(Items.coal, 4), new ItemStack(Blocks.coal_ore)));
        newCrusherRecipes.add(new ShapelessOreRecipe(new ItemStack(Items.string, 4), "wool")); //TODO

        LogHelper.info("Finished adding " + newCrusherRecipes.size() + " Crusher Recipes");


        for (String anOreDict : oreDict) {
            if (anOreDict.startsWith("dust")) { //Ore to Dust
                String ore = anOreDict.replaceFirst("dust", "");
                if (OreDictionary.doesOreNameExist("ore" + ore)) {
                    List<ItemStack> inputOre = OreDictionary.getOres("ore" + ore);
                    List<ItemStack> outputDust = OreDictionary.getOres(anOreDict);

                    for (ItemStack anInputOre : inputOre) {
                        switch (anInputOre.getUnlocalizedName()) {
                            case "tile.oreRedstone":
                                crusherRecipes.put(anInputOre, new ItemStack(outputDust.get(0).getItem(), 8, outputDust.get(0).getItemDamage()));
                                break;
                            case "tile.oreLapis":
                                crusherRecipes.put(anInputOre, new ItemStack(outputDust.get(0).getItem(), 8, outputDust.get(0).getItemDamage()));
                                break;
                            default:
                                crusherRecipes.put(anInputOre, new ItemStack(outputDust.get(0).getItem(), 2, outputDust.get(0).getItemDamage()));
                        }
                    }
                }
            } else if (anOreDict.startsWith("ingot")) { //Ingot to Dust
                String dust = anOreDict.replaceFirst("ingot", "");
                if (OreDictionary.doesOreNameExist("dust" + dust)) {
                    List<ItemStack> inputOre = OreDictionary.getOres(anOreDict);
                    List<ItemStack> outputDust = OreDictionary.getOres("dust" + dust);

                    for (ItemStack anInputOre : inputOre) {
                        crusherRecipes.put(anInputOre, new ItemStack(outputDust.get(0).getItem(), 1, outputDust.get(0).getItemDamage()));
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

    @SuppressWarnings("SuspiciousMethodCalls")
    public ItemStack getOutput(ItemStack itemStack) {
        for (ShapelessOreRecipe newCrusherRecipe : newCrusherRecipes) {
            ArrayList<Object> list = newCrusherRecipe.getInput();
            if (list.get(0) instanceof OreDictionary) {
                ArrayList<OreDictionary> dictList = (ArrayList<OreDictionary>) list.get(0);
                if (dictList.contains(new ItemStack(itemStack.getItem(), 1, itemStack.getItemDamage())))
                    return newCrusherRecipe.getRecipeOutput();
            } else {
                if (list.contains(new ItemStack(itemStack.getItem(), 1, itemStack.getItemDamage())))
                    return newCrusherRecipe.getRecipeOutput();
            }
        }
        return null;
    }

    public boolean isItemValidNew(ItemStack itemStack) {
        return getOutput(itemStack) != null;
    }

}
