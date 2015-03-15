package com.teamcos.modularsystems.manager;

import com.teamcos.modularsystems.notification.GuiNotificationConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiManager implements IGuiHandler {
    public static final int NOTIFICATION_CONFIG_ID = 0;
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch(ID)
        {
        case NOTIFICATION_CONFIG_ID :
            return new GuiNotificationConfig();
        default : return null;
        }
    }
}
