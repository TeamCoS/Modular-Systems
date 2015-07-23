package com.pauljoda.modularsystems.storage.blocks;

import com.pauljoda.modularsystems.core.ModularSystems;
import com.pauljoda.modularsystems.core.lib.Reference;
import com.pauljoda.modularsystems.storage.tiles.TileStorageCore;
import com.teambr.bookshelf.collections.BlockTextures;
import com.teambr.bookshelf.common.blocks.BaseBlock;
import com.teambr.bookshelf.common.blocks.rotation.FourWayRotation;
import com.teambr.bookshelf.common.blocks.rotation.IRotation;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Modular-Systems
 * Created by Paul Davis on 7/23/2015
 */
public class BlockStorageCore extends BaseBlock {

    public BlockStorageCore() {
        super(Material.wood, Reference.MOD_ID + ":storageCore", TileStorageCore.class);
    }

    @Override
    public IRotation getDefaultRotation() {
        return new FourWayRotation();
    }

    @Override
    public CreativeTabs getCreativeTab() {
        return ModularSystems.tabModularSystems;
    }

    @Override
    public void generateDefaultTextures(IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon(Reference.MOD_ID + ":storageCoreSide");
        textures = new BlockTextures(iconRegister, Reference.MOD_ID + ":storageCoreSide");
        textures.setFront(iconRegister.registerIcon(Reference.MOD_ID + ":storageCoreFront"));
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase livingBase, ItemStack itemStack) {
        super.onBlockPlacedBy(world, x, y, z, livingBase, itemStack);
        if(itemStack.hasDisplayName()) {
            TileStorageCore tile = (TileStorageCore)world.getTileEntity(x, y, z);
            tile.setCustomName(itemStack.getDisplayName());
        }
    }
}