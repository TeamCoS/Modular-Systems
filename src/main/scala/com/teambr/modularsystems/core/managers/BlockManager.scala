package com.teambr.modularsystems.core.managers

import com.teambr.modularsystems.storage.blocks.{BlockStorageExpansion, BlockStorageCore}
import com.teambr.modularsystems.storage.tiles.{TileStorageBasic, TileStorageCore}
import net.minecraft.block.Block
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.oredict.OreDictionary

/**
 * Modular-Systems
 * Created by Dyonovan on 04/08/15
 */

object BlockManager {

    val storageCore = new BlockStorageCore
    val storageBasic = new BlockStorageExpansion("storageBasic", List(), classOf[TileStorageBasic])

    def preInit() : Unit = {

        //Storage
        registerBlock(storageCore, "storageCore", classOf[TileStorageCore])
        registerBlock(storageBasic, "storageBasic", classOf[TileStorageBasic])
    }
    /**
     * Helper method for registering block
     *
     * @param block      The block to register
     * @param name       The name to register the block to
     * @param tileEntity The tile entity, null if none
     * @param oreDict    The ore dict tag, should it be needed
     */
    def registerBlock(block: Block, name: String, tileEntity: Class[_ <: TileEntity], oreDict: String) : Unit = {
        GameRegistry.registerBlock(block, name)
        if (tileEntity != null)
            GameRegistry.registerTileEntity(tileEntity, name)
        if (oreDict != null)
            OreDictionary.registerOre(oreDict, block)
    }

    /**
     * No ore dict helper method
     *
     * @param block      The block to add
     * @param name       The name
     * @param tileEntity The tile
     */
    def registerBlock(block: Block, name: String, tileEntity: Class[_ <: TileEntity]) : Unit = {
        registerBlock(block, name, tileEntity, null)
    }
}

