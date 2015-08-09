package com.teambr.modularsystems.power.gui

import com.teambr.bookshelf.client.gui.GuiColor
import com.teambr.bookshelf.client.gui.component.display.{GuiComponentArrow, GuiComponentFluidTank}
import com.teambr.modularsystems.power.container.ContainerBankLiquids
import com.teambr.modularsystems.power.tiles.TileBankLiquids
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
 * @since August 08, 2015
 */
class GuiBankLiquids[C <: ContainerBankLiquids](container : C, player: EntityPlayer, tileEntity: TileBankLiquids)
        extends GuiPowerBase[ContainerBankLiquids](container, tileEntity, player.inventory, 175, 165, "inventory.liquidspower.title") {


    override def addComponents(): Unit = {
        if (tileEntity != null) {
            components += new GuiComponentFluidTank(80, 18, 75, 60, tileEntity.tank) {
                override def getDynamicToolTip(mouseX: Int, mouseY: Int): ArrayBuffer[String] = {
                    var tipLiquid = new ArrayBuffer[String]()
                    if (tileEntity.tank.getFluid != null)
                        tipLiquid += GuiColor.YELLOW + tileEntity.tank.getFluid.getLocalizedName
                    else tipLiquid += "Empty"
                    tipLiquid += tileEntity.tank.getFluidAmount + " / " + tileEntity.tank.getCapacity + "mb"
                    tipLiquid
                }
            }

            components += new GuiComponentArrow(48, 20) {
                override def getCurrentProgress: Int = 0
            }
        }
    }
}
