package com.teamcos.modularsystems.furnace.containers;

import com.teamcos.modularsystems.furnace.tiles.TileEntityFurnaceCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;

public class ContainerModularFurnaceCrafter extends Container
{
	private TileEntityFurnaceCore tileEntity;
	private int lastCookTime = 0;
	private int lastBurnTime = 0;
	private int lastItemBurnTime = 0;

	public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
	public IInventory craftResult = new InventoryCraftResult();
	private World worldObj;

	public ContainerModularFurnaceCrafter(InventoryPlayer par1InventoryPlayer, TileEntityFurnaceCore tileEntity)
	{

		this.worldObj = tileEntity.getWorldObj();
		int i;
		int i1;
		this.tileEntity = tileEntity;

		// Input
		addSlotToContainer(new Slot(tileEntity, 0, 88, 29));

		// Fuel
		addSlotToContainer(new Slot(tileEntity, 1, 88, 65));

		//Output
		addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player, tileEntity, 2, 148, 47));

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

        this.addSlotToContainer(new SlotCrafting(par1InventoryPlayer.player, this.craftMatrix, this.craftResult, 47, 9, 45));
        for (i = 0; i < 3; ++i)
        {
            for (i1 = 0; i1 < 3; ++i1)
            {
                this.addSlotToContainer(new Slot(this.craftMatrix, i1 + i * 3, 32 + i1 * 18, 9 + i * 18));
            }
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
		par1ICrafting.sendProgressBarUpdate(this, 47, this.tileEntity.getFurnaceCookTime());
		par1ICrafting.sendProgressBarUpdate(this, 1, this.tileEntity.getFurnaceBurnTime());
		par1ICrafting.sendProgressBarUpdate(this, 2, this.tileEntity.getCurrentItemBurnTime());
	}

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.crafters.size(); ++i)
        {
            ICrafting icrafting = (ICrafting)this.crafters.get(i);

            if (this.lastCookTime != this.tileEntity.getFurnaceCookTime())
            {

                icrafting.sendProgressBarUpdate(this, 0, this.tileEntity.getFurnaceCookTime());
            }

            if (this.lastBurnTime != this.tileEntity.getFurnaceBurnTime())
            {
                icrafting.sendProgressBarUpdate(this, 1, this.tileEntity.getFurnaceBurnTime());
            }

            if (this.lastItemBurnTime != this.tileEntity.getCurrentItemBurnTime())
            {
                icrafting.sendProgressBarUpdate(this, 2, this.tileEntity.getCurrentItemBurnTime());
            }
        }

        this.lastCookTime = this.tileEntity.getFurnaceCookTime();
        this.lastBurnTime = this.tileEntity.getFurnaceBurnTime();
        this.lastItemBurnTime = this.tileEntity.getCurrentItemBurnTime();
    }

    @Override
    public void updateProgressBar(int par1, int par2)
    {
        if (par1 == 0) {
            tileEntity.setFurnaceCookTime(par2);
        }

        if (par1 == 1)
        {
            tileEntity.setFurnaceBurnTime(par2);
        }

        if (par1 == 2)
        {
            tileEntity.setCurrentItemBurnTime(par2);
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

            if (par2 == 2)
            {
                if (!this.mergeItemStack(itemstack1, 3, 39, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (par2 != 1 && par2 != 0)
            {
                if (FurnaceRecipes.smelting().getSmeltingResult(itemstack1) != null)
                {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false))
                    {
                        return null;
                    }
                }
                else if (TileEntityFurnace.isItemFuel(itemstack1))
                {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false))
                    {
                        return null;
                    }
                }
                else if (par2 >= 3 && par2 < 30)
                {
                    if (!this.mergeItemStack(itemstack1, 30, 39, false))
                    {
                        return null;
                    }
                }
                else if (par2 >= 30 && par2 < 39 && !this.mergeItemStack(itemstack1, 3, 30, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 3, 39, false))
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
