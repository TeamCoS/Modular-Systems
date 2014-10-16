package com.pauljoda.modularsystems.helpers;

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
