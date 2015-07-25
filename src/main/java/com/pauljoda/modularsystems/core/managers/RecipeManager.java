package com.pauljoda.modularsystems.core.managers;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * This is where we add all our recipes
 */
public class RecipeManager {
    public static void init() {

        //Furnace
        GameRegistry.addRecipe(new ItemStack(BlockManager.furnaceCore, 1),
                "III",
                "IFI",
                "III", 'I', Items.iron_ingot, 'F', Blocks.furnace);

        //Crusher
        GameRegistry.addRecipe(new ItemStack(BlockManager.crusherCore, 1),
                "FFF",
                "FOF",
                "FFF", 'F', Items.flint, 'O', Blocks.furnace);

        //I/O
        GameRegistry.addRecipe(new ItemStack(BlockManager.io, 1),
                " P ",
                "HDH",
                " P ", 'P', Blocks.piston, 'H', Blocks.hopper, 'D', Blocks.dispenser);

        //Solid Fuel Bank
        GameRegistry.addRecipe(new ItemStack(BlockManager.powerSolids, 1),
                "CIC",
                "ITI",
                "CIC", 'C', Items.coal, 'I', Items.iron_ingot, 'T', Blocks.chest);

        //Liquid Fuel Bank
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockManager.powerLiquids, 1),
                "BIB",
                "IGI",
                "BIB", 'B', Items.bucket, 'I', Items.iron_ingot, 'G', "blockGlass"));

        //RF Fuel Bank
        GameRegistry.addRecipe(new ItemStack(BlockManager.powerRF, 1),
                "RIR",
                "IPI",
                "RIR", 'R', Blocks.redstone_block, 'I', Items.iron_ingot, 'P', Blocks.piston);

        //IC2 Fuel Bank
        if (Loader.isModLoaded("IC2"))
            GameRegistry.addRecipe(new ItemStack(BlockManager.powerIC2, 1),
                    "IiI",
                    "IPI",
                    "IiI", 'I', Blocks.iron_block, 'i', Items.iron_ingot, 'P', Blocks.piston);

        //Botania Fuel Bank
        if(Loader.isModLoaded("Botania"))
            GameRegistry.addRecipe(new ItemStack(BlockManager.powerMana, 1),
                    "SIS",
                    "IFI",
                    "SIS", 'S', Item.getItemFromBlock(Blocks.sapling), 'I', Items.iron_ingot, 'F', Items.flower_pot);

        //Storage Core
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockManager.storageCore, 1),
                "LWL",
                "WCW",
                "LWL", 'L', "treeWood", 'W', "plankWood", 'C', Blocks.chest));

        //Basic Storage Expansion
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockManager.storageBasic, 4),
                "WWW",
                "WSW",
                "WWW", 'W', "plankWood", 'S', Items.string));

        //Capacity Expansion
        GameRegistry.addShapelessRecipe(new ItemStack(BlockManager.storageCapacity, 1),
                BlockManager.storageBasic, Blocks.chest);

        GameRegistry.addShapelessRecipe(new ItemStack(BlockManager.storageSearch, 1),
                BlockManager.storageBasic, Items.writable_book);
    }
}
