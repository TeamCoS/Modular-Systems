package com.pauljoda.modularsystems.storage.blocks;

import com.pauljoda.modularsystems.core.ModularSystems;
import com.pauljoda.modularsystems.core.managers.BlockManager;
import com.pauljoda.modularsystems.storage.tiles.TileEntityStorageCore;
import com.pauljoda.modularsystems.storage.tiles.TileEntityStorageExpansion;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class BlockStorageExpansion extends BlockContainer {

	public BlockStorageExpansion() {
		super(Material.wood);
		setBlockName("modularsystems:blockStorageExpansion");
		setStepSound(Block.soundTypeWood);
		setHardness(3.5f);
		setCreativeTab(ModularSystems.tabModularSystems);
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		blockIcon = par1IconRegister.registerIcon("modularsystems:chestSide");
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		if(player.isSneaking())
			return false;

		TileEntityStorageExpansion dummy = (TileEntityStorageExpansion)world.getTileEntity(x, y, z);

		if(dummy != null && dummy.getCore() != null)
		{
			TileEntityStorageCore core = dummy.getCore();
			return core.getBlockType().onBlockActivated(world, core.xCoord, core.yCoord, core.zCoord, player, par6, par7, par8, par9);
		}

		if(dummy.getCore() == null)
		{
			dummy.coreY = -100;
			if(world.isRemote)
				player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + "Could not locate core, please replace this block"));
		}
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase par5EntityLivingBase, ItemStack itemstack)
	{
		TileEntityStorageExpansion expansion = (TileEntityStorageExpansion)world.getTileEntity(x, y, z);

		for(int i = -1; i <= 1; i++)
		{
			for(int j = -1; j <= 1; j++)
			{
				for(int k = -1; k <= 1; k++)
				{
					Block localBlock = world.getBlock(i + x, j + y, k + z);
					if(localBlock == BlockManager.storageCore)
					{
						TileEntityStorageCore core = (TileEntityStorageCore)world.getTileEntity(i + x, j + y, k + z);
						if(core.getAnchor() == null)
						{
							expansion.isAnchor = true;
							expansion.setCore(core);
							expansion.getCore().setInventoryRows(core.inventoryRows + 1);
							world.markBlockForUpdate(core.xCoord, core.yCoord, core.zCoord);
							break;
						}
					}
				}
			}
		}
		super.onBlockPlacedBy(world, x, y, z, par5EntityLivingBase, itemstack);
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block par5, int par6)
	{
		TileEntityStorageExpansion expansion = (TileEntityStorageExpansion)world.getTileEntity(x, y, z);
		if(expansion.getCore() != null)
		{
			TileEntityStorageCore core = expansion.getCore();
			core.dropItems(x, y, z);
			expansion.getCore().setInventoryRows(core.inventoryRows - 1);
			world.markBlockForUpdate(core.xCoord, core.yCoord, core.zCoord);
		}
		super.breakBlock(world, x, y, z, par5, par6);
	}
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityStorageExpansion();
	}
}
