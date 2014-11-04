package com.teamcos.modularsystems;

import com.teamcos.modularsystems.helpers.KeyInputHelper;
import com.teamcos.modularsystems.manager.GuiManager;
import com.teamcos.modularsystems.notification.NotificationKeyBinding;
import com.teamcos.modularsystems.notification.NotificationTickHandler;
import com.teamcos.modularsystems.proxy.CommonProxyAPI;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(name = "Modular Systems API", modid = "modularsystemsapi", version = "0.0.1")

public class ModularSystemsAPI {
    @Mod.Instance("modularsystemsapi")
    public static ModularSystemsAPI instance;

    @SidedProxy( clientSide="com.teamcos.modularsystems.proxy.ClientProxyAPI", serverSide="com.teamcos.modularsystems.proxy.CommonProxyAPI")
    public static CommonProxyAPI proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        if(event.getSide() == Side.CLIENT) {
            FMLCommonHandler.instance().bus().register(new NotificationTickHandler());
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
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
