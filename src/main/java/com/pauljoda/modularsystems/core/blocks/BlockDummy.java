package com.pauljoda.modularsystems.core.blocks;

import com.pauljoda.modularsystems.core.lib.Reference;
import com.pauljoda.modularsystems.core.renderers.BlockDummyRenderer;
import com.pauljoda.modularsystems.core.tiles.AbstractCore;
import com.pauljoda.modularsystems.core.tiles.DummyTile;
import com.pauljoda.modularsystems.crusher.tiles.TileCrusherCore;
import com.pauljoda.modularsystems.furnace.tiles.TileEntityFurnaceCore;
import com.teambr.bookshelf.Bookshelf;
import com.teambr.bookshelf.collections.BlockTextures;
import com.teambr.bookshelf.common.blocks.BaseBlock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Random;

public class BlockDummy extends BaseBlock {
    @SideOnly(Side.CLIENT)
    protected IIcon furnaceOverlay, crusherOverlay;

    public BlockDummy(Material mat, String name, Class<? extends TileEntity> tile) {
        super(mat, name, tile);
    }

    @Override
    public Item getItemDropped(int i, Random r, int e) {
        return null;
    }

    /**
     * Called when the blocks is activated. We are using it to open our GUI
     */
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        if(!player.isSneaking()) {
            DummyTile us = (DummyTile)world.getTileEntity(x, y, z);
            if(us.getCore() != null)
                player.openGui(Bookshelf.instance, 0, world, us.getCore().xCoord, us.getCore().yCoord, us.getCore().zCoord);
            return true;
        }
        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block par5, int par6) {
        DummyTile dummy = (DummyTile)world.getTileEntity(x, y, z);
        AbstractCore core = dummy.getCore();

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
     * Used to add the textures for the blocks. Uses blocks name by default
     *
     * Initialize the {@link BlockTextures} object here
     * @param iconRegister Icon Registry
     */
    public void generateDefaultTextures(IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon("minecraft:stone");
        this.furnaceOverlay = iconRegister.registerIcon(Reference.MOD_ID + ":furnaceOverlay");
        this.crusherOverlay = iconRegister.registerIcon(Reference.MOD_ID + ":crusherOverlay");

        textures = new BlockTextures(iconRegister, "minecraft:stone");
        textures.setOverlay(iconRegister.registerIcon("minecraft:hopper_top"));
    }

    /**
     * Get the overlay for the dummy based on the core
     * @param core The core this is attached to
     * @return The correct overlay icon
     */
    public IIcon getOverlayIcon(AbstractCore core) {
        return core instanceof TileEntityFurnaceCore ? furnaceOverlay : core instanceof TileCrusherCore ? crusherOverlay : textures.getOverlay();
    }

    @Override
    public int getRenderType() {
        return BlockDummyRenderer.renderID;
    }
}
