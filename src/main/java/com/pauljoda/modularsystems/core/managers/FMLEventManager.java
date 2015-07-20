package com.pauljoda.modularsystems.core.managers;

import com.pauljoda.modularsystems.core.collections.BlockValues;
import com.pauljoda.modularsystems.core.network.AddCalculationPacket;
import com.pauljoda.modularsystems.core.registries.BlockValueRegistry;
import com.teambr.bookshelf.collections.Couplet;
import com.teambr.bookshelf.helpers.BlockHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import net.minecraft.block.Block;
import net.minecraft.network.NetHandlerPlayServer;

import java.util.Map;

public class FMLEventManager {

    @SubscribeEvent
    public void onClientConnectEvent(FMLNetworkEvent.ServerConnectionFromClientEvent event) {
        for (Map.Entry<String, BlockValues> entry : BlockValueRegistry.INSTANCE.values.entrySet()){
            Couplet<Block, Integer> block = BlockHelper.getBlockFromString(entry.getKey());
            BlockValues value = entry.getValue();
            PacketManager.net.sendTo(new AddCalculationPacket.CalculationMessage(block.getFirst(), block.getSecond(),
                            value.getSpeedFunction(), value.getEfficiencyFunction(), value.getMultiplicityFunction()),
                    ((NetHandlerPlayServer)event.handler).playerEntity);
        }
    }
}
