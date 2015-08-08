package com.teambr.modularsystems.power.gui

import java.awt.Color

import com.teambr.bookshelf.client.gui.GuiBase
import com.teambr.bookshelf.client.gui.component.BaseComponent
import com.teambr.bookshelf.client.gui.component.control.GuiComponentSetNumber
import com.teambr.bookshelf.client.gui.component.display.{GuiComponentText, GuiTabCollection}
import com.teambr.bookshelf.client.gui.component.listeners.IMouseEventListener
import com.teambr.bookshelf.network.PacketManager
import com.teambr.modularsystems.core.common.tiles.AbstractCore
import com.teambr.modularsystems.core.network.OpenContainerPacket
import com.teambr.modularsystems.power.tiles.TileBankBase
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.init.Blocks
import net.minecraft.inventory.Container
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
 * @since August 07, 2015
 */
abstract class GuiPowerBase[C <: Container](container : C, tile: TileBankBase, player: InventoryPlayer, width: Int, height: Int, name: String)
                                        extends GuiBase[C](container, width, height, name) {

    var core = tile.getCore.orNull
    addRightTabs(rightTabs)

    override def drawGuiContainerForegroundLayer(x: Int, y: Int): Unit = {
        super.drawGuiContainerForegroundLayer(x, y)

        if (tile.getCore.isDefined) {
            core = core.getWorld.getTileEntity(core.getPos).asInstanceOf[AbstractCore]
        }
    }

    override def addRightTabs(tabs: GuiTabCollection): Unit = {
        if (tile != null && core != null) {
            //Core Gui Tab
            val coreTab = new ArrayBuffer[BaseComponent]
            tabs.addTab(coreTab.toList, 95, 100, new Color(100, 150, 150), new ItemStack(Blocks.furnace))

            //Priority Tab
            var priorityTab = new ArrayBuffer[BaseComponent]
            priorityTab += new GuiComponentText("Fuel Priority", 22, 7)
            priorityTab += new GuiComponentSetNumber(26, 25, 40, tile.getPriority, 0, 100) {
                override def setValue(i: Int): Unit = {
                    tile.priority = i
                    PacketManager.updateTileWithClientInfo(tile.getWorld.getTileEntity(tile.getPos))
                }
            }
            tabs.addTab(priorityTab.toList, 95, 55, new Color(255, 68, 51), new ItemStack(Blocks.anvil))
            tabs.getTabs.head.setToolTip(ArrayBuffer("Core Gui"))
            tabs.getTabs(1).setToolTip(ArrayBuffer("Fuel Priority"))

            //Link Core tab to Core Gui
            tabs.getTabs.head.setMouseEventListener(new IMouseEventListener {
                override def onMouseDown(component: BaseComponent, mouseX: Int, mouseY: Int, button: Int): Unit = {
                    if (tile.getCore.isDefined) {
                        com.teambr.modularsystems.core.network.PacketManager.net.sendToServer(
                            new OpenContainerPacket(core.getPos))
                    }
                }

                override def onMouseDrag(component: BaseComponent, mouseX: Int, mouseY: Int, button: Int, time: Long): Unit = {}

                override def onMouseUp(component: BaseComponent, mouseX: Int, mouseY: Int, button: Int): Unit = {}
            })
        }
    }

}
