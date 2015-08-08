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
import net.minecraftforge.fml.common.registry.GameRegistry
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
        addBannedBlock(Blocks.log, -1)
        addBannedBlock(Blocks.log2, -1)
        addBannedBlock(Blocks.dirt, -1)
        addBannedBlock(Blocks.ice, -1)
        addBannedBlock(Blocks.snow, -1)
        addBannedBlock(Blocks.snow_layer, -1)
        addBannedBlock(Blocks.leaves, -1)
        addBannedBlock(Blocks.leaves2, -1)
        addBannedBlock(Blocks.pumpkin, -1)
        addBannedBlock(Blocks.tnt, -1)
        addBannedBlock(Blocks.hay_block, -1)
        addBannedBlock(Blocks.wool, -1)
        addBannedBlock(Blocks.grass, -1)
        addBannedBlock(Blocks.bedrock, -1)
        addBannedBlock(Blocks.diamond_ore, -1)
        addBannedBlock(Blocks.emerald_ore, -1)
        addBannedBlock(Blocks.gold_ore, -1)
        addBannedBlock(Blocks.iron_door, -1)
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
        if (block eq Blocks.redstone_block) return false
        if (block.hasTileEntity(block.getDefaultState)) {
            return true
        }
        if (!block.isNormalCube) {
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
        val id: GameRegistry.UniqueIdentifier = GameRegistry.findUniqueIdentifierFor(block)
        if (!bannedBlocks.contains(id.modId + ":" + id.name + ":" + meta))
            bannedBlocks.add(id.modId + ":" + id.name + ":" + meta)
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
        val id: GameRegistry.UniqueIdentifier = GameRegistry.findUniqueIdentifierFor(block)
        val blockName: String = id.modId + ":" + id.name + ":" + meta
        val blockWithNoMeta: String = id.modId + ":" + id.name + ":" + -1
        bannedBlocks.contains(blockName) || bannedBlocks.contains(blockWithNoMeta)
    }
}
