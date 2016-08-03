package com.teambr.modularsystems.core.registries

import java.io.File
import java.util

import com.google.gson.reflect.TypeToken
import com.teambr.bookshelf.helper.LogHelper
import com.teambr.bookshelf.util.JsonUtils
import com.teambr.modularsystems.core.ModularSystems
import com.teambr.modularsystems.core.collections.CrusherRecipes
import net.minecraft.init.{Blocks, Items}
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.oredict.OreDictionary

import scala.collection.JavaConversions._

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
    def init(): Unit = {
        if (!loadFromFile)
            generateDefaults()
        else
            LogHelper.info("Block Values loaded successfully")
    }

    /**
     * Load the values from the file
 *
     * @return True if successful
     */
    def loadFromFile(): Boolean = {
        LogHelper.info("Loading Block Values...")
        crusherRecipes = JsonUtils.readFromJson[util.ArrayList[CrusherRecipes]](new TypeToken[util.ArrayList[CrusherRecipes]]() {
        }, ModularSystems.configFolderLocation + File.separator + "Registries" + File.separator + "crusherRecipes.json")
        if (crusherRecipes == null)
            crusherRecipes = new util.ArrayList[CrusherRecipes]()
        crusherRecipes.nonEmpty
    }

    /**
     * Save the current registry to a file
     */
    def saveToFile(): Unit = {
        if (crusherRecipes.nonEmpty) JsonUtils.writeToJson(crusherRecipes, ModularSystems.configFolderLocation +
                File.separator + "Registries" + File.separator + "crusherRecipes.json")
    }

    /**
     * Used to generate the default values
     */
    def generateDefaults(): Unit = {
        LogHelper.info("Json not found. Creating Dynamic Crusher Recipe List...")

        val oreDict = OreDictionary.getOreNames

        for (i <- oreDict) {
            if (i.startsWith("dust")) {
                val oreList = OreDictionary.getOres(i.replaceFirst("dust", "ore"))
                if (!oreList.isEmpty) {
                    i.replaceFirst("dust", "ore") match {
                        case "oreRedstone" =>
                            crusherRecipes.add(new CrusherRecipes("oreRedstone",
                                getItemStackString(new ItemStack(Items.REDSTONE)), 6, getItemStackString(new ItemStack(Items.REDSTONE))))
                        /*case "oreLapis" =>
                            crusherRecipes.add(new CrusherRecipes("oreLapis",
                                getItemStackString(new ItemStack(Items.dye, 1, 4)), 6, getItemStackString(new ItemStack(Items.dye, 1, 4))))*/
                        case _ =>
                            val itemList = OreDictionary.getOres(i)
                            if (itemList.size() > 0)
                                crusherRecipes.add(new CrusherRecipes(i.replaceFirst("dust", "ore"),
                                    getItemStackString(new ItemStack(itemList.get(0).getItem, 1, itemList.get(0).getItemDamage)),
                                    2, ""))
                    }
                }
            } else if (i.startsWith("ingot")) {
                val oreList = OreDictionary.getOres(i.replaceFirst("ingot", "dust"))
                if (!oreList.isEmpty) {
                    val itemList = OreDictionary.getOres(i.replaceFirst("ingot", "dust"))
                    if (itemList.size() > 0) {
                        crusherRecipes.add(new CrusherRecipes(i, getItemStackString(
                            new ItemStack(itemList.get(0).getItem, 1, itemList.get(0).getItemDamage)), 1, ""))
                    }
                }
            }
        }

        crusherRecipes.add(new CrusherRecipes("oreLapis",
            getItemStackString(new ItemStack(Items.DYE, 1, 4)), 6, getItemStackString(new ItemStack(Items.DYE, 1, 4))))
        crusherRecipes.add(new CrusherRecipes(getOreDict(new ItemStack(Items.BLAZE_ROD)),
            getItemStackString(new ItemStack(Items.BLAZE_ROD)), 5, getItemStackString(new ItemStack(Items.BLAZE_ROD))))
        crusherRecipes.add(new CrusherRecipes("cobblestone",
            getItemStackString(new ItemStack(Blocks.SAND)), 1, ""))
        crusherRecipes.add(new CrusherRecipes(getOreDict(new ItemStack(Items.BONE)),
            getItemStackString(new ItemStack(Items.DYE, 1, 15)), 4, getItemStackString(new ItemStack(Items.DYE, 1, 15))))
        crusherRecipes.add(new CrusherRecipes("oreQuartz",
            getItemStackString(new ItemStack(Items.QUARTZ)), 3, getItemStackString(new ItemStack(Items.QUARTZ))))
        crusherRecipes.add(new CrusherRecipes(getOreDict(new ItemStack(Blocks.CLAY)),
            getItemStackString(new ItemStack(Items.CLAY_BALL)), 4, ""))
        crusherRecipes.add(new CrusherRecipes("oreDiamond",
            getItemStackString(new ItemStack(Items.DIAMOND)), 2, getItemStackString(new ItemStack(Items.DIAMOND))))
        crusherRecipes.add(new CrusherRecipes("oreEmerald",
            getItemStackString(new ItemStack(Items.EMERALD)), 2, getItemStackString(new ItemStack(Items.EMERALD))))
        crusherRecipes.add(new CrusherRecipes("glowstone",
            getItemStackString(new ItemStack(Items.GLOWSTONE_DUST)), 3, getItemStackString(new ItemStack(Items.GLOWSTONE_DUST))))
        crusherRecipes.add(new CrusherRecipes("oreCoal",
            getItemStackString(new ItemStack(Items.COAL, 1, 0)), 3, getItemStackString(new ItemStack(Items.COAL, 1, 0))))
        crusherRecipes.add(new CrusherRecipes(getOreDict(new ItemStack(Blocks.WOOL)),
            getItemStackString(new ItemStack(Items.STRING)), 3, getItemStackString(new ItemStack(Items.STRING))))
        crusherRecipes.add(new CrusherRecipes("blockGlass",
            getItemStackString(new ItemStack(Blocks.SAND)), 1, ""))
        crusherRecipes.add(new CrusherRecipes(getOreDict(new ItemStack(Blocks.GRAVEL)),
            getItemStackString(new ItemStack(Items.FLINT)), 1, getItemStackString(new ItemStack(Items.FLINT))))


        saveToFile()
        LogHelper.info("Finished adding " + crusherRecipes.size + " Crusher Recipes")
    }

    /**
     * Get the oreDict tag for an item
 *
     * @param itemstack The stack to try
     * @return The string for this stack or OreDict name
     */
    private def getOreDict(itemstack: ItemStack): String = {
        val registered: Array[Int] = OreDictionary.getOreIDs(itemstack)
        if (registered.length > 0)
            OreDictionary.getOreName(registered(0))
        else {
            getItemStackString(itemstack)
        }
    }

    /**
     * Get the output for an input
     */
    def getOutput(itemStack: ItemStack): Option[(ItemStack, ItemStack)] = {
        if (itemStack != null) {
            for (i <- crusherRecipes) {
                val name = i.input.split(":")
                val stackOut = getItemStackFromString(i.output)
                val stackExtra = getItemStackFromString(i.outputSecondary)
                name.length match {
                    case 3 =>
                        val stackIn = getItemStackFromString(i.input)
                        if (stackIn != null && itemStack.isItemEqual(stackIn)) {
                            return Some((new ItemStack(stackOut.getItem, i.qty, stackOut.getItemDamage), stackExtra))
                        }
                    case 1 =>
                        if (stackOut != null && stackOut.getItem != null && checkOreDict(i.input, itemStack))
                            return Some((new ItemStack(stackOut.getItem, i.qty, stackOut.getItemDamage), stackExtra))
                }
            }
        }
        None
    }

    /**
     * Checks if the item is a valid item for this registry
 *
     * @param itemStack The stack to test
     * @return True if an output exists
     */
    def isItemValid(itemStack: ItemStack): Boolean = {
        getOutput(itemStack).isDefined
    }

    /**
     * Get the oreDict tag for an item
 *
     * @param itemStack The stack to find
     * @return The string for this stack or OreDict name
     */
    def checkOreDict(oreDict: String, itemStack: ItemStack): Boolean = {
        val oreList = OreDictionary.getOres(oreDict)
        for (i <- oreList) {
            if (i.getItemDamage == OreDictionary.WILDCARD_VALUE) {
                if (i.getItem == itemStack.getItem)
                    return true
            } else if (i.isItemEqual(itemStack)) {
                return true
            }
        }
        false
    }

    private def getItemStackString(itemStack: ItemStack): String = {
        itemStack.getItem.getRegistryName.toString + ":" + itemStack.getItemDamage
    }

    private def getItemStackFromString(item: String): ItemStack = {
        val name: Array[String] = item.split(":")
        name.length match {
            case 3 =>
                if (item == "")
                    null
                else
                    new ItemStack(GameRegistry.findItem(name(0), name(1)), 1, Integer.valueOf(name(2)))
            case _ => null
        }
    }

    def getRecipes: util.ArrayList[CrusherRecipesStack] = {
        val list = new util.ArrayList[CrusherRecipesStack]
        for (i <- crusherRecipes) {
            val outStack = getItemStackFromString(i.output)
            outStack.stackSize = i.qty
            if (getItemStackFromString(i.input) == null) {
                for (j <- OreDictionary.getOres(i.input)) {

                    list.add(new CrusherRecipesStack(j, outStack, getItemStackFromString(i.outputSecondary)))
                }
            } else list.add(new CrusherRecipesStack(getItemStackFromString(i.input), outStack, getItemStackFromString(i.outputSecondary)))
        }
        list
    }

    class CrusherRecipesStack(i: ItemStack, o: ItemStack, s: ItemStack) {
        var input = i
        var output = o
        var secondary = s
    }
}
