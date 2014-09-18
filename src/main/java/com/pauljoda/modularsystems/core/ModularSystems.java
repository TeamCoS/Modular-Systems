package com.pauljoda.modularsystems.core;

import java.io.File;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

import com.pauljoda.modularsystems.core.lib.Reference;
import com.pauljoda.modularsystems.core.managers.BlockManager;
import com.pauljoda.modularsystems.core.proxy.ClientProxy;
import com.pauljoda.modularsystems.core.proxy.CommonProxy;
import com.pauljoda.modularsystems.core.util.GeneralSettings;
import com.pauljoda.modularsystems.core.util.VersionChecking;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(name = Reference.MOD_NAME, modid = Reference.MOD_ID, version = Reference.VERSION)

public class ModularSystems {

	@Instance(Reference.MOD_ID)
	public static ModularSystems instance;

	@SidedProxy( clientSide="com.pauljoda.modularsystems.core.proxy.ClientProxy", serverSide="com.pauljoda.modularsystems.core.proxy.CommonProxy")
	public static CommonProxy proxy;

	//Creates the Creative Tab
	public static CreativeTabs tabModularSystems = new CreativeTabs("tabModularSystems"){

		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return Item.getItemFromBlock(BlockManager.furnaceCore);
		}
	};

	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		
		//Set up config
		GeneralSettings.init(new File(event.getModConfigurationDirectory().getAbsolutePath() + File.separator + Reference.CHANNEL_NAME.toLowerCase() + File.separator + "ModularSystems.cfg"));
		FMLCommonHandler.instance().bus().register(new GeneralSettings());
		
		//Version Check
		VersionChecking.execute();
		
		BlockManager.registerBlocks();
		BlockManager.register();
		BlockManager.registerCraftingRecipes();
		
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		
		//Register Tiles
		proxy.registerTileEntities();
		
		//Set Up Custom Renderers
		ClientProxy.setCustomRenderers();
		
		//Setup GUIs
		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy); 
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {}
}
