package com.pauljoda.modularsystems.storage.events;

import com.pauljoda.modularsystems.core.managers.BlockManager;
import com.pauljoda.modularsystems.storage.tiles.TileEntityStorageExpansion;
import com.pauljoda.modularsystems.storage.tiles.TileStorageCore;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.world.BlockEvent;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/25/2015
 */
public class BlockBreakEvent {
    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        if(event.block == BlockManager.storageCore) {
            TileStorageCore core = (TileStorageCore)event.world.getTileEntity(event.x, event.y, event.z);
            if(!core.canOpen(event.getPlayer())) {
                event.setCanceled(true);
                event.getPlayer().addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("event.breakBlock.permission")));
            }
        } else if(event.world.getTileEntity(event.x, event.y, event.z) != null &&
                event.world.getTileEntity(event.x, event.y, event.z) instanceof TileEntityStorageExpansion) {
            TileEntityStorageExpansion tile = (TileEntityStorageExpansion) event.world.getTileEntity(event.x, event.y, event.z);
            if (tile.getCore() != null && !tile.getCore().canOpen(event.getPlayer())) {
                event.setCanceled(true);
                event.getPlayer().addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("event.breakBlock.permission")));
            }
        }
    }
}
