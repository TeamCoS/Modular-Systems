package com.pauljoda.modularsystems.storage.items

import java.util

import cofh.api.energy.IEnergyContainerItem
import com.pauljoda.modularsystems.core.items.BaseItem
import com.pauljoda.modularsystems.storage.tiles.TileStorageRemote
import com.teambr.bookshelf.Bookshelf
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.Vec3
import net.minecraft.world.{IBlockAccess, World}

/**
 * Modular-Systems
 * Created by Dyonovan on 02/08/15
 */

object ItemStorageRemoteTest {
    val CAPACITY: Int = 10000
    val MAX_IN_OUT: Int = 100
    val MAX_DISTANCE: Int = 30

    def getTile(world: IBlockAccess, x: Int, y: Int, z: Int): Option[TileStorageRemote] = world.getTileEntity(x, y, z) match {
        case tile: TileStorageRemote => Some(tile)
        case _ => None
    }

    def getCoords(tag: NBTTagCompound): Option[Array[Int]] = {
        tag.hasKey("coreX") match {
            case true =>
                Some(Array(
                    tag.getInteger("coreX"),
                    tag.getInteger("coreY"),
                    tag.getInteger("coreZ")
                ))
            case _ => None
        }
    }

    def canOpen(tile: TileStorageRemote, player: EntityPlayer): Boolean = {
        tile.getCore match {
            case null => false
            case _ => tile.getCore.canOpen(player) match {
                case false => false
                case _ => MAX_DISTANCE >= Vec3.createVectorHelper(player.posX, player.posY, player.posZ).
                        distanceTo(Vec3.createVectorHelper(tile.xCoord, tile.xCoord, tile.xCoord)) match {
                    case false => false
                    case _ => true
                }
            }
        }
    }
}

class ItemStorageRemoteTest extends BaseItem("itemStorageRemote", 1) with IEnergyContainerItem {

    import ItemStorageRemoteTest._

    {
        setMaxDamage(16)
        setHasSubtypes(true)
    }


    /*
     * Item Functions
     */
    override def onItemRightClick(itemStack: ItemStack, world: World, player: EntityPlayer): ItemStack = {
        getCoords(itemStack.stackTagCompound) match {
            case Some(coords) =>
                getTile(world, coords(0), coords(1), coords(2)) match {
                    case Some(tile) =>
                        if (canOpen(tile, player)) {
                            if (extractEnergy(itemStack, MAX_IN_OUT, simulate = false) == 100)
                                player.openGui(Bookshelf.instance, 0, world, tile.getCore.xCoord, tile.getCore.yCoord, tile.getCore.zCoord)
                        }
                    case _ =>
                }
            case _ =>
        }
        itemStack
    }


    override def onCreated(itemstack: ItemStack, world: World, player: EntityPlayer) = {
        setEnergy(itemstack, 0)
    }

    @SideOnly(Side.CLIENT)
    override def getSubItems(item: Item, par2CreativeTabs: CreativeTabs, list$: util.List[_]) {
        val list = list$.asInstanceOf[util.List[ItemStack]]
        var is = new ItemStack(this)
        setFull(is)
        list.add(is)

        is = new ItemStack(this)
        setEnergy(is, 0)
        list.add(is)
    }

    @SideOnly(Side.CLIENT)
    override def addInformation(itemStack: ItemStack, player: EntityPlayer, list$: util.List[_], par4: Boolean) {
        val list = list$.asInstanceOf[util.List[String]]
        getCoords(itemStack.stackTagCompound) match {
            case Some(coords) =>
                list.add("Receiver Location X:" + coords(0) + " Y:" + coords(1) + " Z:" + coords(2))
            case _ => list.add("Unlinked")
        }
        itemStack.stackTagCompound.hasKey("Energy") match {
            case true => list.add(itemStack.stackTagCompound.getInteger("Energy") + "/" + CAPACITY + " RF")
            case _ => list.add("0/" + CAPACITY + " RF")
        }
    }

    /*
     * Energy Functions
     */
    override def receiveEnergy(container: ItemStack, maxReceive: Int, simulate: Boolean): Int = {
        container.stackTagCompound.hasKey("Energy") match {
            case false => container.stackTagCompound = new NBTTagCompound
            case _ =>
        }
        var energy = container.stackTagCompound.getInteger("Energy")
        val energyReceived = Math.min(CAPACITY - energy, Math.min(MAX_IN_OUT, maxReceive))
        if (!simulate) {
            energy += energyReceived
            container.stackTagCompound.setInteger("Energy", energy)
            updateDamage(container)
        }
        energyReceived
    }

    override def extractEnergy(container: ItemStack, maxExtract: Int, simulate: Boolean): Int = {
        container.stackTagCompound.hasKey("Energy") match {
            case true =>
                var energy: Int = container.stackTagCompound.getInteger("Energy")
                val energyExtracted: Int = Math.min(energy, Math.min(MAX_IN_OUT, maxExtract))
                if (!simulate) {
                    energy -= energyExtracted
                    container.stackTagCompound.setInteger("Energy", energy)
                    updateDamage(container)
                }
                energyExtracted
            case _ => 0
        }
    }

    override def getEnergyStored(container: ItemStack): Int = {
        container.stackTagCompound.hasKey("Energy") match {
            case true => container.stackTagCompound.getInteger("Energy")
            case _ => 0
        }
    }

    override def getMaxEnergyStored(container: ItemStack): Int = {
        CAPACITY
    }

    private def updateDamage(stack: ItemStack) {
        stack.setItemDamage((16 - (getEnergyStored(stack).toFloat / getMaxEnergyStored(stack)) * 16).toInt)
    }

    def setEnergy(itemstack: ItemStack, energy: Int) = {
        itemstack.stackTagCompound match {
            case null => itemstack.stackTagCompound = new NBTTagCompound
            case _ =>
        }
        itemstack.stackTagCompound.setInteger("Energy", energy)
        updateDamage(itemstack)
    }

    def setFull(container: ItemStack) {
        setEnergy(container, CAPACITY)
    }
}

