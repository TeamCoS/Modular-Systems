package com.pauljoda.modularsystems.storage.blocks;

import com.pauljoda.modularsystems.core.ModularSystems;
import com.pauljoda.modularsystems.core.lib.Reference;
import com.pauljoda.modularsystems.storage.tiles.TileEntityStorageExpansion;
import com.teambr.bookshelf.collections.BlockTextures;
import com.teambr.bookshelf.common.blocks.BaseBlock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/24/2015
 */
public class BlockStorageExpansion extends BaseBlock {
    @SideOnly(Side.CLIENT)
    protected IIcon errorIcon;
    /**
     * Used as a common class for all blocks. Makes things a bit easier
     *
     * @param mat  What material the block should be
     * @param name The unlocalized name of the block : Must be format "MODID:name"
     * @param tile Should the block have a tile, pass the class
     */
    public BlockStorageExpansion(Material mat, String name, Class<? extends TileEntity> tile) {
        super(mat, name, tile);
    }

    @Override
    public CreativeTabs getCreativeTab() {
        return ModularSystems.tabModularSystems;
    }

    /**
     * Used to add the textures for the block. Uses block name by default
     *
     * Initialize the {@link BlockTextures} object here
     * @param iconRegister Icon Registry
     */
    public void generateDefaultTextures(IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon(blockName);
        textures = new BlockTextures(iconRegister, blockName);
        this.errorIcon = iconRegister.registerIcon(Reference.MOD_ID + ":storageNoConnection");
    }

    //The sides are: Bottom (0), Top (1), North (2), South (3), West (4), East (5).
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        return metadata == 5 ? this.errorIcon : this.blockIcon;
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World world, int x, int y, int z){
        super.onBlockAdded(world, x, y, z);
        TileEntityStorageExpansion tile = (TileEntityStorageExpansion)world.getTileEntity(x, y, z);
        tile.searchAndConnect();
    }

    /**
     * Empty Contents of an Inventory into world on Block Break
     * @param world
     * @param x X-Coord in world
     * @param y Y-Coord in world
     * @param z Z-Coord in world
     */
    public void dropItems(World world, int x, int y, int z) {
        TileEntityStorageExpansion tile = (TileEntityStorageExpansion)world.getTileEntity(x, y, z);
        tile.removeFromNetwork(true);
    }
}
