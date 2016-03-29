package com.teambr.modularsystems.core.network;

import com.teambr.modularsystems.core.lib.Reference;
import com.teambr.modularsystems.storage.network.ScrollStorageCore;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * This file was created for Modular-Systems
 * <p/>
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Dyonovan
 * @since August 08, 2015
 */
public class PacketManager {

    public static SimpleNetworkWrapper net;
    private static int nextPacketId = 0;

    public static void initPackets() {
        net = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID().toUpperCase());

        registerMessage(OpenContainerPacket.class, OpenContainerPacket.class);
        registerMessage(AddCalculationPacket.class, AddCalculationPacket.CalculationMessage.class);
        registerMessage(SyncBlockValues.class, SyncBlockValues.class);
        registerMessage(DeleteValuesPacket.class, DeleteValuesPacket.class);

        registerMessage(ScrollStorageCore.class, ScrollStorageCore.class);
    }

    @SuppressWarnings("unchecked")
    private static void registerMessage(Class packet, Class message) {
        net.registerMessage(packet, message, nextPacketId, Side.CLIENT);
        net.registerMessage(packet, message, nextPacketId, Side.SERVER);
        nextPacketId++;
    }
}
