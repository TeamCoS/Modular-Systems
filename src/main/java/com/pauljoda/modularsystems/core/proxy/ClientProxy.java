package com.pauljoda.modularsystems.core.proxy;

import com.pauljoda.modularsystems.core.managers.BlockManager;
import com.pauljoda.modularsystems.core.renderers.BlockDummyRenderer;
import com.pauljoda.modularsystems.core.renderers.SpecialDummyRenderer;
import com.pauljoda.modularsystems.core.renderers.TileSpecialDummyRenderer;
import com.pauljoda.modularsystems.power.tiles.TileBankBase;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInterModComms;
import net.minecraft.item.ItemStack;

public class ClientProxy extends CommonProxy {
    public void preInit() {
        BlockDummyRenderer.renderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new BlockDummyRenderer());

        SpecialDummyRenderer.renderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new SpecialDummyRenderer());

        ClientRegistry.bindTileEntitySpecialRenderer(TileBankBase.class, new TileSpecialDummyRenderer());
    }

    public void init() {
        //Hide Blocks from NEI
        if (Loader.isModLoaded("NotEnoughItems")) {
            codechicken.nei.api.API.hideItem(new ItemStack(BlockManager.furnaceCoreActive));
            codechicken.nei.api.API.hideItem(new ItemStack(BlockManager.crusherCoreActive));
            codechicken.nei.api.API.hideItem(new ItemStack(BlockManager.generatorCoreActive));
            codechicken.nei.api.API.hideItem(new ItemStack(BlockManager.dummy));
        }

        //Register with Waila
        FMLInterModComms.sendMessage("Waila", "register",
                "com.teambr.bookshelf.api.waila.WailaDataProvider.callbackRegister");
    }
}
