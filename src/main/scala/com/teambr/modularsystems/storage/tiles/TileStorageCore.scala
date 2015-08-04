package com.teambr.modularsystems.storage.tiles

import com.teambr.modularsystems.storage.network.StorageNetwork
import com.teambr.modularsystems.temp.collections.InventoryTile
import com.teambr.modularsystems.temp.tiles.{BaseTile, TileEntityStorageExpansion}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.{IChatComponent, StatCollector}

import scala.collection.mutable

/**
 * Modular-Systems
 * Created by Dyonovan on 02/08/15
 */

class TileStorageCore extends BaseTile with IInventory {

    var customName = StatCollector.translateToLocal("inventory.storageCore.title")

    protected[storage] var inventory = new InventoryTile(66)
    protected[storage] var craftingGrid = new InventoryTile(9)
    protected[storage] var network = new StorageNetwork

    protected[storage] var ownerName : String
    protected[storage] var isSecured : Boolean
    protected[storage] var allowedPlayers : List[String]

    protected[storage] var hasSearchUpgrade : Boolean
    protected[storage] var hasSortingUpgrade : Boolean
    protected[storage] var hasCraftingUpgrade : Boolean

    /**
     * Used to get how many rows this has in the GUI
     * @return The number of rows in the container
     */
    def getInventoryRowCount : Int = {
        (inventory.getSizeInventory / 11) + (if (inventory.getSizeInventory % 11 > 0) 1 else 0)
    }

    /**
     * Used to remove a node from the network
     * @param node The node to remove
     * @return True if found and removed
     */
    def DeleteFromNetwork(node : TileEntityStorageExpansion) : Boolean = {
        false
    }

    /**
     * Used to get the storage network
     * @return The network of this tile
     */
    def getNetwork : StorageNetwork = {
        network
    }

    /**
     * Used to destroy the network
     */
    def destroyNetwork() {
        network.destroyNetwork(worldObj)
        network = null
    }

    /**
     * Used to get the inventory
     * @return The inventory stack
     */
    def getInventory: mutable.Stack[ItemStack] = {
        inventory.getValues
    }

    /**
     * Used to add inventory space
     * @param count How many slots to add
     */
    def pushNewInventory(count: Int) {
            for (i <- 0 to (count - 1))
                inventory.push(null)
    }

    /**
     * Used to remove inventory space
     * @param count How many slots to remove
     * @return The stacks that were in those top slots
     */
    def popInventory(count: Int): mutable.MutableList[ItemStack] = {
        val stacks = new mutable.MutableList[ItemStack]
        for (i <- 0 to (count - 1))
            stacks :+ inventory.pop
        stacks
    }

    def sortInventory: Boolean = {
        var madeChange = false

        var oldInv = new mutable.Stack[ItemStack]()
        for (stack <- inventory.getValues) {
            stack != null match {
                case true => oldInv.push(stack)
                case _ => oldInv.push(null)
            }
        }
        inventory.getValues.sortBy(r => ItemStack)

        checkAndMerge()

        var timeOut = 100
        while (!compareInventories(oldInv, getInventory) && timeOut > 0) {
            madeChange = true

        }



    }

    /********************************************************************************************************************
    ******************************************* IInventory Methods ******************************************************
    *********************************************************************************************************************/


    override def decrStackSize(index: Int, count: Int): ItemStack = ???

    override def closeInventory(player: EntityPlayer): Unit = ???

    override def getSizeInventory: Int = ???

    override def getInventoryStackLimit: Int = ???

    override def clear(): Unit = ???

    override def isItemValidForSlot(index: Int, stack: ItemStack): Boolean = ???

    override def getStackInSlotOnClosing(index: Int): ItemStack = ???

    override def openInventory(player: EntityPlayer): Unit = ???

    override def getFieldCount: Int = ???

    override def getField(id: Int): Int = ???

    override def setInventorySlotContents(index: Int, stack: ItemStack): Unit = ???

    override def isUseableByPlayer(player: EntityPlayer): Boolean = ???

    override def getStackInSlot(index: Int): ItemStack = ???

    override def setField(id: Int, value: Int): Unit = ???

    override def getDisplayName: IChatComponent = ???

    override def getName: String = ???

    override def hasCustomName: Boolean = ???
}
