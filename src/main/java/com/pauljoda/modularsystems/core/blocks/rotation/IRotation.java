package com.pauljoda.modularsystems.core.blocks.rotation;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.util.ForgeDirection;

public interface IRotation {

    /**
     * Get the direction that the block is facing from a metadata value
     * @param meta The metadata of the block
     * @return What direction the block is facing
     */
    public ForgeDirection convertMetaToDirection(int meta);

    /**
     * Get the metadata for the given direction
     * @param dir What direction the block is facing
     * @return The metadata for that direction
     */
    public int convertDirectionToMeta(ForgeDirection dir);

    /**
     * Used when the block is placed, it will calculate what meta should be put onto the block
     * @param entity The player entity
     * @return The meta to write to the block
     */
    public int getMetaFromEntity(EntityLivingBase entity);
}
