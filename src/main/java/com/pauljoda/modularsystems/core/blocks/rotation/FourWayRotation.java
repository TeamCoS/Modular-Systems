package com.pauljoda.modularsystems.core.blocks.rotation;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;

public class FourWayRotation implements IRotation {
    @Override
    public ForgeDirection convertMetaToDirection(int meta) {
        switch (meta) {
            case 1 :
                return ForgeDirection.NORTH;
            case 2 :
                return ForgeDirection.EAST;
            case 3 :
                return ForgeDirection.SOUTH;
            case 4 :
                return ForgeDirection.WEST;
            default :
                return ForgeDirection.UNKNOWN;
        }
    }

    @Override
    public int convertDirectionToMeta(ForgeDirection dir) {
        switch (dir) {
            case NORTH :
                return 1;
            case EAST :
                return 2;
            case SOUTH :
                return 3;
            case WEST :
                return 4;
            default :
                return 0;
        }
    }

    @Override
    public int getMetaFromEntity(EntityLivingBase entity) {
        int direction = MathHelper.floor_double((double) ((entity.rotationYaw * 4F) / 360F) + 0.5D) & 3;
        /**
         * 0 South
         * 1 West
         * 2 North
         * 3 East
         */
        switch (direction) {
            case 0 :
                return convertDirectionToMeta(ForgeDirection.NORTH);
            case 1 :
                return convertDirectionToMeta(ForgeDirection.EAST);
            case 2 :
                return convertDirectionToMeta(ForgeDirection.SOUTH);
            case 3 :
                return convertDirectionToMeta(ForgeDirection.WEST);
            default :
                return 0;
        }
    }
}
