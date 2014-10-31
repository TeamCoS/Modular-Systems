package com.teamcos.modularsystems.notification;

import com.teamcos.modularsystems.ModularSystemsAPI;
import com.teamcos.modularsystems.manager.GuiManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class NotificationHelper {

    public static void addNotification(Notification notification)
    {
        NotificationTickHandler.guiNotification.queueNotification(notification);
    }

    public static void openConfigurationGui()
    {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if(player != null)
            player.openGui(ModularSystemsAPI.instance, GuiManager.NOTIFICATION_CONFIG_ID, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
    }
}
