package com.teambr.modularsystems.core.network

import com.teambr.bookshelf.Bookshelf
import io.netty.buffer.ByteBuf
import net.minecraft.util.BlockPos
import net.minecraftforge.fml.common.network.simpleimpl.{MessageContext, IMessage, IMessageHandler}

/**
 * This file was created for Modular-Systems
 *
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Dyonovan
 * @since August 08, 2015
 */
class OpenContainerPacket extends IMessage with IMessageHandler[OpenContainerPacket, IMessage] {

    var blockPosition = new BlockPos(0, 0, 0)

    def this(blockPos: BlockPos) {
        this()
        blockPosition = blockPos
    }

    override def fromBytes(buffer : ByteBuf): Unit = {
        blockPosition = BlockPos.fromLong(buffer.readLong())
    }

    override def toBytes(buffer : ByteBuf): Unit = {
        buffer.writeLong(blockPosition.toLong)
    }

    def onMessage(message: OpenContainerPacket, ctx: MessageContext): IMessage = {
        if (ctx.side.isServer) {
            val pos = message.blockPosition
            ctx.getServerHandler.playerEntity.openGui(Bookshelf, 0, ctx.getServerHandler.playerEntity.getEntityWorld,
                pos.getX, pos.getY, pos.getZ)
        }
        null
    }
}
