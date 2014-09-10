package com.pauljoda.modularsystems.furnace.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;

import com.pauljoda.modularsystems.furnace.tiles.TileEntityFurnaceCore;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerModularFurnaceCrafter extends Container
{
	private TileEntityFurnaceCore tileEntity;
	private int lastCookTime = 0;
	private int lastBurnTime = 0;
	private int lastItemBurnTime = 0;

	public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
	public IInventory craftResult = new InventoryCraftResult();
	private World worldObj;

	public ContainerModularFurnaceCrafter(InventoryPlayer par1InventoryPlayer, World par2World, int par3, int par4, int par5, TileEntityFurnaceCore tileEntity)
	{

		this.worldObj = par2World;
		int i;
		int i1;
		this.tileEntity = tileEntity;

		// Input
		addSlotToContainer(new Slot(tileEntity, 0, 88, 29));

		// Fuel
		addSlotToContainer(new Slot(tileEntity, 1, 88, 65));

		//Output
		addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player, tileEntity, 2, 148, 47));


		this.addSlotToContainer(new SlotCrafting(par1InventoryPlayer.player, this.craftMatrix, this.craftResult, 47, 9, 45));
		for (i = 0; i < 3; ++i)
		{
			for (i1 = 0; i1 < 3; ++i1)
			{
				this.addSlotToContainer(new Slot(this.craftMatrix, i1 + i * 3, 32 + i1 * 18, 9 + i * 18));
			}
		}


		for (i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(par1InventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new Slot(par1InventoryPlayer, i, 8 + i * 18, 142));
		}
	}


	public void onCraftMatrixChanged(IInventory par1IInventory)
	{
		this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.worldObj));
	}

	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer)
	{
		super.onContainerClosed(par1EntityPlayer);

		if (!this.worldObj.isRemote)
		{
			for (int i = 0; i < 9; ++i)
			{
				ItemStack itemstack = this.craftMatrix.getStackInSlotOnClosing(i);

				if (itemstack != null)
				{
					par1EntityPlayer.dropPlayerItemWithRandomChoice(itemstack, false);
				}
			}
		}
	}


	@Override
	public void addCraftingToCrafters(ICrafting par1ICrafting)
	{
		super.addCraftingToCrafters(par1ICrafting);
		par1ICrafting.sendProgressBarUpdate(this, 47, this.tileEntity.furnaceCookTime);
		par1ICrafting.sendProgressBarUpdate(this, 1, this.tileEntity.furnaceBurnTime);
		par1ICrafting.sendProgressBarUpdate(this, 2, this.tileEntity.currentItemBurnTime);
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();

		for (int i = 0; i < this.crafters.size(); ++i)
		{
			ICrafting icrafting = (ICrafting)this.crafters.get(i);

			if (this.lastCookTime != this.tileEntity.furnaceCookTime)
				icrafting.sendProgressBarUpdate(this, 0, this.tileEntity.furnaceCookTime);

			if (this.lastBurnTime != this.tileEntity.furnaceBurnTime)
				icrafting.sendProgressBarUpdate(this, 1, this.tileEntity.furnaceBurnTime);

			if (this.lastItemBurnTime != this.tileEntity.currentItemBurnTime)
				icrafting.sendProgressBarUpdate(this, 2, this.tileEntity.currentItemBurnTime);
		}

		this.lastCookTime = this.tileEntity.furnaceCookTime;
		this.lastBurnTime = this.tileEntity.furnaceBurnTime;
		this.lastItemBurnTime = this.tileEntity.currentItemBurnTime;
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2)
	{
		if (par1 == 0)
		{
			this.tileEntity.furnaceCookTime = par2;
		}

		if (par1 == 1)
		{
			this.tileEntity.furnaceBurnTime = par2;
		}

		if (par1 == 2)
		{
			this.tileEntity.currentItemBurnTime = par2;
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityPlayer)
	{
		return tileEntity.isUseableByPlayer(entityPlayer);
	}



	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot)this.inventorySlots.get(par2);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (par2 == 0 || par2 == 49)
			{
				if (!this.mergeItemStack(itemstack1, 10, 46, true))
				{
					return null;
				}

				slot.onSlotChange(itemstack1, itemstack);
			}
			else if (par2 >= 13 && par2 < 40)
			{
				if (!this.mergeItemStack(itemstack1, 40, 49, false))
				{
					return null;
				}
			}
			else if (par2 >= 40 && par2 < 49)
			{
				if (!this.mergeItemStack(itemstack1, 13, 40, false))
				{
					return null;
				}
			}
			else if (!this.mergeItemStack(itemstack1, 13, 49, false))
			{
				return null;
			}


			if (itemstack1.stackSize == 0)
			{
				slot.putStack((ItemStack)null);
			}
			else
			{
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize == itemstack.stackSize)
			{
				return null;
			}

			slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
		}

		return itemstack;
	}


	public boolean func_94530_a(ItemStack par1ItemStack, Slot par2Slot)
	{
		return par2Slot.inventory != this.craftResult && super.func_94530_a(par1ItemStack, par2Slot);
	}

}