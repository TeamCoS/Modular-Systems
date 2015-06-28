package com.pauljoda.modularsystems.core.blocks.rotation;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.util.ForgeDirection;

public class NoRotation implements IRotation {
    @Override
    public ForgeDirection convertMetaToDirection(int meta) {
        return null;
    }

    @Override
    public int convertDirectionToMeta(ForgeDirection dir) {
        return 0;
    }

    @Override
    public int getMetaFromEntity(EntityLivingBase entity) {
        return 0;
    }
}
