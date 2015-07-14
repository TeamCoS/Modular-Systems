package com.pauljoda.modularsystems.core.network;

import com.teambr.bookshelf.Bookshelf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class OpenContainerPacket implements IMessageHandler<OpenContainerPacket.UpdateMessage, IMessage> {

    @Override
    public IMessage onMessage(OpenContainerPacket.UpdateMessage message, MessageContext ctx) {
        if (ctx.side.isServer()) {
            ctx.getServerHandler().playerEntity.openGui(Bookshelf.instance, 0,
                    ctx.getServerHandler().playerEntity.getEntityWorld(),
                    message.x, message.y, message.z);
        }
        return null;
    }

    public static class UpdateMessage implements IMessage {

        private int x, y, z;

        @SuppressWarnings("unused")
        public UpdateMessage() {}

        public UpdateMessage(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            x = buf.readInt();
            y = buf.readInt();
            z = buf.readInt();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(x);
            buf.writeInt(y);
            buf.writeInt(z);
        }
    }
}
