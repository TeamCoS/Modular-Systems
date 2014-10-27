package com.teamcos.modularsystems.utilities.tiles.shapes;

import com.teamcos.modularsystems.utilities.tiles.FueledRecipeTile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class Cuboid {
    private int hMin;
    private int hMax;
    private int vMin;
    private int vMax;
    private int depthVal;
    private int direction;

    public enum Measurement {
        HMIN,
        HMAX,
        VMIN,
        VMAX,
        DEPTHVAL,
        DIRECTION
    }

    public Cuboid()
    {
        hMin = hMax = vMin = vMax = depthVal = -1;
    }

    public Cuboid(FueledRecipeTile tile)
    {
        direction = tile.getWorldObj().getBlockMetadata(tile.xCoord, tile.yCoord, tile.zCoord);
        hMin = getHorizontalMin(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord, tile.getMaxSize(), direction);
        hMax = getHorizontalMax(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord, tile.getMaxSize(), direction);
        vMin = getVerticalMin(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord, tile.getMaxSize(), direction);
        vMax = getVerticalMax(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord, tile.getMaxSize(), direction);
        depthVal = getDepthVal(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord, tile.getMaxSize(), direction);
    }

    public void setCube(FueledRecipeTile tile)
    {
        direction = tile.getWorldObj().getBlockMetadata(tile.xCoord, tile.yCoord, tile.zCoord);
        hMin = getHorizontalMin(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord, tile.getMaxSize(), direction);
        hMax = getHorizontalMax(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord, tile.getMaxSize(), direction);
        vMin = getVerticalMin(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord, tile.getMaxSize(), direction);
        vMax = getVerticalMax(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord, tile.getMaxSize(), direction);
        depthVal = getDepthVal(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord, tile.getMaxSize(), direction);
    }

    public void reset()
    {
        hMin = hMax = vMin = vMax = depthVal = -1;
    }

    public int getMeasurement(Measurement measure)
    {
        switch(measure)
        {
        case HMIN :
            return hMin;
        case HMAX :
            return hMax;
        case VMIN :
            return vMin;
        case VMAX :
            return vMax;
        case DEPTHVAL :
            return depthVal;
        case DIRECTION :
            return direction;
        default :
            return -1;
        }
    }

    public void readFromNBT(NBTTagCompound tagCompound)
    {
        hMin = tagCompound.getInteger("hMin");
        hMax = tagCompound.getInteger("hMax");
        vMin = tagCompound.getInteger("vMin");
        vMax = tagCompound.getInteger("vMax");
        depthVal = tagCompound.getInteger("depthVal");
        direction = tagCompound.getInteger("Direction");
    }

    public void writeToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setInteger("hMin", hMin);
        tagCompound.setInteger("hMax", hMax);
        tagCompound.setInteger("vMin", vMin);
        tagCompound.setInteger("vMax", vMax);
        tagCompound.setInteger("depthVal", depthVal);
        tagCompound.setInteger("Direction", direction);
    }

    public static int getHorizontalMin(World worldObj, int x, int y, int z, int maxSize, int dir) {
        int output = 0;
        int depthMultiplier = ((dir == 2 || dir == 4) ? 1 : -1);
        boolean forwardZ = ((dir == 2) || (dir == 3));
        int xCheck = x + (forwardZ ? 0 : depthMultiplier);
        int yCheck = y;
        int zCheck = z + (forwardZ ? depthMultiplier : 0);

        while (worldObj.isAirBlock(xCheck, yCheck, zCheck))
        {
            output++;
            if (forwardZ)
                xCheck = xCheck + depthMultiplier;
            else
                zCheck = zCheck - depthMultiplier;

            if (output > maxSize)
                return -1;
        }
        return output;
    }

    public static int getHorizontalMax(World worldObj, int x, int y, int z, int maxSize, int dir)
    {
        int output = 0;
        int depthMultiplier = ((dir == 2 || dir == 4) ? 1 : -1);
        boolean forwardZ = ((dir == 2) || (dir == 3));
        int xCheck = x + (forwardZ ? 0 : depthMultiplier);
        int yCheck = y;
        int zCheck = z + (forwardZ ? depthMultiplier : 0);

        while (worldObj.isAirBlock(xCheck, yCheck, zCheck))
        {
            output++;
            if (forwardZ)
                xCheck = xCheck - depthMultiplier;
            else
                zCheck = zCheck + depthMultiplier;

            if (output > maxSize)
                return -1;
        }
        return output;
    }

    public static int getVerticalMin(World worldObj, int x, int y, int z, int maxSize, int dir)
    {
        int output = 0;
        int depthMultiplier = ((dir == 2 || dir == 4) ? 1 : -1);
        boolean forwardZ = ((dir == 2) || (dir == 3));
        int xCheck = x + (forwardZ ? 0 : depthMultiplier);
        int yCheck = y;
        int zCheck = z + (forwardZ ? depthMultiplier : 0);

        while (worldObj.isAirBlock(xCheck, yCheck, zCheck))
        {
            output++;
            yCheck--;

            if (output > maxSize)
                return -1;
        }
        return output;
    }

    public static int getVerticalMax(World worldObj, int x, int y, int z, int maxSize, int dir)
    {
        int output = 0;
        int depthMultiplier = ((dir == 2 || dir == 4) ? 1 : -1);
        boolean forwardZ = ((dir == 2) || (dir == 3));
        int xCheck = x + (forwardZ ? 0 : depthMultiplier);
        int yCheck = y;
        int zCheck = z + (forwardZ ? depthMultiplier : 0);

        while (worldObj.isAirBlock(xCheck, yCheck, zCheck))
        {
            output++;
            yCheck++;

            if (output > maxSize)
                return -1;
        }
        return output;
    }

    public static int getDepthVal(World worldObj, int x, int y, int z, int maxSize, int dir)
    {
        int output = 0;
        int depthMultiplier = ((dir == 2 || dir == 4) ? 1 : -1);
        boolean forwardZ = ((dir == 2) || (dir == 3));
        int xCheck = x + (forwardZ ? 0 : depthMultiplier);
        int yCheck = y;
        int zCheck = z + (forwardZ ? depthMultiplier : 0);

        while (worldObj.isAirBlock(xCheck, yCheck, zCheck))
        {
            output++;
            if (forwardZ)
                zCheck = zCheck + depthMultiplier;
            else
                xCheck = xCheck + depthMultiplier;

            if (output > maxSize)
                return -1;
        }
        return output;
    }
}
