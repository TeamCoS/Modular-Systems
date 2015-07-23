package com.pauljoda.modularsystems.core.network;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pauljoda.modularsystems.core.collections.CrusherRecipes;
import com.pauljoda.modularsystems.core.functions.CompressionFunctions;
import com.pauljoda.modularsystems.core.registries.CrusherRecipeRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

/**
 * Modular-Systems
 * Created by Dyonovan on 22/07/15
 */
@SuppressWarnings("unused")
public class SyncCrusherRecipes implements IMessage, IMessageHandler<SyncCrusherRecipes, IMessage> {

    public String jsonCrusherRecipes;

    public SyncCrusherRecipes() {}

    public SyncCrusherRecipes(ArrayList<CrusherRecipes> values) {
        Gson gson = new GsonBuilder().create();
        jsonCrusherRecipes = gson.toJson(values);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        byte[] compressedBytes = null;
        int readableBytes = buf.readInt();

        if (readableBytes > 0)
            compressedBytes = buf.readBytes(readableBytes).array();

        if (compressedBytes != null)
            this.jsonCrusherRecipes = CompressionFunctions.decompressStringFromByteArray(compressedBytes);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        byte[] compressedBytes = null;

        if (jsonCrusherRecipes != null)
            compressedBytes = CompressionFunctions.compressStringToByteArray(jsonCrusherRecipes);

        if (compressedBytes != null) {
            buf.writeInt(compressedBytes.length);
            buf.writeBytes(compressedBytes);
        } else
            buf.writeInt(0);
    }

    @Override
    public IMessage onMessage(SyncCrusherRecipes message, MessageContext ctx) {
        if (message.jsonCrusherRecipes != null) {
            Gson gson = new Gson();
            CrusherRecipeRegistry.INSTANCE.crusherRecipes = gson.fromJson(message.jsonCrusherRecipes,
                    new TypeToken<ArrayList<CrusherRecipes>>() {}.getType());
        }
        return null;
    }
}
