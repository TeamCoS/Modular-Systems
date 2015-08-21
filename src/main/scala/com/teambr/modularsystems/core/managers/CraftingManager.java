package com.teambr.modularsystems.core.managers;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
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
                    "III", 'I', Items.iron_ingot, 'F', Blocks.furnace);

        //Crusher
            GameRegistry.addRecipe(new ItemStack(BlockManager.crusherCore(), 1),
                    "FFF",
                    "FOF",
                    "FFF", 'F', Items.flint, 'O', Blocks.piston);

        //I/O
        GameRegistry.addRecipe(new ItemStack(BlockManager.ioExpansion(), 1),
                " P ",
                "HDH",
                " P ", 'P', Blocks.piston, 'H', Blocks.hopper, 'D', Blocks.dispenser);

        //Solid Fuel Bank
        GameRegistry.addRecipe(new ItemStack(BlockManager.bankSolids(), 1),
                "CIC",
                "ITI",
                "CIC", 'C', Items.coal, 'I', Items.iron_ingot, 'T', Blocks.chest);

        //Liquid Fuel Bank
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockManager.bankLiquids(), 1),
                "BIB",
                "IGI",
                "BIB", 'B', Items.bucket, 'I', Items.iron_ingot, 'G', "blockGlass"));

        //RF Fuel Bank
        GameRegistry.addRecipe(new ItemStack(BlockManager.bankRF(), 1),
                "RIR",
                "IPI",
                "RIR", 'R', Blocks.redstone_block, 'I', Items.iron_ingot, 'P', Blocks.piston);

        //Storage Core
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockManager.storageCore(), 1),
                    "LWL",
                    "WCW",
                    "LWL", 'L', "logWood", 'W', "plankWood", 'C', Blocks.chest));

            //Basic Storage Expansion
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockManager.storageBasic(), 4),
                    "WWW",
                    "WSW",
                    "WWW", 'W', "plankWood", 'S', Items.string));

            //Capacity Expansion
            GameRegistry.addShapelessRecipe(new ItemStack(BlockManager.storageCapacity(), 1),
                    BlockManager.storageBasic(), Blocks.chest);

            //Search Expansion
            GameRegistry.addShapelessRecipe(new ItemStack(BlockManager.storageSearch(), 1),
                    BlockManager.storageBasic(), Items.writable_book);

            //Sort Expansion
            GameRegistry.addShapelessRecipe(new ItemStack(BlockManager.storageSort(), 1),
                    BlockManager.storageBasic(), Items.book);

            //Crafting Expansion
            GameRegistry.addShapelessRecipe(new ItemStack(BlockManager.storageCrafting(), 1),
                    BlockManager.storageBasic(), Blocks.crafting_table);

            //Hopping Expansion
            GameRegistry.addShapelessRecipe(new ItemStack(BlockManager.storageHopping(), 1),
                    BlockManager.storageBasic(), Blocks.hopper, Items.ender_pearl);

            //Smashing Expansion
            GameRegistry.addShapelessRecipe(new ItemStack(BlockManager.storageSmashing(), 1),
                    BlockManager.storageBasic(), Items.iron_pickaxe);

    }
}
