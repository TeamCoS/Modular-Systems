package com.teamcos.modularsystems.utilities.tiles;

import com.teamcos.modularsystems.core.util.InventoryUtil;
import com.teamcos.modularsystems.core.util.WorldUtil;
import com.teamcos.modularsystems.helpers.Coord;
import com.teamcos.modularsystems.helpers.LocalBlockCollections;
import com.teamcos.modularsystems.utilities.block.DummyIOBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class DummyTile extends ModularTileEntity implements ISidedInventory {

    private Coord coreLoc;
    private int icon = 1;
    private int coolDown = 80;
    private int slot = 4;
    private int metadata = 0;

    public DummyTile() {}

    public FueledRecipeTile getCore() {
        TileEntity te = worldObj.getTileEntity(coreLoc.x, coreLoc.y, coreLoc.z);
        if (te instanceof FueledRecipeTile) {
            return (FueledRecipeTile) te;
        } else {
            return null;
        }
    }

    public Block getBlock() {
        if(Block.getBlockById(this.icon) == null) {
            return Blocks.cobblestone;
        }

        return Block.getBlockById(this.icon);
    }

    /******************************************************************************************************************
     **************************************************  Tile Methods  ************************************************
     ******************************************************************************************************************/

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        coreLoc = Coord.readFromNBT(tagCompound);

        slot = tagCompound.getInteger("Slot");
        icon = tagCompound.getInteger("Icon");
        metadata = tagCompound.getInteger("Meta");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);

        coreLoc.writeToNBT(tagCompound);

        tagCompound.setInteger("Slot", slot);
        tagCompound.setInteger("Icon", icon);
        tagCompound.setInteger("Meta", metadata);
    }

    @Override
    public void updateEntity() {
        if (getCore() == null) {
            worldObj.setBlock(xCoord, yCoord, zCoord, getBlock());
        } else if(coolDown < 0) {
            if (worldObj.getBlock(xCoord, yCoord, zCoord) instanceof DummyIOBlock && !worldObj.isRemote && slot == 2) {
                for (Coord dir : LocalBlockCollections.getAdjacentBlocks()) {
                    int x = xCoord + dir.x;
                    int y = yCoord + dir.y;
                    int z = zCoord + dir.z;
                    TileEntity te = worldObj.getTileEntity(x, y, z);
                    FueledRecipeTile core = getCore();
                    if (te instanceof IInventory && core != null) {
                        if(te instanceof DummyTile) {
                            DummyTile dummy = (DummyTile) te;
                            if(dummy.getCore() != null) {
                                if (WorldUtil.areTilesSame(dummy.getCore(), this.getCore())) {
                                    continue;
                                }
                            } else {
                                continue;
                            }
                        } else if (te instanceof FueledRecipeTile) {
                            if(WorldUtil.areTilesSame(te, core)) {
                                continue;
                            }
                        }

                        if (core.getStackInSlot(2) != null) {
                            InventoryUtil.moveItemInto(core, 2, te, -1, 64, ForgeDirection.UP, true, true);
                        }
                    }
                }
            }
            coolDown = 80;
        }
        coolDown--;
    }

    /*****************************************************************************************************************
     *********************************************** Inventory methods ***********************************************
     *****************************************************************************************************************/

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

    @Override
    public int[] getAccessibleSlotsFromSide(int var1) {
        if(slot != 4) {
            if(slot == 0) {
                var1 = 0;
            } else if(slot == 1) {
                var1 = 1;
            } else if(slot == 2) {
                var1 = 2;
            }
        }
        if(getCore() != null) {
            return getCore().getAccessibleSlotsFromSide(var1);
        } else {
            return new int[]{0};
        }
    }

    /******************************************************************************************************************
     *************************************************  Helper Methods  ***********************************************
     ******************************************************************************************************************/

    public void setBlock(int id) {
        this.icon = id;
    }

    public void setMetadata(int metadata) {
        this.metadata = metadata;
    }

    public int getMetadata() {
        return metadata;
    }

    public void setCore(FueledRecipeTile core) {
        this.coreLoc = new Coord(core);
    }

    public void unsetCore() {
        this.coreLoc = null;
    }
}
