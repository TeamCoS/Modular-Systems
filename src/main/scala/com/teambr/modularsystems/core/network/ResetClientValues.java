package com.teambr.modularsystems.core.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jcraft.jorbis.Block;
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
 * @author Dyonovan
 * @since 3/24/2016
 */
public class ResetClientValues implements IMessage, IMessageHandler<ResetClientValues, IMessage> {

    public String jsonBlockValues;

    public ResetClientValues() {}

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    @Override
    public IMessage onMessage(ResetClientValues message, MessageContext ctx) {

        if (ctx.side.isClient()) {
            LogHelper.info("Restoring client values...");
            BlockValueRegistry.INSTANCE.init();
        }
        return null;
    }
}
