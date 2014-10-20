package com.teamcos.modularsystems.core.managers;

import com.teamcos.modularsystems.core.ModularSystems;
import com.teamcos.modularsystems.core.helper.ConfigHelper;
import com.teamcos.modularsystems.core.helper.OreDictionaryHelper;
import com.teamcos.modularsystems.enchanting.blocks.BlockEnchantmentAlter;
import com.teamcos.modularsystems.enchanting.tiles.TileEntityEnchantmentAlter;
import com.teamcos.modularsystems.furnace.blocks.BlockCrafter;
import com.teamcos.modularsystems.furnace.blocks.BlockFurnaceAddition;
import com.teamcos.modularsystems.furnace.blocks.BlockFurnaceCore;
import com.teamcos.modularsystems.furnace.blocks.BlockTextureOverlay;
import com.teamcos.modularsystems.furnace.tiles.TileEntityFurnaceCore;
import com.teamcos.modularsystems.manager.ApiModuleManager;
import com.teamcos.modularsystems.oreprocessing.blocks.BlockSmelteryCore;
import com.teamcos.modularsystems.oreprocessing.blocks.BlockSmelteryOverlay;
import com.teamcos.modularsystems.oreprocessing.tiles.TileEntitySmelteryCore;
import com.teamcos.modularsystems.registries.OreProcessingRegistry;
import com.teamcos.modularsystems.storage.blocks.*;
import com.teamcos.modularsystems.storage.tiles.TileEntityStorageCore;
import com.teamcos.modularsystems.storage.tiles.TileEntityStorageExpansion;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class ModuleManager {
    public static void enableFurnaceModule() {
        ApiModuleManager.enableFurnaceModule();
        BlockManager.furnaceCore = new BlockFurnaceCore(false).setBlockName("modularsystems:blockFurnaceCore").setCreativeTab(ModularSystems.tabModularSystems);
        BlockManager.furnaceCoreActive = new BlockFurnaceCore(true).setLightLevel(1F).setBlockName("modularsystems:blockFurnaceCoreActive");
        BlockManager.furnaceCraftingUpgrade = new BlockCrafter();
        BlockManager.furnaceAddition = new BlockFurnaceAddition();
        if (ConfigHelper.useTextures) {
            BlockManager.overLayTexture = new BlockTextureOverlay(ConfigHelper.textureName, "overLayTexture", true);
        } else {
            BlockManager.overLayTexture = new BlockTextureOverlay("modularsystems:custom_overlay", "overLayTexture", true);
        }

        GameRegistry.registerBlock(BlockManager.furnaceCore, "blockFurnaceCore");
        GameRegistry.registerBlock(BlockManager.furnaceCoreActive, "blockFurnaceCoreActive");
        GameRegistry.registerBlock(BlockManager.furnaceCraftingUpgrade, "blockFurnaceCraftingUpgrade");
        GameRegistry.registerBlock(BlockManager.furnaceAddition, "blockFurnaceAddition");
        GameRegistry.registerBlock(BlockManager.overLayTexture, "overLayTexture");

        CraftingManager.getInstance().addRecipe(new ItemStack(BlockManager.furnaceCore, 1),
                "XXX",
                "XxX",
                "XXX", 'X', Items.iron_ingot, 'x', Blocks.furnace);

        CraftingManager.getInstance().addRecipe(new ItemStack(BlockManager.furnaceAddition, 1),
                "XXX",
                "XxX",
                "XXX", 'X', Items.iron_ingot, 'x', BlockManager.furnaceCore);

        CraftingManager.getInstance().addRecipe(new ItemStack(BlockManager.furnaceCraftingUpgrade, 1),
                "XxX",
                "xCx",
                "XxX", 'X', Items.iron_ingot, 'x', new ItemStack(Items.dye, 9, 4), 'C', Blocks.crafting_table);

        GameRegistry.registerTileEntity(TileEntityFurnaceCore.class, "modularsystems:tileEntityFurnaceCore");
    }

    public static void enableStorageModule() {
        ApiModuleManager.enableStorageModule();
        BlockManager.storageCore = new BlockStorageCore();
        BlockManager.basicExpansion = new BlockBasicExpansion();
        BlockManager.storageExpansion = new BlockCapacityExpansion();
        BlockManager.storageHoppingExpansion = new BlockHoppingExpansion();
        BlockManager.storageArmorExpansion = new BlockArmorExpansion();
        BlockManager.storageSmashingExpansion = new BlockSmashingExpansion();
        BlockManager.storageSortingExpansion = new BlockSortingExpansion();
        BlockManager.storageCraftingExpansion = new BlockCraftingExpansion();

        GameRegistry.registerBlock(BlockManager.storageCore, "blockStorageCore");
        GameRegistry.registerBlock(BlockManager.basicExpansion, "blockBasicExpansion");
        GameRegistry.registerBlock(BlockManager.storageExpansion, "blockStorageExpansion");
        GameRegistry.registerBlock(BlockManager.storageHoppingExpansion, "blockHoppingStorageExpansion");
        GameRegistry.registerBlock(BlockManager.storageArmorExpansion, "blockArmorStorageExpansion");
        GameRegistry.registerBlock(BlockManager.storageSmashingExpansion, "blockSmashingStorageExpansion");
        GameRegistry.registerBlock(BlockManager.storageSortingExpansion, "blockSortingStorageExpansion");
        GameRegistry.registerBlock(BlockManager.storageCraftingExpansion, "blockCraftingStorageExpansion");

        CraftingManager.getInstance().addRecipe(new ItemStack(BlockManager.storageCore, 1),
                "XxX",
                "xCx",
                "XxX", 'X', Blocks.log, 'x', Blocks.planks, 'C', Blocks.chest);

        CraftingManager.getInstance().addRecipe(new ItemStack(BlockManager.basicExpansion, 1),
                "XXX",
                "XxX",
                "XXX", 'X', Blocks.planks, 'x', Items.string);

        CraftingManager.getInstance().addShapelessRecipe(new ItemStack(BlockManager.storageExpansion, 1), BlockManager.basicExpansion, Blocks.chest);

        CraftingManager.getInstance().addShapelessRecipe(new ItemStack(BlockManager.storageHoppingExpansion, 1), BlockManager.basicExpansion, Items.ender_pearl, Blocks.hopper);

        CraftingManager.getInstance().addRecipe(new ItemStack(BlockManager.storageArmorExpansion, 1),
                " X ",
                "XsX",
                " X ", 'X', Items.leather, 's', BlockManager.basicExpansion);

        CraftingManager.getInstance().addShapelessRecipe(new ItemStack(BlockManager.storageSmashingExpansion, 1), Items.diamond_pickaxe, BlockManager.basicExpansion);

        CraftingManager.getInstance().addShapelessRecipe(new ItemStack(BlockManager.storageCraftingExpansion, 1), Blocks.crafting_table, BlockManager.basicExpansion);

        CraftingManager.getInstance().addRecipe(new ItemStack(BlockManager.storageSortingExpansion, 1),
                "XXX",
                "XxX",
                "XXX", 'X', Items.book, 'x', BlockManager.basicExpansion);

        GameRegistry.registerTileEntity(TileEntityStorageCore.class, "modularsystems:tileEntityStorageCore");
        GameRegistry.registerTileEntity(TileEntityStorageExpansion.class, "modularsystems:tileEntityStorageExpansion");
    }

    public static void enableEnchantingModule() {
        ApiModuleManager.enableEnchantingModule();
        BlockManager.enchantmentAlter = new BlockEnchantmentAlter();

        GameRegistry.registerBlock(BlockManager.enchantmentAlter, "blockEnchantmentAlter");

        CraftingManager.getInstance().addRecipe(new ItemStack(BlockManager.enchantmentAlter, 1),
                "   ",
                " x ",
                "XeX", 'X', Blocks.emerald_block, 'x', Items.book, 'e', Blocks.enchanting_table);

        GameRegistry.registerTileEntity(TileEntityEnchantmentAlter.class, "modularsystems:tileEntityEnchantmentAlter");
    }

    public static void enableOreProcessingModule() {
        ApiModuleManager.enableOreProcessing();

        OreDictionaryHelper.initDustsAsNeeded();
        OreProcessingRegistry.addOreProcessingRecipe(Blocks.cobblestone, Blocks.sand);

        BlockManager.smelteryCore = new BlockSmelteryCore(false).setBlockName("modularsystems:blockSmelteryCore").setCreativeTab(ModularSystems.tabModularSystems);
        BlockManager.smelteryCoreActive = new BlockSmelteryCore(true).setBlockName("modularsystems:blockSmelteryCoreActive").setLightLevel(1.0F);
        BlockManager.smeleryOverlay = new BlockSmelteryOverlay();

        GameRegistry.registerBlock(BlockManager.smelteryCore, "smelteryCore");
        GameRegistry.registerBlock(BlockManager.smelteryCoreActive, "smelteryCoreActive");
        GameRegistry.registerBlock(BlockManager.smeleryOverlay, "smelteryOverlay");

        CraftingManager.getInstance().addRecipe(new ItemStack(BlockManager.smelteryCore, 1),
                "XXX",
                "XxX",
                "XXX", 'X', Items.flint, 'x', Blocks.furnace);

        GameRegistry.registerTileEntity(TileEntitySmelteryCore.class, "modularsystems:tileEntitySmetleryCore");
    }
}
