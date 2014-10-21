package com.teamcos.modularsystems.utilities.block;

import com.teamcos.modularsystems.utilities.tiles.DummyIOTile;
import com.teamcos.modularsystems.utilities.tiles.DummyTile;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class DummyIOBlock extends DummyBlock {

    public DummyIOBlock(Material material, boolean inTab) {
        super(material, inTab);
        setStepSound(Block.soundTypeStone);
        setHardness(3.5f);
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon("dispenser_front_vertical");
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        DummyTile dummy = (DummyTile) world.getTileEntity(x, y, z);
        if(player.isSneaking()) {
            dummy.nextSlot();

            this.displaySlot(world, x, y, z, player);

            return false;
        }

        return super.onBlockActivated(world, x, y, z, player, par6, par7, par8, par9);
    }

    private void displaySlot(World world, int x, int y, int z, EntityPlayer player) {
        DummyTile dummy = (DummyTile)world.getTileEntity(x, y, z);
        player.addChatMessage(dummy.getSlotNameForChat());
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new DummyIOTile();
    }
}
