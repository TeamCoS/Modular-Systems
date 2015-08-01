package com.pauljoda.modularsystems.core.managers;

import com.pauljoda.modularsystems.core.registries.ConfigRegistry;
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
        if (ConfigRegistry.furnaceCore)
            GameRegistry.addRecipe(new ItemStack(BlockManager.furnaceCore, 1),
                    "III",
                    "IFI",
                    "III", 'I', Items.iron_ingot, 'F', Blocks.furnace);

        //Crusher
        if (ConfigRegistry.crusherCore)
            GameRegistry.addRecipe(new ItemStack(BlockManager.crusherCore, 1),
                    "FFF",
                    "FOF",
                    "FFF", 'F', Items.flint, 'O', Blocks.piston);

        //I/O
        GameRegistry.addRecipe(new ItemStack(BlockManager.io, 1),
                " P ",
                "HDH",
                " P ", 'P', Blocks.piston, 'H', Blocks.hopper, 'D', Blocks.dispenser);

        //Redstone In
        GameRegistry.addRecipe(new ItemStack(BlockManager.redstoneControlIn, 1),
                " C ",
                "RSR",
                "   ", 'C', Items.comparator, 'R', Items.redstone, 'S', Blocks.redstone_torch);

        //Redstone Out
        GameRegistry.addRecipe(new ItemStack(BlockManager.redstoneControlOut, 1),
                " C ",
                "RSR",
                " B ", 'C', Items.comparator, 'R', Items.redstone, 'S', Blocks.redstone_torch, 'B', Blocks.redstone_block);

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
        if (Loader.isModLoaded("Botania"))
            GameRegistry.addRecipe(new ItemStack(BlockManager.powerMana, 1),
                    "SIS",
                    "IFI",
                    "SIS", 'S', Item.getItemFromBlock(Blocks.sapling), 'I', Items.iron_ingot, 'F', Items.flower_pot);

        //Generator Core
        if (ConfigRegistry.generatorCore)
            GameRegistry.addRecipe(new ItemStack(BlockManager.generatorCore, 1),
                    "IPI",
                    "PBP",
                    "IPI", 'I', Items.iron_ingot, 'P', Blocks.piston, 'B', Blocks.iron_block);

        //RF Output
        GameRegistry.addShapelessRecipe(new ItemStack(BlockManager.supplierRF, 1),
                BlockManager.powerRF);
        GameRegistry.addShapelessRecipe(new ItemStack(BlockManager.powerRF, 1),
                BlockManager.supplierRF);

        //IC2 Output
        if (Loader.isModLoaded("IC2")) {
            GameRegistry.addShapelessRecipe(new ItemStack(BlockManager.supplierIC2, 1),
                    BlockManager.powerIC2);
            GameRegistry.addShapelessRecipe(new ItemStack(BlockManager.powerIC2, 1),
                    BlockManager.supplierIC2);
        }

        //Storage Core
        if (ConfigRegistry.storageSystem) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockManager.storageCore, 1),
                    "LWL",
                    "WCW",
                    "LWL", 'L', "logWood", 'W', "plankWood", 'C', Blocks.chest));

            //Basic Storage Expansion
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockManager.storageBasic, 4),
                    "WWW",
                    "WSW",
                    "WWW", 'W', "plankWood", 'S', Items.string));

            //Capacity Expansion
            GameRegistry.addShapelessRecipe(new ItemStack(BlockManager.storageCapacity, 1),
                    BlockManager.storageBasic, Blocks.chest);

            //Search Expansion
            GameRegistry.addShapelessRecipe(new ItemStack(BlockManager.storageSearch, 1),
                    BlockManager.storageBasic, Items.writable_book);

            //Sort Expansion
            GameRegistry.addShapelessRecipe(new ItemStack(BlockManager.storageSort, 1),
                    BlockManager.storageBasic, Items.book);

            //Security Expansion
            GameRegistry.addShapelessRecipe(new ItemStack(BlockManager.storageSecurity, 1),
                    BlockManager.storageBasic, Blocks.iron_bars);

            //Crafting Expansion
            GameRegistry.addShapelessRecipe(new ItemStack(BlockManager.storageCrafting, 1),
                    BlockManager.storageBasic, Blocks.crafting_table);

            //Hopping Expansion
            GameRegistry.addShapelessRecipe(new ItemStack(BlockManager.storageHopping, 1),
                    BlockManager.storageBasic, Blocks.hopper, Items.ender_pearl);

            //Smashing Expansion
            GameRegistry.addShapelessRecipe(new ItemStack(BlockManager.storageSmashing, 1),
                    BlockManager.storageBasic, Items.iron_pickaxe);

            //IO Expansion
            GameRegistry.addShapelessRecipe(new ItemStack(BlockManager.storageIO, 1),
                    BlockManager.storageBasic, Blocks.hopper, Blocks.piston, Items.book);
        }
    }
}
