package com.teambr.modularsystems.core

import java.io.File

import com.teambr.modularsystems.core.achievement.ModAchievements
import com.teambr.modularsystems.core.commands.OpenValueConfig
import com.teambr.modularsystems.core.common.CommonProxy
import com.teambr.modularsystems.core.lib.Reference
import com.teambr.modularsystems.core.managers._
import com.teambr.modularsystems.core.network.PacketManager
import com.teambr.modularsystems.core.registries._
import com.teambr.modularsystems.storage.event.ToolTipEvent
import net.minecraft.command.ServerCommandManager
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent, FMLServerStartingEvent}
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.common.{Mod, SidedProxy}
import net.minecraftforge.fml.relauncher.{Side, SideOnly}
import org.apache.logging.log4j.LogManager

/**
  * This file was created for the Modular-Systems
  * API. The source is available at:
  * https://github.com/TeamCoS/Modular-Systems
  *
  * Bookshelf if licensed under the is licensed under a
  * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
  * http://creativecommons.org/licenses/by-nc-sa/4.0/
  *
  * @author Paul Davis <pauljoda>
  * @since August 02, 2015
  */
@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION,
    dependencies = Reference.DEPENDENCIES, modLanguage = "scala")
object ModularSystems {
    // In Java, allows us to get the instance
    def getInstance = this

    //The logger. For logging
    final val logger = LogManager.getLogger(Reference.MOD_NAME)

    var configFolderLocation : String = ""

    @SidedProxy(clientSide = "com.teambr.modularsystems.core.client.ClientProxy",
        serverSide = "com.teambr.modularsystems.core.common.CommonProxy")
    var proxy : CommonProxy = null

    var tabModularSystems : CreativeTabs = new CreativeTabs("tabModularSystems") {
        @SideOnly(Side.CLIENT)
        override def getTabIconItem: Item = Item.getItemFromBlock(Blocks.FURNACE)
    }

    @EventHandler def preInit(event : FMLPreInitializationEvent) = {
        configFolderLocation = event.getModConfigurationDirectory.getAbsolutePath + File.separator + "Modular-Systems"
        ConfigRegistry.preInit()
        BlockManager.preInit()
        ItemManager.preInit()
        FurnaceBannedBlocks.init()
        BlockValueRegistry.INSTANCE.init()
        FluidFuelValues.init()
        proxy.preInit()
        CraftingManager.init()
        ModAchievements.start()
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiManager)

        MinecraftForge.EVENT_BUS.register(BlockValueRegistry.INSTANCE)
        MinecraftForge.EVENT_BUS.register(EventManager.INSTANCE)
        MinecraftForge.EVENT_BUS.register(new ToolTipEvent)
    }

    @EventHandler def init(event : FMLInitializationEvent) =  {
        ItemManager.init() // Must be before proxy registration
        CrusherRecipeRegistry.init() //Must be after ItemManager.init
        PacketManager.initPackets()
        proxy.init()
        //FMLCommonHandler.instance().bus().register(ModAchievements)
        MinecraftForge.EVENT_BUS.register(ModAchievements)
    }

    @EventHandler def postInit(event : FMLPostInitializationEvent) = {
        proxy.postInit()
    }


    @Mod.EventHandler
    def serverLoad(event : FMLServerStartingEvent) {
        event.getServer.getCommandManager.asInstanceOf[ServerCommandManager].registerCommand(new OpenValueConfig())
    }
}
