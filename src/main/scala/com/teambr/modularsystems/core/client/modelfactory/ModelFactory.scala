package com.teambr.modularsystems.core.client.modelfactory

import com.teambr.modularsystems.core.client.modelfactory.models._
import com.teambr.modularsystems.core.lib.Reference
import com.teambr.modularsystems.core.managers.BlockManager
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.resources.model.ModelResourceLocation
import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.{ TextureStitchEvent, ModelBakeEvent }
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.{ EventPriority, SubscribeEvent }

/**
 * This file was created for Modular-Systems
 *
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis <pauljoda>
 * @since August 06, 2015
 */
object ModelFactory {
    val INSTANCE = new ModelFactory

    def register() : Unit = {
        MinecraftForge.EVENT_BUS.register(INSTANCE)
    }

    // Get the default model resource location for a block state
    // Used to put an entry into the model registry
    def getModelResourceLocation(state: IBlockState): ModelResourceLocation = {
        new ModelResourceLocation(Block.blockRegistry.getNameForObject(state.getBlock).asInstanceOf[ResourceLocation], new DefaultStateMapper().getPropertyString(state.getProperties))
    }

    var STORAGE_CONNECTED_TEXTURE_NO_CONNECTIONS : TextureAtlasSprite = null
    var STORAGE_CONNECTED_TEXTURE_ANTI_CORNERS : TextureAtlasSprite = null
    var STORAGE_CONNECTED_TEXTURE_CORNERS : TextureAtlasSprite = null
    var STORAGE_CONNECTED_TEXTURE_HORIZONTAL : TextureAtlasSprite = null
    var STORAGE_CONNECTED_TEXTURE_VERTICAL : TextureAtlasSprite = null

    var STORAGE_BASIC_TEXTURE : TextureAtlasSprite = null
    var STORAGE_BACKIO_TEXTURE : TextureAtlasSprite = null
    var STORAGE_CAPACITY_TEXTURE : TextureAtlasSprite = null
    var STORAGE_CRAFTING_TEXTURE : TextureAtlasSprite = null
    var STORAGE_HOPPING_TEXTURE : TextureAtlasSprite = null
    var STORAGE_IO_TEXTURE : TextureAtlasSprite = null
    var STORAGE_NOCONNECTION_TEXTURE : TextureAtlasSprite = null
    var STORAGE_REMOTE_TEXTURE : TextureAtlasSprite = null
    var STORAGE_SEARCH_TEXTURE : TextureAtlasSprite = null
    var STORAGE_SECURITY_TEXTURE : TextureAtlasSprite = null
    var STORAGE_SIDEIO_TEXTURE : TextureAtlasSprite = null
    var STORAGE_SMASHING_TEXTURE : TextureAtlasSprite = null
    var STORAGE_SORT_TEXTURE : TextureAtlasSprite = null
}

class ModelFactory {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    def textureStitch(event : TextureStitchEvent.Pre): Unit = {
        //Here is where we want to stitch in all our extra stuff. Since Minecraft won't let us do this anymore forge has
        //been nice enough to give us a little helper event. Thanks Forge!

        //Overlays
        event.map.registerSprite(new ResourceLocation(Reference.MOD_ID + ":blocks/furnaceOverlay"))
        event.map.registerSprite(new ResourceLocation(Reference.MOD_ID + ":blocks/crusherOverlay"))
        event.map.registerSprite(new ResourceLocation(Reference.MOD_ID + ":blocks/solidsOverlay"))
        event.map.registerSprite(new ResourceLocation(Reference.MOD_ID + ":blocks/liquidsOverlay"))
        event.map.registerSprite(new ResourceLocation(Reference.MOD_ID + ":blocks/crusherExpansionOverlay"))

        //Core Icons
        event.map.registerSprite(new ResourceLocation(Reference.MOD_ID + ":blocks/crusherFront_off"))
        event.map.registerSprite(new ResourceLocation(Reference.MOD_ID + ":blocks/crusherFront_On"))

        //Storage Connected Textures
        ModelFactory.STORAGE_CONNECTED_TEXTURE_NO_CONNECTIONS = event.map.registerSprite(new ResourceLocation(Reference.MOD_ID + ":blocks/storageNoConnections"))
        ModelFactory.STORAGE_CONNECTED_TEXTURE_ANTI_CORNERS = event.map.registerSprite(new ResourceLocation(Reference.MOD_ID + ":blocks/storageAntiCorners"))
        ModelFactory.STORAGE_CONNECTED_TEXTURE_CORNERS = event.map.registerSprite(new ResourceLocation(Reference.MOD_ID + ":blocks/storageCorners"))
        ModelFactory.STORAGE_CONNECTED_TEXTURE_HORIZONTAL = event.map.registerSprite(new ResourceLocation(Reference.MOD_ID + ":blocks/storageHorizontal"))
        ModelFactory.STORAGE_CONNECTED_TEXTURE_VERTICAL = event.map.registerSprite(new ResourceLocation(Reference.MOD_ID + ":blocks/storageVertical"))

        //Storage Specific
        ModelFactory.STORAGE_BASIC_TEXTURE = event.map.registerSprite(new ResourceLocation(Reference.MOD_ID + ":blocks/storageBasic"))
        ModelFactory.STORAGE_BACKIO_TEXTURE = event.map.registerSprite(new ResourceLocation(Reference.MOD_ID + ":blocks/storageBack"))
        ModelFactory.STORAGE_CAPACITY_TEXTURE = event.map.registerSprite(new ResourceLocation(Reference.MOD_ID + ":blocks/storageCapacity"))
        ModelFactory.STORAGE_CRAFTING_TEXTURE = event.map.registerSprite(new ResourceLocation(Reference.MOD_ID + ":blocks/storageCrafting"))
        ModelFactory.STORAGE_HOPPING_TEXTURE = event.map.registerSprite(new ResourceLocation(Reference.MOD_ID + ":blocks/storageHopping"))
        ModelFactory.STORAGE_IO_TEXTURE = event.map.registerSprite(new ResourceLocation(Reference.MOD_ID + ":blocks/storageIO"))
        ModelFactory.STORAGE_NOCONNECTION_TEXTURE = event.map.registerSprite(new ResourceLocation(Reference.MOD_ID + ":blocks/storageNoConnection"))
        ModelFactory.STORAGE_REMOTE_TEXTURE = event.map.registerSprite(new ResourceLocation(Reference.MOD_ID + ":blocks/storageRemote"))
        ModelFactory.STORAGE_SEARCH_TEXTURE = event.map.registerSprite(new ResourceLocation(Reference.MOD_ID + ":blocks/storageSearch"))
        ModelFactory.STORAGE_SECURITY_TEXTURE = event.map.registerSprite(new ResourceLocation(Reference.MOD_ID + ":blocks/storageSecurity"))
        ModelFactory.STORAGE_SIDEIO_TEXTURE = event.map.registerSprite(new ResourceLocation(Reference.MOD_ID + ":blocks/storageSide"))
        ModelFactory.STORAGE_SMASHING_TEXTURE = event.map.registerSprite(new ResourceLocation(Reference.MOD_ID + ":blocks/storageSmashing"))
        ModelFactory.STORAGE_SORT_TEXTURE = event.map.registerSprite(new ResourceLocation(Reference.MOD_ID + ":blocks/storageSort"))
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    def bakeModels(event : ModelBakeEvent) : Unit = {
        val itemModelMesher = Minecraft.getMinecraft.getRenderItem.getItemModelMesher


        event.modelRegistry.putObject(new ModelResourceLocation(Reference.MOD_ID + ":" + "proxy", "normal"), new ModelProxy())
        event.modelRegistry.putObject(new ModelResourceLocation(Reference.MOD_ID + ":" + "proxy", "inventory"), new ModelProxy())
        itemModelMesher.register(Item.getItemFromBlock(BlockManager.proxy), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + "proxy", "inventory"))

        event.modelRegistry.putObject(new ModelResourceLocation(Reference.MOD_ID + ":" + "furnaceCore", "normal"), new ModelFurnaceCore())
        event.modelRegistry.putObject(new ModelResourceLocation(Reference.MOD_ID + ":" + "furnaceCore", "inventory"), new ModelFurnaceCore())
        itemModelMesher.register(Item.getItemFromBlock(BlockManager.furnaceCore), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + "furnaceCore", "inventory"))

        event.modelRegistry.putObject(new ModelResourceLocation(Reference.MOD_ID + ":" + "crusherCore", "normal"), new ModelOtherCore())
        event.modelRegistry.putObject(new ModelResourceLocation(Reference.MOD_ID + ":" + "crusherCore", "inventory"), new ModelOtherCore())
        itemModelMesher.register(Item.getItemFromBlock(BlockManager.crusherCore), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + "crusherCore", "inventory"))

        event.modelRegistry.putObject(new ModelResourceLocation(Reference.MOD_ID + ":" + "bankSolids", "normal"), new ModelCoreExpansion(BlockManager.bankSolids, false))
        event.modelRegistry.putObject(new ModelResourceLocation(Reference.MOD_ID + ":" + "bankSolids", "inventory"), new ModelCoreExpansion(BlockManager.bankSolids, true))
        itemModelMesher.register(Item.getItemFromBlock(BlockManager.bankSolids), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + "bankSolids", "inventory"))

        event.modelRegistry.putObject(new ModelResourceLocation(Reference.MOD_ID + ":" + "bankLiquids", "normal"), new ModelCoreExpansion(BlockManager.bankLiquids, false))
        event.modelRegistry.putObject(new ModelResourceLocation(Reference.MOD_ID + ":" + "bankLiquids", "inventory"), new ModelCoreExpansion(BlockManager.bankLiquids, true))
        itemModelMesher.register(Item.getItemFromBlock(BlockManager.bankLiquids), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + "bankLiquids", "inventory"))

        event.modelRegistry.putObject(new ModelResourceLocation(Reference.MOD_ID + ":" + "crusherExpansion", "normal"), new ModelCoreExpansion(BlockManager.crusherExpansion, false))
        event.modelRegistry.putObject(new ModelResourceLocation(Reference.MOD_ID + ":" + "crusherExpansion", "inventory"), new ModelCoreExpansion(BlockManager.crusherExpansion, true))
        itemModelMesher.register(Item.getItemFromBlock(BlockManager.crusherExpansion), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + "crusherExpansion", "inventory"))

        event.modelRegistry.putObject(new ModelResourceLocation(Reference.MOD_ID + ":" + "storageBasic", "normal"), new ModelStorageExpansion())
        event.modelRegistry.putObject(new ModelResourceLocation(Reference.MOD_ID + ":" + "storageBasic", "inventory"), new ModelStorageExpansion())
        itemModelMesher.register(Item.getItemFromBlock(BlockManager.storageBasic), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + "storageBasic", "inventory"))

        event.modelRegistry.putObject(new ModelResourceLocation(Reference.MOD_ID + ":" + "storageCapacity", "normal"), new ModelStorageExpansion())
        event.modelRegistry.putObject(new ModelResourceLocation(Reference.MOD_ID + ":" + "storageCapacity", "inventory"), new ModelStorageExpansion())
        itemModelMesher.register(Item.getItemFromBlock(BlockManager.storageCapacity), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + "storageCapacity", "inventory"))

        event.modelRegistry.putObject(new ModelResourceLocation(Reference.MOD_ID + ":" + "storageCrafting", "normal"), new ModelStorageExpansion())
        event.modelRegistry.putObject(new ModelResourceLocation(Reference.MOD_ID + ":" + "storageCrafting", "inventory"), new ModelStorageExpansion())
        itemModelMesher.register(Item.getItemFromBlock(BlockManager.storageCrafting), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + "storageCrafting", "inventory"))
    }
}
