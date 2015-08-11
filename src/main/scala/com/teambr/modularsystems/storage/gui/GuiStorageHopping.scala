package com.teambr.modularsystems.storage.gui

import com.teambr.bookshelf.client.gui.GuiBase
import com.teambr.bookshelf.client.gui.component.control.GuiComponentSlider
import com.teambr.bookshelf.common.container.ContainerGeneric
import com.teambr.bookshelf.network.PacketManager
import com.teambr.modularsystems.storage.tiles.TileStorageHopping

/**
 * This file was created for Modular-Systems
 *
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis <pauljoda>
 * @since August 10, 2015
 */
class GuiStorageHopping(val tile: TileStorageHopping) extends GuiBase[ContainerGeneric](new ContainerGeneric, 100, 50, "inventory.storageHopping.title") {
    override def addComponents() : Unit = {
       components += new GuiComponentSlider[Int](10, 25, 80, List.range(0, 11), tile.range) {
           override def onValueChanged(value : Int): Unit = {
               tile.range = value
               PacketManager.updateTileWithClientInfo(tile)
           }
       }
    }
}
