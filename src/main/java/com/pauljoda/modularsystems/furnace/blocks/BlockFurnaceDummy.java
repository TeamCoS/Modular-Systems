package com.pauljoda.modularsystems.furnace.blocks;

import com.dyonovan.brlib.BRLib;
import com.dyonovan.brlib.collections.BlockTextures;
import com.dyonovan.brlib.common.blocks.BaseBlock;
import com.pauljoda.modularsystems.core.lib.Reference;
import com.pauljoda.modularsystems.core.renderers.BlockDummyRenderer;
import com.pauljoda.modularsystems.furnace.tiles.TileEntityFurnaceCore;
import com.pauljoda.modularsystems.furnace.tiles.TileEntityFurnaceDummy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BlockFurnaceDummy extends BaseBlock {
    public BlockFurnaceDummy() {
        super(Material.rock, Reference.MOD_ID + ":furnaceDummy", TileEntityFurnaceDummy.class);
    }

    /**
     * Called when the block is activated. We are using it to open our GUI
     */
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        if(!player.isSneaking()) {
            TileEntityFurnaceDummy us = (TileEntityFurnaceDummy)world.getTileEntity(x, y, z);
            if(us.getCore() != null)
                player.openGui(BRLib.instance, 0, world, us.getCore().xCoord, us.getCore().yCoord, us.getCore().zCoord);
            return true;
        }
        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block par5, int par6) {
        TileEntityFurnaceDummy dummy = (TileEntityFurnaceDummy)world.getTileEntity(x, y, z);
        TileEntityFurnaceCore core = dummy.getCore();

        if(core != null) {
            core.setDirty();
        }

        Block block = dummy.getStoredBlock();
        int meta = dummy.getMetadata();

        if (world.isAirBlock(x, y, z)) {
            float f = world.rand.nextFloat() * 0.8F + 0.1F;
            float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
            float f2 = world.rand.nextFloat() * 0.8F + 0.1F;

            EntityItem entityitem = new EntityItem(
                    world,
                    (double) x + f,
                    (double) y + f1,
                    (double) z + f2,
                    new ItemStack(block, 1, meta)
            );

            world.spawnEntityInWorld(entityitem);
        }
        super.breakBlock(world, x, y, z, par5, par6);
    }


    /**
     * Used to add the textures for the block. Uses block name by default
     *
     * Initialize the {@link BlockTextures} object here
     * @param iconRegister Icon Registry
     */
    public void generateDefaultTextures(IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon("minecraft:stone");
        textures = new BlockTextures(iconRegister, "minecraft:stone");
        textures.setOverlay(iconRegister.registerIcon(Reference.MOD_ID + ":furnaceDummy"));
    }

    @Override
    public int getRenderType() {
        return BlockDummyRenderer.renderID;
    }
}
