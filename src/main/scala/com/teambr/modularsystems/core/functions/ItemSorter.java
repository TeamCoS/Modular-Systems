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

        //Check by stack size
        return o1.stackSize > o2.stackSize ? -1 : o1.stackSize == o2.stackSize ? 0 : 1;
    }
}
