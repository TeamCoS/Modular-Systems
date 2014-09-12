package com.pauljoda.modularsystems.storage.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.pauljoda.modularsystems.core.ModularSystems;
import com.pauljoda.modularsystems.core.managers.BlockManager;
import com.pauljoda.modularsystems.storage.tiles.TileEntityStorageCore;
import com.pauljoda.modularsystems.storage.tiles.TileEntityStorageExpansion;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockStorageCore extends BlockContainer {

	@SideOnly(Side.CLIENT)
	private IIcon chestFront;
	Random furnaceRand = new Random();

	public BlockStorageCore() {
		super(Material.wood);
		setBlockName("modularsystems:blockStorageCore");
		setCreativeTab(ModularSystems.tabModularSystems);
		setHardness(3.5F);
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
	{
		return Item.getItemFromBlock(BlockManager.storageCore);
	}

	public void onBlockAdded(World par1World, int par2, int par3, int par4)
	{
		super.onBlockAdded(par1World, par2, par3, par4);
		this.setDefaultDirection(par1World, par2, par3, par4);
	}

	private void setDefaultDirection(World par1World, int par2, int par3, int par4)
	{
		if (!par1World.isRemote)
		{
			Block block = par1World.getBlock(par2, par3, par4 - 1);
			Block block1 = par1World.getBlock(par2, par3, par4 + 1);
			Block block2 = par1World.getBlock(par2 - 1, par3, par4);
			Block block3 = par1World.getBlock(par2 + 1, par3, par4);
			byte b0 = 3;

			if (block.func_149730_j() && !block1.func_149730_j())
			{
				b0 = 3;
			}

			if (block1.func_149730_j() && !block.func_149730_j())
			{
				b0 = 2;
			}

			if (block2.func_149730_j() && !block3.func_149730_j())
			{
				b0 = 5;
			}

			if (block3.func_149730_j() && !block2.func_149730_j())
			{
				b0 = 4;
			}


			par1World.setBlockMetadataWithNotify(par2, par3, par4, b0, 2);
		}
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int metadata, float what, float these, float are)
	{
		if(player.isSneaking())
			return false;

		player.openGui(ModularSystems.instance, 0, world, x, y, z);
		return true;
	}
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int par1, int par2)
	{
		return par2 == 0 && par1 == 3 ? this.chestFront : (par1 != par2 ? this.blockIcon : this.chestFront);
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		this.blockIcon = par1IconRegister.registerIcon("modularsystems:chestSide");
		this.chestFront = par1IconRegister.registerIcon("modularsystems:chestFront");
	}

	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack)
	{
		int l = MathHelper.floor_double((double)(par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

		if (l == 0)
		{
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 2, 2);
		}

		if (l == 1)
		{
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 5, 2);

		}

		if (l == 2)
		{
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 3, 2);

		}

		if (l == 3)
		{
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 4, 2);

		}

		if (par6ItemStack.hasDisplayName())
		{
			((TileEntityStorageCore)par1World.getTileEntity(par2, par3, par4)).setGuiDisplayName(par6ItemStack.getDisplayName());
		}
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block par5, int par6)
	{
		TileEntityStorageCore core = (TileEntityStorageCore) world.getTileEntity(x, y, z);

		for (int i1 = 0; i1 < core.getSizeInventory(); ++i1)
		{
			ItemStack itemstack = core.getStackInSlot(i1);

			if (itemstack != null)
			{
				float f = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
				float f1 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
				float f2 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;

				while (itemstack.stackSize > 0)
				{
					int j1 = this.furnaceRand.nextInt(21) + 10;

					if (j1 > itemstack.stackSize)
					{
						j1 = itemstack.stackSize;
					}

					itemstack.stackSize -= j1;
					EntityItem entityitem = new EntityItem(world, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

					if (itemstack.hasTagCompound())
					{
						entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
					}

					float f3 = 0.05F;
					entityitem.motionX = (double)((float)this.furnaceRand.nextGaussian() * f3);
					entityitem.motionY = (double)((float)this.furnaceRand.nextGaussian() * f3 + 0.2F);
					entityitem.motionZ = (double)((float)this.furnaceRand.nextGaussian() * f3);
					world.spawnEntityInWorld(entityitem);
				}
			}
		}

		if(core.getAnchor() != null)
		{
			TileEntityStorageExpansion expansion = (TileEntityStorageExpansion)world.getTileEntity(core.anchorX, core.anchorY, core.anchorZ);
			expansion.invalidateCore();
			world.markBlockForUpdate(core.anchorX, core.anchorY, core.anchorZ);
		}
		world.func_147453_f(x, y, z, par5);

		super.breakBlock(world, x, y, z, par5, par6);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityStorageCore();
	}
}
