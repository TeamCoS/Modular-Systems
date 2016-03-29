package com.teambr.modularsystems.storage.network;

import com.teambr.modularsystems.storage.container.ContainerStorageCore;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
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
 * @since 3/29/2016
 */
public class UpdateFilterString implements IMessage, IMessageHandler<UpdateFilterString, IMessage> {

    public String filterString;

    public UpdateFilterString() {}

    public UpdateFilterString(String toUpdate) {
        filterString = toUpdate;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        filterString = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, filterString);
    }

    @Override
    public IMessage onMessage(UpdateFilterString message, MessageContext ctx) {
        if (ctx.side.isServer()) {
            EntityPlayer player = ctx.getServerHandler().playerEntity;
            if(player.openContainer instanceof ContainerStorageCore) {
                ContainerStorageCore containerStorageCore = (ContainerStorageCore) player.openContainer;
                containerStorageCore.filterString_$eq(message.filterString);
            }
        }
        return null;
    }
}