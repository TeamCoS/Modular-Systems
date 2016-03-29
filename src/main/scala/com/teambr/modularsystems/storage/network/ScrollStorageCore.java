package com.teambr.modularsystems.storage.network;

import com.teambr.bookshelf.helper.LogHelper;
import com.teambr.modularsystems.core.registries.BlockValueRegistry;
import com.teambr.modularsystems.storage.container.ContainerStorageCore;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * This file was created for Modular-Systems
 * <p/>
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis "pauljoda"
 * @since 3/28/2016
 */
public class ScrollStorageCore implements IMessage, IMessageHandler<ScrollStorageCore, IMessage> {

    public float scrollValue;

    public ScrollStorageCore() {}

    public ScrollStorageCore(float pos) {
        scrollValue = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        scrollValue = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeFloat(scrollValue);
    }

    @Override
    public IMessage onMessage(ScrollStorageCore message, MessageContext ctx) {
        if (ctx.side.isServer()) {
            EntityPlayer player = ctx.getServerHandler().playerEntity;
            if(player.openContainer instanceof ContainerStorageCore) {
                ContainerStorageCore containerStorageCore = (ContainerStorageCore) player.openContainer;
                containerStorageCore.scrollTo(message.scrollValue);
            }
        }
        return null;
    }
}
