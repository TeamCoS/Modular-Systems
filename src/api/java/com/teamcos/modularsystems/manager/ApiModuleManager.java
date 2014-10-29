package com.teamcos.modularsystems.manager;

import com.teamcos.modularsystems.core.helper.ConfigHelper;
import com.teamcos.modularsystems.utilities.block.BlockFurnaceOverlay;
import com.teamcos.modularsystems.utilities.block.BlockSmelteryOverlay;
import com.teamcos.modularsystems.utilities.block.DummyBlock;
import com.teamcos.modularsystems.utilities.block.DummyIOBlock;
import com.teamcos.modularsystems.utilities.tiles.DummyIOTile;
import com.teamcos.modularsystems.utilities.tiles.DummyTile;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class ApiModuleManager {

    public static void enableFurnaceModule(CreativeTabs tab) {
        registerDummy(tab);
        registerDummyIO(tab);
        if (ConfigHelper.useTextures) {
            ApiBlockManager.furnaceOverlay = new BlockFurnaceOverlay(ConfigHelper.textureName, "furnaceOverlay", true);
        } else {
            ApiBlockManager.furnaceOverlay = new BlockFurnaceOverlay("modularsystems:custom_overlay", "furnaceOverlay", true);
        }

        GameRegistry.registerBlock(ApiBlockManager.furnaceOverlay, "furnaceOverlay");

        CraftingManager.getInstance().addRecipe(new ItemStack(ApiBlockManager.dummyIOBlock, 1),
                "XXX",
                "XxX",
                "XXX", 'X', Blocks.cobblestone, 'x', Blocks.dispenser);
    }

    public static void enableStorageModule() {

    }

    public static void enableEnchantingModule() {

    }

    public static void enableOreProcessing(CreativeTabs tab) {
        registerDummy(tab);
        registerDummyIO(tab);

        ApiBlockManager.smelteryOverlay = new BlockSmelteryOverlay();

        GameRegistry.registerBlock(ApiBlockManager.smelteryOverlay, "smelteryOverlay");

        CraftingManager.getInstance().addRecipe(new ItemStack(ApiBlockManager.dummyIOBlock, 1),
                "XXX",
                "XxX",
                "XXX", 'X', Items.flint, 'x', Blocks.dispenser);
    }

    private static boolean registeredDummy = false;
    private static boolean registeredDummyIO = false;

    public static void registerDummy(CreativeTabs tab) {
        if (!registeredDummy) {
            ApiBlockManager.dummyBlock = new DummyBlock(tab, Material.rock, true);
            GameRegistry.registerBlock(ApiBlockManager.dummyBlock, "modularDummy");
            GameRegistry.registerTileEntity(DummyTile.class, "dummyTile");
            registeredDummy = true;
        }
    }

    public static void registerDummyIO(CreativeTabs tab) {
        if (!registeredDummyIO) {
            ApiBlockManager.dummyIOBlock = new DummyIOBlock(tab, Material.rock, true);
            GameRegistry.registerBlock(ApiBlockManager.dummyIOBlock, "modularDummyIO");
            GameRegistry.registerTileEntity(DummyIOTile.class, "dummyIOTile");
            registeredDummyIO = true;
        }
    }
}
