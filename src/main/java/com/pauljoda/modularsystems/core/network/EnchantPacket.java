package com.pauljoda.modularsystems.core.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import com.pauljoda.modularsystems.enchanting.container.ContainerModularEnchanting;

import cpw.mods.fml.common.network.ByteBufUtils;

public class EnchantPacket extends AbstractPacket {

	int enchantId;
	int level;

	public EnchantPacket() {}

	public EnchantPacket(int i, int j)
	{
		enchantId = i;
		level = j;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {		
		ByteBufUtils.writeVarShort(buffer, enchantId);
		ByteBufUtils.writeVarShort(buffer, level);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {		
		enchantId = ByteBufUtils.readVarShort(buffer);
		level = ByteBufUtils.readVarShort(buffer);
	}

	@Override
	public void handleClientSide(EntityPlayer player) {		
	}

	@Override
	public void handleServerSide(EntityPlayer par1Player) {
		EntityPlayerMP player = (EntityPlayerMP)par1Player;
		ContainerModularEnchanting container = (ContainerModularEnchanting)player.openContainer;
		container.addEnchantment(enchantId, level, player);
	}

}
