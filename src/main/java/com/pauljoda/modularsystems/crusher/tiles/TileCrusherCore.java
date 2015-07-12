package com.pauljoda.modularsystems.crusher.tiles;

import com.pauljoda.modularsystems.core.functions.BlockCountFunction;
import com.pauljoda.modularsystems.core.tiles.AbstractCore;
import com.teambr.bookshelf.api.waila.IWaila;
import com.teambr.bookshelf.common.tiles.IOpensGui;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class TileCrusherCore extends AbstractCore implements IOpensGui, IWaila {
    @Override
    protected void updateBlockState(boolean positiveBurnTime, World world, int x, int y, int z) {

    }

    @Override
    protected ItemStack recipe(ItemStack is) {
        return null;
    }

    @Override
    protected int getItemBurnTime(ItemStack stack) {
        return 0;
    }

    @Override
    protected boolean isBlockBanned(Block block, int meta) {
        return false;
    }

    @Override
    protected void generateValues(BlockCountFunction function) {

    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public void returnWailaHead(List<String> tip) {

    }

    @Override
    public void returnWailaBody(List<String> tip) {

    }

    @Override
    public void returnWailaTail(List<String> tip) {

    }
}
