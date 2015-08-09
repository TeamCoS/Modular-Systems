package com.teambr.modularsystems.core.managers

import com.teambr.modularsystems.core.common.blocks.BlockProxy
import com.teambr.modularsystems.core.common.tiles.TileProxy
import com.teambr.modularsystems.crusher.blocks.BlockCrusherCore
import com.teambr.modularsystems.crusher.tiles.TileCrusherCore
import com.teambr.modularsystems.furnace.blocks.BlockFurnaceCore
import com.teambr.modularsystems.furnace.tiles.TileEntityFurnaceCore
import com.teambr.modularsystems.power.blocks.BlockPower
import com.teambr.modularsystems.power.tiles.{TileBankLiquids, TileBankSolids}
import com.teambr.modularsystems.storage.blocks.{BlockStorageExpansion, BlockStorageCore}
import com.teambr.modularsystems.storage.tiles.{ TileStorageCrafting, TileStorageCapacity, TileStorageBasic, TileStorageCore }
import net.minecraft.block.Block
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.oredict.OreDictionary

/**
 * Modular-Systems
 * Created by Dyonovan on 04/08/15
 */

object BlockManager {

    //Cores
    val furnaceCore = new BlockFurnaceCore("furnaceCore")
    val crusherCore = new BlockCrusherCore("crusherCore")

    //Power
    val bankSolids = new BlockPower("bankSolids", classOf[TileBankSolids])
    val bankLiquids = new BlockPower("bankLiquids", classOf[TileBankLiquids])

    //Storage
    val storageCore = new BlockStorageCore
    val storageBasic = new BlockStorageExpansion("storageBasic", List(), classOf[TileStorageBasic])
    val storageCapacity = new BlockStorageExpansion("storageCapacity", List(), classOf[TileStorageCapacity])
    val storageCrafting = new BlockStorageExpansion("storageCrafting", List(), classOf[TileStorageCrafting])

    val proxy = new BlockProxy("proxy", classOf[TileProxy])

    def preInit() : Unit = {
        //Core
        registerBlock(proxy, "proxy", classOf[TileProxy])
        registerBlock(furnaceCore, "furnaceCore", classOf[TileEntityFurnaceCore])
        registerBlock(crusherCore, "crusherCore", classOf[TileCrusherCore])

        //Power
        registerBlock(bankSolids, "bankSolids", classOf[TileBankSolids])
        registerBlock(bankLiquids, "bankLiquids", classOf[TileBankLiquids])

        //Storage
        registerBlock(storageCore, "storageCore", classOf[TileStorageCore])
        registerBlock(storageBasic, "storageBasic", classOf[TileStorageBasic])
        registerBlock(storageCapacity, "storageCapacity", classOf[TileStorageCapacity])
        registerBlock(storageCrafting, "storageCrafting", classOf[TileStorageCrafting])
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

