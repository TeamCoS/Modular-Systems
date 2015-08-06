package com.teambr.modularsystems.storage.tiles

import java.util.Collections

import com.teambr.bookshelf.common.tiles.traits.{OpensGui, Inventory, UpdatingTile}
import com.teambr.modularsystems.core.collections.ItemSorter
import com.teambr.modularsystems.storage.container.ContainerStorageCore
import com.teambr.modularsystems.storage.gui.GuiStorageCore
import com.teambr.modularsystems.storage.network.StorageNetwork
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.{ NBTTagCompound, NBTTagList }
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import net.minecraftforge.common.util.Constants

import scala.collection.mutable.ArrayBuffer

/**
 * Modular-Systems
 * Created by Dyonovan on 04/08/15
 */
class TileStorageCore extends TileEntity with Inventory with UpdatingTile with OpensGui {

    val craftingInventory = new Inventory {
        override def hasCustomName(): Boolean = true
        override var inventoryName: String = "inventory.storageCore.crafting"
        override def initialSize: Int = 9
    }

    var network = new StorageNetwork

    var ownerName : String = _
    var allowedPlayers = ArrayBuffer[String]()

    var isSecured = false
    var hasSearchUpgrade = false
    var hasSortUpgrade = false
    var hasCraftingUpgrade = false

    /**
     * Used to get how many rows this has in the GUI
     * @return The number of rows in the container
     */
    def getInventoryRowCount : Int = (inventoryContents.size / 11) + (if (inventoryContents.size % 11 > 0) 1 else 0)

    /**
     * Used to remove a node from the network
     * @param node The node to remove
     * @return True if found and removed
     */
    def deleteFromNetwork(node: TileEntityStorageExpansion) : Boolean = network.deleteNode(node)

    /**
     * Used to get the storage network
     * @return The network of this tile
     */
    def getNetwork : StorageNetwork = network

    /**
     * Used to destroy the network
     */
    def destroyNetwork() {
        network.destroyNetwork(worldObj)
        network = null
    }

    def sortInventory : Boolean = {
        var madeChange : Boolean = false
        val oldInv : java.util.Stack[ItemStack] = new java.util.Stack[ItemStack]
        import scala.collection.JavaConversions._
        for (stack <- inventoryContents) {
            if (stack != null) oldInv.push(stack)
            else oldInv.push(null)
        }
        Collections.sort(inventoryContents, new ItemSorter)
        checkAndMerge()
        var timeOut : Int = 100
        while (!compareInventories(oldInv, inventoryContents) && timeOut > 0) {
            madeChange = true
            Collections.sort(inventoryContents, new ItemSorter)
            checkAndMerge()
            timeOut -= 1
        }
        madeChange
    }

    private def compareInventories (stack1 : java.util.Stack[ItemStack], stack2 : java.util.Stack[ItemStack]) : Boolean = {
        for(i <- 0 until stack1.size()) {
            if(stack1.size <= i || stack2.size <= i)
                return false
            else {
                if (stack1.get(i) != null && stack2.get(i) == null)
                    return false
                else if (stack1.get(i) == null && stack2.get(i) != null)
                    return false
                else if(!ItemStack.areItemStacksEqual(stack1.get(i), stack2.get(i)) && !ItemStack.areItemStackTagsEqual(stack1.get(i), stack2.get(i)))
                    return false
            }
        }
        true
    }

    /*******************************************************************************************************************
      ******************************************* Expansion Methods *****************************************************
      *******************************************************************************************************************/

    /**
     * Used to check if the player opening is the owner
     * @param player The player trying to open
     * @return True if you can open this
     */
    def canOpen(player : EntityPlayer) : Boolean =
        (ownerName == null || ownerName.equalsIgnoreCase("") || !isSecured) || ((player.getDisplayName.getUnformattedTextForChat == this.ownerName) || isInAllowPlayers(player.getDisplayName.getUnformattedTextForChat) || player.capabilities.isCreativeMode)

    /**
     * Sets the owner to what opened
     * @param player The new owner
     */
    def setOwner(player : EntityPlayer) : Unit = this.ownerName = player.getDisplayName.getUnformattedTextForChat

    /**
     * Checks if this player is allowed to open it, though he is not the owner
     * @param name The name of the player
     * @return True if allowed
     */
    def isInAllowPlayers(name : String) : Boolean = {
        for (playerID : String <- allowedPlayers) if (playerID == name) return true
        false
    }

    /**
     * Used to add a player to the list of allowed player
     * @param player The player to add
     */
    def addPlayerToAllowedPlayers(player : EntityPlayer) : Unit =
        allowedPlayers += player.getDisplayName.getUnformattedTextForChat

    /**
     * Used to add a player to the list of allowed player, by name
     * @param player The player to add
     */
    def addPlayerToAllowedPlayers(player : String) : Unit = allowedPlayers += player



    /*******************************************************************************************************************
      *********************************************** Tile Methods ******************************************************
      *******************************************************************************************************************/

    /**
     * Used to write data to the tag
     * @param tag The tag to write to
     */
    override def writeToNBT(tag : NBTTagCompound) : Unit = {
        super[TileEntity].writeToNBT(tag)

        craftingInventory.writeToNBT(tag, "CRAFTING")

        tag.setString("CustomName", inventoryName)
        tag.setString("Owner", if(ownerName != null && ownerName.length > 1) ownerName else "NULL")

        tag.setBoolean("Search", hasSearchUpgrade)
        tag.setBoolean("Sorting", hasSortUpgrade)
        tag.setBoolean("Crafting", hasCraftingUpgrade)

        super[Inventory].writeToNBT(tag)

        tag.setBoolean("Secured", isSecured)
        val securityList = new NBTTagList
        for(i <- allowedPlayers.indices) {
            val player = allowedPlayers(i)
            if(player != null) {
                val tagz = new NBTTagCompound
                tagz.setString("AllowedPlayer:" + i, player)
                securityList.appendTag(tagz)
            }
        }
        tag.setTag("SecurityList", securityList)
    }

    /**
     * Used to read data from the tag
     * @param tag The tag to read from
     */
    override def readFromNBT(tag : NBTTagCompound) : Unit = {
        super[TileEntity].readFromNBT(tag)

        craftingInventory.readFromNBT(tag, "CRAFTING")

        inventoryName = tag.getString("CustomName")
        ownerName = tag.getString("Owner")

        hasSearchUpgrade = tag.getBoolean("Search")
        hasSortUpgrade = tag.getBoolean("Sorting")
        hasCraftingUpgrade = tag.getBoolean("Crafting")

        super[Inventory].readFromNBT(tag)

        isSecured = tag.getBoolean("Secured")
        val securityList = tag.getTagList("SecurityList", Constants.NBT.TAG_COMPOUND)
        for(i <- 0 until securityList.tagCount()) {
            val tagz = securityList.getCompoundTagAt(i)
            allowedPlayers += tagz.getString("AllowedPlayer:" + i)
        }
    }

    override def onClientTick(): Unit = {
    }

    override def onServerTick(): Unit = {

    }

    /**
     * Since for some reason Minecraft thought it would be a good idea to name these the same for different
     * classes
     */
    override def markDirty() : Unit = {
        super[TileEntity].markDirty()
        super[Inventory].markDirty()
    }

    /*******************************************************************************************************************
      ***************************************** IInventory Methods ******************************************************
      *******************************************************************************************************************/

    private def checkAndMerge() : Unit = {
        for(i <- 0 until inventoryContents.size()) {
            if(getStackInSlot(i) != null) {
                if(getStackInSlot(i).stackSize <= 0) {
                    setInventorySlotContents(i, null)
                }
            }
        }

        for(i <- 0 until inventoryContents.size - 1) {
            val stack1 = getStackInSlot(i)
            val stack2 = getStackInSlot(i + 1)

            if (stack1 != null && stack2 != null &&
                    stack1.getItem == stack2.getItem &&
                    stack1.getItemDamage == stack2.getItemDamage &&
                    stack1.getTagCompound == stack2.getTagCompound) {
                if (stack1.stackSize < stack1.getMaxStackSize) {
                    val maxMerge : Int = stack1.getMaxStackSize - stack1.stackSize
                    val actualMerge : Int = if (stack2.stackSize >= maxMerge) maxMerge else stack2.stackSize
                    stack1.stackSize += actualMerge
                    stack2.stackSize -= actualMerge
                    if (stack2.stackSize <= 0) setInventorySlotContents(i + 1, null)
                }
            }
        }
    }

    override var inventoryName: String = "inventory.storageCore.title"

    override def hasCustomName(): Boolean = true

    override def initialSize: Int = 66

    override def getServerGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Option[ContainerStorageCore] = {
        if (canOpen(player)) Some(new ContainerStorageCore(player.inventory, this))
        else None
    }

    override def getClientGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Option[GuiStorageCore] = {
        if (canOpen(player)) Some(new GuiStorageCore(player, this))
        else None
    }
}
