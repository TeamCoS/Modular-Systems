package com.teambr.modularsystems.power.gui

import com.teambr.modularsystems.power.container.ContainerSolidsBank
import com.teambr.modularsystems.power.tiles.TileBankSolids
import net.minecraft.entity.player.EntityPlayer

/**
 * This file was created for Modular-Systems
 *
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Dyonovan
 * @since August 07, 2015
 */
class GuiSolidsBank[C <: ContainerSolidsBank](container : C, player: EntityPlayer, tileEntity: TileBankSolids)
        extends GuiPowerBase[ContainerSolidsBank](container, tileEntity, player.inventory, 175, 165, "inventory.solidspower.title") {

    override def addComponents(): Unit = { }
}
