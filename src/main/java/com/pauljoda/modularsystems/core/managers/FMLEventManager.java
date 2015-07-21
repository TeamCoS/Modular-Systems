package com.pauljoda.modularsystems.core.managers;

import com.pauljoda.modularsystems.core.network.SyncBlockValues;
import com.pauljoda.modularsystems.core.registries.BlockValueRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayerMP;

public class FMLEventManager {

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player != null) {
            PacketManager.net.sendTo(new SyncBlockValues(BlockValueRegistry.INSTANCE.values), (EntityPlayerMP) event.player);
        }
    }
}
