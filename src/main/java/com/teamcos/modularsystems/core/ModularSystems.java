package com.teamcos.modularsystems.core;

import com.teamcos.modularsystems.core.fakeplayer.FakePlayerPool;
import com.teamcos.modularsystems.core.helper.BlockValueHelper;
import com.teamcos.modularsystems.core.helper.ConfigHelper;
import com.teamcos.modularsystems.core.helper.LogHelper;
import com.teamcos.modularsystems.core.helper.VersionHelper;
import com.teamcos.modularsystems.core.lib.Reference;
import com.teamcos.modularsystems.core.managers.ModuleManager;
import com.teamcos.modularsystems.core.network.PacketPipeline;
import com.teamcos.modularsystems.core.proxy.ClientProxy;
import com.teamcos.modularsystems.core.proxy.CommonProxy;
import com.teamcos.modularsystems.helpers.VanillaFuelHandler;
import com.teamcos.modularsystems.manager.GuiManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;

@Mod(name = Reference.MOD_NAME, modid = Reference.MOD_ID, version = Reference.VERSION)

public class ModularSystems {

    @Mod.Instance(Reference.MOD_ID)
    public static ModularSystems instance;

    @SidedProxy( clientSide="com.teamcos.modularsystems.core.proxy.ClientProxy", serverSide="com.teamcos.modularsystems.core.proxy.CommonProxy")
    public static CommonProxy proxy;

    public static final PacketPipeline packetPipeline = new PacketPipeline();


    //Creates the Creative Tab
    public static CreativeTabs tabModularSystems = new CreativeTabs("tabModularSystems"){

        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return Item.getItemFromBlock(Blocks.furnace);

        }
    };

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){

        //Set up config
        ConfigHelper.init(new File(event.getModConfigurationDirectory().getAbsolutePath() + File.separator  + "ModularSystems/ModularSystems.cfg"));
        FMLCommonHandler.instance().bus().register(new ConfigHelper());

        //Load block values for speed/efficiency
        try {
            BlockValueHelper.init();
        } catch (ParserConfigurationException e) {
            LogHelper.error(e.getMessage());
        } catch (TransformerException e) {
            LogHelper.error(e.getMessage());
        } catch (SAXException e) {
            LogHelper.error(e.getMessage());
        } catch (IOException e) {
            LogHelper.error(e.getMessage());
        }

        //Version Check
        VersionHelper.execute();

        if(ConfigHelper.enableFurnace) ModuleManager.enableFurnaceModule();
        if(ConfigHelper.enableStorage) ModuleManager.enableStorageModule();
        if(ConfigHelper.enableEnchanting) ModuleManager.enableEnchantingModule();
        if(ConfigHelper.enableOreProcessing) ModuleManager.enableOreProcessingModule();

        MinecraftForge.EVENT_BUS.register(FakePlayerPool.instance);
        GameRegistry.registerFuelHandler(new VanillaFuelHandler());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

        //Open Network Pipeline
        packetPipeline.initalise();

        //Set Up Custom Renderers
        ClientProxy.setCustomRenderers();

        //Setup GUIs
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiManager());

        FMLInterModComms.sendMessage("Waila", "register", "com.teamcos.modularsystems.core.waila.WailaDataProvider.callbackRegister");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        //Close Network
        packetPipeline.postInitialise();
    }
}
