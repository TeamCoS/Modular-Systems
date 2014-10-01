package com.pauljoda.modularsystems.storage.blocks;

import com.pauljoda.modularsystems.core.lib.Reference;
import com.pauljoda.modularsystems.storage.tiles.TileEntityStorageExpansion;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockArmorExpansion extends BlockBasicExpansion {

	public BlockArmorExpansion() {
		super();
		setBlockName("modularsystems:blockArmorStorageExpansion");
	}
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		blockIcon = par1IconRegister.registerIcon("modularsystems:chestSideArmor");
		errorIcon = par1IconRegister.registerIcon("modularsystems:chestSideError");
	}
	
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		TileEntityStorageExpansion tile = new TileEntityStorageExpansion();
		tile.setTileType(Reference.ARMOR_STORAGE_EXPANSION);
		return tile;
	}
}
