package com.teambr.modularsystems.storage.items

import cofh.api.energy.IEnergyContainerItem
import com.teambr.bookshelf.Bookshelf
import com.teambr.bookshelf.client.gui.{ GuiColor, GuiTextFormat }
import com.teambr.modularsystems.core.common.items.BaseItem
import com.teambr.modularsystems.storage.tiles.TileStorageRemote
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.{Vec3, BlockPos}
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

/**
 * This file was created for Modular-Systems
 *
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Dyonovan
 * @since August 10, 2015
 */
class ItemStorageRemote extends BaseItem("itemStorageRemote", 1) with IEnergyContainerItem{

    final val CAPACITY = 10000
    final val MAX_IN_OUT = 100

    setMaxDamage(16)
    setHasSubtypes(true)

    override def onCreated(itemStack: ItemStack, world: World, player: EntityPlayer): Unit = setEnergy(itemStack, 0)

    override def onItemRightClick(itemStack: ItemStack, world: World, player: EntityPlayer): ItemStack = {
        if (!world.isRemote) {
            val coords = getCoords(itemStack.getTagCompound)
            if (coords.isDefined) {
                world.getTileEntity(coords.get) match {
                    case tile: TileStorageRemote =>
                        if (isValid(tile, player)) {
                            extractEnergy(itemStack, MAX_IN_OUT, simulate = false)
                            val core = tile.getCore.get.getPos
                            player.openGui(Bookshelf, 0, world, core.getX, core.getY, core.getZ)
                        }
                    case _ =>
                }
            }
        }
        itemStack
    }

    @SideOnly(Side.CLIENT)
    override def getSubItems(item: Item, tab: CreativeTabs, subItems: java.util.List[ItemStack]): Unit = {
        var is = new ItemStack(this)
        setEnergy(is, CAPACITY)
        subItems.asInstanceOf[java.util.List[ItemStack]].add(is)

        is = new ItemStack(this)
        setEnergy(is, 0)
        subItems.asInstanceOf[java.util.List[ItemStack]].add(is)
    }

    @SideOnly(Side.CLIENT)
    override def addInformation(stack: ItemStack, player: EntityPlayer, list: java.util.List[String], boolean: Boolean): Unit = {
        if (stack.hasTagCompound) {
            if (stack.getTagCompound.getLong("Core") != 0) {
                val position = BlockPos.fromLong(stack.getTagCompound.getLong("Core"))
                list.asInstanceOf[java.util.List[String]].add(GuiTextFormat.BOLD + "" +  GuiColor.GREEN +  "Receiver Location:")
                list.asInstanceOf[java.util.List[String]].add("  " + GuiTextFormat.ITALICS + "" +  GuiColor.YELLOW +  "X: " + GuiColor.WHITE + position.getX)
                list.asInstanceOf[java.util.List[String]].add("  " + GuiTextFormat.ITALICS + "" +  GuiColor.YELLOW +  "Y: " + GuiColor.WHITE + position.getY)
                list.asInstanceOf[java.util.List[String]].add("  " + GuiTextFormat.ITALICS + "" +  GuiColor.YELLOW +  "Z: " + GuiColor.WHITE + position.getZ)

            } else list.asInstanceOf[java.util.List[String]].add(GuiColor.RED + "Unlinked")
            if (stack.getTagCompound.getInteger("Energy") != 0) {
                list.asInstanceOf[java.util.List[String]].add(GuiColor.ORANGE + (stack.getTagCompound.getInteger("Energy") + "/" + CAPACITY + " RF"))
            } else list.asInstanceOf[java.util.List[String]].add(GuiColor.RED + "0/" + CAPACITY + " RF")
        } else list.asInstanceOf[java.util.List[String]].add(GuiColor.RED + "Unlinked")
    }

    private def getCoords(tag: NBTTagCompound): Option[BlockPos] = {
        if (tag.hasKey("Core")) {
            return Some(BlockPos.fromLong(tag.getLong("Core")))
        }
        None
    }

    private def isValid(tile: TileStorageRemote, player: EntityPlayer): Boolean = {
        if (tile.getCore.isDefined) {
            if (tile.getWorld.provider.getDimensionId == player.dimension) {
                if (tile.getCore.get.canOpen(player)) {
                    val distance = new Vec3(player.posX, player.posY, player.posZ).distanceTo(
                        new Vec3(tile.getPos.getX, tile.getPos.getY, tile.getPos.getZ)).toInt
                    if (tile.maxDistance >= distance) {
                        return true
                    }
                }
            }
        }
        false
    }

    private def setEnergy(container: ItemStack, energy: Int): Unit = {
        var tag = new NBTTagCompound
        if (container.getTagCompound != null)
            tag = container.getTagCompound
        tag.setInteger("Energy", energy)

        container.setTagCompound(tag)
        updateDamage(container)
    }

    private def updateDamage(stack: ItemStack): Unit = {
        val r = getEnergyStored(stack).toFloat / getMaxEnergyStored(stack)
        val res = 16 - (r * 16).toInt
        stack.setItemDamage(res)
    }

    override def extractEnergy(container: ItemStack, maxExtract: Int, simulate: Boolean): Int = {
        var energy = container.getTagCompound.getInteger("Energy")
        val energyExtracted = Math.min(energy, Math.min(MAX_IN_OUT, maxExtract))

        if (!simulate) {
            energy -= energyExtracted
            setEnergy(container, energy)
        }
        energyExtracted
    }

    override def getEnergyStored(container: ItemStack): Int = container.getTagCompound.getInteger("Energy")

    override def getMaxEnergyStored(container: ItemStack): Int = CAPACITY

    override def receiveEnergy(container: ItemStack, maxReceive: Int, simulate: Boolean): Int = {
        var energy = container.getTagCompound.getInteger("Energy")
        val energyReceived = Math.min(CAPACITY - energy, Math.min(MAX_IN_OUT, maxReceive))

        if (!simulate) {
            energy += energyReceived
            setEnergy(container, energy)
        }
        energyReceived
    }
}
