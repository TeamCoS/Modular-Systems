package com.pauljoda.modularsystems.core.managers;

import com.pauljoda.modularsystems.core.ModularSystems;
import com.pauljoda.modularsystems.core.crafting.OreProcessingRecipies;
import com.pauljoda.modularsystems.core.helper.ConfigHelper;
import com.pauljoda.modularsystems.core.helper.OreDictionaryHelper;
import com.pauljoda.modularsystems.enchanting.blocks.BlockEnchantmentAlter;
import com.pauljoda.modularsystems.furnace.blocks.*;
import com.pauljoda.modularsystems.oreprocessing.blocks.BlockSmelteryCore;
import com.pauljoda.modularsystems.oreprocessing.blocks.BlockSmelteryDummy;
import com.pauljoda.modularsystems.oreprocessing.blocks.BlockSmelteryDummyIO;
import com.pauljoda.modularsystems.oreprocessing.blocks.BlockSmelteryOverlay;
import com.pauljoda.modularsystems.storage.blocks.*;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class ModuleManager
{
    public static void enableFurnaceModule()
    {
        BlockManager.furnaceCore = new BlockFurnaceCore(false).setBlockName("modularsystems:blockFurnaceCore").setCreativeTab(ModularSystems.tabModularSystems);
        BlockManager.furnaceCoreActive = new BlockFurnaceCore(true).setLightLevel(1F).setBlockName("modularsystems:blockFurnaceCoreActive");
        BlockManager. furnaceDummy = new BlockFurnaceDummy();
        BlockManager.furnaceCraftingUpgrade = new BlockCrafter();
        BlockManager.furnaceDummyIO = new BlockFurnaceDummyIO();
        BlockManager.furnaceAddition = new BlockFurnaceAddition();
        if(ConfigHelper.useTextures)
            BlockManager.overLayTexture = new BlockTextureOverlay(ConfigHelper.textureName, "overLayTexture", true);
        else
            BlockManager.overLayTexture = new BlockTextureOverlay("modularsystems:custom_overlay", "overLayTexture", true);

        GameRegistry.registerBlock(BlockManager.furnaceCore, "blockFurnaceCore");
        GameRegistry.registerBlock(BlockManager.furnaceCoreActive, "blockFurnaceCoreActive");
        GameRegistry.registerBlock(BlockManager.furnaceDummy, "blockFurnaceDummy");
        GameRegistry.registerBlock(BlockManager.furnaceCraftingUpgrade, "blockFurnaceCraftingUpgrade");
        GameRegistry.registerBlock(BlockManager.furnaceDummyIO, "blockFurnaceDummyIO");
        GameRegistry.registerBlock(BlockManager.furnaceAddition, "blockFurnaceAddition");
        GameRegistry.registerBlock(BlockManager.overLayTexture, "overLayTexture");

        CraftingManager.getInstance().addRecipe(new ItemStack(BlockManager.furnaceCore, 1),
                "XXX",
                "XxX",
                "XXX", 'X', Items.iron_ingot, 'x', Blocks.furnace);

        CraftingManager.getInstance().addRecipe(new ItemStack(BlockManager.furnaceDummyIO, 1),
                "XXX",
                "XxX",
                "XXX", 'X', Blocks.cobblestone, 'x', Blocks.dispenser);

        CraftingManager.getInstance().addRecipe(new ItemStack(BlockManager.furnaceAddition, 1),
                "XXX",
                "XxX",
                "XXX", 'X', Items.iron_ingot, 'x', BlockManager.furnaceCore);

        CraftingManager.getInstance().addRecipe(new ItemStack(BlockManager.furnaceCraftingUpgrade, 1),
                "XxX",
                "xCx",
                "XxX", 'X', Items.iron_ingot, 'x', new ItemStack(Items.dye, 9, 4), 'C', Blocks.crafting_table);
    }

    public static void enableStorageModule()
    {
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
    }

    public static void enableEnchantingModule()
    {
        BlockManager.enchantmentAlter = new BlockEnchantmentAlter();

        GameRegistry.registerBlock(BlockManager.enchantmentAlter, "blockEnchantmentAlter");

        CraftingManager.getInstance().addRecipe(new ItemStack(BlockManager.enchantmentAlter, 1),
                "   ",
                " x ",
                "XeX", 'X', Blocks.emerald_block, 'x', Items.book, 'e', Blocks.enchanting_table);
    }

    public static void enableOreProcessingModule()
    {
        OreDictionaryHelper.initDustsAsNeeded();
        OreProcessingRecipies.addOreProcessingRecipe(Blocks.cobblestone, Blocks.sand);

        BlockManager.smelteryCore = new BlockSmelteryCore(false).setBlockName("modularsystems:blockSmelteryCore").setCreativeTab(ModularSystems.tabModularSystems);
        BlockManager.smelteryCoreActive = new BlockSmelteryCore(true).setBlockName("modularsystems:blockSmelteryCoreActive").setLightLevel(1.0F);
        BlockManager.smelteryDummy = new BlockSmelteryDummy();
        BlockManager.smelteryDummyIO = new BlockSmelteryDummyIO();
        BlockManager.smeleryOverlay = new BlockSmelteryOverlay();

        GameRegistry.registerBlock(BlockManager.smelteryCore, "smelteryCore");
        GameRegistry.registerBlock(BlockManager.smelteryCoreActive, "smelteryCoreActive");
        GameRegistry.registerBlock(BlockManager.smelteryDummy, "smelteryDummy");
        GameRegistry.registerBlock(BlockManager.smelteryDummyIO, "smelteryDummyIO");
        GameRegistry.registerBlock(BlockManager.smeleryOverlay, "smelteryOverlay");

        CraftingManager.getInstance().addRecipe(new ItemStack(BlockManager.smelteryCore, 1),
                "XXX",
                "XxX",
                "XXX", 'X', Items.flint, 'x', Blocks.furnace);

        CraftingManager.getInstance().addRecipe(new ItemStack(BlockManager.smelteryDummyIO, 1),
                "XXX",
                "XxX",
                "XXX", 'X', Items.flint, 'x', Blocks.dispenser);
    }
}
