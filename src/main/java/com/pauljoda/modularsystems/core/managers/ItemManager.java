package com.pauljoda.modularsystems.core.managers;

import com.pauljoda.modularsystems.enchanting.items.ItemEnchantingUpgrade;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;

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
		
	}
}
