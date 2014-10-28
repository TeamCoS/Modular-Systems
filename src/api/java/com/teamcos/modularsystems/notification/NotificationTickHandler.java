package com.teamcos.modularsystems.notification;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;

public class NotificationTickHandler {
    public static GuiNotification guiNotification;

    @SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.theWorld != null) {
            if(guiNotification == null)
                guiNotification = new GuiNotification(mc);
            guiNotification.update();
        }
    }
}
