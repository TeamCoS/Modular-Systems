package com.teambr.modularsystems.storage.gui

import com.teambr.bookshelf.client.gui.GuiBase
import com.teambr.bookshelf.client.gui.component.display.GuiComponentArrow
import com.teambr.modularsystems.storage.container.ContainerStorageRemote
import com.teambr.modularsystems.storage.tiles.TileStorageRemote
import net.minecraft.entity.player.EntityPlayer

/**
 * This file was created for Modular-Systems
 *
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Dyonovan
 * @since August 10, 2015
 */
class GuiStorageRemote(player: EntityPlayer, tile: TileStorageRemote)
            extends GuiBase[ContainerStorageRemote](new ContainerStorageRemote(player.inventory, tile), 175, 165, "inventory.storageRemote.title"){

    override def addComponents(): Unit = {
        components += new GuiComponentArrow(79, 34) {
            override def getCurrentProgress: Int = 0
        }
    }
}
