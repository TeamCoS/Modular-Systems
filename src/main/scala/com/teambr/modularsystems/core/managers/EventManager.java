package com.teambr.modularsystems.core.managers;

import com.teambr.bookshelf.helper.LogHelper;
import com.teambr.modularsystems.core.network.PacketManager;
import com.teambr.modularsystems.core.network.ResetClientValues;
import com.teambr.modularsystems.core.network.SyncBlockValues;
import com.teambr.modularsystems.core.registries.BlockValueRegistry;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * This file was created for the Modular-Systems
 * <p>
 * Modular-Systems if licensed under the is licensed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Dyonovan
 * @since 3/24/2016
 */
public class EventManager {

    public static EventManager INSTANCE = new EventManager();

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player != null && event.player.getServer().isDedicatedServer()) {
            PacketManager.net.sendTo(new SyncBlockValues(BlockValueRegistry.INSTANCE.values), (EntityPlayerMP) event.player);
            //PacketManager.net.sendTo(new SyncCrusherRecipes(CrusherRecipeRegistry.INSTANCE.crusherRecipes), (EntityPlayerMP) event.player);
            LogHelper.info("Sync Complete...");
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.player != null && event.player.getServer().isDedicatedServer()) {
            PacketManager.net.sendTo(new ResetClientValues(), (EntityPlayerMP) event.player);
        }
    }
}
