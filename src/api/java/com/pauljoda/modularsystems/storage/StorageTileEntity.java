package com.pauljoda.modularsystems.storage;

import com.pauljoda.modularsystems.helpers.Coord;
import com.pauljoda.modularsystems.helpers.LocalBlockCollections;
import com.pauljoda.modularsystems.helpers.WorldUtil;
import com.pauljoda.modularsystems.tiles.TileLocatable;
import com.pauljoda.modularsystems.utilities.tiles.ModularTileEntity;
import net.minecraft.block.Block;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import java.util.HashSet;
import java.util.Set;

public abstract class StorageTileEntity extends ModularTileEntity implements IInventory, IEntitySelector {

    public int coreX;
    public int coreY;
    public int coreZ;

    int cooldown = 30;
    public boolean isConnected;


    public StorageTileEntity()
    {	}

    public abstract void addToCore(Block block);

    protected abstract void onStorageDisconnect();

    protected abstract void onStorageConnect();

    public TileEntityStorageCore getCore()
    {
        TileEntity tileEntity = worldObj.getTileEntity(coreX, coreY, coreZ);
        if (tileEntity instanceof TileEntityStorageCore) {
            return (TileEntityStorageCore) tileEntity;
        } else {
            return null;
        }
    }

    public void setCore(TileEntityStorageCore core)
    {
        coreX = core.getX();
        coreY = core.getY();
        coreZ = core.getZ();
    }

    public void invalidateCore()
    {
        onStorageDisconnect();

        isConnected = false;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void validateCore()
    {
        onStorageConnect();

        isConnected = true;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        coreX = tagCompound.getInteger("coreX");
        coreY = tagCompound.getInteger("coreY");
        coreZ = tagCompound.getInteger("coreZ");

        isConnected = tagCompound.getBoolean("isConnected");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        tagCompound.setInteger("coreX", coreX);
        tagCompound.setInteger("coreY", coreY);
        tagCompound.setInteger("coreZ", coreZ);

        tagCompound.setBoolean("isConnected", isConnected);
    }

    public boolean isConnected()
    {
        if (worldObj.isRemote) return false;
        return isConnected(new HashSet<Coord>());
    }

    public boolean isConnected(Set<Coord> coords)
    {
        if (!worldObj.isRemote && getCore() != null && coords.add(new Coord(this))) {
            for (Coord coord : LocalBlockCollections.getAdjacentBlocks()) {
                TileEntity tileEntity = worldObj.getTileEntity(xCoord + coord.x, yCoord + coord.y, zCoord + coord.z);
                if (tileEntity instanceof StorageTileEntity) {
                    StorageTileEntity buddy = (StorageTileEntity) tileEntity;
                    if (buddy.getCore() != null && buddy.isConnected &&
                            WorldUtil.areSame(this.getCore(), buddy.getCore()) && buddy.isConnected(coords)) {
                        return true;
                    }
                } else if (tileEntity instanceof TileEntityStorageCore) {
                    if (WorldUtil.areSame(this.getCore(), new TileLocatable(tileEntity))) return true;
                }
            }
        }
        return false;
    }

    @Override
    public void updateEntity()
    {
        //Check for connections
        if(cooldown < 0 && !worldObj.isRemote)
        {
            //This is slightly different behavior, but waiting for the next tick will hardly break the system.
            boolean connectedCheck = isConnected();
            if(connectedCheck && isConnected) invalidateCore();
            else if(connectedCheck && !isConnected) validateCore();
            cooldown = 20;
        }
        else if(cooldown > 20 && !worldObj.isRemote)
            isConnected = isConnected();

        cooldown--;
    }

    protected void tryInsertSlot(ItemStack stack)
    {
        if(getCore() != null && isConnected)
        {
            for(int i = 0; i < getCore().getInventoryRows() * 11; i++)
            {
                if(getCore().isItemValidForSlot(i, stack))
                {
                    ItemStack targetStack = getCore().getStackInSlot(i);
                    if (targetStack == null) {
                        getCore().setInventorySlotContents(i, stack.copy());
                        stack.stackSize = 0;
                        markDirty();
                        return;
                    }
                    else if (getCore().isItemValidForSlot(i, stack) &&
                            StorageHelper.areMergeCandidates(stack, targetStack)) {
                        int space = targetStack.getMaxStackSize()
                                - targetStack.stackSize;
                        int mergeAmount = Math.min(space, stack.stackSize);
                        ItemStack copy = targetStack.copy();
                        copy.stackSize += mergeAmount;
                        getCore().setInventorySlotContents(i, copy);
                        stack.stackSize -= mergeAmount;
                        markDirty();
                        return;
                    }
                }

            }
        }
    }

    @Override
    public int getSizeInventory() {
        if(getCore() != null && isConnected)
            return getCore().getSizeInventory();
        else
            return 0;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        if(getCore() != null && isConnected)
            return getCore().getStackInSlot(i);
        else
            return null;
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        if(getCore() != null && isConnected)
            return getCore().decrStackSize(i, j);
        else
            return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        if(getCore() != null && isConnected)
            return getCore().getStackInSlotOnClosing(i);
        else
            return null;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        if(getCore() != null && isConnected)
            getCore().setInventorySlotContents(i, itemstack);
    }

    @Override
    public int getInventoryStackLimit() {
        if(getCore() != null && isConnected)
            return getCore().getInventoryStackLimit();
        else
            return 0;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && entityplayer.getDistanceSq((double) xCoord + 0.5, (double) yCoord + 0.5, (double) zCoord + 0.5) <= 64.0;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        if(getCore() != null && isConnected)
            return getCore().isItemValidForSlot(i, itemstack);
        else
            return false;
    }

    @Override
    public String getInventoryName() {
        if(getCore() != null && isConnected)
            return getCore().getInventoryName();
        else
            return null;
    }

    @Override
    public boolean hasCustomInventoryName() {
        if(getCore() != null && isConnected)
            return getCore().hasCustomInventoryName();
        else
            return false;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
    }

    @Override
    public boolean isEntityApplicable(Entity p_82704_1_) {
        return false;
    }
}
