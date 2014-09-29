package com.pauljoda.modularsystems.core.managers;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

import com.pauljoda.modularsystems.core.ModularSystems;
import com.pauljoda.modularsystems.core.helper.ConfigHelper;
import com.pauljoda.modularsystems.enchanting.blocks.BlockEnchantmentAlter;
import com.pauljoda.modularsystems.furnace.blocks.BlockCrafter;
import com.pauljoda.modularsystems.furnace.blocks.BlockFurnaceAddition;
import com.pauljoda.modularsystems.furnace.blocks.BlockFurnaceAddition;
import com.pauljoda.modularsystems.furnace.blocks.BlockFurnaceCore;
import com.pauljoda.modularsystems.furnace.blocks.BlockFurnaceDummy;
import com.pauljoda.modularsystems.furnace.blocks.BlockFurnaceDummyIO;
import com.pauljoda.modularsystems.furnace.blocks.BlockFurnaceDummyIO;
import com.pauljoda.modularsystems.furnace.blocks.BlockTextureOverlay;
import com.pauljoda.modularsystems.storage.blocks.BlockArmorExpansion;
import com.pauljoda.modularsystems.storage.blocks.BlockBasicExpansion;
import com.pauljoda.modularsystems.storage.blocks.BlockHoppingExpansion;
import com.pauljoda.modularsystems.storage.blocks.BlockSmashingExpansion;
import com.pauljoda.modularsystems.storage.blocks.BlockSortingExpansion;
import com.pauljoda.modularsystems.storage.blocks.BlockStorageCore;
import com.pauljoda.modularsystems.storage.blocks.BlockCapacityExpansion;

import cpw.mods.fml.common.registry.GameRegistry;

public class BlockManager {

	//Furnace
	public static Block furnaceCore;
	public static Block furnaceCoreActive;
	public static Block furnaceDummy;
	public static Block furnaceCraftingUpgrade;
	public static Block furnaceDummyIO;
	public static Block furnaceAddition;
	public static Block overLayTexture;
	
	//Storage
	public static Block storageCore;
	public static Block basicExpansion;
	public static Block storageExpansion;
	public static Block storageHoppingExpansion;
	public static Block storageArmorExpansion;
	public static Block storageSmashingExpansion;
	public static Block storageSortingExpansion;
	
	//Enchatment
	public static Block enchantmentAlter;

	public static void createBlocks()
	{
		//Furnace
		furnaceCore = new BlockFurnaceCore(false).setBlockName("modularsystems:blockFurnaceCore").setCreativeTab(ModularSystems.tabModularSystems);
		furnaceCoreActive = new BlockFurnaceCore(true).setLightLevel(1F).setBlockName("modularsystems:blockFurnaceCoreActive");
		furnaceDummy = new BlockFurnaceDummy();
		furnaceCraftingUpgrade = new BlockCrafter();
		furnaceDummyIO = new BlockFurnaceDummyIO();
		furnaceAddition = new BlockFurnaceAddition();
		if(ConfigHelper.useTextures)
			overLayTexture = new BlockTextureOverlay(ConfigHelper.textureName, "overLayTexture", true);
		else
			overLayTexture = new BlockTextureOverlay("modularsystems:custom_overlay", "overLayTexture", true);
	
		//Storage
		storageCore = new BlockStorageCore();
		basicExpansion = new BlockBasicExpansion();
		storageExpansion = new BlockCapacityExpansion();
		storageHoppingExpansion = new BlockHoppingExpansion();
		storageArmorExpansion = new BlockArmorExpansion();
		storageSmashingExpansion = new BlockSmashingExpansion();
		storageSortingExpansion = new BlockSortingExpansion();
		
		//Enchantment
		enchantmentAlter = new BlockEnchantmentAlter();
	}
	
	public static void registerBlocks()
	{
		//Furnace
		GameRegistry.registerBlock(furnaceCore, "blockFurnaceCore");
		GameRegistry.registerBlock(furnaceCoreActive, "blockFurnaceCoreActive");
		GameRegistry.registerBlock(furnaceDummy, "blockFurnaceDummy");
		GameRegistry.registerBlock(furnaceCraftingUpgrade, "blockFurnaceCraftingUpgrade");
		GameRegistry.registerBlock(furnaceDummyIO, "blockFurnaceDummyIO");
		GameRegistry.registerBlock(furnaceAddition, "blockFurnaceAddition");
		GameRegistry.registerBlock(overLayTexture, "overLayTexture");
	
		//Storage
		GameRegistry.registerBlock(storageCore, "blockStorageCore");
		GameRegistry.registerBlock(basicExpansion, "blockBasicExpansion");
		GameRegistry.registerBlock(storageExpansion, "blockStorageExpansion");
		GameRegistry.registerBlock(storageHoppingExpansion, "blockHoppingStorageExpansion");
		GameRegistry.registerBlock(storageArmorExpansion, "blockArmorStorageExpansion");
		GameRegistry.registerBlock(storageSmashingExpansion, "blockSmashingStorageExpansion");
		GameRegistry.registerBlock(storageSortingExpansion, "blockSortingStorageExpansion");
		
		//Enchantment
		GameRegistry.registerBlock(enchantmentAlter, "blockEnchantmentAlter");
	}
	
	public static void registerBlockCrafting()
	{
		//Furnace
		CraftingManager.getInstance().addRecipe(new ItemStack(furnaceCore, 1),
				"XXX",
				"XxX",
				"XXX", 'X', Items.iron_ingot, 'x', Blocks.furnace);

		CraftingManager.getInstance().addRecipe(new ItemStack(furnaceDummyIO, 1),
				"XXX",
				"XxX",
				"XXX", 'X', Blocks.cobblestone, 'x', Blocks.dispenser);
		
		CraftingManager.getInstance().addRecipe(new ItemStack(furnaceAddition, 1),
				"XXX",
				"XxX",
				"XXX", 'X', Items.iron_ingot, 'x', furnaceCore);

		CraftingManager.getInstance().addRecipe(new ItemStack(furnaceCraftingUpgrade, 1),
				"XxX",
				"xCx",
				"XxX", 'X', Items.iron_ingot, 'x', new ItemStack(Items.dye, 9, 4), 'C', Blocks.crafting_table); 
		
		//Storage
		CraftingManager.getInstance().addRecipe(new ItemStack(storageCore, 1),
				"XxX",
				"xCx",
				"XxX", 'X', Blocks.log, 'x', Blocks.planks, 'C', Blocks.chest);
		
		CraftingManager.getInstance().addRecipe(new ItemStack(basicExpansion, 1),
				"XXX",
				"XxX",
				"XXX", 'X', Blocks.planks, 'x', Items.string);
		
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(storageExpansion, 1), basicExpansion, Blocks.chest);
		
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(storageHoppingExpansion, 1), basicExpansion, Items.ender_pearl, Blocks.hopper);
		
		CraftingManager.getInstance().addRecipe(new ItemStack(storageArmorExpansion, 1),
				" X ",
				"XsX",
				" X ", 'X', Items.leather, 's', basicExpansion);
		
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(storageSmashingExpansion, 1), Items.diamond_pickaxe, basicExpansion);
		
		CraftingManager.getInstance().addRecipe(new ItemStack(storageSortingExpansion, 1),
				"XXX",
				"XxX",
				"XXX", 'X', Items.book, 'x', basicExpansion);
		
		//Enchanting
		CraftingManager.getInstance().addRecipe(new ItemStack(enchantmentAlter, 1),
				"   ",
				" x ",
				"XeX", 'X', Blocks.emerald_block, 'x', Items.book, 'e', Blocks.enchanting_table);
	}
}
