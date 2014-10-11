package com.pauljoda.modularsystems.furnace.tiles;

import com.pauljoda.modularsystems.core.tiles.ModularTileEntity;
import com.pauljoda.modularsystems.core.util.Coord;
import com.pauljoda.modularsystems.core.util.InventoryUtil;
import com.pauljoda.modularsystems.core.util.WorldUtil;
import com.pauljoda.modularsystems.furnace.blocks.BlockFurnaceDummyIO;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityFurnaceDummy extends ModularTileEntity implements ISidedInventory
{
    public int slot = 4;
    int coreX;
    int coreY;
    int coreZ;
    int icon = 1;
    int metadata = 0;
    int coolDown = 80;

    public TileEntityFurnaceDummy()
    {
    }

    @Override
    public void updateEntity()
    {
        if(coolDown < 0)
        {
            if (worldObj.getBlock(xCoord, yCoord, zCoord) instanceof BlockFurnaceDummyIO && !worldObj.isRemote && slot == 2)
            {
                for(int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++)
                {
                    Coord check = new Coord(xCoord, yCoord, zCoord).offset(ForgeDirection.VALID_DIRECTIONS[i]);

                    if (worldObj.getTileEntity(check.x, check.y, check.z) instanceof IInventory && getCore() != null)
                    {
                        if(worldObj.getTileEntity(check.x, check.y, check.z) instanceof TileEntityFurnaceDummy)
                        {
                            TileEntityFurnaceDummy dummy = (TileEntityFurnaceDummy)worldObj.getTileEntity(check.x, check.y, check.z);
                            if(dummy.getCore() != null)
                            {
                                if (WorldUtil.areTilesSame(dummy.getCore(), this.getCore()))
                                    continue;
                            }
                            else
                                continue;
                        }

                        else if(worldObj.getTileEntity(check.x, check.y, check.z) instanceof TileEntityFurnaceCore)
                        {
                            if(WorldUtil.areTilesSame(worldObj.getTileEntity(check.x, check.y, check.z), this.getCore()))
                                continue;
                        }

                        if (getCore().getStackInSlot(2) != null)
                            InventoryUtil.moveItemInto(getCore(), 2, worldObj.getTileEntity(check.x, check.y, check.z), -1, 64, ForgeDirection.UP, true, true);
                    }
                }
            }
            coolDown = 80;
        }
        coolDown--;
    }

    public void setCore(TileEntityFurnaceCore core)
    {
        coreX = core.xCoord;
        coreY = core.yCoord;
        coreZ = core.zCoord;
    }

    public TileEntityFurnaceCore getCore()
    {
        return (TileEntityFurnaceCore)worldObj.getTileEntity(coreX, coreY, coreZ);
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
        if(getCore() != null)
            return getCore().getSizeInventory();
        else
            return 0;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        if(getCore() != null)
            return getCore().getStackInSlot(i);
        else
            return null;
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        if(getCore() != null)
            return getCore().decrStackSize(i, j);
        else
            return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        if(getCore() != null)
            return getCore().getStackInSlotOnClosing(i);
        else
            return null;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        if(getCore() != null)
            getCore().setInventorySlotContents(i, itemstack);
    }

    @Override
    public int getInventoryStackLimit() {
        if(getCore() != null)
            return getCore().getInventoryStackLimit();
        else
            return 0;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return getCore() != null;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        if(getCore() != null)
            return getCore().isItemValidForSlot(i, itemstack);
        else
            return false;
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
        if(getCore() != null)
            return getCore().getAccessibleSlotsFromSide(var1);
        else
            return new int[] {0};
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        if(getCore() != null)
            return getCore().isItemValidForSlot(i, itemstack);
        else
            return false;
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        if(getCore() != null)
        {
            if(slot <= 3)
            {
                j = slot;
                return getCore().canExtractItem(i, itemstack, j);
            }
            else
            {
                return getCore().canExtractItem(i, itemstack, j);
            }
        }
        return false;
    }

    @Override
    public String getInventoryName() {
        if(getCore() != null)
            return getCore().getInventoryName();
        else
            return "";
    }

    @Override
    public boolean hasCustomInventoryName() {
        if(getCore() != null)
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
}
