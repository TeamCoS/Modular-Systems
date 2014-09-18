package com.pauljoda.modularsystems.core;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ModularTileEntity extends TileEntity {

	public boolean isPowered() {
		return isPoweringTo(worldObj, xCoord, yCoord + 1, zCoord, 0) ||
				isPoweringTo(worldObj, xCoord, yCoord - 1, zCoord, 1) ||
				isPoweringTo(worldObj, xCoord, yCoord, zCoord + 1, 2) ||
				isPoweringTo(worldObj, xCoord, yCoord, zCoord - 1, 3) ||
				isPoweringTo(worldObj, xCoord + 1, yCoord, zCoord, 4) ||
				isPoweringTo(worldObj, xCoord - 1, yCoord, zCoord, 5);
	}

	public static boolean isPoweringTo(World world, int x, int y, int z, int side) {
		return world.getBlock(x, y, z).isProvidingWeakPower(world, x, y, z, side) > 0;
	}
	
	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		this.writeToNBT(nbtTag);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.func_148857_g());
	}
	
	
}
