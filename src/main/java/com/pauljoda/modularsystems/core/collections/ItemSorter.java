package com.pauljoda.modularsystems.core.collections;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Comparator;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/25/2015
 */
public class ItemSorter implements Comparator<ItemStack> {
    @Override
    public int compare(ItemStack o1, ItemStack o2) {

        //Check if any are null
        if(o1 != null && o2 == null)
            return -1;
        else if(o1 == null && o2 != null)
            return 1;
        else if(o1 == null)
            return 0;

        //Check if same type
        if(o1.getItem() == o2.getItem()) {

            //Check for special tags
            if(o1.getTagCompound() != null && o2.getTagCompound() == null)
                return -1; //Lets put things with tags at the front
            else if(o1.getTagCompound() == null && o2.getTagCompound() != null)
                return 1;
            else if(o1.getTagCompound() != null)
                return o1.stackSize > o2.stackSize ? -1 : o1.stackSize == o2.stackSize ? 0 : 1;

            //Look for different damage
            if(o1.getItemDamage() != o2.getItemDamage()) {
                if(o1.getItemDamage() < o2.getItemDamage())
                    return -1;
                else
                    return 1;
            }

            if(o1.stackSize == o2.stackSize)
                return 0;
            return o1.stackSize > o2.stackSize ? -1 : 1;
        }

        //Check by ID
        return Item.getIdFromItem(o1.getItem()) < Item.getIdFromItem(o2.getItem()) ? -1 : 1;
    }
}
