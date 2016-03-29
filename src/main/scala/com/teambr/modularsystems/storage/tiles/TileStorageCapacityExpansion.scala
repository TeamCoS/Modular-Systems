package com.teambr.modularsystems.storage.tiles

import com.teambr.bookshelf.util.WorldUtils

/**
  * This file was created for Modular-Systems
  *
  * Modular-Systems is licensed under the
  * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
  * http://creativecommons.org/licenses/by-nc-sa/4.0/
  *
  * @author Paul Davis "pauljoda"
  * @since 3/29/2016
  */
class TileStorageCapacityExpansion extends TileStorageExpansion {

    /**
      * Called after this has been added to a network
      */
    override def addedToNetwork(): Unit = getCore.pushInventory(11)

    /**
      * Called right before this is removed from a network
      */
    override def removedFromNetwork(): Unit = {
        getCore match {
            case core : TileStorageCore =>
                val stacks = core.popInventory(11)
                for(stack <- stacks)
                    WorldUtils.dropStack(worldObj, stack, pos)
            case _ =>
        }
    }
}
