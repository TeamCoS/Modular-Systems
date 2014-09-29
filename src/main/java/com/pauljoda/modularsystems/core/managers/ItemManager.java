package com.pauljoda.modularsystems.core.managers;

import com.pauljoda.modularsystems.enchanting.items.ItemEnchantingUpgrade;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class ItemManager {

	//Enchanting Upgrades
	public static Item enchantmentUpgradeBasic;
	public static Item enchantmentUpgradeProtection;
	public static Item enchantmentUpgradeFireProtection;
	public static Item enchantmentUpgradeFeatherFalling;
	public static Item enchantmentUpgradeBlastResistance;
	public static Item enchantmentUpgradeProjectileProtection;
	public static Item enchantmentUpgradeRespiration;
	public static Item enchantmentUpgradeAquaAffinity;
	public static Item enchantmentUpgradeThorns;
	public static Item enchantmentUpgradeSharpness;
	public static Item enchantmentUpgradeSmite;
	public static Item enchantmentUpgradeSpiders;
	public static Item enchantmentUpgradeKnockback;
	public static Item enchantmentUpgradeFireAspect;
	public static Item enchantmentUpgradeLooting;
	public static Item enchantmentUpgradeEfficiency;
	public static Item enchantmentUpgradeSilkTouch;
	public static Item enchantmentUpgradeUnbreaking;
	public static Item enchantmentUpgradeFortune;
	public static Item enchantmentUpgradePower;
	public static Item enchantmentUpgradePunch;
	public static Item enchantmentUpgradeFlame;
	public static Item enchantmentUpgradeInfinity;
	public static Item enchantmentUpgradeLuckOfTheSea;
	public static Item enchantmentUpgradeLure;

	public static void createItems()
	{
		//Enchanting Upgrades
		enchantmentUpgradeBasic = new ItemEnchantingUpgrade("Basic", null);
		enchantmentUpgradeProtection = new ItemEnchantingUpgrade("Protection", Enchantment.protection);
		enchantmentUpgradeFireProtection = new ItemEnchantingUpgrade("FireProtection", Enchantment.fireProtection);
		enchantmentUpgradeFeatherFalling = new ItemEnchantingUpgrade("FeatherFalling", Enchantment.featherFalling);
		enchantmentUpgradeBlastResistance = new ItemEnchantingUpgrade("BlastResistance", Enchantment.blastProtection);
		enchantmentUpgradeProjectileProtection = new ItemEnchantingUpgrade("ProjectileProtection", Enchantment.projectileProtection);
		enchantmentUpgradeRespiration = new ItemEnchantingUpgrade("Respiration", Enchantment.respiration);
		enchantmentUpgradeAquaAffinity = new ItemEnchantingUpgrade("AquaAffinity", Enchantment.aquaAffinity);
		enchantmentUpgradeThorns = new ItemEnchantingUpgrade("Thorns", Enchantment.thorns);
		enchantmentUpgradeSharpness = new ItemEnchantingUpgrade("Sharpness", Enchantment.sharpness);
		enchantmentUpgradeSmite = new ItemEnchantingUpgrade("Smite", Enchantment.smite);
		enchantmentUpgradeSpiders = new ItemEnchantingUpgrade("Spiders", Enchantment.baneOfArthropods);
		enchantmentUpgradeKnockback = new ItemEnchantingUpgrade("Knockback", Enchantment.knockback);
		enchantmentUpgradeFireAspect = new ItemEnchantingUpgrade("FireAspect", Enchantment.fireAspect);
		enchantmentUpgradeLooting = new ItemEnchantingUpgrade("Looting", Enchantment.looting);
		enchantmentUpgradeEfficiency = new ItemEnchantingUpgrade("Efficiency", Enchantment.efficiency);
		enchantmentUpgradeSilkTouch = new ItemEnchantingUpgrade("SilkTouch", Enchantment.silkTouch);
		enchantmentUpgradeUnbreaking = new ItemEnchantingUpgrade("Unbreaking", Enchantment.unbreaking);
		enchantmentUpgradeFortune = new ItemEnchantingUpgrade("Fortune", Enchantment.fortune);
		enchantmentUpgradePower = new ItemEnchantingUpgrade("Power", Enchantment.power);
		enchantmentUpgradePunch = new ItemEnchantingUpgrade("Punch", Enchantment.punch);
		enchantmentUpgradeFlame = new ItemEnchantingUpgrade("Flame", Enchantment.flame);
		enchantmentUpgradeInfinity = new ItemEnchantingUpgrade("Infinity", Enchantment.infinity);
		enchantmentUpgradeLuckOfTheSea = new ItemEnchantingUpgrade("LuckOfTheSea", Enchantment.field_151370_z);
		enchantmentUpgradeLure = new ItemEnchantingUpgrade("Lure", Enchantment.field_151369_A);
	}

	public static void registerItems()
	{
		//Enchanting Upgrades
		GameRegistry.registerItem(enchantmentUpgradeBasic, "enchantmentUpgradeBasic");
		GameRegistry.registerItem(enchantmentUpgradeProtection, "enchantmentUpgradeProtection");
		GameRegistry.registerItem(enchantmentUpgradeFireProtection, "enchantmentUpgradeFireProtection");
		GameRegistry.registerItem(enchantmentUpgradeFeatherFalling, "enchantmentUpgradeFeatherFalling");
		GameRegistry.registerItem(enchantmentUpgradeBlastResistance, "enchantmentUpgradeBlastResistance");
		GameRegistry.registerItem(enchantmentUpgradeProjectileProtection, "enchantmentUpgradeProjectileProtection");
		GameRegistry.registerItem(enchantmentUpgradeRespiration, "enchantmentUpgradeRespiration");
		GameRegistry.registerItem(enchantmentUpgradeAquaAffinity, "enchantmentUpgradeAquaAffinity");
		GameRegistry.registerItem(enchantmentUpgradeThorns, "enchantmentUpgradeThorns");
		GameRegistry.registerItem(enchantmentUpgradeSharpness, "enchantmentUpgradeSharpness");
		GameRegistry.registerItem(enchantmentUpgradeSmite, "enchantmentUpgradeSmite");
		GameRegistry.registerItem(enchantmentUpgradeSpiders, "enchantmentUpgradeSpiders");
		GameRegistry.registerItem(enchantmentUpgradeKnockback, "enchantmentUpgradeKnockback");
		GameRegistry.registerItem(enchantmentUpgradeFireAspect, "enchantmentUpgradeFireAspect");
		GameRegistry.registerItem(enchantmentUpgradeLooting, "enchantmentUpgradeLooting");
		GameRegistry.registerItem(enchantmentUpgradeEfficiency, "enchantmentUpgradeEfficiency");
		GameRegistry.registerItem(enchantmentUpgradeSilkTouch, "enchantmentUpgradeSilkTouch");
		GameRegistry.registerItem(enchantmentUpgradeUnbreaking, "enchantmentUpgradeUnbreaking");
		GameRegistry.registerItem(enchantmentUpgradeFortune, "enchantmentUpgradeFortune");
		GameRegistry.registerItem(enchantmentUpgradePower, "enchantmentUpgradePower");
		GameRegistry.registerItem(enchantmentUpgradePunch, "enchantmentUpgradePunch");
		GameRegistry.registerItem(enchantmentUpgradeFlame, "enchantmentUpgradeFlame");
		GameRegistry.registerItem(enchantmentUpgradeInfinity, "enchantmentUpgradeInfinity");
		GameRegistry.registerItem(enchantmentUpgradeLuckOfTheSea, "enchantmentUpgradeLuckOfTheSea");
		GameRegistry.registerItem(enchantmentUpgradeLure, "enchantmentUpgradeLure");
	}

	public static void registerItemCrafting()
	{
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(enchantmentUpgradeBasic, 1), Items.iron_ingot, Items.book, Items.paper);
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(enchantmentUpgradeProtection, 1), Items.iron_chestplate, enchantmentUpgradeBasic, Blocks.obsidian);
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(enchantmentUpgradeFireProtection, 1), Items.iron_chestplate, enchantmentUpgradeBasic, Items.fire_charge);
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(enchantmentUpgradeFeatherFalling, 1), Items.iron_boots, enchantmentUpgradeBasic, Items.feather);
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(enchantmentUpgradeBlastResistance, 1), Items.iron_chestplate, enchantmentUpgradeBasic, Blocks.tnt);
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(enchantmentUpgradeProjectileProtection, 1), Items.iron_chestplate, enchantmentUpgradeBasic, Items.arrow);
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(enchantmentUpgradeRespiration, 1), Items.iron_helmet, enchantmentUpgradeBasic, Items.bucket);
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(enchantmentUpgradeAquaAffinity, 1), Items.iron_helmet, enchantmentUpgradeBasic, Items.water_bucket);
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(enchantmentUpgradeThorns, 1), Items.iron_chestplate, enchantmentUpgradeBasic, Blocks.cactus);
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(enchantmentUpgradeSharpness, 1), Items.iron_sword, enchantmentUpgradeBasic, Items.flint);
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(enchantmentUpgradeSmite, 1), Items.iron_sword, enchantmentUpgradeBasic, Items.rotten_flesh);
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(enchantmentUpgradeSpiders, 1), Items.iron_sword, enchantmentUpgradeBasic, Items.spider_eye);
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(enchantmentUpgradeKnockback, 1), Items.iron_sword, enchantmentUpgradeBasic, Blocks.piston);
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(enchantmentUpgradeFireAspect, 1), Items.iron_sword, enchantmentUpgradeBasic, Items.flint_and_steel);
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(enchantmentUpgradeLooting, 1), Items.iron_sword, enchantmentUpgradeBasic, Items.emerald);
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(enchantmentUpgradeEfficiency, 1), Items.iron_pickaxe, enchantmentUpgradeBasic, Items.sugar);
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(enchantmentUpgradeSilkTouch, 1), Items.iron_pickaxe, enchantmentUpgradeBasic, Items.string);
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(enchantmentUpgradeUnbreaking, 1), Items.iron_pickaxe, enchantmentUpgradeBasic, Blocks.obsidian);
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(enchantmentUpgradeFortune, 1), Items.iron_pickaxe, enchantmentUpgradeBasic, Items.diamond);
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(enchantmentUpgradePower, 1), Items.bow, enchantmentUpgradeBasic, Items.redstone);
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(enchantmentUpgradePunch, 1), Items.bow, enchantmentUpgradeBasic, Blocks.piston);
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(enchantmentUpgradeFlame, 1), Items.bow, enchantmentUpgradeBasic, Items.flint_and_steel);
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(enchantmentUpgradeInfinity, 1), Items.bow, enchantmentUpgradeBasic, Items.arrow);
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(enchantmentUpgradeLuckOfTheSea, 1), Items.fishing_rod, enchantmentUpgradeBasic, Items.leather_boots);
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(enchantmentUpgradeLure, 1), Items.fishing_rod, enchantmentUpgradeBasic, Items.fish);
	}
}
