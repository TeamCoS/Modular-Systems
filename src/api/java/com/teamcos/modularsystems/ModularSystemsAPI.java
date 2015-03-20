package com.teamcos.modularsystems;

import com.teamcos.modularsystems.helpers.KeyInputHelper;
import com.teamcos.modularsystems.manager.GuiManager;
import com.teamcos.modularsystems.notification.NotificationKeyBinding;
import com.teamcos.modularsystems.notification.NotificationTickHandler;
import com.teamcos.modularsystems.proxy.CommonProxyAPI;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

@Mod(name = "Modular Systems API", modid = "modularsystemsapi", version = "0.0.1")

public class ModularSystemsAPI {
    @Mod.Instance("modularsystemsapi")
    public static ModularSystemsAPI instance;

    @SidedProxy( clientSide="com.teamcos.modularsystems.proxy.ClientProxyAPI", serverSide="com.teamcos.modularsystems.proxy.CommonProxyAPI")
    public static CommonProxyAPI proxy;

    public static Configuration notificationConfig;
    public static int notificationXPos;

    public static boolean ic2Present = false;

    public static void set(String categoryName, String propertyName, int newValue) {

        notificationConfig.load();
        if (notificationConfig.getCategoryNames().contains(categoryName)) {
            if (notificationConfig.getCategory(categoryName).containsKey(propertyName)) {
                notificationConfig.getCategory(categoryName).get(propertyName).set(newValue);
            }
        }
        notificationConfig.save();
        reloadValues();
    }

    public static void reloadValues() {
        notificationXPos = notificationConfig.getInt("notification xpos", "notifications", 1, 0, 2, "0: Left   1: Center   2: Right");
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        if(event.getSide() == Side.CLIENT) {
            FMLCommonHandler.instance().bus().register(new NotificationTickHandler());

            notificationConfig = new Configuration(new File(event.getModConfigurationDirectory().getAbsolutePath() + File.separator  + "ModularSystems/ModularSystemsAPINotificationsSettings.cfg"));
            notificationXPos = notificationConfig.getInt("notification xpos", "notifications", 1, 0, 2, "0: Left\n1: Center\n2: Right");
            notificationConfig.save();
        }
        if (Loader.isModLoaded("IC2")) ic2Present = true;
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.registerRenderers();
        if(event.getSide() == Side.CLIENT)
        {
            NotificationKeyBinding.init();
        }
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiManager());
        FMLCommonHandler.instance().bus().register(new KeyInputHelper());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }
}
