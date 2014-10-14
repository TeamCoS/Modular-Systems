package com.pauljoda.modularsystems.furnace.blocks;

import com.pauljoda.modularsystems.core.ModularSystems;
import com.pauljoda.modularsystems.core.proxy.ClientProxy;
import com.pauljoda.modularsystems.furnace.tiles.TileEntityFurnaceCore;
import com.pauljoda.modularsystems.furnace.tiles.TileEntityFurnaceDummy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockBasicDummy extends BlockContainer {

	public BlockBasicDummy(Material material, boolean inTab)
	{
		super(material);
		if(inTab)
			setCreativeTab(ModularSystems.tabModularSystems);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i)
	{
		return new TileEntityFurnaceDummy();
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block par5, int par6)
	{
		TileEntityFurnaceDummy dummy = (TileEntityFurnaceDummy)world.getTileEntity(x, y, z);

        if(dummy.getCore() != null)
            dummy.getCore().isDirty = true;

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
