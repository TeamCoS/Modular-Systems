package com.teambr.modularsystems.temp.tiles

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.{NetworkManager, Packet}
import net.minecraft.network.play.server.S35PacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity

/**
 * Modular-Systems
 * Created by Dyonovan on 02/08/15
 */
class BaseTile extends TileEntity {

    override def getDescriptionPacket : Packet = {
        var nbtTag = new NBTTagCompound
        this.writeToNBT(nbtTag)
        new S35PacketUpdateTileEntity(this.pos, 1, nbtTag)
    }

    override def onDataPacket(net : NetworkManager, pkt : S35PacketUpdateTileEntity) = {
        readFromNBT(pkt.getNbtCompound)
    }

}
