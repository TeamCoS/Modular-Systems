package com.teambr.modularsystems.storage.gui

import com.teambr.bookshelf.client.gui.GuiBase
import com.teambr.modularsystems.storage.container.ContainerStorageCore
import com.teambr.modularsystems.storage.tiles.TileStorageCore
import net.minecraft.entity.player.EntityPlayer

/**
 * Modular-Systems
 * Created by Dyonovan on 04/08/15
 */
class GuiStorageCore(player: EntityPlayer, val storageCore : TileStorageCore)
        extends GuiBase[ContainerStorageCore](new ContainerStorageCore(player.inventory, storageCore), 250, 220, storageCore.inventoryName) {

    override def addComponents(): Unit = {}
}
