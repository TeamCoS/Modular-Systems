package com.teambr.modularsystems.core.client.modelfactory

import com.teambr.modularsystems.core.client.modelfactory.models.{ ModelOtherCore, ModelPowerBank, ModelFurnaceCore, ModelProxy }
import com.teambr.modularsystems.core.lib.Reference
import com.teambr.modularsystems.core.managers.BlockManager
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper
import net.minecraft.client.resources.model.ModelResourceLocation
import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.ModelBakeEvent
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
}

class ModelFactory {
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

        event.modelRegistry.putObject(new ModelResourceLocation(Reference.MOD_ID + ":" + "bankSolids", "normal"), new ModelPowerBank(BlockManager.bankSolids, false))
        event.modelRegistry.putObject(new ModelResourceLocation(Reference.MOD_ID + ":" + "bankSolids", "inventory"), new ModelPowerBank(BlockManager.bankSolids, true))
        itemModelMesher.register(Item.getItemFromBlock(BlockManager.bankSolids), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + "bankSolids", "inventory"))
    }
}
