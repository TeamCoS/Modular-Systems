package com.pauljoda.modularsystems.storage.tiles;

import java.util.List;

import com.pauljoda.modularsystems.core.util.GeneralSettings;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityStorageCore extends TileEntity implements IInventory {

	public ItemStack[] inv;
	public int inventoryRows = 6;
	private final int MAX_EXPANSIONS = GeneralSettings.maxExpansionSize;

	public int anchorX;
	public int anchorY;
	public int anchorZ;
	
	public TileEntityStorageCore()
	{
		inv = new ItemStack[11 * MAX_EXPANSIONS];
	}

	public TileEntityStorageExpansion getAnchor()
	{
		return (TileEntityStorageExpansion)worldObj.getTileEntity(anchorX, anchorY, anchorZ);
	}
	
	public void setAnchor(TileEntityStorageExpansion expansion)
	{
		anchorX = expansion.xCoord;
		anchorY = expansion.yCoord;
		anchorZ = expansion.zCoord;
	}
	public void dropItems(int x, int y, int z)
	{
		for (int i1 = inventoryRows * 11 - 11; i1 < inventoryRows * 11; ++i1)
		{
			ItemStack itemstack = getStackInSlot(i1);
			inv[i1] = null;
			if (itemstack != null)
			{
				float f = this.worldObj.rand.nextFloat() * 0.8F + 0.1F;
				float f1 = this.worldObj.rand.nextFloat() * 0.8F + 0.1F;
				float f2 = this.worldObj.rand.nextFloat() * 0.8F + 0.1F;

				while (itemstack.stackSize > 0)
				{
					int j1 = this.worldObj.rand.nextInt(21) + 10;

					if (j1 > itemstack.stackSize)
					{
						j1 = itemstack.stackSize;
					}

					itemstack.stackSize -= j1;
					EntityItem entityitem = new EntityItem(worldObj, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

					if (itemstack.hasTagCompound())
					{
						entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
					}

					float f3 = 0.05F;
					entityitem.motionX = (double)((float)this.worldObj.rand.nextGaussian() * f3);
					entityitem.motionY = (double)((float)this.worldObj.rand.nextGaussian() * f3 + 0.2F);
					entityitem.motionZ = (double)((float)this.worldObj.rand.nextGaussian() * f3);
					worldObj.spawnEntityInWorld(entityitem);
				}
			}
		}
	}
	
	public void setInventoryRows(int i)
	{
		this.inventoryRows = i;
		this.markDirty();
	}
	@Override
	public int getSizeInventory() {
		return inv.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inv[slot];
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		inv[slot] = stack;
		if (stack != null && stack.stackSize > getInventoryStackLimit()) {
			stack.stackSize = getInventoryStackLimit();
		}  
		markDirty();
	}

	@Override
	public ItemStack decrStackSize(int slot, int amt) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			if (stack.stackSize <= amt) {
				setInventorySlotContents(slot, null);
			} else {
				stack = stack.splitStack(amt);
				if (stack.stackSize == 0) {
					setInventorySlotContents(slot, null);
				}
			}
		}
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			setInventorySlotContents(slot, null);
		}
		return stack;
	}

	@Override
	public String getInventoryName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasCustomInventoryName() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this &&
				player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		
		this.inventoryRows = tagCompound.getInteger("Rows");
		
		this.anchorX = tagCompound.getInteger("AnchorX");
		this.anchorY = tagCompound.getInteger("AnchorY");
		this.anchorZ = tagCompound.getInteger("AnchorZ");
		
		NBTTagList itemsTag = tagCompound.getTagList("Items", 10);
		this.inv = new ItemStack[getSizeInventory()];
		for (int i = 0; i < itemsTag.tagCount(); i++)
		{
			NBTTagCompound nbtTagCompound1 = itemsTag.getCompoundTagAt(i);
			NBTBase nbt = nbtTagCompound1.getTag("Slot");
			int j = -1;
			if ((nbt instanceof NBTTagByte)) {
				j = nbtTagCompound1.getByte("Slot") & 0xFF;
			} else {
				j = nbtTagCompound1.getShort("Slot");
			}
			if ((j >= 0) && (j < this.inv.length)) {
				this.inv[j] = ItemStack.loadItemStackFromNBT(nbtTagCompound1);
			}
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		
		tagCompound.setInteger("Rows", this.inventoryRows);
		
		tagCompound.setInteger("AnchorX", anchorX);
		tagCompound.setInteger("AnchorY", anchorY);
		tagCompound.setInteger("AnchorZ", anchorZ);
		
		NBTTagList nbtTagList = new NBTTagList();
		for (int i = 0; i < this.inv.length; i++) {
			if (this.inv[i] != null)
			{
				NBTTagCompound nbtTagCompound1 = new NBTTagCompound();
				nbtTagCompound1.setShort("Slot", (short)i);
				this.inv[i].writeToNBT(nbtTagCompound1);
				nbtTagList.appendTag(nbtTagCompound1);
			}
		}
		tagCompound.setTag("Items", nbtTagList);
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
	public void openInventory() {
		// TODO Auto-generated method stub

	}

	@Override
	public void closeInventory() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setGuiDisplayName(String displayName) { }

}
