package com.teambr.modularsystems.storage.network;

import com.teambr.modularsystems.storage.container.ContainerStorageCore;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * This file was created for Modular-Systems
 * <p>
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis "pauljoda"
 * @since 3/29/2016
 */
public class FillCraftingGrid implements IMessage, IMessageHandler<FillCraftingGrid, IMessage> {

    public NBTTagCompound tag;

    public FillCraftingGrid() {
    }

    public FillCraftingGrid(NBTTagCompound pos) {
        tag = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        tag = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, tag);
    }

    @Override
    public IMessage onMessage(FillCraftingGrid message, MessageContext ctx) {
        if (ctx.side.isServer()) {
            EntityPlayer player = ctx.getServerHandler().playerEntity;
            if (player.openContainer instanceof ContainerStorageCore) {
                ContainerStorageCore containerStorageCore = (ContainerStorageCore) player.openContainer;
                containerStorageCore.fillCraftingGrid(message.tag);
            }
        }
        return null;
    }
}