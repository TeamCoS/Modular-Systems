package com.teambr.modularsystems.core.network;

import com.teambr.modularsystems.core.registries.BlockValueRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * This file was created for Modular-Systems
 * <p/>
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis "pauljoda"
 * @since 3/24/2016
 */
public class DeleteValuesPacket implements IMessage, IMessageHandler<DeleteValuesPacket, IMessage> {

    protected int blockID;
    protected int meta;

    public DeleteValuesPacket() {}

    public DeleteValuesPacket(int b, int i) {
        this.blockID = b;
        this.meta = i;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.blockID = buf.readInt();
        this.meta = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.blockID);
        buf.writeInt(this.meta);
    }

    @Override
    public IMessage onMessage(DeleteValuesPacket message, MessageContext ctx) {
        if(ctx.side == Side.SERVER) {
            BlockValueRegistry.INSTANCE.deleteBlockValues(Block.getBlockById(message.blockID), message.meta);
            PacketManager.net.sendToAll(message);
        } else {
            BlockValueRegistry.INSTANCE.deleteBlockValues(Block.getBlockById(message.blockID), message.meta);
        }
        return null;
    }
}