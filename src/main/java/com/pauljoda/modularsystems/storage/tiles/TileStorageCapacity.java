package com.pauljoda.modularsystems.storage.tiles;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;
import java.util.Random;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/24/2015
 */
public class TileStorageCapacity extends TileStorageBasic {
    @Override
    public void addedToNetwork() {
        super.addedToNetwork();
        getCore().pushNewInventory(11);
        worldObj.markBlockForUpdate(core.x, core.y, core.z);
    }

    @Override
    public void removedFromNetwork() {
        super.removedFromNetwork();
        if(getCore() != null) {
            List<ItemStack> stacks = getCore().popInventory(11);
            for (ItemStack itemStack : stacks) {
                Random rand = new Random();
                if (itemStack != null && itemStack.stackSize > 0) {
                    float rx = rand.nextFloat() * 0.8F + 0.1F;
                    float ry = rand.nextFloat() * 0.8F + 0.1F;
                    float rz = rand.nextFloat() * 0.8F + 0.1F;

                    EntityItem entityItem = new EntityItem(worldObj,
                            xCoord + rx, yCoord + ry, zCoord + rz,
                            new ItemStack(itemStack.getItem(), itemStack.stackSize, itemStack.getItemDamage()));

                    if (itemStack.hasTagCompound())
                        entityItem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());

                    float factor = 0.05F;
                    entityItem.motionX = rand.nextGaussian() * factor;
                    entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
                    entityItem.motionZ = rand.nextGaussian() * factor;
                    worldObj.spawnEntityInWorld(entityItem);

                    itemStack.stackSize = 0;
                }
            }
            worldObj.markBlockForUpdate(core.x, core.y, core.z);
        }
    }
}
