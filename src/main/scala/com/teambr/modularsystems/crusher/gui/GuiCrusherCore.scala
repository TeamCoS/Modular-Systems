package com.teambr.modularsystems.crusher.gui

import java.awt.Color

import com.teambr.bookshelf.client.gui.GuiBase
import com.teambr.bookshelf.client.gui.component.BaseComponent
import com.teambr.bookshelf.client.gui.component.display.{GuiComponentText, GuiTabCollection, GuiComponentArrow, GuiComponentFlame}
import com.teambr.modularsystems.crusher.container.ContainerCrusherCore
import com.teambr.modularsystems.crusher.tiles.TileCrusherCore
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.ItemStack

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
class GuiCrusherCore(player: EntityPlayer, tile: TileCrusherCore)
        extends GuiBase[ContainerCrusherCore](new ContainerCrusherCore(player.inventory, tile), 175, 165, "inventory.crusher.title"){

    protected var core = tile
    protected val coreLocation = tile.getPos
    addRightTabs(rightTabs)

    override def drawGuiContainerBackgroundLayer(f: Float, i: Int, j:Int): Unit = {
        core = core.getWorld.getTileEntity(coreLocation).asInstanceOf[TileCrusherCore]
        super[GuiBase].drawGuiContainerBackgroundLayer(f, i, j)
    }

    override def addComponents(): Unit = {
        components += new GuiComponentFlame(61, 55) {
            override def getCurrentBurn: Int = if (core.isBurning) core.getBurnTimeRemainingScaled(14) else 0

            override def getDynamicToolTip(x: Int, y: Int): ArrayBuffer[String] = {
                ArrayBuffer(core.values.burnTime + " ticks left.")
            }
        }
        components += new GuiComponentArrow(59, 34) {
            override def getCurrentProgress: Int = core.getCookProgressScaled(24)
        }
    }

    override def addRightTabs(tabs: GuiTabCollection): Unit = {
        if (core != null) {
            var furnaceInfoSpeed = new ArrayBuffer[BaseComponent]()
            furnaceInfoSpeed += new GuiComponentText("Information", 26, 6, new Color(255, 204, 0))
            furnaceInfoSpeed += new GuiComponentText("Speed: ", 5, 23, new Color(255, 255, 255))
            val speed: Double = {
                if ((-1 * (((core.values.speed + 200) / 200) - 1)) != 0) {
                    (-1 * (((core.values.speed + 200) / 200) - 1)) * 100
                } else
                    0.00
            }
            furnaceInfoSpeed += new GuiComponentText(f"$speed%.2f" + "%", 15, 33, if (speed > 0) new Color(92, 230, 46) else if (speed == 0) new Color(0, 0, 0) else new Color(230, 46, 0))
            furnaceInfoSpeed += new GuiComponentText("Efficiency: ", 5, 48, new Color(255, 255, 255))
            val eff: Double = {
                if (-1 * (100 - ((1600 + core.values.efficiency) / 1600) * 100) != 0)
                    -1 * (100 - ((1600 + core.values.efficiency) / 1600) * 100)
                else
                    0.00

            }
            val multi: Double = core.values.multiplicity + 1
            furnaceInfoSpeed += new GuiComponentText(f"$eff%.2f" + "%", 15, 58, if (eff > 0) new Color(92, 230, 46) else if (eff == 0) new Color(0, 0, 0) else new Color(230, 46, 0))
            furnaceInfoSpeed += new GuiComponentText("Multiplicity: ", 5, 73, new Color(255, 255, 255))
            furnaceInfoSpeed += new GuiComponentText(f"$multi%.2f"+ "x", 15, 83, if (core.values.multiplicity > 0) new Color(92, 230, 46) else new Color(0, 0, 0))
            furnaceInfoSpeed += new GuiComponentText("Secondary", 5, 98, new Color(255, 255, 255))
            furnaceInfoSpeed += new GuiComponentText("Output Chance:", 5, 108, new Color(255, 255, 255))
            furnaceInfoSpeed += new GuiComponentText(core.getCrusherExtraCount + "%", 15, 118, if (core.getCrusherExtraCount > 0) new Color(92, 230, 46) else new Color(0, 0, 0))
            tabs.addTab(furnaceInfoSpeed.toList, 95, 135, new Color(150, 112, 50), new ItemStack(Items.BOOK))
        }
    }
}
