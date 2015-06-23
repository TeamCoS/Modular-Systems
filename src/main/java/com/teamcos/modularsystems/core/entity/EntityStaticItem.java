package com.teamcos.modularsystems.core.entity;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityStaticItem extends EntityItem {
    /**
     * The maximum age of this EntityItem.  The item is expired once this is reached.
     */
    public int lifespan = 6000;

    public EntityStaticItem(World world, double x, double y, double z) {
        super(world);
        this.hoverStart = 0;
        this.setSize(0.25F, 0.25F);
        this.yOffset = this.height / 2.0F;
        this.setPosition(x, y, z);
        this.rotationYaw = (float)(Math.random() * 360.0D);
    }

    public EntityStaticItem(World world, double x, double y, double z, ItemStack stack) {
        this(world, x, y, z);
        this.setEntityItemStack(stack);
        this.lifespan = (stack.getItem() == null ? 6000 : stack.getItem().getEntityLifespan(stack, world));
    }

    public EntityStaticItem(World world) {
        super(world);
        this.hoverStart = (float)(Math.random() * Math.PI * 2.0D);
        this.setSize(0.25F, 0.25F);
        this.yOffset = this.height / 2.0F;
    }
 
    @Override
    public void onUpdate() {}
}
