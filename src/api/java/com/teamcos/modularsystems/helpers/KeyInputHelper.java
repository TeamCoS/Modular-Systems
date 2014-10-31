package com.teamcos.modularsystems.helpers;

import com.teamcos.modularsystems.notification.NotificationHelper;
import com.teamcos.modularsystems.notification.NotificationKeyBinding;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;

public class KeyInputHelper {
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if(NotificationKeyBinding.menu.isPressed())
            NotificationHelper.openConfigurationGui();
    }
}
