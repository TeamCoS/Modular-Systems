package com.teambr.modularsystems.storage.network;

import com.teambr.modularsystems.storage.container.ContainerStorageCore;
import com.teambr.modularsystems.storage.gui.GuiStorageCore;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.PacketLoggingHandler;
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
public class UpdateStorageContainer implements IMessage, IMessageHandler<UpdateStorageContainer, UpdateStorageContainer> {

    public UpdateStorageContainer() {}

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public void toBytes(ByteBuf buf) {}

    @Override
    public UpdateStorageContainer onMessage(UpdateStorageContainer message, MessageContext ctx) {
        if(ctx.side.isClient()) {
            if(Minecraft.getMinecraft().currentScreen instanceof GuiStorageCore) {
                GuiStorageCore container = (GuiStorageCore) Minecraft.getMinecraft().currentScreen;
                container.inventory().isDirty_$eq(true);
                return new UpdateStorageContainer();
            }
        } else {
            EntityPlayer player = ctx.getServerHandler().playerEntity;
            if(player.openContainer instanceof ContainerStorageCore) {
                ContainerStorageCore containerStorageCore = (ContainerStorageCore) player.openContainer;
                containerStorageCore.isDirty_$eq(true);
            }
        }
        return null;
    }
}
