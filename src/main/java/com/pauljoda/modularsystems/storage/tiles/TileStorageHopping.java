package com.pauljoda.modularsystems.storage.tiles;

import com.pauljoda.modularsystems.storage.gui.GuiStorageHopping;
import com.teambr.bookshelf.inventory.ContainerGeneric;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/27/2015
 */
public class TileStorageHopping extends TileStorageBasic {
    public int range;

    public TileStorageHopping() {
        range = 3;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if(!isPowered())
            hooverItems(range, 0.05D);
    }

    public void hooverItems(double range, double speed) {
        if(getCore() != null) {
            AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(xCoord - range, yCoord - range, zCoord - range, xCoord + range + 1, yCoord + range + 1, zCoord + range + 1);
            @SuppressWarnings("unchecked")
            List<Entity> interestingItems = worldObj.getEntitiesWithinAABB(EntityItem.class, bb);

            for (Entity entity : interestingItems) {
                double x = (xCoord + 0.5D - entity.posX);
                double y = (yCoord + 0.5D - entity.posY);
                double z = (zCoord + 0.5D - entity.posZ);

                double distance = Math.sqrt(x * x + y * y + z * z);
                if (distance < 1.1) {
                    onEntityCollidedWithBlock(entity);
                } else {
                    double var11 = 1.0 - distance / 15.0;
                    worldObj.spawnParticle("portal", entity.posX, entity.posY - 0.8D, entity.posZ, 0, 0, 0);

                    if (var11 > 0.0D) {
                        var11 *= var11;
                        entity.motionX += x / distance * var11 * speed;
                        entity.motionY += y / distance * var11 * speed * 4;
                        entity.motionZ += z / distance * var11 * speed;
                    }
                }

            }
        }
    }

    public void onEntityCollidedWithBlock(Entity entity) {
        if (!worldObj.isRemote) {
            if (entity instanceof EntityItem && !entity.isDead) {
                EntityItem item = (EntityItem)entity;
                ItemStack stack = item.getEntityItem().copy();
                tryInsertSlot(stack);
                if (stack.stackSize == 0) {
                    item.setDead();
                } else {
                    item.setEntityItemStack(stack);
                }
            }
        }
    }

    public void tryInsertSlot(ItemStack stack) {
        if(getCore() != null) {
            for(int i = 0; i < getCore().getSizeInventory(); i++) {
                if(getCore().isItemValidForSlot(i, stack)) {
                    ItemStack targetStack = getCore().getStackInSlot(i);
                    if (targetStack == null) {
                        getCore().setInventorySlotContents(i, stack.copy());
                        stack.stackSize = 0;
                        markDirty();
                        return;
                    }
                    else if (getCore().isItemValidForSlot(i, stack) &&
                            areMergeCandidates(stack, targetStack)) {
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

    public static boolean areItemAndTagEqual(final ItemStack stackA, ItemStack stackB) {
        return stackA.isItemEqual(stackB) && ItemStack.areItemStackTagsEqual(stackA, stackB);
    }

    public static boolean areMergeCandidates(ItemStack source, ItemStack target) {
        return areItemAndTagEqual(source, target) && target.stackSize < target.getMaxStackSize();
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        range = tag.getInteger("Range");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setInteger("Range", range);
    }

    /*******************************************************************************************************************
     ****************************************** IOpensGui Methods ******************************************************
     *******************************************************************************************************************/

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return getCore() != null ? (player.isSneaking() && getCore().canOpen(player) ? new ContainerGeneric(): getCore().getServerGuiElement(ID, player, world, x, y, z)) : null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return getCore() != null ? (player.isSneaking() && getCore().canOpen(player) ? new GuiStorageHopping(this) : getCore().getClientGuiElement(ID, player, world, x, y, z)) : null;
    }
}
