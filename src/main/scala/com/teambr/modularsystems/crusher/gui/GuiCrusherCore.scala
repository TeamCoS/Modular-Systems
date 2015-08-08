package com.teambr.modularsystems.crusher.gui

import com.teambr.bookshelf.client.gui.GuiBase
import com.teambr.modularsystems.crusher.container.ContainerCrusherCore
import com.teambr.modularsystems.crusher.tiles.TileCrusherCore
import net.minecraft.entity.player.EntityPlayer

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
class GuiCrusherCore(player: EntityPlayer, tile: TileCrusherCore)
        extends GuiBase[ContainerCrusherCore](new ContainerCrusherCore(player.inventory, tile), 175, 165, "inventory.crusher.title"){
    override def addComponents(): Unit = ???
}
