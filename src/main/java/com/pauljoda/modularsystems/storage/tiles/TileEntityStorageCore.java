package com.pauljoda.modularsystems.storage.tiles;

import java.util.List;

import com.pauljoda.modularsystems.core.GeneralSettings;
import com.pauljoda.modularsystems.core.abstracts.ModularTileEntity;
import com.pauljoda.modularsystems.core.lib.Reference;
import com.pauljoda.modularsystems.core.managers.BlockManager;
import com.pauljoda.modularsystems.storage.blocks.BlockCapacityExpansion;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
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

public class TileEntityStorageCore extends ModularTileEntity implements IInventory {

	public ItemStack[] inv;
	public int inventoryRows = 6;
	private final int MAX_EXPANSIONS = GeneralSettings.maxExpansionSize;

	public TileEntityStorageCore()
	{
		inv = new ItemStack[11 * MAX_EXPANSIONS];
	}

	public void sortInventoryAlphabetically()
	{
		String temp, temp2;
		ItemStack swapper;
		for(int i = 0; i < inventoryRows * 11; i++)
		{
			for(int j = 1; j < (inventoryRows * 11) - 1; j++)
			{
				if(inv[j - 1] != null && inv[j] != null)
				{
					temp = inv[j - 1].getDisplayName();
					temp2 = inv[j].getDisplayName();
					if(temp.compareTo(temp2) > 0)
					{
						swapper = inv[j - 1].copy();
						this.setInventorySlotContents(j - 1, inv[j].copy());
						this.setInventorySlotContents(j, swapper);
					}
					else if(temp.equals(temp2))
					{
						if(inv[j - 1].stackSize < inv[j - 1].getMaxStackSize())
						{
							int mergeSize = inv[j - 1].getMaxStackSize() - inv[j - 1].stackSize;
							if(inv[j].stackSize >= mergeSize)
							{
								inv[j - 1].stackSize += mergeSize;
								inv[j].stackSize -= mergeSize;
							}
							else
							{
								inv[j - 1].stackSize += inv[j].stackSize;
								this.setInventorySlotContents(j, null);
							}
						}

						else if(inv[j - 1].getItemDamage() > inv[j].getItemDamage())
						{
							swapper = inv[j - 1].copy();
							this.setInventorySlotContents(j - 1, inv[j].copy());
							this.setInventorySlotContents(j, swapper);
						}
					}
				}
				else if(inv[j - 1] == null && inv[j] != null)
				{
					this.setInventorySlotContents(j - 1, inv[j].copy());
					this.setInventorySlotContents(j, null);
				}
			}
		}
		markDirty();

	}

	public void sortInventoryByIndex()
	{
		int temp, temp2;
		ItemStack swapper;
		for(int i = 0; i < inventoryRows * 11; i++)
		{
			for(int j = 1; j < (inventoryRows * 11) - 1; j++)
			{
				if(inv[j - 1] != null && inv[j] != null)
				{
					temp = Item.getIdFromItem(inv[j - 1].getItem());
					temp2 = Item.getIdFromItem(inv[j].getItem());
					if(temp2 > temp)
					{
						swapper = inv[j - 1].copy();
						this.setInventorySlotContents(j - 1, inv[j].copy());
						this.setInventorySlotContents(j, swapper);
					}
					else if(temp == temp2)
					{
						if(inv[j - 1].getItemDamage() > inv[j].getItemDamage())
						{
							swapper = inv[j - 1].copy();
							this.setInventorySlotContents(j - 1, inv[j].copy());
							this.setInventorySlotContents(j, swapper);
							continue;
						}
						//Handle meta blocks merging
						else if(inv[j - 1].getItemDamage() < inv[j].getItemDamage())
							continue;
						
						else if(inv[j - 1].stackSize < inv[j - 1].getMaxStackSize())
						{
							int mergeSize = inv[j - 1].getMaxStackSize() - inv[j - 1].stackSize;
							if(inv[j].stackSize >= mergeSize)
							{
								inv[j - 1].stackSize += mergeSize;
								inv[j].stackSize -= mergeSize;
							}
							else
							{
								inv[j - 1].stackSize += inv[j].stackSize;
								this.setInventorySlotContents(j, null);
							}
						}
					}
				}
				else if(inv[j - 1] == null && inv[j] != null)
				{
					this.setInventorySlotContents(j - 1, inv[j].copy());
					this.setInventorySlotContents(j, null);
				}
			}
		}
		markDirty();
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

	public boolean hasSpecificUpgrade(int upgradeId)
	{
		for(int i = -1; i <= 1; i++)
		{
			for(int j = -1; j <= 1; j++)
			{
				for(int k = -1; k <= 1; k++)
				{
					//Checks to make sure we are looking at only adjacent blocks
					if(!(Math.abs(i) == 1 ? (Math.abs(j) == 0 && Math.abs(k) == 0) : ((Math.abs(j) == 1) ? Math.abs(k) == 0 : Math.abs(k) == 1)))
						continue;

					Block localBlock = worldObj.getBlock(i + xCoord, j + yCoord, k + zCoord);

					if(BlockCapacityExpansion.isStorageExpansion(localBlock))
					{
						TileEntityStorageExpansion expansion = (TileEntityStorageExpansion)worldObj.getTileEntity(i + xCoord, j + yCoord, k + zCoord);
						if(expansion.tileType == upgradeId)
							return true;
						while(expansion.getChild() != null)
						{
							expansion = expansion.getChild();
							if(expansion.tileType == upgradeId)
								return true;
						}
					}
				}
			}
		}
		return false;
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
		return null;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);

		this.inventoryRows = tagCompound.getInteger("Rows");

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
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack stack) {
		if(i < this.inventoryRows * 11)
			return true;
		else
			return false;
	}

	public void setGuiDisplayName(String displayName) { }

}
