package com.pauljoda.modularsystems.core.network;

import com.pauljoda.modularsystems.core.managers.PacketManager;
import com.pauljoda.modularsystems.core.registries.BlockValueRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/21/2015
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
