package com.pauljoda.modularsystems.storage.tiles;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;

import com.pauljoda.modularsystems.core.lib.Reference;
import com.pauljoda.modularsystems.fakeplayer.FakePlayerPool;
import com.pauljoda.modularsystems.fakeplayer.FakePlayerPool.PlayerUser;
import com.pauljoda.modularsystems.fakeplayer.ModularSystemsFakePlayer;

public class TileEntityStorageExpansion extends TileEntity implements IInventory, IEntitySelector {

	public int coreX;
	public int coreY;
	public int coreZ;

	public int nextX;
	public int nextY;
	public int nextZ;

	public int tileType;


	public TileEntityStorageExpansion()
	{	}

	public TileEntityStorageCore getCore()
	{
		return (TileEntityStorageCore) worldObj.getTileEntity(coreX, coreY, coreZ);
	}

	public void setCore(TileEntityStorageCore core)
	{
		coreX = core.xCoord;
		coreY = core.yCoord;
		coreZ = core.zCoord;
	}

	public void setNext(TileEntityStorageExpansion next)
	{
		nextX = next.xCoord;
		nextY = next.yCoord;
		nextZ = next.zCoord;
	}

	public void invalidateCore()
	{
		this.coreY = -100;
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 2);
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	public void invalidateExpansion()
	{
		if(getCore() != null)
		{
			TileEntityStorageCore core = getCore();
			core.dropItems(xCoord, yCoord, zCoord);
			getCore().setInventoryRows(core.inventoryRows - 1);
			worldObj.markBlockForUpdate(core.xCoord, core.yCoord, core.zCoord);
		}

		if(getNext() != null)
		{
			TileEntityStorageExpansion expansion = getNext();
			expansion.invalidateExpansion();
			expansion.invalidateCore();
		}
	}

	public TileEntityStorageExpansion getNext()
	{
		return (TileEntityStorageExpansion)worldObj.getTileEntity(nextX, nextY, nextZ);
	}

	public void setTileType(int type)
	{
		this.tileType = type;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);

		coreX = tagCompound.getInteger("coreX");
		coreY = tagCompound.getInteger("coreY");
		coreZ = tagCompound.getInteger("coreZ");

		nextX = tagCompound.getInteger("nextX");
		nextY = tagCompound.getInteger("nextY");
		nextZ = tagCompound.getInteger("nextZ");

		tileType = tagCompound.getInteger("Type");
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);

		tagCompound.setInteger("coreX", coreX);
		tagCompound.setInteger("coreY", coreY);
		tagCompound.setInteger("coreZ", coreZ);

		tagCompound.setInteger("nextX", nextX);
		tagCompound.setInteger("nextY", nextY);
		tagCompound.setInteger("nextZ", nextZ);

		tagCompound.setInteger("Type", tileType);
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


	@Override
	public void updateEntity()
	{
		switch(tileType)
		{
		case Reference.SMASHING_STORAGE_EXPANSION :
			hooverItems(1.0D, 0.1D);
			break;
		case Reference.HOPPING_STORAGE_EXPANSION :
			hooverItems(3.0D, 0.05D);
			break;
		}
	}

	public void hooverItems(double range, double speed)
	{
		if(getCore() != null)
		{
			AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(xCoord - range, yCoord - range, zCoord - range, xCoord + range, yCoord + range, zCoord + range);
			@SuppressWarnings("unchecked")
			List<Entity> interestingItems = worldObj.getEntitiesWithinAABB(EntityItem.class, bb);

			for (Entity entity : interestingItems) {
				double x = (xCoord + 0.5D - entity.posX);
				double y = (yCoord + 0.5D - entity.posY);
				double z = (zCoord + 0.5D - entity.posZ);

				double distance = Math.sqrt(x * x + y * y + z * z);
				if (distance < 1.1) {
					onEntityCollidedWithBlock(entity);
				} else {
					double var11 = 1.0 - distance / 15.0;

					if (var11 > 0.0D) {
						var11 *= var11;
						entity.motionX += x / distance * var11 * speed;
						entity.motionY += y / distance * var11 * speed * 4;
						entity.motionZ += z / distance * var11 * speed;
					}
				}

			}
		}
	}
	public void onEntityCollidedWithBlock(Entity entity) 
	{
		if (!worldObj.isRemote) 
		{
			if (entity instanceof EntityItem && !entity.isDead) 
			{
				EntityItem item = (EntityItem)entity;
				ItemStack stack = item.getEntityItem().copy();
				tryInsertSlot(stack);
				if (stack.stackSize == 0) {
					item.setDead();
				} else {
					item.setEntityItemStack(stack);
				}
			}
		}
	}

	public void tryInsertSlot(ItemStack stack)
	{
		if(getCore() != null)
		{
			for(int i = 0; i < getCore().inventoryRows * 11; i++)
			{
				if(getCore().isItemValidForSlot(i, stack))
				{
					ItemStack targetStack = getCore().getStackInSlot(i);
					if (targetStack == null) {
						getCore().setInventorySlotContents(i, stack.copy());
						stack.stackSize = 0;
						markDirty();
						return;
					}
					else if (getCore().isItemValidForSlot(i, stack) &&
							areMergeCandidates(stack, targetStack)) {
						int space = targetStack.getMaxStackSize()
								- targetStack.stackSize;
						int mergeAmount = Math.min(space, stack.stackSize);
						ItemStack copy = targetStack.copy();
						copy.stackSize += mergeAmount;
						getCore().setInventorySlotContents(i, copy);
						stack.stackSize -= mergeAmount;
						markDirty();
						return;
					}
				}

			}
		}
	}

	public static boolean areItemAndTagEqual(final ItemStack stackA, ItemStack stackB) {
		return stackA.isItemEqual(stackB) && ItemStack.areItemStackTagsEqual(stackA, stackB);
	}

	public static boolean areMergeCandidates(ItemStack source, ItemStack target) {
		return areItemAndTagEqual(source, target) && target.stackSize < target.getMaxStackSize();
	}

	public void tryBreakBlock()
	{
		int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		meta -= 2;
		if(getCore() != null)
		{
			int x = xCoord;
			int y = yCoord;
			int z = zCoord;
			switch(meta)
			{
			case 0 :
				y--;
				break;
			case 1 :
				y++;
				break;
			case 2 :
				z--;
				break;
			case 3 :
				z++;
				break;
			case 4 :
				x--;
				break;
			case 5 :
				x++;
				break;
			}
			breakBlock(x,y,z);
		}
	}
	
	private void breakBlock(int x, int y, int z) {
		if (worldObj.isRemote) return;

		if (worldObj.blockExists(x, y, z)) {
			final Block block = worldObj.getBlock(x, y, z);
			if (block != null) {
				final int metadata = worldObj.getBlockMetadata(x, y, z);
				if (block != Blocks.bedrock && block.getBlockHardness(worldObj, z, y, z) > -1.0F) {
					breakBlock(x, y, z, block, metadata);
				}
			}
		}
	}

	private void breakBlock(final int x, final int y, final int z, final Block block, final int metadata) {
		if (!(worldObj instanceof WorldServer)) return;
		FakePlayerPool.instance.executeOnPlayer((WorldServer)worldObj, new PlayerUser() {
			@Override
			public void usePlayer(ModularSystemsFakePlayer fakePlayer) {
				fakePlayer.inventory.currentItem = 0;
				fakePlayer.inventory.setInventorySlotContents(0, new ItemStack(Items.diamond_pickaxe, 0, 0));

				BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(x, y, z, worldObj, block, blockMetadata, fakePlayer);
				if (MinecraftForge.EVENT_BUS.post(event)) return;

				if (ForgeHooks.canHarvestBlock(block, fakePlayer, metadata)) {
					worldObj.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + (metadata << 12));
					worldObj.setBlockToAir(x, y, z);

					List<ItemStack> items = block.getDrops(worldObj, x, y, z, metadata, 0);
					if (items != null) {
						for(ItemStack item : items)
						{
							EntityItem stack = new EntityItem(worldObj, x + 0.5D, y + 0.5D, z + 0.5D, new ItemStack(item.getItem(), item.stackSize, item.getItemDamage()));
							worldObj.spawnEntityInWorld(stack);
						}
						
					}
				}
			}
		});
	}
	
	@Override
	public int getSizeInventory() {
		if(getCore() != null)
			return getCore().getSizeInventory();
		else 
			return 0;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		if(getCore() != null)
			return getCore().getStackInSlot(i);
		else
			return null;
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if(getCore() != null)
			return getCore().decrStackSize(i, j);
		else
			return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		if(getCore() != null)
			return getCore().getStackInSlotOnClosing(i);
		else 
			return null;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		if(getCore() != null)
			getCore().setInventorySlotContents(i, itemstack);
	}

	@Override
	public int getInventoryStackLimit() {
		if(getCore() != null)
			return getCore().getInventoryStackLimit();
		else 
			return 0;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) != this ? false : entityplayer.getDistanceSq((double)xCoord + 0.5, (double)yCoord + 0.5, (double)zCoord + 0.5) <= 64.0;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		if(getCore() != null)
			return getCore().isItemValidForSlot(i, itemstack);
		else
			return false;
	}

	@Override
	public String getInventoryName() {
		if(getCore() != null)
			return getCore().getInventoryName();
		else
			return null;
	}

	@Override
	public boolean hasCustomInventoryName() {
		if(getCore() != null)
			return getCore().hasCustomInventoryName();
		else
			return false;
	}

	@Override
	public void openInventory() {		
	}

	@Override
	public void closeInventory() {		
	}

	@Override
	public boolean isEntityApplicable(Entity p_82704_1_) {
		// TODO Auto-generated method stub
		return false;
	}
}
