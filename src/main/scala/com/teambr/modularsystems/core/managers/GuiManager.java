package com.teambr.modularsystems.core.managers;

import com.teambr.modularsystems.core.client.gui.GuiBlockValueConfig;
import com.teambr.modularsystems.core.common.container.ContainerBlockValueConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 * This file was created for Modular-Systems
 * <p/>
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis "pauljoda"
 * @since 3/24/2016
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
