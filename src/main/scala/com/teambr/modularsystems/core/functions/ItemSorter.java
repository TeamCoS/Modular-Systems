package com.teambr.modularsystems.core.functions;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Comparator;

/**
 * This file was created for Modular-Systems
 * <p/>
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis "pauljoda"
 * @since 3/28/2016
 */
public class ItemSorter implements Comparator<ItemStack> {
    // Created Instance
    public static ItemSorter INSTANCE = new ItemSorter();

    @Override
    public int compare(ItemStack o1, ItemStack o2) {

        //Check if any are null
        if(o1 != null && o2 == null)
            return -1;
        else if(o1 == null && o2 != null)
            return 1;
        else if(o1 == null)
            return 0;

        // Same size, go ahead and organize by id
        if(o1.stackSize == o2.stackSize) {
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

        //Check by stack size
        return o1.stackSize > o2.stackSize ? -1 : 1;
    }
}
