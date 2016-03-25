package com.teambr.modularsystems.core.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.teambr.bookshelf.helper.LogHelper;
import com.teambr.modularsystems.core.collections.BlockValues;
import com.teambr.modularsystems.core.functions.CompressionFunctions;
import com.teambr.modularsystems.core.registries.BlockValueRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.HashMap;
import java.util.LinkedHashMap;

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
            LogHelper.info("Syncing data to client...");
            Gson gson = new Gson();
            BlockValueRegistry.INSTANCE.values = gson.fromJson(message.jsonBlockValues,
                    new TypeToken<LinkedHashMap<java.lang.String, BlockValues>>() {}.getType());
        }
        return null;
    }
}
