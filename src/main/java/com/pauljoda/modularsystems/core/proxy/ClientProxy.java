package com.pauljoda.modularsystems.core.proxy;

import com.pauljoda.modularsystems.core.renderers.BlockDummyRenderer;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
    public void init() {
        BlockDummyRenderer.renderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new BlockDummyRenderer());
    }
}
