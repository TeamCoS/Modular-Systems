package com.pauljoda.modularsystems.core.managers;

import com.pauljoda.modularsystems.core.network.GetHashPacket;
import com.pauljoda.modularsystems.core.registries.BlockValueRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;

public class FMLEventManager {

    @SubscribeEvent
    public void onClientConnectEvent(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        int hash = BlockValueRegistry.INSTANCE.values.hashCode();
        PacketManager.net.sendToServer(new GetHashPacket.HashMessage(hash));
    }
}
