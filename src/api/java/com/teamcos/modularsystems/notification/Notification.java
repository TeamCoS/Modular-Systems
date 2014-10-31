package com.teamcos.modularsystems.notification;

import net.minecraft.item.ItemStack;

public class Notification {
    public static final double DEFAULT_DURATION = 3000;
    public static final double SHORT_DURATION = 1000;
    private ItemStack icon;
    private String title;
    private String description;
    private double duration;

    public Notification(ItemStack stack, String t, String d, double dur)
    {
        icon = stack;
        title = t;
        description = d;
        duration = dur;
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

    public double getDuration() { return duration; }
}
