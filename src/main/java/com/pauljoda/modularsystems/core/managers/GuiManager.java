package com.pauljoda.modularsystems.core.managers;

import com.pauljoda.modularsystems.core.container.ContainerBlockValueConfig;
import com.pauljoda.modularsystems.core.gui.GuiBlockValueConfig;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiManager implements IGuiHandler {
    public static final int VALUE_CONFIG = 0;

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
