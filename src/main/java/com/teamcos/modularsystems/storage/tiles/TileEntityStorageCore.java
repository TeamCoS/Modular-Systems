package com.teamcos.modularsystems.storage.tiles;

import com.teamcos.modularsystems.core.helper.ConfigHelper;
import com.teamcos.modularsystems.core.lib.Reference;
import com.teamcos.modularsystems.core.tiles.ModularTileEntity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class TileEntityStorageCore extends ModularTileEntity implements IInventory {

	public ItemStack[] inv;
	public int inventoryRows = 6;
	private final int MAX_EXPANSIONS = ConfigHelper.maxExpansionSize;

    public boolean hasSortingUpgrade;
    public boolean hasArmorUpgrade;
    public boolean hasCraftingUpgrade;

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
							if(inv[j].stackSize > mergeSize)
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
					if(temp2 < temp)
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
							if(inv[j].stackSize > mergeSize)
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
		for (int i1 = inventoryRows * 11 - 11; i1 < inventoryRows * 11 && i1 < ConfigHelper.maxExpansionSize * 11 ; ++i1)
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
        switch(upgradeId)
        {
            case Reference.ARMOR_STORAGE_EXPANSION :
                return hasArmorUpgrade;
            case Reference.SORTING_STORAGE_EXPANSION :
                return hasSortingUpgrade;
            case Reference.CRAFTING_STORAGE_EXPANSION :
                return hasCraftingUpgrade;
            default :
                return false;
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
		if(slot < ConfigHelper.maxExpansionSize * 11)
			return inv[slot];
		else
			return null;
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

        this.hasArmorUpgrade = tagCompound.getBoolean("hasArmorUpgrade");
        this.hasSortingUpgrade = tagCompound.getBoolean("hasSortingUpgrade");
        this.hasCraftingUpgrade = tagCompound.getBoolean("hasCraftingUpgrade");

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

        tagCompound.setBoolean("hasArmorUpgrade", this.hasArmorUpgrade);
        tagCompound.setBoolean("hasSortingUpgrade", this.hasSortingUpgrade);
        tagCompound.setBoolean("hasCraftingUpgrade", this.hasCraftingUpgrade);

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
        return i < this.inventoryRows * 11;
	}

	public void setGuiDisplayName(String displayName) { }

}
