package com.teamcos.modularsystems.manager;

import com.teamcos.modularsystems.utilities.block.DummyBlock;
import com.teamcos.modularsystems.utilities.block.DummyIOBlock;
import com.teamcos.modularsystems.utilities.tiles.DummyIOTile;
import com.teamcos.modularsystems.utilities.tiles.DummyTile;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class ApiModuleManager {

    public static void enableFurnaceModule() {
        registerDummy();
        registerDummyIO();

        CraftingManager.getInstance().addRecipe(new ItemStack(ApiBlockManager.dummyIOBlock, 1),
                "XXX",
                "XxX",
                "XXX", 'X', Blocks.cobblestone, 'x', Blocks.dispenser);
    }

    public static void enableStorageModule() {

    }

    public static void enableEnchantingModule() {

    }

    public static void enableOreProcessing() {
        registerDummy();
        registerDummyIO();

        CraftingManager.getInstance().addRecipe(new ItemStack(ApiBlockManager.dummyIOBlock, 1),
                "XXX",
                "XxX",
                "XXX", 'X', Items.flint, 'x', Blocks.dispenser);
    }

    private static boolean registeredDummy = false;
    private static boolean registeredDummyIO = false;

    public static void registerDummy() {
        if (!registeredDummy) {
            ApiBlockManager.dummyBlock = new DummyBlock(Material.rock, false);
            GameRegistry.registerBlock(ApiBlockManager.dummyBlock, "modularDummy");
            GameRegistry.registerTileEntity(DummyTile.class, "dummyTile");
        }
    }

    public static void registerDummyIO() {
        if (!registeredDummyIO) {
            ApiBlockManager.dummyIOBlock = new DummyIOBlock(Material.rock, false);
            GameRegistry.registerBlock(ApiBlockManager.dummyIOBlock, "modularDummyIO");
            GameRegistry.registerTileEntity(DummyIOTile.class, "dummyIOTile");
        }
    }
}
