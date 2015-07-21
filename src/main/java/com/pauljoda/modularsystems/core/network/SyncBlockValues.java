package com.pauljoda.modularsystems.core.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.pauljoda.modularsystems.core.collections.BlockValues;
import com.pauljoda.modularsystems.core.functions.CompressionFunctions;
import com.pauljoda.modularsystems.core.registries.BlockValueRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class SyncBlockValues implements IMessage, IMessageHandler<SyncBlockValues, IMessage> {

    public String jsonBlockValues;

    public SyncBlockValues() {}

    public SyncBlockValues(HashMap<String, BlockValues> values) {
        Gson gson = new GsonBuilder().create();
        jsonBlockValues = gson.toJson(values);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        byte[] compressedBytes = null;
        int readableBytes = buf.readInt();

        if (readableBytes > 0)
            compressedBytes = buf.readBytes(readableBytes).array();

        if (compressedBytes != null)
            this.jsonBlockValues = CompressionFunctions.decompressStringFromByteArray(compressedBytes);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        byte[] compressedBytes = null;

        if (jsonBlockValues != null)
            compressedBytes = CompressionFunctions.compressStringToByteArray(jsonBlockValues);

        if (compressedBytes != null) {
            buf.writeInt(compressedBytes.length);
            buf.writeBytes(compressedBytes);
        } else
            buf.writeInt(0);
    }

    @Override
    public IMessage onMessage(SyncBlockValues message, MessageContext ctx) {

        if (message.jsonBlockValues != null) {
            Gson gson = new Gson();
            BlockValueRegistry.INSTANCE.values = gson.fromJson(message.jsonBlockValues,
                    new TypeToken<LinkedHashMap<String, BlockValues>>() {}.getType());
        }
        return null;
    }
}
