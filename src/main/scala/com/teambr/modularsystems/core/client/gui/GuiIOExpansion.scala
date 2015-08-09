package com.teambr.modularsystems.core.client.gui

import com.teambr.bookshelf.client.gui.GuiBase
import com.teambr.bookshelf.client.gui.component.control.GuiComponentCheckBox
import com.teambr.bookshelf.common.container.ContainerGeneric
import com.teambr.bookshelf.network.PacketManager
import com.teambr.modularsystems.core.common.tiles.TileIOExpansion

/**
 * This file was created for Modular-Systems
 *
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis <pauljoda>
 * @since August 09, 2015
 */
class GuiIOExpansion(tile : TileIOExpansion) extends GuiBase[ContainerGeneric](new ContainerGeneric, 100, 70, "inventory.io.title") {
    override def addComponents() : Unit = {
        if(tile != null) {

            components += new GuiComponentCheckBox(20, 20, "inventory.io.import", tile.input) {
                override def setValue(bool : Boolean) = {
                    tile.input = bool
                    PacketManager.updateTileWithClientInfo(tile)
                }
            }

            components += new GuiComponentCheckBox(20, 35, "inventory.io.export", tile.output) {
                override def setValue(bool : Boolean) = {
                    tile.output = bool
                    PacketManager.updateTileWithClientInfo(tile)
                }
            }
            components += new GuiComponentCheckBox(20, 50, "inventory.io.auto", tile.auto) {
                override def setValue(bool : Boolean) = {
                    tile.auto = bool
                    PacketManager.updateTileWithClientInfo(tile)
                }
            }
        }
    }
}
