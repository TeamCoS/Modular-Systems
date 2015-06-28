package com.pauljoda.modularsystems.core;

import com.pauljoda.modularsystems.core.lib.Reference;
import com.pauljoda.modularsystems.core.proxy.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

@Mod(name = Reference.MOD_NAME, modid = Reference.MOD_ID, version = Reference.VERSION)
public class ModularSystems {
    @Mod.Instance(Reference.MOD_ID)
    public static ModularSystems instance;

    @SidedProxy( clientSide="com.pauljoda.modularsystems.core.proxy.ClientProxy", serverSide="com.pauljoda.modularsystems.core.proxy.CommonProxy")
    public static CommonProxy proxy;

    //Creates the Creative Tab
    public static CreativeTabs tabModularSystems = new CreativeTabs("tabModularSystems"){

        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return Item.getItemFromBlock(Blocks.furnace);

        }
    };

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){ }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) { }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) { }
}
