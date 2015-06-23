package com.teamcos.modularsystems.helpers;

import com.teamcos.modularsystems.functions.WorldFunction;
import com.teamcos.modularsystems.utilities.tiles.shapes.Cuboid;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocalBlockCollections {

    private static final List<Coord> adjacentBlocks = Collections.unmodifiableList(adjacentBlocks(0, 0, 0));
    private static final List<ForgeDirection> notUpDirections;
    private static final List<ForgeDirection> neighborDirections;

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

    static {
        List<ForgeDirection> dirs = new ArrayList<ForgeDirection>();
        dirs.add(ForgeDirection.DOWN);
        dirs.add(ForgeDirection.EAST);
        dirs.add(ForgeDirection.WEST);
        dirs.add(ForgeDirection.NORTH);
        dirs.add(ForgeDirection.SOUTH);
        notUpDirections = Collections.unmodifiableList(dirs);
        dirs = new ArrayList<ForgeDirection>();
        dirs.add(ForgeDirection.DOWN);
        dirs.add(ForgeDirection.EAST);
        dirs.add(ForgeDirection.WEST);
        dirs.add(ForgeDirection.NORTH);
        dirs.add(ForgeDirection.SOUTH);
        neighborDirections = Collections.unmodifiableList(dirs);
    }

    public static List<ForgeDirection> getNeighborDirections() {
        return neighborDirections;
    }

    public static List<Coord> getAdjacentBlocks() {
        return adjacentBlocks;
    }

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
}
