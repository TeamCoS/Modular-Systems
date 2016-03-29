package com.teambr.modularsystems.furnace.gui

import java.awt.Color

import com.teambr.bookshelf.client.gui.{GuiColor, GuiBase}
import com.teambr.bookshelf.client.gui.component.BaseComponent
import com.teambr.bookshelf.client.gui.component.display.{GuiComponentArrow, GuiComponentFlame, GuiComponentText, GuiTabCollection}
import com.teambr.modularsystems.furnace.container.ContainerFurnaceCore
import com.teambr.modularsystems.furnace.tiles.TileEntityFurnaceCore
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.util.text.translation.I18n

import scala.collection.mutable.ArrayBuffer

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
class GuiFurnaceCore(player: EntityPlayer, tile: TileEntityFurnaceCore) extends
        GuiBase[ContainerFurnaceCore](new ContainerFurnaceCore(player.inventory, tile), 175, 165, "inventory.furnace.title") {

    protected var core = tile
    protected val coreLocation = tile.getPos

    addRightTabs(rightTabs)

    override def drawGuiContainerBackgroundLayer(f: Float, i: Int, j:Int): Unit = {
        core = core.getWorld.getTileEntity(coreLocation).asInstanceOf[TileEntityFurnaceCore]
        super[GuiBase].drawGuiContainerBackgroundLayer(f, i, j)
    }

    override def addComponents(): Unit = {
        components += new GuiComponentFlame(81, 55) {
            override def getCurrentBurn: Int = if (core.isBurning) core.getBurnTimeRemainingScaled(14) else 0

            override def getDynamicToolTip(x: Int, y: Int): ArrayBuffer[String] = {
                ArrayBuffer(core.values.burnTime + " ticks left.")
            }
        }
        components += new GuiComponentArrow(79, 34) {
            override def getCurrentProgress: Int = core.getCookProgressScaled(24)
        }
    }

    override def addRightTabs(tabs: GuiTabCollection): Unit = {
        if (core != null) {
            var furnaceInfoSpeed = new ArrayBuffer[BaseComponent]()
            furnaceInfoSpeed += new GuiComponentText(GuiColor.ORANGE  + I18n.translateToLocal("inventory.info"), 26, 6)
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
            furnaceInfoSpeed += new GuiComponentText(f"$eff%.2f" + "%", 15, 58, if (eff > 0) new Color(92, 230, 46) else if (eff == 0) new Color(0, 0, 0) else new Color(230, 46, 0))
            val multi : Double = core.values.multiplicity + 1
            furnaceInfoSpeed += new GuiComponentText("Multiplicity: ", 5, 73, new Color(255, 255, 255))
            furnaceInfoSpeed += new GuiComponentText(f"$multi%.2f" + "x", 15, 83, if (core.values.multiplicity > 0) new Color(92, 230, 46) else new Color(0, 0, 0))
            tabs.addTab(furnaceInfoSpeed.toList, 95, 100, new Color(150, 112, 50), new ItemStack(Items.book))
        }
    }
}
