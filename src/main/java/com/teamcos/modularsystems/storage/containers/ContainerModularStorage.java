package com.teamcos.modularsystems.storage.containers;

import com.teamcos.modularsystems.core.inventory.SlotArmor;
import com.teamcos.modularsystems.storage.tiles.TileEntityStorageCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ContainerModularStorage extends Container {

	public TileEntityStorageCore storageCore;
	public List itemList = new ArrayList();
	public int currentBottomRow = 0;
	public boolean hasArmorViewable;
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
    public IInventory craftResult = new InventoryCraftResult();
    public World worldObj;

	public ContainerModularStorage(InventoryPlayer playerInventory, TileEntityStorageCore tileEntityStorageCore, final EntityPlayer thePlayer, boolean hasArmorUpgrade, boolean hasCraftingVisible)
	{
		storageCore = tileEntityStorageCore;
		hasArmorViewable = hasArmorUpgrade;
        worldObj = storageCore.getWorldObj();

		for (int i = 0; i < storageCore.inventoryRows; i++)
		{
			for (int j = 0; j < 11; j++)
			{
				if(i < 6)
					addSlotToContainer(new Slot(tileEntityStorageCore, j + i * 11, 68 + j * 18, 18 + i * 18));
				else
					addSlotToContainer(new Slot(tileEntityStorageCore, j + i * 11, -1000, -1000));
			}
		}

		for (int i = 0; i < 4; ++i)
		{
			if(hasArmorViewable)
				this.addSlotToContainer(new SlotArmor(playerInventory, playerInventory.getSizeInventory() - 1 - i, 259, 143 + i * 18, i, thePlayer));
			else
				this.addSlotToContainer(new SlotArmor(playerInventory, playerInventory.getSizeInventory() - 1 - i, -1000, -1000, i, thePlayer));    

		}

		bindPlayerInventory(playerInventory);

        if(hasCraftingVisible)
        {
            this.addSlotToContainer(new SlotCrafting(playerInventory.player, this.craftMatrix, this.craftResult, 1000, 10, 124));
            for (int i = 0; i < 3; ++i)
            {
                for (int i1 = 0; i1 < 3; ++i1)
                {
                    this.addSlotToContainer(new Slot(this.craftMatrix, i1 + i * 3, 6 + i1 * 18, 154 + i * 18));
                }
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

    public boolean func_94530_a(ItemStack par1ItemStack, Slot par2Slot)
    {
        return par2Slot.inventory != this.craftResult && super.func_94530_a(par1ItemStack, par2Slot);
    }

	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
						86 + j * 18, 140 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventoryPlayer, i, 86 + i * 18, 198));
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		ItemStack stack = null;
		Slot slotObject = (Slot)this.inventorySlots.get(slot);
		//null checks and checks if the item can be stacked (maxStackSize > 1)
		if (slotObject != null && slotObject.getHasStack()) {
			ItemStack stackInSlot = slotObject.getStack();
			stack = stackInSlot.copy();
			//merges the item into player inventory since its in the tileEntity
			if (slot < storageCore.inventoryRows * 11) {
				if (!this.mergeItemStack(stackInSlot, (storageCore.inventoryRows * 11) + 4, (storageCore.inventoryRows * 11) + 40, true)) {
					return null;
				}
			}
			//places it into the tileEntity if possible since its in the player inventory
			else if (!this.mergeItemStack(stackInSlot, 0, storageCore.inventoryRows * 11, false)) {
				return null;
			}

			if (stackInSlot.stackSize == 0) {
				slotObject.putStack(null);
			} else {
				slotObject.onSlotChanged();
			}

			if (stackInSlot.stackSize == stack.stackSize) {
				return null;
			}
			slotObject.onPickupFromSlot(player, stackInSlot);
		}
		return stack;
	}

	public void scrollTo(float index)
	{
		int i = storageCore.inventoryRows - 6;
		int j = (int)((double)(index * (float)i) + 0.5D);
		currentBottomRow = j;
		if (j < 0)
		{
			j = 0;
		}

		for(int it = 0; it < this.storageCore.inventoryRows * 11; it++)
		{
			Slot slot = (Slot)this.inventorySlots.get(it);
			slot.xDisplayPosition = -1000;
			slot.yDisplayPosition = -1000;
		}

		for (int f = 0; f < 6; f++)
		{
			for (int s = 0; s < 11; s++)
			{
				Slot slot = (Slot)this.inventorySlots.get((s + (f+j) * 11));
				slot.xDisplayPosition = 68 + s * 18;
				slot.yDisplayPosition = 18 + f * 18;
			}
		}
	}
	
	public void sortInventoryAlphabetically()
	{
		storageCore.sortInventoryAlphabetically();
	}
	
	public void sortInventoryByIndex()
	{
		storageCore.sortInventoryByIndex();
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		return true;
	}
}
