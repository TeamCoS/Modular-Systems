package com.teamcos.modularsystems.furnace.containers;

import com.teamcos.modularsystems.furnace.tiles.TileEntityFurnaceCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;

public class ContainerModularFurnace extends Container
{
    private TileEntityFurnaceCore tileEntity;
    private int lastCookTime;
    private int lastBurnTime;
    private int lastItemBurnTime;

    public ContainerModularFurnace(InventoryPlayer playerInventory, TileEntityFurnaceCore tileEntity)
    {
        this.tileEntity = tileEntity;

        // Input
        addSlotToContainer(new Slot(tileEntity, 0, 56, 17));

        // Fuel
        addSlotToContainer(new Slot(tileEntity, 1, 56, 53));

        // Output
        addSlotToContainer(new SlotFurnace(playerInventory.player, tileEntity, 2, 116, 35));

        bindPlayerInventory(playerInventory);
    }

    @Override
    public void addCraftingToCrafters(ICrafting par1ICrafting)
    {
        super.addCraftingToCrafters(par1ICrafting);
        par1ICrafting.sendProgressBarUpdate(this, 0, this.tileEntity.getFurnaceCookTime());
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
        } else if (par1 == 1) {
            tileEntity.setFurnaceBurnTime(par2);
        } else if (par1 == 2) {
            tileEntity.setCurrentItemBurnTime(par2);
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer)
    {
        return tileEntity.isUseableByPlayer(entityPlayer);
    }

    private void bindPlayerInventory(InventoryPlayer playerInventory)
    {
        // Inventory
        for(int y = 0; y < 3; y++)
            for(int x = 0; x < 9; x++)
                addSlotToContainer(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));

        // Action Bar
        for(int x = 0; x < 9; x++)
            addSlotToContainer(new Slot(playerInventory, x, 8 + x * 18, 142));
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

}
