package com.pauljoda.modularsystems.enchanting.tiles;

import com.pauljoda.modularsystems.core.abstracts.ModularTileEntity;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TileEntityEnchantmentAlter extends ModularTileEntity implements ISidedInventory {

	public ItemStack[] inv;

	//Automation related 
	private static final int[] slots_top = new int[] {0};
	private static final int[] slots_bottom = new int[] {0};
	private static final int[] slots_sides = new int[] {};

	public TileEntityEnchantmentAlter(){
		inv = new ItemStack[26];
	}

	@Override
	public void updateEntity()
	{}

	@Override
	public int getSizeInventory() {
		return inv.length;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return inv[i];
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
		return inv[0] == null;
	}

	public int countBlocksInRange(int range, Block block)
	{
		int count = 0;
		for(int x = -range; x <= range; x++)
		{
			for(int y = 0; y <= range; y++)
			{
				for(int z = -range; z <= range; z++)
				{
                    if(block != Blocks.bookshelf)
                    {
                        if (worldObj.getBlock(this.xCoord + x, this.yCoord + y, this.zCoord + z) == block)
                        count++;
                    }
                    else
                        count += worldObj.getBlock(this.xCoord + x, this.yCoord + y, this.zCoord + z).getEnchantPowerBonus(worldObj, this.xCoord + x, this.yCoord + y, this.zCoord + z);

				}
			}
		}
		return count;
	}
	public boolean canPlaceUpgrade(ItemStack item)
	{
		if(!(item.getItem() instanceof ItemEnchantedBook))
			return false;

        List enchants = getEnchantsUnlocked();
		for(int i = 0; i < inv.length; i++)
		{
			if(inv[i] != null && enchants != null)
            {
                Map map = EnchantmentHelper.getEnchantments(item);
                Iterator iterator = map.keySet().iterator();
                while(iterator.hasNext())
                {
                    int loc = ((Integer) iterator.next()).intValue();
                    Enchantment enchant = Enchantment.enchantmentsList[loc];
                    if (enchants.contains(enchant))
                        return false;
                }
            }
		}
		return true;
	}

	public List getEnchantsUnlocked()
	{
		List enchants = new ArrayList<Enchantment>();
		for(int i = 0; i < inv.length; i++)
		{
			if(inv[i] != null && inv[i].getItem() instanceof ItemEnchantedBook)
            {
                Map map = EnchantmentHelper.getEnchantments(inv[i]);
                Iterator iterator = map.keySet().iterator();
                while(iterator.hasNext())
                {
                    int loc = ((Integer) iterator.next()).intValue();
                    Enchantment enchant = Enchantment.enchantmentsList[loc];
                    enchants.add(enchant);
                }
            }
		}
		return enchants;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		switch(side)
		{
		case 0 : return slots_bottom;
		case 1 : return slots_top;
		default : return slots_sides;
		}
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack item, int side) {
		return this.isItemValidForSlot(slot, item);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack item, int side) {
		return slot == 0;
	}
}
