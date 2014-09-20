package com.pauljoda.modularsystems.storage.blocks;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.pauljoda.modularsystems.core.lib.Reference;
import com.pauljoda.modularsystems.storage.tiles.TileEntityStorageExpansion;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSortingExpansion extends BlockBasicExpansion {

	public BlockSortingExpansion() {
		super();
		setBlockName("modularsystems:blockSortingStorageExpansion");
	}
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		blockIcon = par1IconRegister.registerIcon("modularsystems:chestSideSorting");
		errorIcon = par1IconRegister.registerIcon("modularsystems:chestSideError");
	}
	
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		TileEntityStorageExpansion tile = new TileEntityStorageExpansion();
		tile.setTileType(Reference.SORTING_STORAGE_EXPANSION);
		return tile;
	}
}