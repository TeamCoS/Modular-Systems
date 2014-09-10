package com.pauljoda.modularsystems.furnace.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.pauljoda.modularsystems.furnace.tiles.TileEntityFurnaceCore;
import com.pauljoda.modularsystems.furnace.tiles.TileEntityFurnaceDummy;
import com.pauljoda.modularsystems.proxy.ClientProxy;

public class BlockFurnaceDummy extends BlockContainer
{

	public BlockFurnaceDummy()
	{
		super(Material.rock);
		setBlockName("modularsystems:blockFurnaceDummy");
		setStepSound(Block.soundTypeStone);
		setHardness(3.5f);
		
	}
	public int meta = 0;
	Random furnaceRand = new Random();
	
	@Override
	public TileEntity createNewTileEntity(World world, int i)
	{
		return new TileEntityFurnaceDummy();
	}


	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
	{
		return null;
	}

	@Override
	public void registerBlockIcons(IIconRegister iconRegister)
	{
			blockIcon = iconRegister.registerIcon("cobblestone"); 

	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block par5, int par6)
	{
		TileEntityFurnaceDummy dummy = (TileEntityFurnaceDummy)world.getTileEntity(x, y, z);

		if(dummy != null && dummy.getCore() != null)
		{
			dummy.getCore().isValidMultiblock = false;
			dummy.getCore().invalidateMultiblock();

		}
		if(world.getBlock(x, y, z) == null)
		{
			float f = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
			float f1 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
			float f2 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;

			EntityItem entityitem = new EntityItem(world, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(dummy.getBlock(), 1, dummy.getMeta()));

			world.spawnEntityInWorld(entityitem);
		}
		super.breakBlock(world, x, y, z, par5, par6);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		if(player.isSneaking())
			return false;

		TileEntityFurnaceDummy dummy = (TileEntityFurnaceDummy)world.getTileEntity(x, y, z);

		if(dummy != null && dummy.getCore() != null)
		{
			TileEntityFurnaceCore core = dummy.getCore();
			return core.getBlockType().onBlockActivated(world, core.xCoord, core.yCoord, core.zCoord, player, par6, par7, par8, par9);
		}

		return true;
	}
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	@Override
	public int getRenderType()
	{
		return ClientProxy.furnaceDummyRenderType;
	}

	@Override
	public boolean canRenderInPass(int pass)
	{
		//Set the static var in the client proxy
		ClientProxy.renderPass = pass;
		//the block can render in both passes, so return true always
		return true;
	}
	@Override
	public int getRenderBlockPass()
	{
		return 1;
	}

}