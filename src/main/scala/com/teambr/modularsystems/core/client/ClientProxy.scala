package com.teambr.modularsystems.core.client

import com.teambr.bookshelf.common.blocks.properties.PropertyRotation
import com.teambr.modularsystems.core.client.modelfactory.ModelFactory
import com.teambr.modularsystems.core.common.CommonProxy
import com.teambr.modularsystems.core.common.blocks.traits.CoreStates
import com.teambr.modularsystems.core.managers.{ BlockManager, ItemRenderManager }
import net.minecraft.client.renderer.block.statemap.StateMap.Builder
import net.minecraftforge.client.model.ModelLoader

/**
 * This file was created for the Modular-Systems
 *
 * Modular-Systems if licensed under the is licensed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis <pauljoda>
 * @since August 02, 2015
 */
class ClientProxy extends CommonProxy {

    override def preInit() = {
        ModelLoader.setCustomStateMapper(BlockManager.furnaceCore,
            (new Builder).addPropertiesToIgnore(PropertyRotation.FOUR_WAY.getProperty).addPropertiesToIgnore(BlockManager.furnaceCore.asInstanceOf[CoreStates].PROPERTY_ACTIVE).build())
    }

    override def init() = {

        ModelFactory.register()

        //Register Inventory Renderer for Items
        ItemRenderManager.registerItemRenderer()
    }
}
