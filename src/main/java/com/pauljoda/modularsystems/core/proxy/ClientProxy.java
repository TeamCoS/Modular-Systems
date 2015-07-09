package com.pauljoda.modularsystems.core.proxy;

import com.pauljoda.modularsystems.core.managers.BlockManager;
import com.pauljoda.modularsystems.core.renderers.BlockDummyRenderer;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInterModComms;
import net.minecraft.item.ItemStack;

public class ClientProxy extends CommonProxy {
    public void preInit() {
        BlockDummyRenderer.renderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new BlockDummyRenderer());
    }

    public void init() {
        //Hide Blocks from NEI
        if (Loader.isModLoaded("NotEnoughItems")) {
            codechicken.nei.api.API.hideItem(new ItemStack(BlockManager.furnaceCoreActive));
            codechicken.nei.api.API.hideItem(new ItemStack(BlockManager.furnaceDummy));
        }

        //Register with Waila
        FMLInterModComms.sendMessage("Waila", "register",
                "com.pauljoda.modularsystems.core.waila.WailaDataProvider.callbackRegister");
    }
}
