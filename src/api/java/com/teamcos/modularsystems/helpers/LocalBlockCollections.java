package com.teamcos.modularsystems.helpers;

import com.teamcos.modularsystems.functions.WorldFunction;
import com.teamcos.modularsystems.utilities.tiles.shapes.Cuboid;
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

    public interface BlockFunction {
        public boolean innerBlock(World world, int x, int y, int z);
        public boolean wallBlock(World world, int x, int y, int z);
    }

//    public void exploreXZFace(World world, Coord origin, int maxSize, BlockFunction function) {
//        int xMin = origin.x - maxSize;
//        int xMax = origin.x + maxSize;
//        int zMin = origin.z - maxSize;
//        int zMax = origin.z + maxSize;
//        int xLow = origin.x;
//        int xHigh = origin.x;
//        int zLow = origin.z;
//        int zHigh = origin.z;
//        for (;xLow > xMin && function.wallBlock(world, xLow, origin.y, origin.z) && (function.innerBlock(world, xLow, origin.y - 1, origin.z) || (function.innerBlock(world, xLow, origin.y + 1, origin.z))); xLow--) {}
//        for (;xHigh > xMin && function.wallBlock(world, xHigh, origin.y, origin.z) && (function.innerBlock(world, xHigh, origin.y - 1, origin.z) || (function.innerBlock(world, xHigh, origin.y + 1, origin.z))); xLow--) {}
//    }
//
//    public static void searchCuboidMultiBlock(World world, Coord origin, WorldFunction function, int maxSize) {
//        Bound xBounds = varyXDistance(world, origin, origin.x - maxSize, origin.x + maxSize, function);
//        Bound yBounds = varyYDistance(world, origin, origin.y - maxSize, origin.y + maxSize, function);
//        Bound zBounds = varyZDistance(world, origin, origin.z - maxSize, origin.z + maxSize, function);
//        searchCuboidMultiBlock(world, xBounds, yBounds, zBounds, function);
//    }
//
//    public static void searchCuboidMultiBlock(World world, Bound xBound, Bound yBound, Bound zBound, WorldFunction function) {
//        searchXZFaces(world, xBound, yBound, zBound, function);
//        searchXYFaces(world, xBound, yBound, zBound, function);
//        searchYZFaces(world, xBound, yBound, zBound, function);
//        searchInnerCube(world, xBound, yBound, zBound, function);
//    }
//
//    public static void searchXZFaces(World world, Bound xBound, Bound yBound, Bound zBound, WorldFunction function) {
//
//        for (int x = xBound.lowBound; x <= xBound.highBound && function.shouldContinue(); x++) {
//            for (int z = zBound.lowBound; z <= zBound.highBound && function.shouldContinue(); z++) {
//                function.outerBlock(world, x, yBound.lowBound, z);
//                function.outerBlock(world, x, yBound.highBound, z);
//            }
//        }
//    }
//
//    public static void searchXYFaces(World world, Bound xBound, Bound yBound, Bound zBound, WorldFunction function) {
//
//        for (int x = xBound.lowBound; x <= xBound.highBound && function.shouldContinue(); x++) {
//            for (int y = yBound.lowBound; y <= yBound.highBound && function.shouldContinue(); y++) {
//                function.outerBlock(world, x, y, zBound.lowBound);
//                function.outerBlock(world, x, y, zBound.highBound);
//            }
//        }
//    }
//
//    public static void searchYZFaces(World world, Bound xBound, Bound yBound, Bound zBound, WorldFunction function) {
//
//        for (int y = yBound.lowBound; y <= yBound.highBound && function.shouldContinue(); y++) {
//            for (int z = zBound.lowBound; z <= zBound.highBound && function.shouldContinue(); z++) {
//                function.outerBlock(world, xBound.lowBound, y, z);
//                function.outerBlock(world, xBound.highBound, y, z);
//            }
//        }
//    }
//
//    public static void searchInnerCube(World world, Bound xBound, Bound yBound, Bound zBound, WorldFunction function) {
//        int maxX = xBound.highBound - 1;
//        int maxY = yBound.highBound - 1;
//        int maxZ = zBound.highBound - 1;
//        for (int x = xBound.lowBound + 1; x <= maxX && function.shouldContinue(); x++) {
//            for (int y = yBound.lowBound + 1; y <= maxY && function.shouldContinue(); y++) {
//                for (int z = zBound.lowBound + 1; z <= maxZ && function.shouldContinue(); z++) {
//                    function.innerBlock(world, x, y, z);
//                }
//            }
//        }
//    }
//
//    public static Bound varyXDistance(World world, Coord origin, int xMin, int xMax, WorldFunction function) {
//        int xNeg = origin.x;
//        int xPos = origin.x;
//        for (int x = origin.x; x >= xMin && function.shouldContinue(); x--) {
//            function.outerBlock(world, x, origin.y, origin.z);
//            function.innerBlock(world, x, origin.y + 1, origin.z);
//            function.innerBlock(world, x, origin.y - 1, origin.z);
//            xNeg--;
//        }
//        for (int x = origin.x; x <= xMax && function.shouldContinue(); x++) {
//            function.outerBlock(world, x, origin.y, origin.z);
//            function.innerBlock(world, x, origin.y + 1, origin.z);
//            function.innerBlock(world, x, origin.y - 1, origin.z);
//            xPos++;
//        }
//        return new Bound(xNeg, xPos);
//    }
//
//    public static Bound varyYDistance(World world, Coord origin, int yMin, int yMax, WorldFunction function) {
//        int yNeg = origin.y;
//        int yPos = origin.y;
//        for (int y = origin.y; y >= yMin && function.shouldContinue(); y--) {
//            function.outerBlock(world, origin.x, y, origin.z);
//            function.innerBlock(world, origin.x, origin.y + 1, origin.z);
//            function.innerBlock(world, origin.x, origin.y - 1, origin.z);
//            yNeg--;
//        }
//        for (int y = origin.y; y <= yMax && function.shouldContinue(); y++) {
//            function.outerBlock(world, origin.x, y, origin.z);
//            yPos++;
//        }
//        return new Bound(yNeg, yPos);
//    }
//
//    public static Bound varyZDistance(World world, Coord origin, int xMin, int xMax, WorldFunction function) {
//        int xNeg = origin.x;
//        int xPos = origin.x;
//        for (int x = origin.x; x >= xMin && function.shouldContinue(); x--) {
//            function.outerBlock(world, x, origin.y, origin.z);
//            xNeg--;
//        }
//        for (int x = origin.x; x <= xMax && function.shouldContinue(); x++) {
//            function.outerBlock(world, x, origin.y, origin.z);
//            xPos++;
//        }
//        return new Bound(xNeg, xPos);
//    }

    public static void searchCuboidMultiBlock(World worldObj, WorldFunction function, Cuboid cube, Coord coord) {
        int hMin = cube.getMeasurement(Cuboid.Measurement.HMIN);
        int hMax = cube.getMeasurement(Cuboid.Measurement.HMAX);
        int vMin = cube.getMeasurement(Cuboid.Measurement.VMIN);
        int vMax = cube.getMeasurement(Cuboid.Measurement.VMAX);
        int depthVal = cube.getMeasurement(Cuboid.Measurement.DEPTHVAL);
        int dir = cube.getMeasurement(Cuboid.Measurement.DIRECTION);

        int startX;
        int startY = coord.y - vMin;
        int startZ;

        switch(dir)
        {
            case 2 :
                startX = coord.x + hMin;
                startZ = coord.z;
                break;
            case 3 :
                startX = coord.x - hMin;
                startZ = coord.z;
                break;
            case 4 :
                startX = coord.x;
                startZ = coord.z - hMin;
                break;
            case 5 :
                startX = coord.x;
                startZ = coord.z + hMin;
                break;
            default :
                startX = coord.x;
                startZ = coord.z;
        }

        if (hMin < 0 || hMax < 0 || vMin < 0 || vMax < 0 || depthVal < 0) {
            function.fail();
            return;
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

                    if (x == coord.x && y == coord.y && z == coord.z) continue;

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

    public static void searchCuboidMultiBlock(World worldObj, int xCoord, int yCoord, int zCoord, WorldFunction function, int maxSize) {
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

        int hMin = Cuboid.getHorizontalMin(worldObj, xCoord, yCoord, zCoord, maxSize, dir);
        int hMax = Cuboid.getHorizontalMax(worldObj, xCoord, yCoord, zCoord, maxSize, dir);
        int vMin = Cuboid.getVerticalMin(worldObj, xCoord, yCoord, zCoord, maxSize, dir);
        int vMax = Cuboid.getVerticalMax(worldObj, xCoord, yCoord, zCoord, maxSize, dir);
        int depthVal = Cuboid.getDepthVal(worldObj, xCoord, yCoord, zCoord, maxSize, dir);

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

        if (hMin < 0 || hMax < 0 || vMin < 0 || vMax < 0 || depthVal < 0) {
            function.fail();
            return;
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
     * {@link com.teamcos.modularsystems.enchanting.tiles.TileEntityEnchantmentAlter#yCoord} to
     * {@link com.teamcos.modularsystems.enchanting.tiles.TileEntityEnchantmentAlter#yCoord} + {@code height} - 1
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
