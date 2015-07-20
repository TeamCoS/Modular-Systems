package com.pauljoda.modularsystems.core.managers;

import com.pauljoda.modularsystems.core.network.GetHashPacket;
import com.pauljoda.modularsystems.core.registries.BlockValueRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public class FMLEventManager {

    @SubscribeEvent
    public void onClientConnectEvent(PlayerEvent.PlayerLoggedInEvent event) {
        int hash = BlockValueRegistry.INSTANCE.materialValues.hashCode();
        PacketManager.net.sendToServer(new GetHashPacket.HashMessage(hash));
    }
}
