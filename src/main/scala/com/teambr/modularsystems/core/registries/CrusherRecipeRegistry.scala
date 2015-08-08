package com.teambr.modularsystems.core.registries

import java.io.File
import java.util

import com.teambr.bookshelf.helper.LogHelper
import com.teambr.bookshelf.util.JsonUtils
import com.teambr.modularsystems.core.ModularSystems
import com.teambr.modularsystems.core.collections.CrusherRecipes
import net.minecraftforge.oredict.OreDictionary

/**
 * This file was created for Modular-Systems
 *
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Dyonovan
 * @since August 08, 2015
 */
object CrusherRecipeRegistry {

    var crusherRecipes = new util.ArrayList[CrusherRecipes]()

    /**
     * Add the values
     */
    /*def init(): Unit = {
        if (!loadFromFile)
            //generateDefaults()
        else
            LogHelper.info("Block Values loaded successfully")
    }*/

    /**
     * Load the values from the file
     * @return True if successful
     */
    /*def loadFromFile(): Boolean = {
        LogHelper.info("Loading Block Values...")
        crusherRecipes = JsonUtils.readFromJson[util.ArrayList[CrusherRecipes](new TypeToken[util.ArrayList[CrusherRecipes]]() {
        }, ModularSystems.configFolderLocation + File.separator + "Registries" + File.separator + "crusherRecipes.json")
        if (crusherRecipes == null)
            crusherRecipes = new util.ArrayList[CrusherRecipes]()
        !crusherRecipes.isEmpty
    }*/

    /**
     * Save the current registry to a file
     */
    def saveToFile(): Unit = {
        if (!crusherRecipes.isEmpty) JsonUtils.writeToJson(crusherRecipes, ModularSystems.configFolderLocation +
                File.separator + "Registries" + File.separator + "furnaceBannedBlocks.json")
    }

    /**
     * Used to generate the default values
     */
    LogHelper.info("Json not found. Creating Dynamic Crusher Recipe List...")

    val oreDict = OreDictionary.getOreNames

    for (i <- oreDict) {
        if (i.startsWith("dust")) {
            val oreList = OreDictionary.getOres(i.replaceFirst("dust", ""))
            if (!oreList.isEmpty) {
                i.replaceFirst("dust","ore") match {
                    case "oreRedstone" =>
                }
            }
        } else if (i.startsWith("ingot")) {

        }
    }
}
