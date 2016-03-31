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
 * @since 3/30/2016
 */
public class StorageMessage implements IMessage, IMessageHandler<StorageMessage, IMessage> {

    public enum MESSAGE_ACTION {
        CLEAR_CRAFTING,
        CLEAR_PLAYER
    }

    public MESSAGE_ACTION message;

    public StorageMessage() {}

    public StorageMessage(MESSAGE_ACTION toUpdate) {
        message = toUpdate;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        message = MESSAGE_ACTION.values()[buf.readInt()];
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(message.ordinal());
    }

    @Override
    public IMessage onMessage(StorageMessage message, MessageContext ctx) {
        if (ctx.side.isServer()) {
            EntityPlayer player = ctx.getServerHandler().playerEntity;
            if(player.openContainer instanceof ContainerStorageCore) {
                ContainerStorageCore containerStorageCore = (ContainerStorageCore) player.openContainer;
                switch(message.message) {
                    case CLEAR_CRAFTING :
                        containerStorageCore.clearCraftingGrid(false);
                        break;
                    case CLEAR_PLAYER :
                        containerStorageCore.clearPlayerInv(false);
                        break;
                    default :
                }
            }
        }
        return null;
    }
}