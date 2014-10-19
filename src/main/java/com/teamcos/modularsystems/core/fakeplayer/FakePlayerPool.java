package com.teamcos.modularsystems.core.fakeplayer;

import com.teamcos.modularsystems.core.helper.ConfigHelper;
import com.teamcos.modularsystems.core.helper.LogHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.WorldEvent;

import java.util.Map;
import java.util.Queue;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class FakePlayerPool {

	public interface PlayerUser {
		public void usePlayer(ModularSystemsFakePlayer fakePlayer);
	}

	public interface PlayerUserReturning<T> {
		public T usePlayer(ModularSystemsFakePlayer fakePlayer);
	}

	private static PlayerUserReturning<Void> wrap(final PlayerUser user) {
		return new PlayerUserReturning<Void>() {
			@Override
			public Void usePlayer(ModularSystemsFakePlayer fakePlayer) {
				user.usePlayer(fakePlayer);
				return null;
			}
		};
	}

	private static class WorldPool {
		private final Queue<ModularSystemsFakePlayer> pool = new ConcurrentLinkedQueue<ModularSystemsFakePlayer>();
		private final AtomicInteger playerCount = new AtomicInteger();

		public <T> T executeOnPlayer(WorldServer world, PlayerUserReturning<T> user) {
			ModularSystemsFakePlayer player = pool.poll();

			if (player == null) {
				int id = playerCount.incrementAndGet();
				if (id > ConfigHelper.fakePlayerMax) LogHelper.warn(StatCollector.translateToLocal("Number of fake players in use has reached max. Something may leak them"));
				player = new ModularSystemsFakePlayer(world, id);
			}

			player.isDead = false;
			T result = user.usePlayer(player);
			player.setDead();
			pool.add(player);
			return result;
		}
	}

	private FakePlayerPool() {}

	public static final FakePlayerPool instance = new FakePlayerPool();

	private static final Map<World, WorldPool> worldPools = new WeakHashMap<World, WorldPool>();

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load evt) {
		worldPools.put(evt.world, new WorldPool());
	}

	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload evt) {
		worldPools.remove(evt.world);
	}

	public void executeOnPlayer(WorldServer world, PlayerUser user) {
		executeOnPlayer(world, wrap(user));
	}

	public <T> T executeOnPlayer(WorldServer world, PlayerUserReturning<T> user) {
		WorldPool pool = worldPools.get(world);
		if (pool != null) return pool.executeOnPlayer(world, user);
		else LogHelper.warn(StatCollector.translateToLocal("Trying to execute on world, but it's not loaded"));
		return null;
	}
}
