package com.teamcos.modularsystems.storage.blocks;

import com.teamcos.modularsystems.core.lib.Reference;
import com.teamcos.modularsystems.storage.tiles.TileEntityStorageExpansion;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockSmashingExpansion extends BlockBasicExpansion {

	@SideOnly(Side.CLIENT)
	private IIcon drillFace;

	public BlockSmashingExpansion() {
		super();
		setBlockName("modularsystems:blockSmashingStorageExpansion");
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int metadata, float faceLocationX, float faceLocationY, float faceLocationZ)
	{
		if(player.isSneaking() && player.getHeldItem() == null)
		{
			int side = faceLocationY ==  0 ? 0 :(faceLocationY == 1 ? 1 : (faceLocationZ == 0 ? 2 : (faceLocationZ == 1 ? 3 : (faceLocationX == 0 ? 4 : (faceLocationX == 1 ? 5 : 0)))));
			world.setBlockMetadataWithNotify(x, y, z, side + 2, 2);
		}
		return super.onBlockActivated(world, x, y, z, player, metadata, faceLocationX, faceLocationY, faceLocationZ);
	}
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		blockIcon = par1IconRegister.registerIcon("modularsystems:chestSide");
		drillFace = par1IconRegister.registerIcon("modularsystems:chestSideDrill");
		errorIcon = par1IconRegister.registerIcon("modularsystems:chestSideError");
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		if(meta == 0)
			return side == 3 ? drillFace : blockIcon;
		else if(meta - 2 == side)
			return drillFace;
		else if(meta > 7)
			return errorIcon;
		return blockIcon;
	}

	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemStack)
	{
		world.setBlockMetadataWithNotify(x, y, z, BlockPistonBase.determineOrientation(world, x, y, z, entityLivingBase) + 2, 2);
		super.onBlockPlacedBy(world, x, y, z, entityLivingBase, itemStack);
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block changedBlock)
    {
		TileEntityStorageExpansion expansion = (TileEntityStorageExpansion)world.getTileEntity(x, y, z);
		expansion.tryBreakBlock();
		super.onNeighborBlockChange(world, x, y, z, changedBlock);
    }
	
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		TileEntityStorageExpansion tile = new TileEntityStorageExpansion();
		tile.setTileType(Reference.SMASHING_STORAGE_EXPANSION);
		return tile;
	}
}
