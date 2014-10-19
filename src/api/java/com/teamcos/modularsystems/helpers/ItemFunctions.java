package com.teamcos.modularsystems.helpers;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemFunctions {

    private ItemFunctions(){}

    public static void dropItems(ItemStack itemStack, World worldObj, int x, int y, int z) {

        if (itemStack.stackSize > 0) {

            float f = worldObj.rand.nextFloat() * 0.8F + 0.1F;
            float f1 = worldObj.rand.nextFloat() * 0.8F + 0.1F;
            float f2 = worldObj.rand.nextFloat() * 0.8F + 0.1F;

            do {
                int j1 = worldObj.rand.nextInt(21) + 10;

                if (j1 > itemStack.stackSize) {
                    j1 = itemStack.stackSize;
                }

                itemStack.stackSize -= j1;
                EntityItem entityitem = new EntityItem(worldObj, (double) ((float) x + f), (double) ((float) y + f1), (double) ((float) z + f2), new ItemStack(itemStack.getItem(), j1, itemStack.getItemDamage()));

                if (itemStack.hasTagCompound()) {
                    entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
                }

                float f3 = 0.05F;
                entityitem.motionX = (double) ((float) worldObj.rand.nextGaussian() * f3);
                entityitem.motionY = (double) ((float) worldObj.rand.nextGaussian() * f3 + 0.2F);
                entityitem.motionZ = (double) ((float) worldObj.rand.nextGaussian() * f3);
                worldObj.spawnEntityInWorld(entityitem);
            } while (itemStack.stackSize > 0);
        }
    }
}
