package com.pauljoda.modularsystems.core.proxy;

import com.pauljoda.modularsystems.core.renderer.BasicBlockRenderer;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
    public void init() {
        BasicBlockRenderer.renderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new BasicBlockRenderer());
    }
}
