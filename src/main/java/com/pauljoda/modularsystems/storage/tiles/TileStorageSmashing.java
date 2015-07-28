package com.pauljoda.modularsystems.storage.tiles;

import com.pauljoda.modularsystems.storage.container.ContainerStorageSmashing;
import com.pauljoda.modularsystems.storage.gui.GuiStorageSmashing;
import com.teambr.bookshelf.collections.InventoryTile;
import com.teambr.bookshelf.collections.Location;
import com.teambr.bookshelf.common.blocks.rotation.SixWayRotation;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/27/2015
 */
public class TileStorageSmashing extends TileEntityStorageExpansion implements IInventory {

    protected InventoryTile pickSlot;

    public TileStorageSmashing() {
        pickSlot = new InventoryTile(1);
    }

    @Override
    public void addedToNetwork() {}

    @Override
    public void removedFromNetwork() {
        ItemStack itemStack = pickSlot.getStackInSlot(0);

        if (itemStack != null && itemStack.stackSize > 0) {
            float rx = worldObj.rand.nextFloat() * 0.8F + 0.1F;
            float ry = worldObj.rand.nextFloat() * 0.8F + 0.1F;
            float rz = worldObj.rand.nextFloat() * 0.8F + 0.1F;

            EntityItem entityItem = new EntityItem(worldObj,
                    xCoord + rx, yCoord + ry, zCoord + rz,
                    new ItemStack(itemStack.getItem(), itemStack.stackSize, itemStack.getItemDamage()));

            if (itemStack.hasTagCompound())
                entityItem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());

            float factor = 0.05F;
            entityItem.motionX = worldObj.rand.nextGaussian() * factor;
            entityItem.motionY = worldObj.rand.nextGaussian() * factor + 0.2F;
            entityItem.motionZ = worldObj.rand.nextGaussian() * factor;
            worldObj.spawnEntityInWorld(entityItem);

            itemStack.stackSize = 0;
        }
    }

    /*******************************************************************************************************************
     *********************************************** Tile Methods ******************************************************
     *******************************************************************************************************************/

    @Override
    @SuppressWarnings("unchecked")
    public void updateEntity() {
        super.updateEntity();
        if(!worldObj.isRemote && !isPowered() && getCore() != null && pickSlot.getStackInSlot(0) != null && worldObj.rand.nextInt(20) == 0) {
            SixWayRotation rotation = new SixWayRotation();
            ForgeDirection dir = rotation.convertMetaToDirection(worldObj.getBlockMetadata(xCoord, yCoord, zCoord));
            Location blockBreakLocation = new Location(this);
            blockBreakLocation.travel(dir);
            if(!worldObj.isAirBlock(blockBreakLocation.x, blockBreakLocation.y, blockBreakLocation.z)) {
                Block toBreak = worldObj.getBlock(blockBreakLocation.x, blockBreakLocation.y, blockBreakLocation.z);
                int harvestLevel = pickSlot.getStackInSlot(0).getItem().getHarvestLevel(pickSlot.getStackInSlot(0), "pickaxe");
                if(toBreak.getHarvestLevel(worldObj.getBlockMetadata(blockBreakLocation.x, blockBreakLocation.y, blockBreakLocation.z)) <= harvestLevel) {

                    int fortune = 0;

                    NBTTagList enchantList = pickSlot.getStackInSlot(0).getEnchantmentTagList();
                    if(enchantList != null) {
                        for (int i = 0; i < enchantList.tagCount(); i++) {
                            NBTTagCompound tag = enchantList.getCompoundTagAt(0);
                            if (tag.hasKey("id") && tag.getInteger("id") == 35)
                                fortune = tag.getInteger("lvl");
                        }
                    }

                    ArrayList<ItemStack> itemStacks = toBreak.getDrops(worldObj, blockBreakLocation.x, blockBreakLocation.y, blockBreakLocation.z,
                            worldObj.getBlockMetadata(blockBreakLocation.x, blockBreakLocation.y, blockBreakLocation.z), fortune);

                    for(ItemStack itemStack : itemStacks) {
                        if (itemStack != null && itemStack.stackSize > 0) {
                            float rx = worldObj.rand.nextFloat() * 0.8F + 0.1F;
                            float ry = worldObj.rand.nextFloat() * 0.8F + 0.1F;
                            float rz = worldObj.rand.nextFloat() * 0.8F + 0.1F;

                            EntityItem entityItem = new EntityItem(worldObj,
                                    blockBreakLocation.x + rx, blockBreakLocation.y + ry, blockBreakLocation.z + rz,
                                    new ItemStack(itemStack.getItem(), itemStack.stackSize, itemStack.getItemDamage()));

                            if (itemStack.hasTagCompound())
                                entityItem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());

                            float factor = 0.05F;
                            entityItem.motionX = worldObj.rand.nextGaussian() * factor;
                            entityItem.motionY = worldObj.rand.nextGaussian() * factor + 0.2F;
                            entityItem.motionZ = worldObj.rand.nextGaussian() * factor;
                            worldObj.spawnEntityInWorld(entityItem);

                            itemStack.stackSize = 0;
                        }
                    }
                    worldObj.playAuxSFX(2001, blockBreakLocation.x, blockBreakLocation.y, blockBreakLocation.z, Block.getIdFromBlock(toBreak) + ( worldObj.getBlockMetadata(blockBreakLocation.x, blockBreakLocation.y, blockBreakLocation.z) << 12));
                    worldObj.setBlockToAir(blockBreakLocation.x, blockBreakLocation.y, blockBreakLocation.z);
                    pickSlot.getStackInSlot(0).setItemDamage(pickSlot.getStackInSlot(0).getItemDamage() + 1);
                    if(pickSlot.getStackInSlot(0).getItemDamage() > pickSlot.getStackInSlot(0).getMaxDamage())
                        pickSlot.setStackInSlot(null, 0);
                }
            }
        }
    }

    @Override
    public void readFromNBT (NBTTagCompound tag) {
        super.readFromNBT(tag);
        pickSlot.readFromNBT(tag);
    }

    @Override
    public void writeToNBT (NBTTagCompound tag) {
        super.writeToNBT(tag);
        pickSlot.writeToNBT(tag);
    }

    /*******************************************************************************************************************
     ***************************************** IInventory Methods ******************************************************
     *******************************************************************************************************************/

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return pickSlot.getStackInSlot(slot);
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        if(pickSlot.getStackInSlot(slot) != null) {
            ItemStack returnStack;
            if(pickSlot.getStackInSlot(slot).stackSize <= amount) {
                returnStack = pickSlot.getStackInSlot(slot);
                pickSlot.setStackInSlot(null, slot);
                this.markDirty();
                return returnStack;
            } else {
                returnStack = pickSlot.getStackInSlot(slot).splitStack(amount);
                if(pickSlot.getStackInSlot(slot).stackSize <= 0)
                    pickSlot.setStackInSlot(null, slot);
                this.markDirty();
                return returnStack;
            }
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        return pickSlot.getStackInSlot(slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        pickSlot.setStackInSlot(stack, slot);
    }

    /**
     * Returns the name of the inventory
     */
    public String getInventoryName() {
        return StatCollector.translateToLocal("inventory.storageSmashing.title");
    }

    /**
     * Returns if the inventory is named
     */
    public boolean hasCustomInventoryName() {
        return true;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    /**
     * Do not give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer player) {
        return getCore() != null;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return stack.getItem() instanceof ItemPickaxe;
    }

    /*******************************************************************************************************************
     ****************************************** IOpensGui Methods ******************************************************
     *******************************************************************************************************************/

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return getCore() != null ? new ContainerStorageSmashing(player.inventory, this) : null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return getCore() != null ? new GuiStorageSmashing(new ContainerStorageSmashing(player.inventory, this)) : null;
    }
}
