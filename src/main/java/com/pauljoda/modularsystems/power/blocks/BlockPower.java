package com.pauljoda.modularsystems.power.blocks;

import com.pauljoda.modularsystems.core.ModularSystems;
import com.pauljoda.modularsystems.core.blocks.BlockDummy;
import com.pauljoda.modularsystems.core.tiles.DummyTile;
import com.pauljoda.modularsystems.power.tiles.TilePowerBase;
import com.teambr.bookshelf.Bookshelf;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockPower extends BlockDummy {

    public BlockPower(String name, Class<? extends TileEntity> tile) {
        super(Material.rock, name, tile);

        setCreativeTab(ModularSystems.tabModularSystems);
        setHardness(3.5f);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        if(!player.isSneaking()) {
            DummyTile us = (DummyTile)world.getTileEntity(x, y, z);
            if(us.getCore() != null)
                player.openGui(Bookshelf.instance, 0, world, us.getCore().xCoord, us.getCore().yCoord, us.getCore().zCoord);
            return true;
        } else if (player.isSneaking()) {
            TilePowerBase us = (TilePowerBase) world.getTileEntity(x, y, z);
            if (us != null) {
                player.openGui(Bookshelf.instance, 0, world, x, y, z);
            }
        }
        return true;
    }
}
