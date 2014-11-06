package com.teamcos.modularsystems.proxy;

import com.teamcos.modularsystems.manager.ApiBlockManager;
import com.teamcos.modularsystems.renderers.TankItemRenderer;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxyAPI extends CommonProxyAPI {


    @Override
    public void registerRenderers() {
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ApiBlockManager.fluidTank), new TankItemRenderer());
    }
}
