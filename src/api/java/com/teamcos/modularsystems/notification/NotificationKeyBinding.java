package com.teamcos.modularsystems.notification;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class NotificationKeyBinding {
    public static KeyBinding menu;

    public static void init()
    {
        menu = new KeyBinding("Notification Configuration", Keyboard.KEY_N, "Notifications");

        ClientRegistry.registerKeyBinding(menu);
    }
}
