package com.pauljoda.modularsystems.core.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * This will allow you to open the gui in the tile code
 */
public interface IOpensGui {

    /**
     * Return the container for this tile
     * @param ID Id, probably not needed but could be used for multiple guis
     * @param player The player that is opening the gui
     * @param world The world
     * @param x X Pos
     * @param y Y Pos
     * @param z Z Pos
     * @return The container to open
     */
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z);

    /**
     * Return the gui for this tile
     * @param ID Id, probably not needed but could be used for multiple guis
     * @param player The player that is opening the gui
     * @param world The world
     * @param x X Pos
     * @param y Y Pos
     * @param z Z Pos
     * @return The gui to open
     */
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z);
}
