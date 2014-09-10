package com.pauljoda.modularsystems.furnace.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import com.pauljoda.modularsystems.furnace.tiles.TileEntityFurnaceCore;
import com.pauljoda.modularsystems.furnace.tiles.TileEntityFurnaceDummy;
import com.pauljoda.modularsystems.managers.BlockManager;
import com.pauljoda.modularsystems.proxy.ClientProxy;

public class BlockFurnaceDummyIOActive extends BlockContainer
{
	int lastSlot = 4;
	public BlockFurnaceDummyIOActive()
	{
		super(Material.rock);

		setBlockName("modularsystems:blockFurnaceIOActive");
		setStepSound(Block.soundTypeStone);
		setHardness(3.5f);

	}
	public int meta = 0;

	@Override
	public Item getItemDropped(int par1, Random par2Random, int par3)
	{
		return Item.getItemFromBlock(BlockManager.furnaceDummyIO);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int i)
	{
		return new TileEntityFurnaceDummy();
	}
	
	@Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        blockIcon = iconRegister.registerIcon("dispenser_front_vertical");
    }
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block par5, int par6)
	{
		TileEntityFurnaceDummy dummy = (TileEntityFurnaceDummy)world.getTileEntity(x, y, z);

		if(dummy != null && dummy.getCore() != null)
			dummy.getCore().invalidateMultiblock();

		super.breakBlock(world, x, y, z, par5, par6);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		TileEntityFurnaceDummy dummy = (TileEntityFurnaceDummy)world.getTileEntity(x, y, z);
		if(player.isSneaking())
		{
			if(dummy.slot == 4)
				dummy.slot = 0;

			dummy.slot = dummy.slot + 1;
			if(dummy.slot == 3)
				dummy.slot = 0;

			this.displaySlot(world, x, y, z, player);


			return false;
		}



		if(dummy != null && dummy.getCore() != null)
		{
			TileEntityFurnaceCore core = dummy.getCore();
			return core.getBlockType().onBlockActivated(world, core.xCoord, core.yCoord, core.zCoord, player, par6, par7, par8, par9);
		}

		return true;
	}

	private void displaySlot(World world, int x, int y, int z, EntityPlayer player)
	{
		TileEntityFurnaceDummy dummy = (TileEntityFurnaceDummy)world.getTileEntity(x, y, z);

		if(lastSlot == 4)
			lastSlot = dummy.slot;

		if(lastSlot != dummy.slot)
		{
			switch(dummy.slot)
			{
			case 0 :  player.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + "Slot is Fuel"));
					  lastSlot = dummy.slot;
					  break;

			case 1 :  player.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_AQUA + "Slot is Input"));
					  lastSlot = dummy.slot;
					  break;

			case 2 : player.addChatMessage(new ChatComponentText(EnumChatFormatting.GRAY + "Slot is Output"));
					  lastSlot = dummy.slot;
					  break;
					  
			default : break;
			}
		}
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
