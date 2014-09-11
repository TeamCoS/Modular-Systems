package com.pauljoda.modularsystems.core.util;

import java.io.File;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import com.pauljoda.modularsystems.core.lib.Reference;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.Configuration;

public class GeneralSettings {

	public static Configuration config;

	//Banned Blocks
	public static String[] bannedBlocks;

	//checks to see if we are using resource pack or not
	public static boolean useTextures;
	public static String textureName;
	public static boolean useOverlay;
	
	//Storage
	public static int maxExpansionSize;
	
	public static final String DISPLAY_VERSION_RESULT_CONFIGNAME = "version_check.display_results";
	public static final boolean DISPLAY_VERSION_RESULT_DEFAULT = true;

	public static String LAST_DISCOVERED_VERSION;
	public static final String LAST_DISCOVERED_VERSION_CONFIGNAME = "version_check.last_discovered_version";
	public static final String LAST_DISCOVERED_VERSION_DEFAULT = "";

	public static String LAST_DISCOVERED_VERSION_TYPE;
	public static final String LAST_DISCOVERED_VERSION_TYPE_CONFIGNAME = "version_check.last_discovered_version_type";
	public static final String LAST_DISCOVERED_VERSION_TYPE_DEFAULT = "";
	public static final int VERSION_CHECK_ATTEMPTS = 3;

	public static boolean DISPLAY_VERSION_RESULT;

	public static void init(File configFile) {
		config = new Configuration(configFile);
		config.load();
		syncConfig();
	}

	public static void syncConfig() {

		try {
			/* Version check */
			DISPLAY_VERSION_RESULT = config.get(Configuration.CATEGORY_GENERAL, DISPLAY_VERSION_RESULT_CONFIGNAME, DISPLAY_VERSION_RESULT_DEFAULT).getBoolean(DISPLAY_VERSION_RESULT_DEFAULT);
			LAST_DISCOVERED_VERSION = config.get(Configuration.CATEGORY_GENERAL, LAST_DISCOVERED_VERSION_CONFIGNAME, LAST_DISCOVERED_VERSION_DEFAULT).getString();
			LAST_DISCOVERED_VERSION_TYPE = config.get(Configuration.CATEGORY_GENERAL, LAST_DISCOVERED_VERSION_TYPE_CONFIGNAME, LAST_DISCOVERED_VERSION_TYPE_DEFAULT).getString();

			bannedBlocks = config.get("Settings, Modular Furnace", "Banned Block Unlocalized Names",
					new String[] 	{Blocks.log.getUnlocalizedName(), Blocks.planks.getUnlocalizedName(),
					Blocks.dirt.getUnlocalizedName(), Blocks.ice.getUnlocalizedName(),
					Blocks.snow.getUnlocalizedName(), Blocks.bookshelf.getUnlocalizedName(),
					Blocks.leaves.getUnlocalizedName(), Blocks.melon_block.getUnlocalizedName(),
					Blocks.pumpkin.getUnlocalizedName(), Blocks.tnt.getUnlocalizedName(),
					Blocks.wool.getUnlocalizedName(), Blocks.hay_block.getUnlocalizedName(),
					Blocks.grass.getUnlocalizedName(), Blocks.bedrock.getUnlocalizedName(),
					Blocks.diamond_ore.getUnlocalizedName(), Blocks.iron_ore.getUnlocalizedName(),
					Blocks.emerald_ore.getUnlocalizedName(), Blocks.gold_ore.getUnlocalizedName(),}).getStringList();

			useTextures = config.get("Settings, Modular Furnace", "Use Vanilla Texture For Overlay?", false).getBoolean(true);
			textureName = config.get("Settings, Modular Furnace", "Overlay Texture Name (from assets folder)", "hopper_top").getString();
			useTextures = config.get("Settings, Modular Furnace", "Use Overlay?", false).getBoolean(true);

			maxExpansionSize = config.getInt("Settings, Modular Storage", "Max row expansion", 40, 7, 100, "This will manage the size of the inventory allocation");
		}
		catch (Exception e) {
			FMLLog.log(Level.FATAL, e, Reference.MOD_NAME + " has had a problem loading its general configuration");
		}
		finally {
			config.save();
		}
	}

		@SubscribeEvent
		public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
			if (eventArgs.modID.equalsIgnoreCase(Reference.MOD_ID))
			{
				System.out.println("Config Changed");
				syncConfig();
			}
		}

		public static void set(String categoryName, String propertyName, String newValue) {

			config.load();
			if (config.getCategoryNames().contains(categoryName)) {
				if (config.getCategory(categoryName).containsKey(propertyName)) {
					config.getCategory(categoryName).get(propertyName).set(newValue);
				}
			}
			config.save();
		}

	}