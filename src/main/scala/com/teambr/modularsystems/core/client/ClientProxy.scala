package com.teambr.modularsystems.core.client

import com.teambr.bookshelf.common.blocks.properties.PropertyRotation
import com.teambr.modularsystems.core.client.modelfactory.ModelFactory
import com.teambr.modularsystems.core.client.renderer.TileSpecialDummyRenderer
import com.teambr.modularsystems.core.common.CommonProxy
import com.teambr.modularsystems.core.common.blocks.traits.CoreStates
import com.teambr.modularsystems.core.managers.{ BlockManager, ItemRenderManager }
import com.teambr.modularsystems.power.tiles.TileBankBase
import net.minecraft.client.renderer.block.statemap.StateMap.Builder
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.client.registry.ClientRegistry

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
            (new Builder).ignore(PropertyRotation.FOUR_WAY).ignore(BlockManager.furnaceCore.asInstanceOf[CoreStates].PROPERTY_ACTIVE).build())
        ModelLoader.setCustomStateMapper(BlockManager.crusherCore,
            (new Builder).ignore(PropertyRotation.FOUR_WAY).ignore(BlockManager.furnaceCore.asInstanceOf[CoreStates].PROPERTY_ACTIVE).build())
        ModelLoader.setCustomStateMapper(BlockManager.storageSmashing,
            (new Builder).ignore(PropertyRotation.SIX_WAY).build())

        RenderRegistry.doTheThing()
    }

    override def init() = {

        ModelFactory.register()

        //Register Inventory Renderer for Items
        ItemRenderManager.registerItemRenderer()
    }
}
