package com.pauljoda.modularsystems.core.managers;

import com.pauljoda.modularsystems.core.network.SyncBlockValues;
import com.pauljoda.modularsystems.core.network.SyncCrusherRecipes;
import com.pauljoda.modularsystems.core.registries.BlockValueRegistry;
import com.pauljoda.modularsystems.core.registries.CrusherRecipeRegistry;
import com.teambr.bookshelf.helpers.LogHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class FMLEventManager {

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player != null && MinecraftServer.getServer().isDedicatedServer()) {
            LogHelper.info("Syncing data to client...");
            PacketManager.net.sendTo(new SyncBlockValues(BlockValueRegistry.INSTANCE.values), (EntityPlayerMP) event.player);
            PacketManager.net.sendTo(new SyncCrusherRecipes(CrusherRecipeRegistry.INSTANCE.crusherRecipes), (EntityPlayerMP) event.player);
            LogHelper.info("Sync Complete...");
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.player != null && MinecraftServer.getServer().isDedicatedServer()) {
            //TODO restore client block & CrusherRecipe values
        }
    }
}
