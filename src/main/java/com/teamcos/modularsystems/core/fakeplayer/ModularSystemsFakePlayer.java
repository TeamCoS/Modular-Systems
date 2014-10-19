package com.teamcos.modularsystems.core.fakeplayer;

import com.google.common.base.Preconditions;
import com.mojang.authlib.GameProfile;
import com.teamcos.modularsystems.core.helper.LogHelper;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

import java.util.UUID;

public class ModularSystemsFakePlayer extends FakePlayer {

	ModularSystemsFakePlayer(WorldServer world, int id) {
		super(world, createProfile(String.format("ModularSystemsFakePlayer", id)));
	}

	private static GameProfile createProfile(String name) {
		UUID uuid = UUID.nameUUIDFromBytes(name.getBytes());
		return new GameProfile(uuid, name);
	}

	@Override
	public void setDead() {
		inventory.clearInventory(null, -1);
		isDead = true;
	}

	public ItemStack equipWithAndRightClick(ItemStack itemStack, Vec3 currentPos, Vec3 hitVector, ForgeDirection side, boolean blockExists) {
		setPosition(currentPos.xCoord, currentPos.yCoord, currentPos.zCoord);

		if (blockExists) {

			// find rotations
			float deltaX = (float)(currentPos.xCoord - hitVector.xCoord);
			float deltaY = (float)(currentPos.yCoord - hitVector.yCoord);
			float deltaZ = (float)(currentPos.zCoord - hitVector.zCoord);
			setSneaking(false);
			if (rightClick(
					inventory.getCurrentItem(),
					(int)currentPos.xCoord,
					(int)currentPos.yCoord,
					(int)currentPos.zCoord,
					side,
					deltaX, deltaY, deltaZ,
					blockExists)) {
				setSneaking(true);

				return (inventory.getCurrentItem() == null || inventory.getCurrentItem().stackSize <= 0)? null : inventory.getCurrentItem().copy();
			}
			hitVector.yCoord++;
			setSneaking(true);
		}

		// find rotations
		float deltaX = (float)(currentPos.xCoord - hitVector.xCoord);
		float deltaY = (float)(currentPos.yCoord - hitVector.yCoord);
		float deltaZ = (float)(currentPos.zCoord - hitVector.zCoord);
		float distanceInGroundPlain = (float)Math.sqrt((float)deltaX * deltaX + deltaY * deltaY);

		float pitch = (float)(Math.atan2(deltaZ, deltaX) * 180 / Math.PI);
		float hue = (float)(Math.atan2(deltaY, distanceInGroundPlain) * 180 / Math.PI);

		setRotation(pitch, hue);

		inventory.clearInventory(null, -1);
		inventory.addItemStackToInventory(itemStack);
		rightClick(
				inventory.getCurrentItem(),
				(int)currentPos.xCoord,
				(int)currentPos.yCoord,
				(int)currentPos.zCoord,
				side,
				deltaX, deltaY, deltaZ,
				blockExists);

		return (inventory.getCurrentItem() == null || inventory.getCurrentItem().stackSize <= 0)? null : inventory.getCurrentItem().copy();
	}

	public void dropItemAt(ItemStack itemStack, int x, int y, int z, ForgeDirection direction) {
		setPosition(x + 0.5F, y - 1.5, z + 0.5F);
		Preconditions.checkArgument(direction == ForgeDirection.DOWN, "Other directions than down is not implemented");
		setRotation(0, 90);

		EntityItem entityItem = dropPlayerItemWithRandomChoice(itemStack, false);
		entityItem.motionX = 0;
		entityItem.motionY = 0;
		entityItem.motionZ = 0;
	}

	private boolean rightClick(ItemStack itemStack, int x, int y, int z, ForgeDirection side, float deltaX, float deltaY, float deltaZ, boolean blockExists) {
		if (itemStack == null) return false;

		final int opposite = side.getOpposite().ordinal();
		PlayerInteractEvent event = ForgeEventFactory.onPlayerInteract(this, Action.RIGHT_CLICK_BLOCK, x, y, z, opposite, worldObj);

		if (event.isCanceled()) { return false; }

		final Item usedItem = itemStack.getItem();

		if (usedItem.onItemUseFirst(itemStack, this, worldObj, x, y, z, opposite, deltaX, deltaY, deltaZ)) { return true; }

		if (event.useBlock != Event.Result.DENY && (isSneaking() || usedItem.doesSneakBypassUse(worldObj, x, y, z, this))) {
			Block block = worldObj.getBlock(x, y, z);
			if (block != null) try {
				if (block.onBlockActivated(worldObj, x, y, z, this, opposite, deltaX, deltaY, deltaZ)) return true;
			} catch (Throwable t) {
				LogHelper.warn(StatCollector.translateToLocal("Invalid use of fake player on block, aborting. Don't do it again"));
			}
		}

		if (event.useItem == Event.Result.DENY || usedItem instanceof ItemBlock && blockExists) return false;
		try {
			return itemStack.tryPlaceItemIntoWorld(this, worldObj, x, y, z, opposite, deltaX, deltaY, deltaZ);
		} catch (Throwable t) {
			LogHelper.warn(StatCollector.translateToLocal("Invalid use of fake player with item, aborting. Don't do it again"));
			return false;
		}
	}
}
