package com.teambr.modularsystems.storage.gui

import com.teambr.bookshelf.client.gui.GuiBase
import com.teambr.bookshelf.client.gui.component.control.GuiComponentScrollBar
import com.teambr.modularsystems.storage.container.ContainerStorageCore
import com.teambr.modularsystems.storage.tiles.TileStorageCore
import net.minecraft.entity.player.EntityPlayer

/**
  * This file was created for Modular-Systems
  *
  * Modular-Systems is licensed under the
  * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
  * http://creativecommons.org/licenses/by-nc-sa/4.0/
  *
  * @author Paul Davis "pauljoda"
  * @since 3/27/2016
  */
class GuiStorageCore(player: EntityPlayer, tile: TileStorageCore)
        extends GuiBase[ContainerStorageCore](new ContainerStorageCore(player.inventory, tile),
            250, 220, "inventory.storage.title") {
    override def addComponents(): Unit = {
        components += new GuiComponentScrollBar(227, 26, 108) {
            override def onScroll(position: Float): Unit = {}
        }
    }
}
