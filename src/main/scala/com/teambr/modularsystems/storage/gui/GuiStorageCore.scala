package com.teambr.modularsystems.storage.gui

import java.awt.Color

import com.teambr.bookshelf.client.gui.{GuiTextFormat, GuiColor, GuiBase}
import com.teambr.bookshelf.client.gui.component.BaseComponent
import com.teambr.bookshelf.client.gui.component.control.{GuiComponentTextBox, GuiComponentScrollBar}
import com.teambr.bookshelf.client.gui.component.display.{GuiComponentText, GuiTabCollection}
import com.teambr.bookshelf.helper.LogHelper
import com.teambr.modularsystems.core.network.PacketManager
import com.teambr.modularsystems.core.utils.ClientUtils
import com.teambr.modularsystems.storage.container.ContainerStorageCore
import com.teambr.modularsystems.storage.network.{UpdateFilterString, ScrollStorageCore}
import com.teambr.modularsystems.storage.tiles.TileStorageCore
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.util.text.translation.I18n
import org.lwjgl.input.{Keyboard, Mouse}

import scala.collection.mutable.ArrayBuffer

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
            250, 240, "inventory.storage.title") {

    if(tile.hasSearchUpgrade)
        components(0).setXPos(10)

    lazy val ourRender = new RenderItemLarge(Minecraft.getMinecraft.getTextureManager,
        Minecraft.getMinecraft.getRenderItem.getItemModelMesher.getModelManager,
        Minecraft.getMinecraft.getItemColors)

    var currentScroll = 0.0F

    var updateBar = false
    var TEXT_BOX : Int = 2

    override def setWorldAndResolution(mc: Minecraft, width: Int, height: Int): Unit = {
        super.setWorldAndResolution(mc, width, height)
        itemRender = ourRender
    }

    override def updateScreen(): Unit = {
        inventory.detectAndSendChanges()
        super.updateScreen()
    }

    override def handleMouseInput(): Unit = {
        super.handleMouseInput()
        var scrollDirection = Mouse.getEventDWheel

        if(scrollDirection != 0 && inventory.storageCore.getInventory.size() / 11 >= inventory.rowCount) {

            val rowsAboveDisplay = (inventory.storageCore.getInventory.size() / 11) - inventory.rowCount
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

        components(TEXT_BOX) match {
            case textBox : GuiComponentTextBox =>
                if (textBox != null && textBox.getTextField != null && textBox.getTextField.isFocused)
                    Keyboard.enableRepeatEvents (true)
                else
                    Keyboard.enableRepeatEvents (false)
            case _ => LogHelper.debug("This is not the text box")
        }
    }

    override def keyTyped(typedChar: Char, keyCode: Int): Unit = {
        if (keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode) {
            if(!Keyboard.areRepeatEventsEnabled())
                super.keyTyped(typedChar, keyCode)
            else {
                components(TEXT_BOX) match {
                    case textBox : GuiComponentTextBox =>
                        textBox.keyTyped(typedChar, keyCode)
                    case _ => LogHelper.debug("This is not the text box")
                }
            }
        } else
            super.keyTyped(typedChar, keyCode)
    }

    override def addComponents(): Unit = {
        // Scroll Bar
        components += new GuiComponentScrollBar(227, 26, 18 * inventory.rowCount) {
            override def onScroll(position: Float): Unit = {
                inventory.scrollTo(position)
                PacketManager.net.sendToServer(new ScrollStorageCore(currentScroll))
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

        // Search Bar
        if(tile != null && tile.hasSearchUpgrade) {
            components += new GuiComponentTextBox(132, 7, 108, 16) {
                override def fieldUpdated(value: String): Unit = {
                    inventory.filterString = value
                    PacketManager.net.sendToServer(new UpdateFilterString(value))
                }
            }
        }
    }

    /**
      * Adds the tabs to the right. Overwrite this if you want tabs on your GUI
      *
      * @param tabs List of tabs, put GuiTabs in
      */
    override def addRightTabs(tabs: GuiTabCollection) : Unit = {
        if(tile != null) {
            var infoTab = new ArrayBuffer[BaseComponent]()
            // Information
            infoTab += new GuiComponentText(GuiColor.ORANGE  + I18n.translateToLocal("inventory.info"), 26, 6)

            // Quantity
            infoTab += new GuiComponentText(
                GuiColor.YELLOW + "" + GuiTextFormat.BOLD + I18n.translateToLocal("inventory.storageCore.quantity"),
                5, 23)
            infoTab += new GuiComponentText(GuiColor.WHITE + ClientUtils.formatNumber(tile.getCachedSize), 15, 33) {
                override def render(x : Int, y : Int, mx : Int, my : Int): Unit = {
                    setText(GuiColor.WHITE + ClientUtils.formatNumber(tile.getCachedSize))
                    super.render(x, y, mx, my)
                }
            }

            // Capacity
            infoTab += new GuiComponentText(
                GuiColor.YELLOW + "" + GuiTextFormat.BOLD + I18n.translateToLocal("inventory.storageCore.capacity"),
                5, 48)
            infoTab += new GuiComponentText(GuiColor.WHITE + ClientUtils.formatNumber(tile.getMaxItems), 15, 58) {
                override def render(x : Int, y : Int, mx : Int, my : Int): Unit = {
                    setText(GuiColor.WHITE + ClientUtils.formatNumber(tile.getMaxItems))
                    super.render(x, y, mx, my)
                }
            }

            tabs.addTab(infoTab.toList, 100, 100, new Color(150, 112, 50), new ItemStack(Items.book))
        }
    }
}
