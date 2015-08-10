package com.teambr.modularsystems.power.gui

import java.awt.Color

import com.teambr.bookshelf.client.gui.component.display.GuiComponentPowerBar
import com.teambr.bookshelf.common.container.ContainerGeneric
import com.teambr.modularsystems.power.tiles.TileBankRF
import net.minecraft.entity.player.EntityPlayer

import scala.collection.mutable.ArrayBuffer

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
class GuiBankRF[C <: ContainerGeneric](container: C, player: EntityPlayer, tileEntity: TileBankRF)
        extends GuiPowerBase[ContainerGeneric](container, tileEntity, player.inventory, 140, 120, "inventory.rfpower.title") {

    override def addComponents(): Unit = {
        if (tileEntity != null) {
            components += new GuiComponentPowerBar(56, 23, 18, 74, new Color(255, 0, 0)) {
                override def getEnergyPercent(scale: Int): Int = {
                    tileEntity.getEnergyStored(null) * scale / tileEntity.getMaxEnergyStored(null)
                }

                override def getDynamicToolTip(x: Int, y: Int): ArrayBuffer[String] = {
                    ArrayBuffer(tileEntity.getEnergyStored(null) + " / " + tileEntity.getMaxEnergyStored(null))
                }
            }
        }
    }
}
