package com.pauljoda.modularsystems.core.collections;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Used to navigate location in a better way (hopefully)
 */
public class Location {
    public int x, y, z;

    /**
     * Default constructor, sets all to 0
     */
    public Location() {
        this(-100, -100, -100);
    }

    /**
     * Main constructor for the location
     * @param xPos X Coord
     * @param yPos Y Coord
     * @param zPos Z Coord
     */
    public Location(int xPos, int yPos, int zPos) {
        x = xPos;
        y = yPos;
        z = zPos;
    }

    /**
     * Used to shallow copy from another location
     * @param loc Location to copy
     */
    public void copyLocation(Location loc) {
        this.x = loc.x;
        this.y = loc.y;
        this.z = loc.z;
    }

    /**
     * Used to move the location in a vector
     * @param xOffset X offset (can be negative)
     * @param yOffset Y offset (can be negative)
     * @param zOffset Z offset (can be negative)
     */
    public void travel(int xOffset, int yOffset, int zOffset) {
        x += xOffset;
        y += yOffset;
        z += zOffset;
    }

    /**
     * Used to return a new instance of this with same values
     * @return A shallow copy of this
     */
    public Location createNew() {
        return new Location(x, y, z);
    }

    /**
     * Sets the location to an unreachable location
     */
    public void reset() {
        x = y = z = -100;
    }

    /**
     * Writes this to the tag
     * @param tag The tag to write to
     */
    public void writeToNBT(NBTTagCompound tag) {
        tag.setInteger("Location X", x);
        tag.setInteger("Location Y", y);
        tag.setInteger("Location Z", z);
    }

    /**
     * Read the values for this from the tag
     * @param tag The tag, must have this written to it
     */
    public void readFromNBT(NBTTagCompound tag) {
        this.x = tag.getInteger("Location X");
        this.y = tag.getInteger("Location Y");
        this.z = tag.getInteger("Location Z");
    }
}
