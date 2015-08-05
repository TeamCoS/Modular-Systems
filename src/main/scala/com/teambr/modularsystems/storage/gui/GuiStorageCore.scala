package com.teambr.modularsystems.storage.gui

import com.teambr.bookshelf.client.gui.GuiBase
import com.teambr.bookshelf.common.container.Inventory
import com.teambr.modularsystems.storage.container.ContainerStorageCore
import net.minecraft.entity.player.EntityPlayer

/**
 * Modular-Systems
 * Created by Dyonovan on 04/08/15
 */
class GuiStorageCore(player: EntityPlayer)
        extends GuiBase[ContainerStorageCore](new ContainerStorageCore(player.inventory,
            new Inventory("inventory", false, 24)), 250, 220, "title.dunno") {

    override def addComponents(): Unit = ???
}
