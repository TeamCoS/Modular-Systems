package com.teambr.modularsystems.core

import com.teambr.modularsystems.core.client.itemtooltip.ToolTipEvent
import com.teambr.modularsystems.core.common.CommonProxy
import com.teambr.modularsystems.core.lib.Reference
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.{FMLPostInitializationEvent, FMLInitializationEvent, FMLPreInitializationEvent}
import net.minecraftforge.fml.common.{SidedProxy, Mod}
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
@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, modLanguage = "scala")
object ModularSystems {
    //The logger. For logging
    final val logger = LogManager.getLogger(Reference.MOD_NAME)

    @SidedProxy(clientSide = "com.teambr.modularsystems.core.client.ClientProxy",
                serverSide = "com.teambr.modularsystems.core.common.CommonProxy")
    var proxy : CommonProxy = null

    var tabModularSystems : CreativeTabs = new CreativeTabs("tabModularSystems") {
        @SideOnly(Side.CLIENT)
        override def getTabIconItem: Item = Item.getItemFromBlock(Blocks.furnace)
    }

    @EventHandler def preInit(event : FMLPreInitializationEvent) = {
        proxy.preInit()

        MinecraftForge.EVENT_BUS.register(new ToolTipEvent)
    }

    @EventHandler def init(event : FMLInitializationEvent) =  {
        proxy.init()
    }

    @EventHandler def postInit(event : FMLPostInitializationEvent) = {
        proxy.postInit()
    }
}
