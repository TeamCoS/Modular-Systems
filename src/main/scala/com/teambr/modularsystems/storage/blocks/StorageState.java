package com.teambr.modularsystems.storage.blocks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.Collection;

/**
 * This file was created for Modular-Systems
 * <p/>
 * Modular-Systems is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis <pauljoda>
 * @since 1/17/2016
 */
public class StorageState implements IBlockState {
    public boolean connected;
    public BlockPos pos;
    public IBlockAccess world;
    public Block block;

    public StorageState(boolean c, BlockPos p, IBlockAccess w, Block b) {
        connected = c;
        pos = p;
        world = w;
        block = b;
    }

    @Override
    public Collection<IProperty> getPropertyNames() {
        return null;
    }

    @Override
    public <T extends Comparable<T>> T getValue(IProperty<T> property) {
        return null;
    }

    @Override
    public <T extends Comparable<T>, V extends T> IBlockState withProperty(IProperty<T> property, V value) {
        return null;
    }

    @Override
    public <T extends Comparable<T>> IBlockState cycleProperty(IProperty<T> property) {
        return null;
    }

    @Override
    public ImmutableMap<IProperty, Comparable> getProperties() {
        return null;
    }

    @Override
    public Block getBlock() {
        return block;
    }
}
