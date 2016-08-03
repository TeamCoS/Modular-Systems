package com.teambr.modularsystems.core.registries

import java.io.File
import java.util

import com.google.gson.reflect.TypeToken
import com.teambr.bookshelf.helper.LogHelper
import com.teambr.bookshelf.util.JsonUtils
import com.teambr.modularsystems.core.ModularSystems
import net.minecraft.block.Block
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary

/**
 * This file was created for Modular-Systems
 *
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Dyonovan
 * @since August 07, 2015
 */
object FurnaceBannedBlocks {

    var bannedBlocks = new java.util.ArrayList[String]()

    def init(): Unit = {
        if (!loadFromFile)
            generateDefaults()
        else
            LogHelper.info("Furnace Banned Blocks loaded successfully")
    }

    /**
     * Load the values from the file
     * @return True if successful
     */
    def loadFromFile(): Boolean = {
        LogHelper.info("Loading Furnace Banned Blocks...")
        bannedBlocks = JsonUtils.readFromJson[util.ArrayList[String]](new TypeToken[util.ArrayList[String]]() {
        }, ModularSystems.configFolderLocation + File.separator + "Registries" + File.separator + "furnaceBannedBlocks.json")
        if (bannedBlocks == null)
            bannedBlocks = new util.ArrayList[String]()
        !bannedBlocks.isEmpty
    }

    /**
     * Save the current registry to a file
     */
    def saveToFile(): Unit = {
        if (!bannedBlocks.isEmpty) JsonUtils.writeToJson(bannedBlocks, ModularSystems.configFolderLocation +
                File.separator + "Registries" + File.separator + "furnaceBannedBlocks.json")
    }

    /**
     * Used to generate the default values
     */
    def generateDefaults(): Unit = {
        LogHelper.info("Either this is the first load of the file is missing, generating default banned blocks")
        addBannedBlock(Blocks.LOG, -1)
        addBannedBlock(Blocks.LOG2, -1)
        addBannedBlock(Blocks.DIRT, -1)
        addBannedBlock(Blocks.ICE, -1)
        addBannedBlock(Blocks.SNOW, -1)
        addBannedBlock(Blocks.SNOW_LAYER, -1)
        addBannedBlock(Blocks.LEAVES, -1)
        addBannedBlock(Blocks.LEAVES2, -1)
        addBannedBlock(Blocks.PUMPKIN, -1)
        addBannedBlock(Blocks.TNT, -1)
        addBannedBlock(Blocks.HAY_BLOCK, -1)
        addBannedBlock(Blocks.WOOL, -1)
        addBannedBlock(Blocks.GRASS, -1)
        addBannedBlock(Blocks.BEDROCK, -1)
        addBannedBlock(Blocks.DIAMOND_ORE, -1)
        addBannedBlock(Blocks.EMERALD_ORE, -1)
        addBannedBlock(Blocks.GOLD_ORE, -1)
        addBannedBlock(Blocks.IRON_DOOR, -1)
        val path: File = new File(ModularSystems.configFolderLocation + File.separator + "Registries")
        if (!path.exists) path.mkdirs
        saveToFile()
    }

    /**
     * Check on the fly if this is bad
     * @param block The blocks to check
     * @return True if it shouldn't be there
     */
    def isBadBlockFromBlock(block: Block): Boolean = {
        if (block eq Blocks.REDSTONE_BLOCK) return false
        if (block.hasTileEntity(block.getDefaultState)) {
            return true
        }
        if (!block.isNormalCube(block.getDefaultState)) {
            return true
        }
        val oreDictCheck = OreDictionary.getOreIDs(new ItemStack(block))
        val isWood = OreDictionary.getOreID("logWood")
        val isPlank = OreDictionary.getOreID("plankWood")
        oreDictCheck.contains(isWood) || oreDictCheck.contains(isPlank)
    }

    /**
     * Add a blocks to the ban registry
     * @param block The blocks to ban
     * @param meta The blocks metadata
     */
    def addBannedBlock(block: Block, meta: Int) {
        if (!bannedBlocks.contains(block.getRegistryName.toString + ":" + meta))
            bannedBlocks.add(block.getRegistryName.toString + ":" + meta)
    }

    /**
     * Used for adding by name, useful with command
     * @param blockName The name of the blocks
     *                  'modid:blockname:meta'
     */
    def addBannedBlock(blockName: String) {
        if (!bannedBlocks.contains(blockName)) bannedBlocks.add(blockName)
    }

    /**
     * Check if the blocks has been listed as banned
     * @param block The blocks to check
     * @param meta The blocks's metadata
     * @return True if is banned
     */
    def isBlockBanned(block: Block, meta: Int): Boolean = {
        val blockName: String = block.getRegistryName.toString + ":" + meta
        val blockWithNoMeta: String = block.getRegistryName.toString + ":" + -1
        bannedBlocks.contains(blockName) || bannedBlocks.contains(blockWithNoMeta)
    }
}
