package com.pauljoda.modularsystems.storage.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityStorageExpansion extends TileEntity implements IInventory {

	public int coreX;
	public int coreY;
	public int coreZ;
	
	public boolean isAnchor;
	
	public int nextX;
	public int nextY;
	public int nextZ;

	public TileEntityStorageExpansion()
	{}

	public TileEntityStorageCore getCore()
	{
		return (TileEntityStorageCore) worldObj.getTileEntity(coreX, coreY, coreZ);
	}
	
	public void setCore(TileEntityStorageCore core)
	{
		coreX = core.xCoord;
		coreY = core.yCoord;
		coreZ = core.zCoord;
	}
	
	public void setNext(TileEntityStorageExpansion next)
	{
		nextX = next.xCoord;
		nextY = next.yCoord;
		nextZ = next.zCoord;
	}
	
	public void invalidateCore()
	{
		this.coreY = -100;
	}
	public TileEntityStorageExpansion getNext()
	{
		return (TileEntityStorageExpansion)worldObj.getTileEntity(nextX, nextY, nextZ);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);

		coreX = tagCompound.getInteger("coreX");
		coreY = tagCompound.getInteger("coreY");
		coreZ = tagCompound.getInteger("coreZ");
		
		isAnchor = tagCompound.getBoolean("isAnchor");
		
		nextX = tagCompound.getInteger("nextX");
		nextY = tagCompound.getInteger("nextY");
		nextZ = tagCompound.getInteger("nextZ");
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);

		tagCompound.setInteger("coreX", coreX);
		tagCompound.setInteger("coreY", coreY);
		tagCompound.setInteger("coreZ", coreZ);
		
		tagCompound.setBoolean("isAnchor", isAnchor);
		
		tagCompound.setInteger("nextX", nextX);
		tagCompound.setInteger("nextY", nextY);
		tagCompound.setInteger("nextZ", nextZ);
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
	
	@Override
	public int getSizeInventory() {
		return getCore().getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return getCore().getStackInSlot(i);
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		return getCore().decrStackSize(i, j);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		return getCore().getStackInSlotOnClosing(i);
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		getCore().setInventorySlotContents(i, itemstack);
	}

	@Override
	public int getInventoryStackLimit() {
		return getCore().getInventoryStackLimit();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) != this ? false : entityplayer.getDistanceSq((double)xCoord + 0.5, (double)yCoord + 0.5, (double)zCoord + 0.5) <= 64.0;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return getCore().isItemValidForSlot(i, itemstack);
	}

	@Override
	public String getInventoryName() {
		return getCore().getInventoryName();
	}

	@Override
	public boolean hasCustomInventoryName() {
		return getCore().hasCustomInventoryName();
	}

	@Override
	public void openInventory() {		
	}

	@Override
	public void closeInventory() {		
	}
}
