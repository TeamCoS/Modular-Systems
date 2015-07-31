package com.pauljoda.modularsystems.core.blocks;

import com.pauljoda.modularsystems.core.ModularSystems;
import com.teambr.bookshelf.collections.BlockTextures;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/30/2015
 */
public class BlockRedstoneControl extends BlockDummy {
    /**
     * Creates the dummy block
     *
     * @param mat  The material of the dummy
     * @param name The unlocalized name of the dummy
     * @param tile The tile entity associated with this block
     */
    public BlockRedstoneControl(Material mat, String name, Class<? extends TileEntity> tile) {
        super(mat, name, tile);
    }

    @Override
    public CreativeTabs getCreativeTab() {
        return ModularSystems.tabModularSystems;
    }

    /**
     * Used to add the textures for the blocks. Uses blocks name by default
     *
     * Initialize the {@link BlockTextures} object here
     * @param iconRegister Icon Registry
     */
    public void generateDefaultTextures(IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon("minecraft:redstone_block");

        textures = new BlockTextures(iconRegister, "minecraft:redstone_block");
        textures.setOverlay(iconRegister.registerIcon(this.blockName));
    }
}
