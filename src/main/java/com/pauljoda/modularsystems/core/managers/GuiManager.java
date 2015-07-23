package com.pauljoda.modularsystems.core.managers;

import com.pauljoda.modularsystems.core.container.ContainerBlockValueConfig;
import com.pauljoda.modularsystems.core.gui.GuiBlockValueConfig;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Used to open GUIs. Don't use this for tiles as Bookshelf alrady handles those
 */
public class GuiManager implements IGuiHandler {
    public static final int VALUE_CONFIG = 0; //The block values config ID

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case VALUE_CONFIG :
                return new ContainerBlockValueConfig(player.inventory);
            default :
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case VALUE_CONFIG :
                return new GuiBlockValueConfig(new ContainerBlockValueConfig(player.inventory), 300, 200, "");
            default :
                return null;
        }
    }
}
