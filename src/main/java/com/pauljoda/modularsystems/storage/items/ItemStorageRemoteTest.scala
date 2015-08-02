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

    def getTile(world: IBlockAccess, x: Int, y: Int, z: Int) = world.getTileEntity(x, y, z) match {
        case t: TileStorageRemote => t
        case _ => null
    }

    def getCoords(tag: NBTTagCompound): Option[Array[Int]] = {
        if (tag != null) {
            if (tag.hasKey("coreX")) {
                Some(Array(
                    tag.getInteger("coreX"),
                    tag.getInteger("coreY"),
                    tag.getInteger("coreZ")
                ))
            }
        }
        None
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
        if (!world.isRemote) {
            getCoords(itemStack.stackTagCompound) match {
                case Some(coords) =>
                    if (coords != null) {
                        val tile = getTile(world, coords(0), coords(1), coords(2))
                        if (tile != null) {
                            if (tile.getCore != null && tile.getCore.canOpen(player) && extractEnergy(itemStack, MAX_IN_OUT, simulate = true)
                                    >= MAX_IN_OUT &&
                                    MAX_DISTANCE >= Vec3.createVectorHelper(player.posX, player.posY, player.posZ).distanceTo(Vec3.createVectorHelper(coords(0), coords(1), coords(2)))) {
                                extractEnergy(itemStack, MAX_IN_OUT, simulate = false)
                                player.openGui(Bookshelf.instance, 0, world, tile.getCore.xCoord, tile.getCore.yCoord, tile.getCore.zCoord)
                            }
                        }
                    }

            }
        }
        itemStack
    }


        override def onCreated(itemstack: ItemStack, world: World, player: EntityPlayer) =
        {
            setEnergy(itemstack, 0)
        }

        @SideOnly(Side.CLIENT)
        override def getSubItems(item: Item, par2CreativeTabs: CreativeTabs, list$: util.List[_])
        {
            val list = list$.asInstanceOf[util.List[ItemStack]]
            var is = new ItemStack(this)
            setFull(is)
            list.add(is)

            is = new ItemStack(this)
            setEnergy(is, 0)
            list.add(is)
        }

        /*
         * Energy Functions
         */
        override def receiveEnergy(container: ItemStack, maxReceive: Int, simulate: Boolean): Int =
        {
            if (container.stackTagCompound == null) {
                container.stackTagCompound = new NBTTagCompound
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

        override def extractEnergy(container: ItemStack, maxExtract: Int, simulate: Boolean): Int =
        {
            if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Energy")) {
                return 0
            }
            var energy: Int = container.stackTagCompound.getInteger("Energy")
            val energyExtracted: Int = Math.min(energy, Math.min(MAX_IN_OUT, maxExtract))
            if (!simulate) {
                energy -= energyExtracted
                container.stackTagCompound.setInteger("Energy", energy)
                updateDamage(container)
            }
            energyExtracted
        }

        override def getMaxEnergyStored(container: ItemStack): Int =
        {
            CAPACITY
        }

        private def updateDamage(stack: ItemStack)
        {
            stack.setItemDamage((16 - (getEnergyStored(stack).toFloat / getMaxEnergyStored(stack)) * 16).toInt)
        }

        def setEnergy(itemstack: ItemStack, energy: Int) = {
            itemstack.stackTagCompound match {
                case null => itemstack.stackTagCompound = new NBTTagCompound
            }
            itemstack.stackTagCompound.setInteger("Energy", energy)
            updateDamage(itemstack)
        }

        def setFull(container: ItemStack) {
            setEnergy(container, CAPACITY)
        }
    }
