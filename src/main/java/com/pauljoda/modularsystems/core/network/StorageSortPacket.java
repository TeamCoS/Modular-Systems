package com.pauljoda.modularsystems.core.network;

import com.pauljoda.modularsystems.storage.containers.ContainerModularStorage;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class StorageSortPacket extends AbstractPacket {

	final int ALPHABETICALLY = 0;
	final int NUMERICALLY = 1;
	int type;
	
	public StorageSortPacket() {}
	
	public StorageSortPacket(int i)
	{
		this.type = i;
	}
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		ByteBufUtils.writeVarShort(buffer, type);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		type = ByteBufUtils.readVarShort(buffer);
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		
	}

	@Override
	public void handleServerSide(EntityPlayer par1Player) {
		EntityPlayerMP player = (EntityPlayerMP)par1Player;
		ContainerModularStorage containerCore = (ContainerModularStorage)player.openContainer;
		switch(type)
		{
		case 0 :
			containerCore.sortInventoryAlphabetically();
			break;
		case 1 :
			containerCore.sortInventoryByIndex();
			break;
		}
		
	}

}
