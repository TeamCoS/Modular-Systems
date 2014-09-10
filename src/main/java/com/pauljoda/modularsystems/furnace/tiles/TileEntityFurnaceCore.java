package com.pauljoda.modularsystems.furnace.tiles;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;

import com.pauljoda.modularsystems.furnace.blocks.BlockFurnaceCore;
import com.pauljoda.modularsystems.lib.Reference;
import com.pauljoda.modularsystems.managers.BlockManager;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityFurnaceCore extends TileEntity implements ISidedInventory
{
	//Automation related 
	private static final int[] slots_top = new int[] {0};
	private static final int[] slots_bottom = new int[] {2, 1};
	private static final int[] slots_sides = new int[] {1};
	private static final int[] slots_output_only = new int [] {2};

	//Storage for blocks if they have meaning
	public double speedMultiplier = 8;
	public double efficiencyMultiplier = 1;
	public int smeltingMultiplier = 1;
	public int cookSpeed = 200;
	int direction;
	int maxSize = 20;

	//Size
	public int hMin;
	public int hMax;
	public int vMin;
	public int vMax;
	public int depthVal;

	//Furnace related things
	private ItemStack[] furnaceItems = new ItemStack[3];
	public int furnaceBurnTime;
	public int currentItemBurnTime;
	public int furnaceCookTime;
	private String field_94130_e;

	//Booleans
	public boolean isValidMultiblock = false;
	public boolean crafterEnabled = false;

	//Randomizer
	Random r = new Random();

	//Empty Constructor 
	public TileEntityFurnaceCore()
	{
	}

	/*
	 * Sets the dummies back and invalidates the structure
	 */
	public void invalidateMultiblock()
	{
		int metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);

		revertDummies();

		hMin = 0;
		hMax = 0;
		vMin = 0;
		vMax = 0;
		depthVal = 0;
		speedMultiplier = 8;
		efficiencyMultiplier = 1;
		crafterEnabled = false;
		smeltingMultiplier = 1;
		direction = 0;
		isValidMultiblock = false;
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, metadata, 2);
	}

	//TODO Multi-Block Stuff

	/*
	 * Checks if the structure is known to be valid
	 */
	public boolean getIsValid()
	{
		return isValidMultiblock;
	}

	/*
	 * Called to force update of block
	 */
	public void update()
	{
		worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
	}

	public int getHorizontalMin()
	{
		int output = 0;
		int dir = (worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord));
		if(dir == 0)
			dir = this.direction;
		int depthMultiplier = ((dir == 2 || dir == 4) ? 1 : -1);
		boolean forwardZ = ((dir == 2) || (dir == 3));
		int xCheck = xCoord + (forwardZ ? 0 : depthMultiplier);
		int yCheck = yCoord;
		int zCheck = zCoord + (forwardZ ? depthMultiplier : 0);
		Block airCheck = worldObj.getBlock(xCheck, yCheck, zCheck);

		//get hMin (Left)
		while(airCheck == Blocks.air)
		{
			output++;
			if(forwardZ)
				xCheck = xCheck + depthMultiplier;
			else
				zCheck = zCheck - depthMultiplier;

			airCheck = worldObj.getBlock(xCheck, yCheck, zCheck);
			if(output > maxSize)
				return -1;
		}
		return output;
	}

	public int getHorizontalMax()
	{
		int output = 0;
		int dir = (worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord));
		if(dir == 0)
			dir = this.direction;
		int depthMultiplier = ((dir == 2 || dir == 4) ? 1 : -1);
		boolean forwardZ = ((dir == 2) || (dir == 3));
		int xCheck = xCoord + (forwardZ ? 0 : depthMultiplier);
		int yCheck = yCoord;
		int zCheck = zCoord + (forwardZ ? depthMultiplier : 0);
		Block airCheck = worldObj.getBlock(xCheck, yCheck, zCheck);

		//get hMin (Left)
		while(airCheck == Blocks.air)
		{
			output++;
			if(forwardZ)
				xCheck = xCheck - depthMultiplier;
			else
				zCheck = zCheck + depthMultiplier;

			airCheck = worldObj.getBlock(xCheck, yCheck, zCheck);
			if(output > maxSize)
				return -1;
		}
		return output;
	}

	public int getVerticalMin()
	{
		int output = 0;
		int dir = (worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord));
		if(dir == 0)
			dir = this.direction;
		int depthMultiplier = ((dir == 2 || dir == 4) ? 1 : -1);
		boolean forwardZ = ((dir == 2) || (dir == 3));
		int xCheck = xCoord + (forwardZ ? 0 : depthMultiplier);
		int yCheck = yCoord;
		int zCheck = zCoord + (forwardZ ? depthMultiplier : 0);
		Block airCheck = worldObj.getBlock(xCheck, yCheck, zCheck);

		//get hMin (Left)
		while(airCheck == Blocks.air)
		{
			output++;
			yCheck--;

			airCheck = worldObj.getBlock(xCheck, yCheck, zCheck);
			if(output > maxSize)
				return -1;
		}
		return output;
	}

	public int getVerticalMax()
	{
		int output = 0;
		int dir = (worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord));
		if(dir == 0)
			dir = this.direction;
		int depthMultiplier = ((dir == 2 || dir == 4) ? 1 : -1);
		boolean forwardZ = ((dir == 2) || (dir == 3));
		int xCheck = xCoord + (forwardZ ? 0 : depthMultiplier);
		int yCheck = yCoord;
		int zCheck = zCoord + (forwardZ ? depthMultiplier : 0);
		Block airCheck = worldObj.getBlock(xCheck, yCheck, zCheck);

		//get hMin (Left)
		while(airCheck == Blocks.air)
		{
			output++;
			yCheck++;

			airCheck = worldObj.getBlock(xCheck, yCheck, zCheck);
			if(output > maxSize)
				return -1;
		}
		return output;
	}

	public int getDepthVal()
	{
		int output = 0;
		int dir = (worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord));
		if(dir == 0)
			dir = this.direction;
		int depthMultiplier = ((dir == 2 || dir == 4) ? 1 : -1);
		boolean forwardZ = ((dir == 2) || (dir == 3));
		int xCheck = xCoord + (forwardZ ? 0 : depthMultiplier);
		int yCheck = yCoord;
		int zCheck = zCoord + (forwardZ ? depthMultiplier : 0);
		Block airCheck = worldObj.getBlock(xCheck, yCheck, zCheck);

		//get hMin (Left)
		while(airCheck == Blocks.air)
		{
			output++;
			if(forwardZ)
				zCheck = zCheck + depthMultiplier;
			else
				xCheck = xCheck + depthMultiplier;

			airCheck = worldObj.getBlock(xCheck, yCheck, zCheck);
			if(output > maxSize)
				return -1;
		}
		return output;
	}
	/*
	 * Checks if the structure has been formed properly
	 */
	public boolean checkIfProperlyFormed()
	{
		int dir = (worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord));
		int depthMultiplier = ((dir == 2 || dir == 4) ? 1 : -1);
		boolean forwardZ = ((dir == 2) || (dir == 3));

		/*
		 *          FORWARD     BACKWARD
		 * North 2:   -z              +z
		 * South 3:   +z              -z
		 * East 5:    +x              -x
		 * West 4:    -x              +x
		 * 
		 * Should move BACKWARD for depth (facing = direction of block face, not direction of player looking at face)
		 */

		int hMin = getHorizontalMin();
		int hMax = getHorizontalMax();
		int vMin = getVerticalMin();
		int vMax = getVerticalMax();
		int depthVal = getDepthVal();

		if(hMin < 0 || hMax < 0 || vMin < 0 || vMax < 0 || depthVal < 0)
			return false;

		for(int horiz = -hMin; horiz <= hMax; horiz++)    // Horizontal (X or Z)
		{
			for(int vert = -vMin; vert <= vMax; vert++)   // Vertical (Y)
			{
				for(int depth = 0; depth <= (depthVal + 1); depth++) // Depth (Z or X)
				{
					int x = xCoord + (forwardZ ? horiz : (depth * depthMultiplier));
					int y = yCoord + vert;
					int z = zCoord + (forwardZ ? (depth * depthMultiplier) : horiz);

					Block blockId = worldObj.getBlock(x, y, z);

					if(depth == 0 && vert == 0 && horiz == 0)
						continue;
					if(vert == 0 && horiz == 0 && depth == 1)
					{
						if(blockId != Blocks.air)
							return false;
					}

					if(horiz > -hMin && horiz < hMax)
					{
						if(vert > -vMin && vert < vMax)
						{
							if(depth > 0 && depth < (depthVal + 1))
							{
								if(blockId == Blocks.air)
									continue;
								else
								{
									worldObj.setBlock(x, y, z, Blocks.cobblestone);
									return false;
								}
							}
						}
					}

					if(blockId == Blocks.air || (Reference.isBadBlock(blockId) && !Reference.isModularTile(blockId.getUnlocalizedName())))
						return false;

					if(!Reference.isValidBlock(blockId.getUnlocalizedName()))
					{
						if(Reference.isModularTile(blockId.getUnlocalizedName()))
							return true;

						return false;
					}
				}

			}

		}
		return true;
	}

	/*
	 * Converts the blocks into dummies
	 */
	public void convertDummies()
	{
		int dir = (worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord));
		this.direction = dir;
		int depthMultiplier = ((dir == 2 || dir == 4) ? 1 : -1);
		boolean forwardZ = ((dir == 2) || (dir == 3));


		/*
		 *          FORWARD     BACKWARD
		 * North:   -z              +z
		 * South:   +z              -z
		 * East:    +x              -x
		 * West:    -x              +x
		 * 
		 * Should move BACKWARD for depth (facing = direction of block face, not direction of player looking at face)
		 */

		hMin = getHorizontalMin();
		hMax = getHorizontalMax();
		vMin = getVerticalMin();
		vMax = getVerticalMax();
		depthVal = getDepthVal();

		for(int horiz = -hMin; horiz <= hMax; horiz++)    // Horizontal (X or Z)
		{
			for(int vert = -vMin; vert <= vMax; vert++)   // Vertical (Y)
			{
				for(int depth = 0; depth <= (depthVal + 1); depth++) // Depth (Z or X)
				{
					int x = xCoord + (forwardZ ? horiz : (depth * depthMultiplier));
					int y = yCoord + vert;
					int z = zCoord + (forwardZ ? (depth * depthMultiplier) : horiz);

					if(horiz == 0 && vert == 0 && depth == 0)
						continue;

					if(horiz > hMin && horiz < hMax)
					{
						if(vert > vMin && vert < vMax)
						{
							if(depth > 0 && depth < (depthVal + 1))
							{
								continue;
							}
						}

					}

					

					if(worldObj.getBlock(x, y, z) == BlockManager.furnaceCraftingUpgradeInactive)
					{
						crafterEnabled = true;
						worldObj.setBlock(x, y, z, BlockManager.furnaceCraftingUpgradeActive);
						worldObj.markBlockForUpdate(x, y, z);
						TileEntityFurnaceDummy dummyTE = (TileEntityFurnaceDummy)worldObj.getTileEntity(x, y, z);
						dummyTE.setCore(this);
					}

					else if(worldObj.getBlock(x, y, z) == BlockManager.furnaceAddition)
					{
						smeltingMultiplier++;
						worldObj.setBlock(x, y, z, BlockManager.furnaceAdditionActive);
						worldObj.markBlockForUpdate(x, y, z);
						TileEntityFurnaceDummy dummyTE = (TileEntityFurnaceDummy)worldObj.getTileEntity(x, y, z);
						dummyTE.setCore(this);
					}

					else if(worldObj.getBlock(x, y, z) == BlockManager.furnaceDummyIO)
					{
						worldObj.setBlock(x, y, z, BlockManager.furnaceDummyActiveIO);
						worldObj.markBlockForUpdate(x, y, z);
						TileEntityFurnaceDummy dummyTE = (TileEntityFurnaceDummy)worldObj.getTileEntity(x, y, z);
						dummyTE.setCore(this);
					}
					else if(!Reference.isModularTile(worldObj.getBlock(x, y, z).getUnlocalizedName()) && worldObj.getBlock(x, y, z) != null && worldObj.getBlock(x, y, z) != Blocks.air)
					{

						Block icon = worldObj.getBlock(x, y, z);
						int metadata = worldObj.getBlockMetadata(x, y, z);

						speedMultiplier += Reference.getSpeedMultiplierForBlock(icon);
						if(speedMultiplier <= 0)
							speedMultiplier = 1;

						efficiencyMultiplier += Reference.getEfficiencyMultiplierForBlock(icon);

						worldObj.setBlock(x, y, z, BlockManager.furnaceDummy);

						worldObj.markBlockForUpdate(x, y, z);
						TileEntityFurnaceDummy dummyTE = (TileEntityFurnaceDummy)worldObj.getTileEntity(x, y, z);

						if(icon == BlockManager.furnaceDummy)
						{
							icon = Block.getBlockById(dummyTE.icon);
							metadata = dummyTE.metadata;
						}
						else
						{
							dummyTE.icon = Block.getIdFromBlock(icon);
							dummyTE.metadata = metadata;
						}
						worldObj.markBlockForUpdate(x, y, z);
						dummyTE.setCore(this);
					}
				}
			}
			isValidMultiblock = true;
		}
	}

	/**
	 * Turns the dummies back into blocks
	 */
	private void revertDummies()
	{
		int dir = (worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord));
		if(dir == 0)
			dir = direction;
		int depthMultiplier = ((dir == 2 || dir == 4) ? 1 : -1);
		boolean forwardZ = ((dir == 2) || (dir == 3));

		/*
		 *          FORWARD     BACKWARD
		 * North:   -z              +z
		 * South:   +z              -z
		 * East:    +x              -x
		 * West:    -x              +x
		 * 
		 * Should move BACKWARD for depth (facing = direction of block face, not direction of player looking at face)
		 */


		for(int horiz = -hMin; horiz <= hMax; horiz++)    // Horizontal (X or Z)
		{
			for(int vert = -vMin; vert <= vMax; vert++)   // Vertical (Y)
			{
				for(int depth = 0; depth <= (depthVal + 1); depth++) // Depth (Z or X)
				{
					int x = xCoord + (forwardZ ? horiz : (depth * depthMultiplier));
					int y = yCoord + vert;
					int z = zCoord + (forwardZ ? (depth * depthMultiplier) : horiz);

					Block blockId = worldObj.getBlock(x, y, z);

					if(horiz == 0 && vert == 0 && depth == 0)
						continue;

					if(horiz > hMin && horiz < hMax)
					{
						if(vert > vMin && vert < vMax)
						{
							if(depth > 0 && depth < (depthVal + 1))
							{
								continue;
							}
						}

					}

					if(blockId == BlockManager.furnaceDummy)
					{
						TileEntityFurnaceDummy dummyTE = (TileEntityFurnaceDummy)worldObj.getTileEntity(x, y, z);

						worldObj.setBlock(x, y, z, dummyTE.getBlock());
						worldObj.setBlockMetadataWithNotify(x, y, z, dummyTE.metadata, 2);

						worldObj.markBlockForUpdate(x, y, z);
						continue;
					}

					if(blockId == BlockManager.furnaceCraftingUpgradeActive)
					{
						worldObj.setBlock(x, y, z, BlockManager.furnaceCraftingUpgradeInactive);
						worldObj.markBlockForUpdate(x, y, z);
						continue;
					}

					if(blockId == BlockManager.furnaceAdditionActive)
					{
						worldObj.setBlock(x, y, z, BlockManager.furnaceAddition);
						worldObj.markBlockForUpdate(x, y, z);
						continue;
					}

					if(blockId == BlockManager.furnaceDummyActiveIO)
					{
						worldObj.setBlock(x, y, z, BlockManager.furnaceDummyIO);
						worldObj.markBlockForUpdate(x, y, z);
						continue;
					}

					worldObj.markBlockForUpdate(x, y, z);
				}
			}

		}

		isValidMultiblock = false;
	}

	/**
	 * Sees if there is a crafting upgrade
	 * @return True if upgrade is present
	 */
	public boolean checkIfCrafting()
	{
		int dir = (worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord));
		int depthMultiplier = ((dir == 2 || dir == 4) ? 1 : -1);
		boolean forwardZ = ((dir == 2) || (dir == 3));

		/*
		 *          FORWARD     BACKWARD
		 * North:   -z              +z
		 * South:   +z              -z
		 * East:    +x              -x
		 * West:    -x              +x
		 * 
		 * Should move BACKWARD for depth (facing = direction of block face, not direction of player looking at face)
		 */

		for(int horiz = -hMin; horiz <= hMax; horiz++)    // Horizontal (X or Z)
		{
			for(int vert = -vMin; vert <= vMax; vert++)   // Vertical (Y)
			{
				for(int depth = 0; depth <= (depthVal + 1); depth++) // Depth (Z or X)
				{
					int x = xCoord + (forwardZ ? horiz : (depth * depthMultiplier));
					int y = yCoord + vert;
					int z = zCoord + (forwardZ ? (depth * depthMultiplier) : horiz);

					Block blockId = worldObj.getBlock(x, y, z);

					if(horiz == 0 && vert == 0 && depth == 0)
						continue;

					if(blockId == BlockManager.furnaceCraftingUpgradeActive || blockId == BlockManager.furnaceCraftingUpgradeInactive)
					{
						return true;
					}

				}
			}
		}
		return false;
	}

	/**
	 * Gets the speed multiplier
	 * @return Speed multiplier value
	 */
	public double getSpeed()
	{
		double output = 8;
		int dir = (worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord));
		int depthMultiplier = ((dir == 2 || dir == 4) ? 1 : -1);
		boolean forwardZ = ((dir == 2) || (dir == 3));

		/*
		 *          FORWARD     BACKWARD
		 * North:   -z              +z
		 * South:   +z              -z
		 * East:    +x              -x
		 * West:    -x              +x
		 * 
		 * Should move BACKWARD for depth (facing = direction of block face, not direction of player looking at face)
		 */

		for(int horiz = -hMin; horiz <= hMax; horiz++)    // Horizontal (X or Z)
		{
			for(int vert = -vMin; vert <= vMax; vert++)   // Vertical (Y)
			{
				for(int depth = 0; depth <= (depthVal + 1); depth++) // Depth (Z or X)
				{
					int x = xCoord + (forwardZ ? horiz : (depth * depthMultiplier));
					int y = yCoord + vert;
					int z = zCoord + (forwardZ ? (depth * depthMultiplier) : horiz);

					Block blockId = worldObj.getBlock(x, y, z);

					if(horiz == 0 && vert == 0 && depth == 0)
						continue;

					if(horiz > hMin && horiz < hMax)
					{
						if(vert > vMin && vert < vMax)
						{
							if(depth > 0 && depth < (depthVal + 1))
							{
								continue;
							}
						}

					}
					if(blockId == BlockManager.furnaceDummy)
					{
						TileEntityFurnaceDummy dummy = (TileEntityFurnaceDummy)worldObj.getTileEntity(x, y, z);
						output += Reference.getSpeedMultiplierForBlock(dummy.getBlock());
					}

				}
			}
		}	
		return output;
	}

	//TODO Marker for Furnace and Tile Functions

	//Furnace stuff
	@Override
	public void updateEntity()
	{
		boolean flag = this.furnaceBurnTime > 0;
		boolean flag1 = false;

		if (this.furnaceBurnTime > 0)
		{
			--this.furnaceBurnTime;
		}

		if (!this.worldObj.isRemote)
		{
			if (this.furnaceBurnTime == 0 && this.canSmelt())
			{
				this.currentItemBurnTime = this.furnaceBurnTime = this.scaledBurnTime();

				if (this.furnaceBurnTime > 0)
				{
					flag1 = true;

					if (this.furnaceItems[1] != null)
					{
						--this.furnaceItems[1].stackSize;

						if (this.furnaceItems[1].stackSize == 0)
						{
							this.furnaceItems[1] = furnaceItems[1].getItem().getContainerItem(furnaceItems[1]);
						}
					}
				}
			}

			if (this.isBurning() && this.canSmelt())
			{
				++this.furnaceCookTime;

				if (this.furnaceCookTime >= this.getSpeedMultiplier())
				{
					this.furnaceCookTime = 0;
					this.smeltItem();
					flag1 = true;
				}
			}
			else
			{
				this.furnaceCookTime = 0;
			}

			if (flag != this.furnaceBurnTime > 0)
			{
				flag1 = true;
				BlockFurnaceCore.updateFurnaceBlockState(this.furnaceBurnTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
			}
		}

		if (flag1)
		{
			this.markDirty();
		}
	}

	@Override
	public int getSizeInventory()
	{
		return furnaceItems.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return furnaceItems[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int count)
	{       
		if(this.furnaceItems[slot] != null)
		{
			ItemStack itemStack;

			itemStack = furnaceItems[slot].splitStack(count);

			if(furnaceItems[slot].stackSize <= 0)
				furnaceItems[slot] = null;

			return itemStack;
		}

		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		if(furnaceItems[slot] != null)
		{
			ItemStack stack = furnaceItems[slot];
			furnaceItems[slot] = null;
			return stack;
		}

		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack)
	{
		furnaceItems[slot] = itemStack;

		if(itemStack != null && itemStack.stackSize > getInventoryStackLimit())
			itemStack.stackSize = getInventoryStackLimit();
	}

	public void setGuiDisplayName(String par1Str)
	{
		this.field_94130_e = par1Str;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityPlayer)
	{
		return true;
	}

	//Re-writen for the I/O block
	@Override
	public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack)
	{
		if(par1 == 2)
			return false;
		if(par1 == 1 && isItemFuel(par2ItemStack))
			return true;
		if(par1 == 0)
			return true;
		else
			return false;

	}

	public static boolean isItemFuel(ItemStack par0ItemStack)
	{
		return getItemBurnTime(par0ItemStack) > 0;
	}

	public static int getItemBurnTime(ItemStack p_145952_0_)
	{
		if (p_145952_0_ == null)
		{
			return 0;
		}
		else
		{
			Item item = p_145952_0_.getItem();

			if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.air)
			{
				Block block = Block.getBlockFromItem(item);

				if (block == Blocks.wooden_slab)
				{
					return 150;
				}

				if (block.getMaterial() == Material.wood)
				{
					return 300;
				}

				if (block == Blocks.coal_block)
				{
					return 16000;
				}
			}

			if (item instanceof ItemTool && ((ItemTool)item).getToolMaterialName().equals("WOOD")) return 200;
			if (item instanceof ItemSword && ((ItemSword)item).getToolMaterialName().equals("WOOD")) return 200;
			if (item instanceof ItemHoe && ((ItemHoe)item).getToolMaterialName().equals("WOOD")) return 200;
			if (item == Items.stick) return 100;
			if (item == Items.coal) return 1600;
			if (item == Items.lava_bucket) return 20000;
			if (item == Item.getItemFromBlock(Blocks.sapling)) return 100;
			if (item == Items.blaze_rod) return 2400;
			return GameRegistry.getFuelValue(p_145952_0_);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);

		tagCompound.getInteger("BlockMeta");
		isValidMultiblock = tagCompound.getBoolean("isValidMultiblock");


		NBTTagList itemsTag = tagCompound.getTagList("Items", 10);
		furnaceItems = new ItemStack[this.getSizeInventory()];

		for (int i = 0; i < itemsTag.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound1 = itemsTag.getCompoundTagAt(i);
			byte b0 = nbttagcompound1.getByte("Slot");

			if (b0 >= 0 && b0 < this.furnaceItems.length)
			{
				this.furnaceItems[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}


		this.furnaceBurnTime = tagCompound.getShort("BurnTime");
		this.furnaceCookTime = tagCompound.getShort("CookTime");
		this.currentItemBurnTime = this.scaledBurnTime();
		this.speedMultiplier = tagCompound.getDouble("Speed"); 
		this.efficiencyMultiplier = tagCompound.getDouble("Efficiency");
		this.crafterEnabled = tagCompound.getBoolean("Enabled");
		this.direction = tagCompound.getInteger("Direction");
		this.smeltingMultiplier = tagCompound.getInteger("SmeltingMultiplier");
		this.hMin = tagCompound.getInteger("hMin");
		this.hMax = tagCompound.getInteger("hMax");
		this.vMin = tagCompound.getInteger("vMin");
		this.vMax = tagCompound.getInteger("vMax");
		this.depthVal = tagCompound.getInteger("depthVal");


		if (tagCompound.hasKey("CustomName"))
		{
			this.field_94130_e = tagCompound.getString("CustomName");
		}

	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);

		tagCompound.setBoolean("isValidMultiblock", isValidMultiblock);

		tagCompound.setShort("BurnTime", (short)this.furnaceBurnTime);
		tagCompound.setShort("CookTime", (short)this.furnaceCookTime);
		tagCompound.setDouble("Speed", (double)this.speedMultiplier);
		tagCompound.setDouble("Efficiency", (double)this.efficiencyMultiplier);
		tagCompound.setBoolean("Enabled", (boolean)this.crafterEnabled);
		tagCompound.setInteger("Direction", this.direction);
		tagCompound.setInteger("SmeltingMultiplier", this.smeltingMultiplier);
		tagCompound.setInteger("hMin", hMin);
		tagCompound.setInteger("hMax", hMax);
		tagCompound.setInteger("vMin", vMin);
		tagCompound.setInteger("vMax", vMax);
		tagCompound.setInteger("depthVal", depthVal);


		NBTTagList itemsList = new NBTTagList();

		for (int i = 0; i < this.furnaceItems.length; ++i)
		{
			if (this.furnaceItems[i] != null)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte)i);
				this.furnaceItems[i].writeToNBT(nbttagcompound1);
				itemsList.appendTag(nbttagcompound1);
			}
		}
		tagCompound.setTag("Items", itemsList);

		if (this.hasCustomInventoryName())
		{
			tagCompound.setString("CustomName", this.field_94130_e);
		}

	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		this.writeToNBT(nbtTag);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.func_148857_g());
	}
	public boolean isBurning()
	{
		return furnaceBurnTime > 0;
	}

	private boolean canSmelt()
	{
		if(furnaceItems[0] == null)
			return false;
		else
		{
			ItemStack itemStack = FurnaceRecipes.smelting().getSmeltingResult(furnaceItems[0]);
			if(itemStack == null)
				return false;
			if(furnaceItems[2] == null)
				return true;
			if(!furnaceItems[2].isItemEqual(itemStack))
				return false;

			int resultingStackSize = furnaceItems[2].stackSize + itemStack.stackSize;
			return (resultingStackSize <= getInventoryStackLimit() && resultingStackSize <= itemStack.getMaxStackSize());
		}
	}

	public void smeltItem()
	{
		if (this.canSmelt())
		{
			ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItems[0]);

			if (this.furnaceItems[2] == null)
			{
				for(int i = 0; i < smeltingMultiplier; i++)
				{
					if (this.furnaceItems[0].stackSize <= 0)
					{
						this.furnaceItems[0] = null;
						return;
					}

					if(furnaceItems[2] != null)
					{
						if(furnaceItems[2].stackSize + itemstack.stackSize > furnaceItems[2].getMaxStackSize())
							break;
					}

					if(furnaceItems[2] != null)
						furnaceItems[2].stackSize += itemstack.stackSize;
					else
						this.furnaceItems[2] = itemstack.copy();

					--this.furnaceItems[0].stackSize;

					if (this.furnaceItems[0].stackSize <= 0)
					{
						this.furnaceItems[0] = null;
						return;
					}
				}
				return;
			}

			else if (this.furnaceItems[2].isItemEqual(itemstack))
			{
				for(int i = 0; i < smeltingMultiplier; i++)
				{
					if (this.furnaceItems[0].stackSize <= 0)
					{
						this.furnaceItems[0] = null;
						break;
					}

					if(furnaceItems[2].stackSize + itemstack.stackSize > furnaceItems[2].getMaxStackSize())
						break;

					furnaceItems[2].stackSize += itemstack.stackSize;
					--this.furnaceItems[0].stackSize;

					if (this.furnaceItems[0].stackSize <= 0)
					{
						this.furnaceItems[0] = null;
						break;
					}
				}
			}

		}
	}

	//Reworked for I/O
	@Override
	public int[] getAccessibleSlotsFromSide(int par1)
	{
		switch(par1)
		{
		case 0 : return slots_bottom;
		case 1 : return slots_top;
		case 2 : return slots_output_only;
		default : return slots_sides;

		}
	}

	@Override
	public boolean canInsertItem(int par1, ItemStack par2ItemStack, int par3)
	{
		return this.isItemValidForSlot(par1, par2ItemStack);
	}
	@Override
	public boolean canExtractItem(int par1, ItemStack par2ItemStack, int par3)
	{
		return par3 != 0 || par1 != 1 || par2ItemStack.getItem() == Items.bucket;
	}

	@Override
	public String getInventoryName() {
		return null;
	}

	public boolean hasCustomInventoryName()
	{
		return this.field_94130_e != null && this.field_94130_e.length() > 0;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	public void func_145951_a(String p_145951_1_)
	{
		this.field_94130_e = p_145951_1_;
	}

	//TODO Modular Functions
	/**
	 * Gets speed multiplier
	 * @return Cook time
	 */
	public double getSpeedMultiplier()
	{
		double speed = getSpeed();
		speed = speed / 8;
		return this.cookSpeed / speed;
	}

	@SideOnly(Side.CLIENT)
	public int getCookProgressScaled(int scaleVal)
	{ 
		double scale = this.getSpeedMultiplier();
		return (int) (this.furnaceCookTime * scaleVal / scale);
	}

	@SideOnly(Side.CLIENT)
	public int getBurnTimeRemainingScaled(int scaleVal)
	{
		if(currentItemBurnTime == 0)
		{
			currentItemBurnTime = (int) getSpeedMultiplier();
		} 
		return furnaceBurnTime * scaleVal / this.currentItemBurnTime;
	}

	/**
	 * Used to determine how long an item will burn
	 * @return How many ticks an item will burn
	 */
	public int scaledBurnTime()
	{
		if(efficiencyMultiplier > 0)
			return (int) (TileEntityFurnace.getItemBurnTime(furnaceItems[1]) * efficiencyMultiplier);
		else
			return (int) (TileEntityFurnace.getItemBurnTime(furnaceItems[1]) * (1 / (-1 * efficiencyMultiplier)));
	}

	/**
	 * Gets efficiency for display
	 * @return How efficienct in comparison to vanilla furnace
	 */
	public double getScaledEfficiency()
	{
		if(efficiencyMultiplier > 0)
			return ((1600 * efficiencyMultiplier) / getSpeedMultiplier()) / 8;
		else
			return ((1600 * (1 / (-1 * efficiencyMultiplier))) / getSpeedMultiplier()) / 8;
	}
}