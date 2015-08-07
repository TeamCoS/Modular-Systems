package com.teambr.modularsystems.core.registries

import java.io.{File, IOException}
import java.net.URL
import java.util

import com.google.gson.reflect.TypeToken
import com.teambr.bookshelf.helper.{BlockHelper, LogHelper}
import com.teambr.bookshelf.util.JsonUtils
import com.teambr.modularsystems.core.ModularSystems
import com.teambr.modularsystems.core.collections.{BlockValues, Calculation}
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import org.apache.commons.io.FileUtils

/**
 * This file was created for Modular-Systems
 *
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis <pauljoda>
 * @since August 05, 2015
 */
object BlockValueRegistry {
    var blockValues = new util.LinkedHashMap[java.lang.String, BlockValues]()
    var materialValues = new util.LinkedHashMap[java.lang.String, BlockValues]()

    /**
     * Add the values
     */
    def init() {
        if (!loadFromFile) generateDefaults()
        else LogHelper.info("Block Values loaded successfully")
    }

    /**
     * Load the values from the file
     * @return True if successful
     */
    def loadFromFile : Boolean = {
        LogHelper.info("Loading Block Values...")
        blockValues = JsonUtils.readFromJson[util.LinkedHashMap[java.lang.String, BlockValues]](new TypeToken[util.LinkedHashMap[java.lang.String, BlockValues]]() {
        }, ModularSystems.configFolderLocation + File.separator + "Registries" + File.separator + "blockValues.json")
        materialValues = JsonUtils.readFromJson[util.LinkedHashMap[java.lang.String, BlockValues]](new TypeToken[util.LinkedHashMap[java.lang.String, BlockValues]]() {
        }, ModularSystems.configFolderLocation + File.separator + "Registries" + File.separator + "materialValues.json")
        blockValues != null
    }

    /**
     * Save the current registry to a file
     */
    def saveToFile() {
        validateList()
        if (!blockValues.isEmpty) JsonUtils.writeToJson(blockValues, ModularSystems.configFolderLocation + File.separator + "Registries" + File.separator + "blockValues.json")
        if (!materialValues.isEmpty) JsonUtils.writeToJson(materialValues, ModularSystems.configFolderLocation + File.separator + "Registries" + File.separator + "materialValues.json")
    }

    /**
     * Used to generate the default values
     */
    def generateDefaults() {
        validateList()
        val file : File = new File(ModularSystems.configFolderLocation + File.separator + "Registries" + File.separator + "blockValues.json")
        if (!file.exists) {
            val fileURL : URL = ModularSystems.getClass.getResource("/blockValues.json")
            try {
                FileUtils.copyURLToFile(fileURL, file)
            }
            catch {
                case e : IOException =>
                    e.printStackTrace()
            }
        }
        blockValues = JsonUtils.readFromJson[util.LinkedHashMap[String, BlockValues]](new TypeToken[util.LinkedHashMap[String, BlockValues]]() {
        }, ModularSystems.configFolderLocation + File.separator + "Registries" + File.separator + "blockValues.json")

        addMaterialValues(Material.rock, new Calculation(-1, 200, 0, 1, 0, -100, 0), new Calculation(1, 100, 0, 1, 0, 0, 450), new Calculation(1, 1, 0, 1, 0, 0, 0))
        addMaterialValues(Material.iron, new Calculation(1, 100, 0, 1, 0, 100, 0), new Calculation(10, 1, 0, 1, 0, 0, 1600), new Calculation(1, 1, 0, 1, 0, 0, 0))
        saveToFile()
    }

    /**
     * Add a value to a block
     * @param block The block to map
     * @param meta The meta of that block
     * @param speed Speed Function
     * @param efficiency Efficiency Function
     * @param multiplicity Multiplicity Function
     */
    def addBlockValues(block : Block, meta : Int, speed : Calculation, efficiency : Calculation, multiplicity : Calculation) {
        validateList()
        if (!isBlockRegistered(block, meta)) {
            blockValues.put(BlockHelper.getBlockString(block, meta), new BlockValues(speed, efficiency, multiplicity))
        }
        else {
            LogHelper.info("Someone attempted to add a value to a blocks that already has a value. Replacing...")
            val id : String = if (blockValues.get(BlockHelper.getBlockString(block, meta)) != null) BlockHelper.getBlockString(block, meta) else BlockHelper.getBlockString(block)
            blockValues.get(id).speedFunction =speed
            blockValues.get(id).efficiencyFunction = efficiency
            blockValues.get(id).multiplicityFunction = multiplicity
            LogHelper.info("Replaced")
        }
    }

    /**
     * Removes the values for a given block
     * @param block The block to delete
     * @param meta The meta to delete
     */
    def deleteBlockValues(block : Block, meta : Int) {
        validateList()
        if ((if (blockValues.get(BlockHelper.getBlockString(block)) != null) blockValues.get(BlockHelper.getBlockString(block)) else blockValues.get(BlockHelper.getBlockString(block, meta))) != null) {
            LogHelper.info("Deleting values...")
            blockValues.remove(if (blockValues.get(BlockHelper.getBlockString(block)) != null) BlockHelper.getBlockString(block) else BlockHelper.getBlockString(block, meta))
            LogHelper.info("Values deleted")
        }
        else {
            LogHelper.warning("You can't delete something that does not exist")
        }
    }

    /**
     * Used to add a material value
     * @param material The material to map
     * @param speed Speed function
     * @param efficiency Efficiency function
     * @param multiplicity Multiplicity function
     */
    def addMaterialValues(material : Material, speed : Calculation, efficiency : Calculation, multiplicity : Calculation) {
        validateList()
        if (!isMaterialRegistered(material)) {
            materialValues.put(getMaterialString(material), new BlockValues(speed, efficiency, multiplicity))
        }
        else {
            LogHelper.info("Someone attempted to add a value to a material that already has a value. Replacing...")
            materialValues.get(getMaterialString(material)).speedFunction = speed
            materialValues.get(getMaterialString(material)).efficiencyFunction = efficiency
            materialValues.get(getMaterialString(material)).multiplicityFunction = multiplicity
            LogHelper.info("Replaced")
        }
    }

    /**
     * Is this blocks registered in the registry
     * @param block Block to check
     * @return True if found
     */
    def isBlockRegistered(block : Block, meta : Int) : Boolean =
        blockValues.get(BlockHelper.getBlockString(block, meta)) != null || blockValues.get(BlockHelper.getBlockString(block)) != null

    /**
     * Used to check if the material has been registered
     * @param material The material to check
     * @return True if mapped
     */
    def isMaterialRegistered(material : Material) : Boolean =
        materialValues.get(getMaterialString(material)) != null

    /**
     * Used to check if the material has been registered
     * @param material The material to check
     * @return True if mapped
     */
    def isMaterialRegistered(material : String) : Boolean =
        materialValues.get(material) != null

    /**
     * Converts the material to a string. Would be nice if Minecraft already had something like this
     * @param material Material to name
     * @return The string representation of this material
     */
    def getMaterialString(material : Material) : String = {
        if (material eq Material.air) return "air"
        else if (material eq Material.grass) return "grass"
        else if (material eq Material.ground) return "ground"
        else if (material eq Material.wood) return "wood"
        else if (material eq Material.rock) return "rock"
        else if (material eq Material.iron) return "iron"
        else if (material eq Material.piston) return "piston"
        else if (material eq Material.web) return "web"
        else if (material eq Material.cake) return "cake"
        else if (material eq Material.portal) return "portal"
        else if (material eq Material.dragonEgg) return "dragonEgg"
        else if (material eq Material.gourd) return "gourd"
        else if (material eq Material.clay) return "clay"
        else if (material eq Material.cactus) return "cactus"
        else if (material eq Material.craftedSnow) return "craftedSnow"
        "NULL"
    }

    /**
     * Get the speed value for the block
     * @param block The block
     * @param meta The meta of the block
     * @param x How many are in the function
     * @return What value the equation returns
     */
    def getSpeedValue(block : Block, meta : Int, x : Int) : Double = {
        if (isBlockRegistered(block, meta)) {
            val values : BlockValues = if (blockValues.get(BlockHelper.getBlockString(block)) != null) blockValues.get(BlockHelper.getBlockString(block)) else blockValues.get(BlockHelper.getBlockString(block, meta))
            values.speedFunction.F(x)
        }
        else  0
    }

    /**
     * Get the efficiency value for the block
     * @param block The block
     * @param meta The meta of the block
     * @param x How many are in the function
     * @return What value the equation returns
     */
    def getEfficiencyValue(block : Block, meta : Int, x : Int) : Double = {
        if (isBlockRegistered(block, meta)) {
            val v : BlockValues = if (blockValues.get(BlockHelper.getBlockString(block)) != null) blockValues.get(BlockHelper.getBlockString(block)) else blockValues.get(BlockHelper.getBlockString(block, meta))
            v.efficiencyFunction.F(x)
        }
        else  0
    }

    /**
     * Get the multiplicity value for the block
     * @param block The block
     * @param meta The meta of the block
     * @param x How many are in the function
     * @return What value the equation returns
     */
    def getMultiplicityValue(block : Block, meta : Int, x : Int) : Double = {
        if (isBlockRegistered(block, meta)) {
            val v : BlockValues = if (blockValues.get(BlockHelper.getBlockString(block)) != null) blockValues.get(BlockHelper.getBlockString(block)) else blockValues.get(BlockHelper.getBlockString(block, meta))
            v.multiplicityFunction.F(x)
        }
        else 0
    }

    /**
     * Get the speed value for the material
     * @param mat The string value of the material
     * @param x How many are in the function
     * @return What value the equation returns
     */
    def getSpeedValueMaterial(mat : String, x : Int) : Double = {
        if (isMaterialRegistered(mat)) {
            val blockValues : BlockValues = materialValues.get(mat)
            blockValues.speedFunction.F(x)
        }
        else 0
    }

    /**
     * Get the efficiency value for the material
     * @param mat The string value of the material
     * @param x How many are in the function
     * @return What value the equation returns
     */
    def getEfficiencyValueMaterial(mat : String, x : Int) : Double = {
        if (isMaterialRegistered(mat)) {
            val blockValues : BlockValues = materialValues.get(mat)
            blockValues.efficiencyFunction.F(x)
        }
        else 0
    }

    /**
     * Get the multiplicity value for the material
     * @param mat The string value of the material
     * @param x How many are in the function
     * @return What value the equation returns
     */
    def getMultiplicityValueMaterial(mat : String, x : Int) : Double = {
        if (isMaterialRegistered(mat)) {
            val blockValues : BlockValues = materialValues.get(mat)
            blockValues.multiplicityFunction.F(x)
        }
        else 0
    }

    /**
     * Get the values associated with a block
     * @param block The block
     * @param meta The metadata
     * @return The { @link BlockValues} for this block
     */
    def getBlockValues(block : Block, meta : Int) : BlockValues = {
        if (isBlockRegistered(block, meta)) {
            if (blockValues.get(BlockHelper.getBlockString(block)) != null) blockValues.get(BlockHelper.getBlockString(block)) else blockValues.get(BlockHelper.getBlockString(block, meta))
        }
        else null
    }

    /**
     * Make sure the list exists
     */
    private def validateList() {
        if (blockValues == null) blockValues = new util.LinkedHashMap[String, BlockValues]
        if (materialValues == null) materialValues = new util.LinkedHashMap[String, BlockValues]
    }

    /*/**
     * Save stuff for later
     */
    @SubscribeEvent def onWorldSaveEvent(event : WorldEvent.Save) : Unit = saveToFile()*/
}
