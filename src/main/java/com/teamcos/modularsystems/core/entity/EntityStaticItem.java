package com.teamcos.modularsystems.core.entity;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityStaticItem extends EntityItem
{
 

    /**
     * The maximum age of this EntityItem.  The item is expired once this is reached.
     */
    public int lifespan = 6000;

    public EntityStaticItem(World p_i1709_1_, double p_i1709_2_, double p_i1709_4_, double p_i1709_6_)
    {
        super(p_i1709_1_);
        this.hoverStart = 0;
        this.setSize(0.25F, 0.25F);
        this.yOffset = this.height / 2.0F;
        this.setPosition(p_i1709_2_, p_i1709_4_, p_i1709_6_);
        this.rotationYaw = (float)(Math.random() * 360.0D);
    }

    public EntityStaticItem(World p_i1710_1_, double p_i1710_2_, double p_i1710_4_, double p_i1710_6_, ItemStack p_i1710_8_)
    {
        this(p_i1710_1_, p_i1710_2_, p_i1710_4_, p_i1710_6_);        
        this.setEntityItemStack(p_i1710_8_);
        this.lifespan = (p_i1710_8_.getItem() == null ? 6000 : p_i1710_8_.getItem().getEntityLifespan(p_i1710_8_, p_i1710_1_));
    }

    public EntityStaticItem(World p_i1711_1_)
    {
        super(p_i1711_1_);
        this.hoverStart = (float)(Math.random() * Math.PI * 2.0D);
        this.setSize(0.25F, 0.25F);
        this.yOffset = this.height / 2.0F;
    }
 
    @Override
    public void onUpdate()
    {
    	
    }
}
