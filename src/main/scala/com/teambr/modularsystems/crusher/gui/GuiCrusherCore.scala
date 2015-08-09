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
            furnaceInfoSpeed += new GuiComponentText("Information", 26, 6, 0xFFCC00)
            furnaceInfoSpeed += new GuiComponentText("Speed: ", 5, 23, 0xFFFFFF)
            val speed: Double = {
                if ((-1 * (((core.values.speed + 200) / 200) - 1)) != 0) {
                    (-1 * (((core.values.speed + 200) / 200) - 1)) * 100
                } else
                    0.00
            }
            furnaceInfoSpeed += new GuiComponentText(f"$speed%.2f" + "%", 15, 33, if (speed > 0) 0x5CE62E else if (speed == 0) 0x000000 else 0xE62E00)
            furnaceInfoSpeed += new GuiComponentText("Efficiency: ", 5, 48, 0xFFFFFF)
            val eff: Double = {
                if (-1 * (100 - ((1600 + core.values.efficiency) / 1600) * 100) != 0)
                    -1 * (100 - ((1600 + core.values.efficiency) / 1600) * 100)
                else
                    0.00

            }
            furnaceInfoSpeed += new GuiComponentText(f"$eff%.2f" + "%", 15, 58, if (eff > 0) 0x5CE62E else if (eff == 0) 0x000000 else 0xE62E00)
            furnaceInfoSpeed += new GuiComponentText("Multiplicity: ", 5, 73, 0xFFFFFF)
            furnaceInfoSpeed += new GuiComponentText((core.values.multiplicity + 1) + "x", 15, 83, if (core.values.multiplicity > 0) 0x5CE62E else 0x000000)
            furnaceInfoSpeed += new GuiComponentText("Secondary", 5, 98, 0xFFFFFF)
            furnaceInfoSpeed += new GuiComponentText("Output Chance:", 5, 108, 0xFFFFFF)
            furnaceInfoSpeed += new GuiComponentText(core.getCrusherExpansionCount + "%", 15, 118, if (core.getCrusherExpansionCount > 0) 0x5CE62E else 0x000000)
            tabs.addTab(furnaceInfoSpeed.toList, 95, 135, new Color(150, 112, 50), new ItemStack(Items.book))
        }
    }
}
