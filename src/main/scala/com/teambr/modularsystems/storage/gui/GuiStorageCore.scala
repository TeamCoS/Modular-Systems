package com.teambr.modularsystems.storage.gui

import java.awt.Color

import com.teambr.bookshelf.client.gui.GuiBase
import com.teambr.bookshelf.client.gui.component.BaseComponent
import com.teambr.bookshelf.client.gui.component.control.{ GuiComponentButton, GuiComponentScrollBar, GuiComponentTextBox, GuiComponentTexturedButton }
import com.teambr.bookshelf.client.gui.component.display.{ GuiComponentText, GuiTabCollection }
import com.teambr.bookshelf.helper.GuiHelper
import com.teambr.bookshelf.network.PacketManager
import com.teambr.modularsystems.core.ModularSystems
import com.teambr.modularsystems.storage.container.ContainerStorageCore
import com.teambr.modularsystems.storage.tiles.TileStorageCore
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.util.StatCollector
import org.lwjgl.input.{ Keyboard, Mouse }

import scala.collection.mutable.ArrayBuffer

/**
 * Modular-Systems
 * Created by Dyonovan on 04/08/15
 */
class GuiStorageCore(player: EntityPlayer, val storageCore : TileStorageCore)
        extends GuiBase[ContainerStorageCore](new ContainerStorageCore(player.inventory, storageCore), 250, 220, storageCore.inventoryName) {

    var isInMainArea = false
    var currentScroll = 0.0F

    var textBox : GuiComponentTextBox = null
    var scrollBar : GuiComponentScrollBar = null

    title.setXPos(8)
    addComponents()

    override def addComponents(): Unit = {
        if(storageCore != null) {

            textBox = new GuiComponentTextBox(95, 5, 150, 16) {
                def fieldUpdated(value : String) {
                    inventory.keyTyped(value)
                }
            }
            textBox.getTextField.setVisible(storageCore.hasSearchUpgrade)
            components += textBox

            scrollBar = new GuiComponentScrollBar(227, 26, 108) {
                def onScroll(position : Float) {
                    inventory.scrollTo(position)
                    currentScroll = position
                }
            }
            components += scrollBar

            if(storageCore.hasSortUpgrade) {
                components += new GuiComponentButton(3, 26, 20, 20, "S") {
                    override def doAction() : Unit = {
                        storageCore.sendValueToServer(0, 0)
                    }
                    override def getDynamicToolTip(mouseX : Int, mouseY : Int) : ArrayBuffer[String] =
                        ArrayBuffer(StatCollector.translateToLocal("inventory.storageCore.sort"))
                }
            }

            if(storageCore.hasCraftingUpgrade) {
                components += new GuiComponentTexturedButton(228, 147, 76, 247, 9, 9, 11, 11) {
                    override def doAction() : Unit = {
                        clearCraftingGrid()
                    }
                    override def getDynamicToolTip(mouseX : Int, mouseY : Int) : ArrayBuffer[String] =
                        ArrayBuffer(StatCollector.translateToLocal("inventory.storageCore.clear"))
                }
            }
        }
    }

    def clearCraftingGrid() : Unit = {
        inventory.clearCraftingGrid()
        PacketManager.updateTileWithClientInfo(storageCore)
    }

    protected override def keyTyped(letter : Char, keyCode : Int) {
        if (textBox != null && textBox.getTextField != null && !textBox.getTextField.textboxKeyTyped(letter, keyCode)) {
            super.keyTyped(letter, keyCode)
        }
        else if (textBox != null && textBox.getTextField != null) {
            this.inventory.keyTyped(textBox.getValue)
        }
        else super.keyTyped(letter, keyCode)
    }

    protected override def mouseClicked(par1 : Int, par2 : Int, par3 : Int) {
        super.mouseClicked(par1, par2, par3)
        if (par3 == 2 && storageCore != null && storageCore.hasSortUpgrade) {
            storageCore.sendValueToServer(0, 0)
            GuiHelper.playButtonSound
        }
    }

    override def handleMouseInput() {
        super.handleMouseInput()
        var scrollDirection : Int = Mouse.getEventDWheel
        if (scrollDirection != 0 && inventory.storageCore.getInventoryRowCount > 6) {
            //if (isInMainArea) return
            val rowsAboveDisplay : Int = this.inventory.storageCore.getInventoryRowCount - 6
            if (scrollDirection > 0) scrollDirection = 1
            else scrollDirection = -1
            this.currentScroll = (this.currentScroll.toDouble - scrollDirection.toDouble / rowsAboveDisplay.toDouble).toFloat
            if (currentScroll < 0.0F) currentScroll = 0.0F
            else if (currentScroll > 1.0F) currentScroll = 1.0F
            this.inventory.scrollTo(this.currentScroll)
            this.scrollBar.setPosition(this.currentScroll)
            updateScreen()
        }
        if (textBox != null && textBox.getTextField != null && textBox.getTextField.isFocused) Keyboard.enableRepeatEvents(true)
        else Keyboard.enableRepeatEvents(false)
    }

    override def drawScreen(x : Int, y : Int, f : Float) {
        super.drawScreen(x, y, f)
        isInMainArea = GuiHelper.isInBounds(x, y, guiLeft + 35, guiTop + 17, guiLeft + 220, guiTop + 215)
    }

    override def addRightTabs (tabs : GuiTabCollection) : Unit = {
        if (storageCore != null) {
            val children  = List[BaseComponent](
                new GuiComponentText("Information", 26, 6, new Color(255, 204, 0)),
                new GuiComponentText("Size: ", 10, 23, new Color(255, 255, 255)),
                new GuiComponentText(String.valueOf(storageCore.getSizeInventory()) + " Slots", 20, 33, new Color(119, 119, 199)))
            tabs.addTab(children, 100, 100, new Color(77, 75, 196), new ItemStack(Items.book, 1))
        }
    }
}
