package com.pauljoda.modularsystems.storage.blocks;

import com.pauljoda.modularsystems.core.lib.Reference;
import com.teambr.bookshelf.collections.BlockTextures;
import com.teambr.bookshelf.common.blocks.rotation.IRotation;
import com.teambr.bookshelf.common.blocks.rotation.SixWayRotation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/27/2015
 */
public class BlockStorageSmashing extends BlockStorageExpansion {
    /**
     * Used as a common class for all blocks. Makes things a bit easier
     *
     * @param mat  What material the block should be
     * @param name The unlocalized name of the block : Must be format "MODID:name"
     * @param tile Should the block have a tile, pass the class
     */
    public BlockStorageSmashing(Material mat, String name, Class<? extends TileEntity> tile) {
        super(mat, name, tile);
    }

    /**
     * Used to add the textures for the block. Uses block name by default
     *
     * Initialize the {@link BlockTextures} object here
     * @param iconRegister Icon Registry
     */
    public void generateDefaultTextures(IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon(Reference.MOD_ID + ":storageCapacity");
        textures = new BlockTextures(iconRegister, Reference.MOD_ID + ":storageCapacity");
        textures.setFront(iconRegister.registerIcon(Reference.MOD_ID + ":storageSmashing"));
        this.errorIcon = iconRegister.registerIcon(Reference.MOD_ID + ":storageNoConnection");
    }

    //The sides are: Bottom (0), Top (1), North (2), South (3), West (4), East (5).
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        switch(side) {
            case 0 :
                return textures.getDown(metadata, getDefaultRotation());
            case 1 :
                return textures.getUp(metadata, getDefaultRotation());
            case 2 :
                return textures.getNorth(metadata, getDefaultRotation());
            case 3 :
                return textures.getSouth(metadata, getDefaultRotation());
            case 4 :
                return textures.getWest(metadata, getDefaultRotation());
            case 5 :
                return textures.getEast(metadata, getDefaultRotation());
            default :
                return this.blockIcon;
        }
    }

    /**
     * Used to set the values needed for the block's rotation on placement
     * @return The rotation class needed, none by default
     */
    public IRotation getDefaultRotation() {
        return new SixWayRotation();
    }
}
