package com.teambr.modularsystems.temp.tiles

import com.teambr.bookshelf.collections.Location
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.{NetworkManager, Packet}
import net.minecraft.network.play.server.S35PacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity

/**
 * Modular-Systems
 * Created by Dyonovan on 02/08/15
 */
class BaseTile extends TileEntity {
    def getLocation: Location = {
        new Location(this.pos.getX, this.pos.getY, this.pos.getZ)
    }

    override def getDescriptionPacket : Packet = {
        var nbtTag = new NBTTagCompound
        this.writeToNBT(nbtTag)
        new S35PacketUpdateTileEntity(this.pos, 1, nbtTag)
    }

    override def onDataPacket(net : NetworkManager, pkt : S35PacketUpdateTileEntity) = {
        readFromNBT(pkt.getNbtCompound)
    }

}
