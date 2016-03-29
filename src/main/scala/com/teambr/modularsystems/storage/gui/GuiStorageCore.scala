package com.teambr.modularsystems.storage.gui

import com.teambr.bookshelf.client.gui.GuiBase
import com.teambr.bookshelf.client.gui.component.control.GuiComponentScrollBar
import com.teambr.modularsystems.core.network.PacketManager
import com.teambr.modularsystems.storage.container.ContainerStorageCore
import com.teambr.modularsystems.storage.network.ScrollStorageCore
import com.teambr.modularsystems.storage.tiles.TileStorageCore
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import org.lwjgl.input.Mouse

/**
  * This file was created for Modular-Systems
  *
  * Modular-Systems is licensed under the
  * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
  * http://creativecommons.org/licenses/by-nc-sa/4.0/
  *
  * @author Paul Davis "pauljoda"
  * @since 3/27/2016
  */
class GuiStorageCore(player: EntityPlayer, tile: TileStorageCore)
        extends GuiBase[ContainerStorageCore](new ContainerStorageCore(player.inventory, tile),
            250, 220, "inventory.storage.title") {

    lazy val ourRender = new RenderItemLarge(Minecraft.getMinecraft.getTextureManager,
        Minecraft.getMinecraft.getRenderItem.getItemModelMesher.getModelManager,
        Minecraft.getMinecraft.getItemColors)

    var currentScroll = 0.0F

    var updateBar = false

    override def setWorldAndResolution(mc: Minecraft, width: Int, height: Int): Unit = {
        super.setWorldAndResolution(mc, width, height)
        itemRender = ourRender
    }

    override def updateScreen(): Unit = {
        inventory.updateSlots()
        super.updateScreen()
    }

    override def handleMouseInput(): Unit = {
        super.handleMouseInput()
        var scrollDirection = Mouse.getEventDWheel

        if(scrollDirection != 0 && inventory.storageCore.getInventory.size() / 11 > 6) {

            val rowsAboveDisplay = (inventory.storageCore.getInventory.size() / 11) - 6
            if(scrollDirection > 0)
                scrollDirection = 1
            else
                scrollDirection = -1

            currentScroll = (this.currentScroll.toDouble - (scrollDirection.toDouble / rowsAboveDisplay.toDouble)).toFloat

            if(currentScroll < 0.0F)
                currentScroll = 0.0F
            else if(currentScroll > 1.0F)
                currentScroll = 1.0F

            this.inventory.scrollTo(currentScroll)
            PacketManager.net.sendToServer(new ScrollStorageCore(currentScroll))
            updateBar = true
        }
    }

    override def addComponents(): Unit = {
        components +=  new GuiComponentScrollBar(227, 26, 108) {
            override def onScroll(position: Float): Unit = {
                inventory.scrollTo(position)
                currentScroll = position
            }

            override def render(guiLeft: Int, guiTop: Int, mouseX : Int, mouseY : Int): Unit = {
                if(updateBar) {
                    setPosition(currentScroll)
                    updateBar = false
                }
                super.render(guiLeft, guiTop, mouseX, mouseY)
            }
        }
    }
}
