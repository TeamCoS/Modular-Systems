package com.teambr.modularsystems.power.gui

import com.teambr.modularsystems.power.container.ContainerSolidsBank
import com.teambr.modularsystems.power.tiles.TileBankSolids
import net.minecraft.entity.player.InventoryPlayer

/**
 * This file was created for NeoTech
 *
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Dyonovan
 * @since August 07, 2015
 */
class GuiSolidsBank(player: InventoryPlayer, tileEntity: TileBankSolids)
        extends GuiPowerBase[ContainerSolidsBank](tileEntity, player, 175, 165, "inventory.solidspower.title") {

    override def addComponents(): Unit = { }
}
