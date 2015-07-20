package com.pauljoda.modularsystems.core.network;

import com.pauljoda.modularsystems.core.collections.BlockValues;
import com.pauljoda.modularsystems.core.managers.PacketManager;
import com.pauljoda.modularsystems.core.registries.BlockValueRegistry;
import com.teambr.bookshelf.collections.Couplet;
import com.teambr.bookshelf.helpers.BlockHelper;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;

import java.util.Map;

public class GetHashPacket implements IMessageHandler<GetHashPacket.HashMessage, IMessage> {

    @Override
    public IMessage onMessage(HashMessage message, MessageContext ctx) {
        if (ctx.side.isServer()) {
            if (BlockValueRegistry.INSTANCE.values.hashCode() != message.hash) {
                PacketManager.net.sendTo(new HashMessage(0), ctx.getServerHandler().playerEntity);
                for (Map.Entry<String, BlockValues> entry : BlockValueRegistry.INSTANCE.values.entrySet()){
                    Couplet<Block, Integer> block = BlockHelper.getBlockFromString(entry.getKey());
                    BlockValues value = entry.getValue();
                    PacketManager.net.sendTo(new AddCalculationPacket.CalculationMessage(block.getFirst(), block.getSecond(),
                            value.getSpeedFunction(), value.getEfficiencyFunction(), value.getMultiplicityFunction()),
                            ctx.getServerHandler().playerEntity);
                }
            }
        } else if (ctx.side.isClient()) {
            BlockValueRegistry.INSTANCE.values.clear();
        }
        return null;
    }

    public static class HashMessage implements IMessage {

        private int hash;

        @SuppressWarnings("unused")
        public HashMessage() {}

        public HashMessage(int hash) {
            this.hash = hash;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            hash = buf.readInt();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(hash);
        }
    }
}
