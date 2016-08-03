package com.teambr.modularsystems.core.managers;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * This file was created for NeoTech
 * <p/>
 * NeoTech is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Dyonovan
 * @since August 21, 2015
 */
public class CraftingManager {

    public static void init() {

        //Furnace
        GameRegistry.addRecipe(new ItemStack(BlockManager.furnaceCore(), 1),
                "III",
                "IFI",
                "III", 'I', Items.IRON_INGOT, 'F', Blocks.FURNACE);

        //Crusher
        GameRegistry.addRecipe(new ItemStack(BlockManager.crusherCore(), 1),
                "FFF",
                "FOF",
                "FFF", 'F', Items.FLINT, 'O', Blocks.PISTON);

        //I/O
        GameRegistry.addRecipe(new ItemStack(BlockManager.ioExpansion(), 1),
                " P ",
                "HDH",
                " P ", 'P', Blocks.PISTON, 'H', Blocks.HOPPER, 'D', Blocks.DISPENSER);

        // Crusher Expansion
        GameRegistry.addRecipe(new ItemStack(BlockManager.crusherExpansion()),
                "PFP",
                "PIP",
                "PFP", 'P', Blocks.PISTON, 'F', Items.FLINT, 'I', Blocks.IRON_BLOCK);

        //Solid Fuel Bank
        GameRegistry.addRecipe(new ItemStack(BlockManager.bankSolids(), 1),
                "CIC",
                "ITI",
                "CIC", 'C', Items.COAL, 'I', Items.IRON_INGOT, 'T', Blocks.CHEST);

        //Liquid Fuel Bank
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockManager.bankLiquids(), 1),
                "BIB",
                "IGI",
                "BIB", 'B', Items.BUCKET, 'I', Items.IRON_INGOT, 'G', "blockGlass"));

        //RF Fuel Bank
        GameRegistry.addRecipe(new ItemStack(BlockManager.bankRF(), 1),
                "RIR",
                "IPI",
                "RIR", 'R', Blocks.REDSTONE_BLOCK, 'I', Items.IRON_INGOT, 'P', Blocks.PISTON);

        // Storage Core
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockManager.storageCore()),
                "LWL",
                "WCW",
                "LWL", 'L', "logWood", 'W', "plankWood", 'C', Blocks.CHEST));

        // Simple Storage Expansion
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockManager.simpleStorageExpansion(), 8),
                "WWW",
                "WIW",
                "WWW", 'W', "plankWood", 'I', "ingotIron"));

        // Capacity Storage Expansion
        GameRegistry.addRecipe(new ItemStack(BlockManager.capacityStorageExpansion()),
                "CCC",
                "CSC",
                "CCC", 'C', Blocks.CHEST, 'S', BlockManager.simpleStorageExpansion());

        // Search Storage Expansion
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockManager.searchStorageExpansion()),
                "GBG",
                "GSG",
                "WWW", 'G', "blockGlass", 'B', Blocks.BOOKSHELF, 'S', BlockManager.simpleStorageExpansion(), 'W', "plankWood"));

        // Crafting Storage Expansion
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockManager.craftingStorageExpansion()),
                "WWW",
                "CSC",
                "WWW", 'W', "plankWood", 'C', Blocks.CRAFTING_TABLE, 'S', BlockManager.simpleStorageExpansion()));

        // Armor Storage Expansion
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockManager.armorStorageExpansion()),
                "IWI",
                "ISI",
                "IWI", 'I', "ingotIron", 'W', "plankWood", 'S', BlockManager.simpleStorageExpansion()));
    }
}
