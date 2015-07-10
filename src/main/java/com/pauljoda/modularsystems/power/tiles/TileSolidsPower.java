package com.pauljoda.modularsystems.power.tiles;

import com.pauljoda.modularsystems.core.providers.FuelProvider;
import com.teambr.bookshelf.common.tiles.IOpensGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class TileSolidsPower extends TilePowerBase implements FuelProvider, IOpensGui {

    public TileSolidsPower() {}

    /*
     * Fuel Provider Functions
     */
    @Override
    public boolean canProvide() {
        return false;
    }

    @Override
    public double fuelProvided() {
        return 0;
    }

    @Override
    public double consume() {
        return 0;
    }

    @Override
    public FuelProviderType type() {
        return FuelProviderType.POWER;
    }

    /*
     * Tile Entity Functions
     */
    @Override
    public Object getServerGuiElement(int i, EntityPlayer entityPlayer, World world, int i1, int i2, int i3) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int i, EntityPlayer entityPlayer, World world, int i1, int i2, int i3) {
        return null;
    }
}
