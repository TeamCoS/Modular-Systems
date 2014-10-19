package com.teamcos.modularsystems.furnace.blocks;

import com.teamcos.modularsystems.furnace.tiles.TileEntityFurnaceDummy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Random;

public class BlockFurnaceDummy extends BlockBasicDummy
{

	public BlockFurnaceDummy()
	{
		super(Material.rock, false);
		setBlockName("modularsystems:blockFurnaceDummy");
		setStepSound(Block.soundTypeStone);
		setHardness(3.5f);
		
	}
	public int meta = 0;
	Random furnaceRand = new Random();
	
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
        Block block = dummy.getBlock();
        int meta = dummy.getMeta();

		if(world.isAirBlock(x, y, z))
		{
			float f = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
			float f1 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
			float f2 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;

			EntityItem entityitem = new EntityItem(world, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(block, 1, meta));

			world.spawnEntityInWorld(entityitem);
		}
        super.breakBlock(world, x, y, z, par5, par6);
    }
}
