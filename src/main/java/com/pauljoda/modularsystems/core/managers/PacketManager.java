package com.pauljoda.modularsystems.core.managers;

import com.pauljoda.modularsystems.core.lib.Reference;
import com.pauljoda.modularsystems.core.network.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

/**
 * The manager to send packets to the client or server
 */
public class PacketManager {

    public static SimpleNetworkWrapper net;
    private static int nextPacketId = 0;

    public static void initPackets() {
        net = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID.toUpperCase());

        registerMessage(OpenContainerPacket.class, OpenContainerPacket.UpdateMessage.class);
        registerMessage(AddCalculationPacket.class, AddCalculationPacket.CalculationMessage.class);
        registerMessage(SyncBlockValues.class, SyncBlockValues.class);
        registerMessage(DeleteValuesPacket.class, DeleteValuesPacket.class);
        registerMessage(SyncCrusherRecipes.class, SyncCrusherRecipes.class);
    }

    @SuppressWarnings("unchecked")
    private static void registerMessage(Class packet, Class message) {
        net.registerMessage(packet, message, nextPacketId, Side.CLIENT);
        net.registerMessage(packet, message, nextPacketId, Side.SERVER);
        nextPacketId++;
    }
}
