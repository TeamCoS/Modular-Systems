package com.teambr.modularsystems.core.client;

import com.teambr.modularsystems.core.client.renderer.TileSpecialDummyRenderer;
import com.teambr.modularsystems.power.tiles.TileBankBase;
import net.minecraftforge.fml.client.registry.ClientRegistry;

/**
 * This file was created for Modular-Systems
 * <p/>
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis <pauljoda>
 * @since 1/18/2016
 */
public class RenderRegistry {
    public static void doTheThing() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileBankBase.class, new TileSpecialDummyRenderer());
    }
}
