package com.teamcos.modularsystems.notification;

import net.minecraft.item.ItemStack;

public class Notification {
    private ItemStack icon;
    private String title;
    private String description;

    public Notification(ItemStack stack, String t, String d)
    {
        icon = stack;
        title = t;
        description = d;
    }

    public ItemStack getIcon()
    {
        return icon;
    }

    public String getTitle()
    {
        return title;
    }

    public String getDescription()
    {
        return description;
    }
}
