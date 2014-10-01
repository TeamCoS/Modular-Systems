package com.pauljoda.modularsystems.enchanting.blocks;

import com.pauljoda.modularsystems.core.ModularSystems;
import com.pauljoda.modularsystems.enchanting.tiles.TileEntityEnchantmentAlter;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Random;

public class BlockEnchantmentAlter extends BlockContainer {

	@SideOnly(Side.CLIENT)
	private IIcon top;
	@SideOnly(Side.CLIENT)
	private IIcon bottom;
	private Random r = new Random();

	public BlockEnchantmentAlter() {
		super(Material.rock);
		setBlockName("modularsystems:blockEnchantmentAlter");
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
		this.setLightOpacity(0);
		setHardness(3.5f);
		setCreativeTab(ModularSystems.tabModularSystems);
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

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata)
	{
		return side == 0 ? this.bottom : (side == 1 ? this.top : this.blockIcon);
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register)
	{
		this.blockIcon = register.registerIcon("modularsystems:enchantmentAlterSide");
		this.top = register.registerIcon("modularsystems:enchantmentAlterTop");
		this.bottom = register.registerIcon("modularsystems:enchantmentAlterBottom");
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block par5, int par6)
	{
		TileEntityEnchantmentAlter alter = (TileEntityEnchantmentAlter)world.getTileEntity(x, y, z);
		for (int i1 = 0; i1 < alter.getSizeInventory(); ++i1)
		{
			ItemStack itemstack = alter.getStackInSlot(i1);

			if (itemstack != null)
			{
				float f = this.r .nextFloat() * 0.8F + 0.1F;
				float f1 = this.r.nextFloat() * 0.8F + 0.1F;
				float f2 = this.r.nextFloat() * 0.8F + 0.1F;

				while (itemstack.stackSize > 0)
				{
					int j1 = this.r.nextInt(21) + 10;

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
					entityitem.motionX = (double)((float)this.r.nextGaussian() * f3);
					entityitem.motionY = (double)((float)this.r.nextGaussian() * f3 + 0.2F);
					entityitem.motionZ = (double)((float)this.r.nextGaussian() * f3);
					world.spawnEntityInWorld(entityitem);
				}
			}
			world.func_147453_f(x, y, z, par5);
		}

		super.breakBlock(world, x, y, z, par5, par6);
	}
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		if(player.isSneaking())
			player.openGui(ModularSystems.instance, 1, world, x, y, z);
		else
			player.openGui(ModularSystems.instance, 0, world, x, y, z);
		return true;
	}
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityEnchantmentAlter();
	}
}
