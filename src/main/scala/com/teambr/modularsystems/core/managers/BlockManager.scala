package com.teambr.modularsystems.core.managers

import java.awt.Color

import com.teambr.modularsystems.core.common.blocks.{ BlockCoreExpansion, BlockProxy }
import com.teambr.modularsystems.core.common.tiles.{ TileIOExpansion, TileProxy }
import com.teambr.modularsystems.crusher.blocks.BlockCrusherCore
import com.teambr.modularsystems.crusher.tiles.{TileCrusherExpansion, TileCrusherCore}
import com.teambr.modularsystems.furnace.blocks.BlockFurnaceCore
import com.teambr.modularsystems.furnace.tiles.TileEntityFurnaceCore
import com.teambr.modularsystems.power.tiles.{TileBankRF, TileBankLiquids, TileBankSolids}
import com.teambr.modularsystems.storage.blocks.{BlockStorageCore, BlockStorageExpansion}
import com.teambr.modularsystems.storage.tiles.{TileStorageBasic, TileStorageCapacity, TileStorageCore, TileStorageCrafting}
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
    val bankSolids = new BlockCoreExpansion("bankSolids", classOf[TileBankSolids], 0x4A390E)
    val bankLiquids = new BlockCoreExpansion("bankLiquids", classOf[TileBankLiquids], 0x215045)
    val bankRF = new BlockCoreExpansion("bankRF", classOf[TileBankRF], new Color(174, 0, 36).getRGB)

    //Storage
    val storageCore = new BlockStorageCore
    val storageBasic = new BlockStorageExpansion("storageBasic", List(), classOf[TileStorageBasic])
    val storageCapacity = new BlockStorageExpansion("storageCapacity", List(), classOf[TileStorageCapacity])
    val storageCrafting = new BlockStorageExpansion("storageCrafting", List(), classOf[TileStorageCrafting])

    //Expansions
    val crusherExpansion = new BlockCoreExpansion("crusherExpansion", classOf[TileCrusherExpansion], 0x555555)
    val ioExpansion = new BlockCoreExpansion("ioExpansion", classOf[TileIOExpansion], 0xBBBBBB)

    val proxy = new BlockProxy("proxy", classOf[TileProxy])

    def preInit() : Unit = {
        //Core
        registerBlock(proxy, "proxy", classOf[TileProxy])
        registerBlock(furnaceCore, "furnaceCore", classOf[TileEntityFurnaceCore])
        registerBlock(crusherCore, "crusherCore", classOf[TileCrusherCore])

        //Power
        registerBlock(bankSolids, "bankSolids", classOf[TileBankSolids])
        registerBlock(bankLiquids, "bankLiquids", classOf[TileBankLiquids])
        registerBlock(bankRF, "bankRF", classOf[TileBankRF])

        //Storage
        registerBlock(storageCore, "storageCore", classOf[TileStorageCore])
        registerBlock(storageBasic, "storageBasic", classOf[TileStorageBasic])
        registerBlock(storageCapacity, "storageCapacity", classOf[TileStorageCapacity])
        registerBlock(storageCrafting, "storageCrafting", classOf[TileStorageCrafting])

        //Expansions
        registerBlock(crusherExpansion, "crusherExpansion", classOf[TileCrusherExpansion])
        registerBlock(ioExpansion, "ioExpansion", classOf[TileIOExpansion])
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

