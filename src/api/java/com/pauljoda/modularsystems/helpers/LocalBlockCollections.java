package com.pauljoda.modularsystems.helpers;

import com.pauljoda.modularsystems.functions.WorldFunction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocalBlockCollections {

    private static final List<Coord> adjacentBlocks = Collections.unmodifiableList(adjacentBlocks(0, 0, 0));

    private LocalBlockCollections() {}

    public static List<Coord> adjacentBlocks(int x, int y, int z) {
        List<Coord> coords = new ArrayList<Coord>();
        coords.add(new Coord(x + 1, y, z));
        coords.add(new Coord(x - 1, y, z));
        coords.add(new Coord(x, y + 1, z));
        coords.add(new Coord(x, y - 1, z));
        coords.add(new Coord(x, y, z + 1));
        coords.add(new Coord(x, y, z - 1));
        return coords;
    }

    public static List<Coord> getAdjacentBlocks() {
        return adjacentBlocks;
    }

    public static void searchBlock(World worldObj, int xCoord, int yCoord, int zCoord, WorldFunction function, int maxSize) {
        int dir = (worldObj.getBlockMetadata(xCoord, yCoord, zCoord));

		/*
         *           Horiz         DEPTH
		 * North 2:   +x              +z
		 * South 3:   -x              -z
		 * East 5:    +z              -x
		 * West 4:    -z              +x
		 *
		 * Should move BACKWARD for depth (facing = direction of block face, not direction of player looking at face)
		 */

        int hMin = getHorizontalMin(worldObj, xCoord, yCoord, zCoord, maxSize);
        int hMax = getHorizontalMax(worldObj, xCoord, yCoord, zCoord, maxSize);
        int vMin = getVerticalMin(worldObj, xCoord, yCoord, zCoord, maxSize);
        int vMax = getVerticalMax(worldObj, xCoord, yCoord, zCoord, maxSize);
        int depthVal = getDepthVal(worldObj, xCoord, yCoord, zCoord, maxSize);

        int startX;
        int startY = yCoord - vMin;
        int startZ;

        switch(dir)
        {
            case 2 :
                startX = xCoord + hMin;
                startZ = zCoord;
                break;
            case 3 :
                startX = xCoord - hMin;
                startZ = zCoord;
                break;
            case 4 :
                startX = xCoord;
                startZ = zCoord - hMin;
                break;
            case 5 :
                startX = xCoord;
                startZ = zCoord + hMin;
                break;
            default :
                startX = xCoord;
                startZ = zCoord;
        }

        int horizMax = hMin + hMax;
        int vertMax = vMin + vMax;

        for (int horiz = 0; horiz <= horizMax && function.shouldContinue(); horiz++)    // Horizontal (X or Z)
        {
            for (int vert = 0; vert <= vertMax && function.shouldContinue(); vert++)   // Vertical (Y)
            {
                for (int depth = 0; depth <= depthVal + 1 && function.shouldContinue(); depth++) // Depth (Z or X)
                {
                    int x;
                    int y = startY + vert;
                    int z;
                    switch(dir)
                    {
                        case 2 :
                            x = startX - horiz;
                            z = startZ + depth;
                            break;
                        case 3 :
                            x = startX + horiz;
                            z = startZ - depth;
                            break;
                        case 4 :
                            x = startX + depth;
                            z = startZ + horiz;
                            break;
                        case 5 :
                            x = startX - depth;
                            z = startZ - horiz;
                            break;
                        default :
                            x = 0;
                            z = 0;
                    }

                    if (x == xCoord && y == yCoord && z == zCoord) continue;

                    if (horiz == 0 || horiz == horizMax ||
                            vert == 0 || vert == vertMax ||
                            depth == 0 || depth == depthVal + 1) {
                        function.outerBlock(worldObj, x, y, z);
                    } else {
                        function.innerBlock(worldObj, x, y, z);
                    }
                }
            }
        }
    }

    public static int getHorizontalMin(World worldObj, int x, int y, int z, int maxSize) {
        int output = 0;
        int dir = (worldObj.getBlockMetadata(x, y, z));
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

    public static int getHorizontalMax(World worldObj, int x, int y, int z, int maxSize)
    {
        int output = 0;
        int dir = (worldObj.getBlockMetadata(x, y, z));
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

    public static int getVerticalMin(World worldObj, int x, int y, int z, int maxSize)
    {
        int output = 0;
        int dir = (worldObj.getBlockMetadata(x, y, z));
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

    public static int getVerticalMax(World worldObj, int x, int y, int z, int maxSize)
    {
        int output = 0;
        int dir = (worldObj.getBlockMetadata(x, y, z));
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

    public static int getDepthVal(World worldObj, int x, int y, int z, int maxSize)
    {
        int output = 0;
        int dir = (worldObj.getBlockMetadata(x, y, z));
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

    /**
     * WIP
     * @param center
     * @param xRadius
     * @param yRadius
     * @param zRadius
     * @param valueFunction
     * @param <A>
     * @return
     */
    public static <A> A searchCuboid(Coord center, int xRadius, int yRadius, int zRadius, MyFunction<A> valueFunction) {
        xRadius = Math.abs(xRadius);
        yRadius = Math.abs(yRadius);
        zRadius = Math.abs(zRadius);
        int xStart = center.x - xRadius;
        int xEnd = center.x + xRadius;
        int yStart = center.y - yRadius;
        int yEnd = center.y + yRadius;
        int zStart = center.z - zRadius;
        int zEnd = center.z + zRadius;
        A sum = valueFunction.defaultValue();
        for (int y = yStart; y <= yEnd; y++) {
            for (int x = xStart; x <= xEnd; x++) {
                sum = valueFunction.sum(sum, varyZ(zStart, zEnd, x, y, valueFunction));
            }
        }
        return sum;
    }

    /**
     * Applies the value function to all blocks in a ring at distance {@code range} and from
     * {@link com.pauljoda.modularsystems.enchanting.tiles.TileEntityEnchantmentAlter#yCoord} to
     * {@link com.pauljoda.modularsystems.enchanting.tiles.TileEntityEnchantmentAlter#yCoord} + {@code height} - 1
     * inclusive.
     * @param radius
     * @param height
     * @param valueFunction
     * @param orientation The axis the cyclinder lies along. The faces of the cylinder will lie along the other 2 axes
     * @return
     */
    public static <A> A searchSquareTube(Coord center, Orientation orientation, int radius, int height, MyFunction<A> valueFunction) {
        A sum = valueFunction.defaultValue();
        for (int h = 0; h < height && !valueFunction.shouldReturn(sum); h++) {
            sum = valueFunction.sum(sum, getRingValue(center, orientation, h, radius, valueFunction));
        }
        return sum;
    }

    private static <A> A getRingValue(Coord center, Orientation orientation, int radius, int height, MyFunction<A> valueFunction) {
        radius = Math.abs(radius);

        A sum = valueFunction.defaultValue();
        int min, max, midpt;

        switch (orientation) {
            case X:
                midpt = center.x + height;
                min = center.y - radius;
                max = center.y + radius;
                sum = valueFunction.sum(sum, varyY(min, max, midpt, center.z + radius, valueFunction));
                sum = valueFunction.sum(sum, varyY(min, max, midpt, center.z - radius, valueFunction));
                min = center.z - radius + 1;
                max = center.z + radius - 1;
                sum = valueFunction.sum(sum, varyZ(min, max, midpt, center.y + radius, valueFunction));
                sum = valueFunction.sum(sum, varyZ(min, max, midpt, center.y - radius, valueFunction));
                break;
            case Y:
                midpt = center.y + height;
                min = center.x - radius;
                max = center.x + radius;
                sum = valueFunction.sum(sum, varyX(min, max, midpt, center.z + radius, valueFunction));
                sum = valueFunction.sum(sum, varyX(min, max, midpt, center.z - radius, valueFunction));
                min = center.z - radius + 1;
                max = center.z + radius - 1;
                sum = valueFunction.sum(sum, varyZ(min, max, center.x + radius, midpt, valueFunction));
                sum = valueFunction.sum(sum, varyZ(min, max, center.x - radius, midpt, valueFunction));
                break;
            case Z:
                midpt = center.z + height;
                min = center.x - radius;
                max = center.x + radius;
                sum = valueFunction.sum(sum, varyX(min, max, midpt, center.y + radius, valueFunction));
                sum = valueFunction.sum(sum, varyX(min, max, midpt, center.y - radius, valueFunction));
                min = center.y - radius + 1;
                max = center.y + radius - 1;
                sum = valueFunction.sum(sum, varyY(min, max, midpt, center.x + radius, valueFunction));
                sum = valueFunction.sum(sum, varyY(min, max, midpt, center.x - radius, valueFunction));
                break;
        }

        return sum;
    }

    private static <A> A varyX(int min, int max, int y, int z, MyFunction<A> valueFunction) {
        A sum = valueFunction.defaultValue();
        for (int x = min; x <= max; x++) {
            sum = valueFunction.sum(sum, valueFunction.getValue(x, y, z), valueFunction.getValue(x, y, z));
        }
        return sum;
    }

    private static <A> A varyZ(int min, int max, int x, int y, MyFunction<A> valueFunction) {
        A sum = valueFunction.defaultValue();
        for (int z = min; z <= max; z++) {
            sum = valueFunction.sum(sum, valueFunction.getValue(x, y, z), valueFunction.getValue(x, y, z));
        }
        return sum;
    }

    private static <A> A varyY(int min, int max, int x, int z, MyFunction<A> valueFunction) {
        A sum = valueFunction.defaultValue();
        for (int y = min; y <= max; y++) {
            sum = valueFunction.sum(sum, valueFunction.getValue(x, y, z), valueFunction.getValue(x, y, z));
        }
        return sum;
    }
}
