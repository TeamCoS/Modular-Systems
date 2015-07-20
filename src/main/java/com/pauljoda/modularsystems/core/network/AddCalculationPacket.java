package com.pauljoda.modularsystems.core.network;

import com.pauljoda.modularsystems.core.calculations.Calculation;
import com.pauljoda.modularsystems.core.managers.PacketManager;
import com.pauljoda.modularsystems.core.registries.BlockValueRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;

public class AddCalculationPacket implements IMessageHandler<AddCalculationPacket.CalculationMessage, IMessage> {

    @Override
    public IMessage onMessage(AddCalculationPacket.CalculationMessage message, MessageContext ctx) {
        if(ctx.side == Side.SERVER) {
            BlockValueRegistry.INSTANCE.addBlockValues(Block.getBlockById(message.blockId), message.meta,
                    message.speed,
                    message.efficiency,
                    message.multi);
            PacketManager.net.sendToAll(message);
        } else {
            BlockValueRegistry.INSTANCE.addBlockValues(Block.getBlockById(message.blockId), message.meta,
                    message.speed,
                    message.efficiency,
                    message.multi);
        }
        return null;
    }

    public static class CalculationMessage implements IMessage {

        protected int blockId;
        protected int meta;

        protected Calculation speed;
        protected Calculation efficiency;
        protected Calculation multi;

        public CalculationMessage() {}

        public CalculationMessage(Block block, int metaData, Calculation s, Calculation e, Calculation m) {
            blockId = Block.getIdFromBlock(block);
            meta = metaData;

            speed = new Calculation(s);
            efficiency = new Calculation(e);
            multi = new Calculation(m);
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            blockId = buf.readInt();
            meta = buf.readInt();

            speed = new Calculation(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble());
            efficiency = new Calculation(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble());
            multi = new Calculation(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble());
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(blockId);
            buf.writeInt(meta);

            buf.writeDouble(speed.getScaleFactorNumerator());
            buf.writeDouble(speed.getScaleFactorDenominator());
            buf.writeDouble(speed.getXOffset());
            buf.writeDouble(speed.getPower());
            buf.writeDouble(speed.getYOffset());
            buf.writeDouble(speed.getFloor());
            buf.writeDouble(speed.getCeiling());

            buf.writeDouble(efficiency.getScaleFactorNumerator());
            buf.writeDouble(efficiency.getScaleFactorDenominator());
            buf.writeDouble(efficiency.getXOffset());
            buf.writeDouble(efficiency.getPower());
            buf.writeDouble(efficiency.getYOffset());
            buf.writeDouble(efficiency.getFloor());
            buf.writeDouble(efficiency.getCeiling());

            buf.writeDouble(multi.getScaleFactorNumerator());
            buf.writeDouble(multi.getScaleFactorDenominator());
            buf.writeDouble(multi.getXOffset());
            buf.writeDouble(multi.getPower());
            buf.writeDouble(multi.getYOffset());
            buf.writeDouble(multi.getFloor());
            buf.writeDouble(multi.getCeiling());
        }
    }
}
