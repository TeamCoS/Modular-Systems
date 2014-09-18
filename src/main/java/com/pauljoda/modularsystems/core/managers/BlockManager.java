package com.pauljoda.modularsystems.core.managers;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

import com.pauljoda.modularsystems.core.ModularSystems;
import com.pauljoda.modularsystems.core.util.GeneralSettings;
import com.pauljoda.modularsystems.furnace.blocks.BlockCrafterActive;
import com.pauljoda.modularsystems.furnace.blocks.BlockCrafterInactive;
import com.pauljoda.modularsystems.furnace.blocks.BlockFurnaceAddition;
import com.pauljoda.modularsystems.furnace.blocks.BlockFurnaceAdditionActive;
import com.pauljoda.modularsystems.furnace.blocks.BlockFurnaceCore;
import com.pauljoda.modularsystems.furnace.blocks.BlockFurnaceDummy;
import com.pauljoda.modularsystems.furnace.blocks.BlockFurnaceDummyIO;
import com.pauljoda.modularsystems.furnace.blocks.BlockFurnaceDummyIOActive;
import com.pauljoda.modularsystems.furnace.blocks.BlockTextureOverlay;
import com.pauljoda.modularsystems.storage.blocks.BlockArmorStorageExpansion;
import com.pauljoda.modularsystems.storage.blocks.BlockHoppingStorageExpansion;
import com.pauljoda.modularsystems.storage.blocks.BlockSmashingStorageExpansion;
import com.pauljoda.modularsystems.storage.blocks.BlockStorageCore;
import com.pauljoda.modularsystems.storage.blocks.BlockStorageExpansion;

import cpw.mods.fml.common.registry.GameRegistry;

public class BlockManager {

	//Furnace
	public static Block furnaceCore;
	public static Block furnaceCoreActive;
	public static Block furnaceDummy;
	public static Block furnaceCraftingUpgradeInactive;
	public static Block furnaceCraftingUpgradeActive;
	public static Block furnaceDummyIO;
	public static Block furnaceDummyActiveIO;
	public static Block furnaceAddition;
	public static Block furnaceAdditionActive;
	public static Block overLayTexture;
	
	//Storage
	public static Block storageCore;
	public static Block storageExpansion;
	public static Block storageHoppingExpansion;
	public static Block storageArmorExpansion;
	public static Block storageSmashingExpansion;

	public static void registerBlocks()
	{
		//Furnace
		furnaceCore = new BlockFurnaceCore(false).setBlockName("modularsystems:blockFurnaceCore").setCreativeTab(ModularSystems.tabModularSystems);
		furnaceCoreActive = new BlockFurnaceCore(true).setLightLevel(1F).setBlockName("modularsystems:blockFurnaceCoreActive");
		furnaceDummy = new BlockFurnaceDummy();
		furnaceCraftingUpgradeInactive = new BlockCrafterInactive(Material.wood);
		furnaceCraftingUpgradeActive = new BlockCrafterActive();
		furnaceDummyIO = new BlockFurnaceDummyIO(Material.rock);
		furnaceDummyActiveIO = new BlockFurnaceDummyIOActive();
		furnaceAddition = new BlockFurnaceAddition();
		furnaceAdditionActive = new BlockFurnaceAdditionActive();
		if(GeneralSettings.useTextures)
			overLayTexture = new BlockTextureOverlay(GeneralSettings.textureName, "overLayTexture", true);
		else
			overLayTexture = new BlockTextureOverlay("modularsystems:custom_overlay", "overLayTexture", true);
	
		//Storage
		storageCore = new BlockStorageCore();
		storageExpansion = new BlockStorageExpansion();
		storageHoppingExpansion = new BlockHoppingStorageExpansion();
		storageArmorExpansion = new BlockArmorStorageExpansion();
		storageSmashingExpansion = new BlockSmashingStorageExpansion();
	}
	
	public static void register()
	{
		//Furnace
		GameRegistry.registerBlock(furnaceCore, "blockFurnaceCore");
		GameRegistry.registerBlock(furnaceCoreActive, "blockFurnaceCoreActive");
		GameRegistry.registerBlock(furnaceDummy, "blockFurnaceDummy");
		GameRegistry.registerBlock(furnaceCraftingUpgradeInactive, "blockFurnaceCraftingUpgradeInactive");
		GameRegistry.registerBlock(furnaceCraftingUpgradeActive, "blockFurnaceCraftingUpgradeActive");
		GameRegistry.registerBlock(furnaceDummyIO, "blockFurnaceDummyIO");
		GameRegistry.registerBlock(furnaceDummyActiveIO, "blockFurnaceDummyActiveIO");
		GameRegistry.registerBlock(furnaceAddition, "blockFurnaceAddition");
		GameRegistry.registerBlock(furnaceAdditionActive, "blockFurnaceAdditionActive");
		GameRegistry.registerBlock(overLayTexture, "overLayTexture");
	
		//Storage
		GameRegistry.registerBlock(storageCore, "blockStorageCore");
		GameRegistry.registerBlock(storageExpansion, "blockStorageExpansion");
		GameRegistry.registerBlock(storageHoppingExpansion, "blockHoppingStorageExpansion");
		GameRegistry.registerBlock(storageArmorExpansion, "blockArmorStorageExpansion");
		GameRegistry.registerBlock(storageSmashingExpansion, "blockSmashingStorageExpansion");
	}
	
	public static void registerCraftingRecipes()
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

		CraftingManager.getInstance().addRecipe(new ItemStack(furnaceCraftingUpgradeInactive, 1),
				"XxX",
				"xCx",
				"XxX", 'X', Items.iron_ingot, 'x', new ItemStack(Items.dye, 9, 4), 'C', Blocks.crafting_table); 
		
		CraftingManager.getInstance().addRecipe(new ItemStack(storageCore, 1),
				"XxX",
				"xCx",
				"XxX", 'X', Blocks.log, 'x', Blocks.planks, 'C', Blocks.chest);
		
		CraftingManager.getInstance().addRecipe(new ItemStack(storageExpansion, 1),
				"XXX",
				"XxX",
				"XXX", 'X', Items.stick, 'x', Blocks.chest);
		
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(storageHoppingExpansion, 1), storageExpansion, Items.ender_pearl, Blocks.hopper);
		
		CraftingManager.getInstance().addRecipe(new ItemStack(storageArmorExpansion, 1),
				" X ",
				"XsX",
				" X ", 'X', Items.leather, 's', storageExpansion);
	}
}
