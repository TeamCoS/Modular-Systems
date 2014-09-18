package com.pauljoda.modularsystems.storage.blocks;

import com.pauljoda.modularsystems.core.lib.Reference;
import com.pauljoda.modularsystems.storage.tiles.TileEntityStorageExpansion;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSmashingStorageExpansion extends BlockStorageExpansion {

	@SideOnly(Side.CLIENT)
	private IIcon drillFace;

	public BlockSmashingStorageExpansion() {
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
		else if(meta == 1)
			return errorIcon;
		return blockIcon;
	}

	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemStack)
	{
		int l = MathHelper.floor_double((double)(entityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

		if (MathHelper.abs((float)entityLivingBase.posX - (float)x) < 2.0F && MathHelper.abs((float)entityLivingBase.posZ - (float)z) < 2.0F)
		{
			double d0 = entityLivingBase.posY + 1.82D - (double)entityLivingBase.yOffset;

			if (d0 - (double)y > 2.0D)
			{
				world.setBlockMetadataWithNotify(x, y, z, 3, 2);
			}

			if ((double)y - d0 > 0.0D)
			{
				world.setBlockMetadataWithNotify(x, y, z, 2, 2);
			}
		}
		else
		{
			if (l == 0)
			{
				world.setBlockMetadataWithNotify(x, y, z, 4, 2);
			}

			if (l == 1)
			{
				world.setBlockMetadataWithNotify(x, y, z, 7, 2);
			}

			if (l == 2)
			{
				world.setBlockMetadataWithNotify(x, y, z, 5, 2);
			}

			if (l == 3)
			{
				world.setBlockMetadataWithNotify(x, y, z, 6, 2);
			}
		}
		super.onBlockPlacedBy(world, x, y, z, entityLivingBase, itemStack);
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block changedBlock)
    {
		TileEntityStorageExpansion expansion = (TileEntityStorageExpansion)world.getTileEntity(x, y, z);
		expansion.tryBreakBlock();
    }
	
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		TileEntityStorageExpansion tile = new TileEntityStorageExpansion();
		tile.setTileType(Reference.SMASHING_STORAGE_EXPANSION);
		return tile;
	}
}
