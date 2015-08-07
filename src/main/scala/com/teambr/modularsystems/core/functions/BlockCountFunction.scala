package com.teambr.modularsystems.core.functions

import com.teambr.bookshelf.helper.BlockHelper
import com.teambr.modularsystems.core.registries.BlockValueRegistry
import net.minecraft.block.Block

import scala.collection.immutable.HashMap


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
class BlockCountFunction {
    var blockCount = new HashMap[String, Int]()
    var materialCount = new HashMap[String, Int]()

    /**
     * Add a block to the count function
     *
     * Also adds to the material count if the block isn't mapped already
     *
     * @param block The block to add
     * @param meta The block metadata
     */
    def addBlock(block : Block, meta : Int) : Unit = {
        var i : Int = blockCount.get(BlockHelper.getBlockString(block, meta)) match {
            case Some(value) => value
            case None => 0
        }
        i += 1
        blockCount += (BlockHelper.getBlockString(block, meta) -> i)
        if(!BlockValueRegistry.isBlockRegistered(block, meta)) {
            var j : Int = materialCount.get(BlockValueRegistry.getMaterialString(block.getMaterial)) match {
                case Some(value) => value
                case None => 0
            }
            j += 1
            materialCount + (BlockValueRegistry.getMaterialString(block.getMaterial) -> j)
        }
    }

    /**
     * Get the block count for a block
     * @param block The block
     * @param meta Metadata
     * @return How many are in the function
     */
    def getBlockCount(block : Block, meta : Int) : Int = {
        blockCount.get(BlockHelper.getBlockString(block, meta)) match {
            case Some(value) => value
            case _ => 0
        }
    }

    /**
     * Get the material count for
     * @param mat Material to check
     * @return How many of this material are in the count, doesn't include those with block mappings
     */
    def getMaterialCount(mat : String) : Int = {
        materialCount.get(mat) match {
            case Some(value) => value
            case _ => 0
        }
    }

    /**
     * Returns a list of strings that are the block ids
     */
    def getBlockIds : Set[String] = blockCount.keySet

    /**
     * Returns a list of material strings. Use the BlockValueRegistry to convert back to normal materials
     */
    def getMaterialStrings : Set[String] = materialCount.keySet
}
