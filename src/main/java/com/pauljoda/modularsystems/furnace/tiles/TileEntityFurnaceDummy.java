package com.pauljoda.modularsystems.furnace.tiles;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityFurnaceDummy extends TileEntity implements ISidedInventory
{
	public int slot = 4;
	TileEntityFurnaceCore tileEntityCore;
	int coreX;
	int coreY;
	int coreZ;
	int icon = 1;
	int metadata = 0;

	public TileEntityFurnaceDummy()
	{
	}

	public void setCore(TileEntityFurnaceCore core)
	{
		coreX = core.xCoord;
		coreY = core.yCoord;
		coreZ = core.zCoord;
		tileEntityCore = core;
	}

	public TileEntityFurnaceCore getCore()
	{
		if(tileEntityCore == null)
			tileEntityCore = (TileEntityFurnaceCore)worldObj.getTileEntity(coreX, coreY, coreZ);

		return tileEntityCore;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);

		coreX = tagCompound.getInteger("CoreX");
		coreY = tagCompound.getInteger("CoreY");
		coreZ = tagCompound.getInteger("CoreZ");

		slot = tagCompound.getInteger("Slot");
		icon = tagCompound.getInteger("Icon");
		metadata = tagCompound.getInteger("Meta");
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);

		tagCompound.setInteger("CoreX", coreX);
		tagCompound.setInteger("CoreY", coreY);
		tagCompound.setInteger("CoreZ", coreZ);

		tagCompound.setInteger("Slot", slot);
		tagCompound.setInteger("Icon", icon);
		tagCompound.setInteger("Meta", metadata);
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
	
	public Block getBlock()
	{
		if(Block.getBlockById(this.icon) == null)
			return Blocks.cobblestone;
		
		return Block.getBlockById(this.icon);
	}
	
	public int getMeta()
	{
		return metadata;
	}
	
	@Override
	public int getSizeInventory() {
		return tileEntityCore.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return tileEntityCore.getStackInSlot(i);
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		return tileEntityCore.decrStackSize(i, j);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		return tileEntityCore.getStackInSlotOnClosing(i);
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		tileEntityCore.setInventorySlotContents(i, itemstack);
	}

	@Override
	public int getInventoryStackLimit() {
		return tileEntityCore.getInventoryStackLimit();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return getCore() != null ? true : false;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return tileEntityCore.isItemValidForSlot(i, itemstack);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		if(slot != 4)
		{
			if(slot == 0)
				var1 = 0;
			if(slot == 1)
				var1 = 1;
			if(slot == 2)
				var1 = 2;
		}

		tileEntityCore = this.getCore();
		return tileEntityCore.getAccessibleSlotsFromSide(var1);
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int j) {
		tileEntityCore = this.getCore();
		return tileEntityCore.isItemValidForSlot(i, itemstack);
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j) {
		tileEntityCore = this.getCore();
		if(slot <= 3)
		{
			j = slot;
			return tileEntityCore.canExtractItem(i, itemstack, j);
		}
		else
		{
			return tileEntityCore.canExtractItem(i, itemstack, j);
		}
	}

	@Override
	public String getInventoryName() {
		return tileEntityCore.getInventoryName();
	}

	@Override
	public boolean hasCustomInventoryName() {
		return tileEntityCore.hasCustomInventoryName();
	}

	@Override
	public void openInventory() {		
	}

	@Override
	public void closeInventory() {		
	}
}
