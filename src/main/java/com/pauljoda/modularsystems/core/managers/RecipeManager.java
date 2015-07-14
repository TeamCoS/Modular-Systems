package com.pauljoda.modularsystems.core.managers;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

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
    }
}
