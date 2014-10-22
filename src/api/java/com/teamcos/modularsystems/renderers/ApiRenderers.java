package com.teamcos.modularsystems.renderers;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class ApiRenderers {

    public static int renderPass;
    public static int apiDummyRenderType;
    public static void init() {
        apiDummyRenderType = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new ApiBlockRenderer());
    }
}
