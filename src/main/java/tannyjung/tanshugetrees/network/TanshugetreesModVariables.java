package tannyjung.tanshugetrees.network;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import tannyjung.tanshugetrees.TanshugetreesMod;

public class TanshugetreesModVariables {

	@EventBusSubscriber(modid = TanshugetreesMod.MODID)
	public static class EventBusVariableHandlers {
		@SubscribeEvent
		public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
			if (event.getEntity() instanceof ServerPlayer player) {
				HolderLookup.Provider registries = player.registryAccess();
				SavedData mapdata = MapVariables.get(player.level());
				SavedData worlddata = WorldVariables.get(player.level());
				if (mapdata != null) {
					PacketDistributor.sendToPlayer(player, new SavedDataSyncPayload(0, mapdata.save(new CompoundTag(), registries)));
				}
				if (worlddata != null) {
					PacketDistributor.sendToPlayer(player, new SavedDataSyncPayload(1, worlddata.save(new CompoundTag(), registries)));
				}
			}
		}

		@SubscribeEvent
		public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
			if (event.getEntity() instanceof ServerPlayer player) {
				HolderLookup.Provider registries = player.registryAccess();
				SavedData worlddata = WorldVariables.get(player.level());
				if (worlddata != null) {
					PacketDistributor.sendToPlayer(player, new SavedDataSyncPayload(1, worlddata.save(new CompoundTag(), registries)));
				}
			}
		}

		@SubscribeEvent
		public static void onWorldTick(LevelTickEvent.Post event) {
			if (event.getLevel() instanceof ServerLevel level) {
				HolderLookup.Provider registries = level.registryAccess();
				WorldVariables worldVariables = WorldVariables.get(level);
				if (worldVariables._syncDirty) {
					PacketDistributor.sendToPlayersInDimension(level, new SavedDataSyncPayload(1, worldVariables.save(new CompoundTag(), registries)));
					worldVariables._syncDirty = false;
				}
				MapVariables mapVariables = MapVariables.get(level);
				if (mapVariables._syncDirty) {
					PacketDistributor.sendToAllPlayers(new SavedDataSyncPayload(0, mapVariables.save(new CompoundTag(), registries)));
					mapVariables._syncDirty = false;
				}
			}
		}
	}

	public static class WorldVariables extends SavedData {
		public static final String DATA_NAME = "tanshugetrees_worldvars";
		public static final SavedData.Factory<WorldVariables> FACTORY = new SavedData.Factory<>(WorldVariables::new, WorldVariables::load, DataFixTypes.LEVEL);
		boolean _syncDirty = false;

		public static WorldVariables load(CompoundTag tag, HolderLookup.Provider provider) {
			WorldVariables data = new WorldVariables();
			data.read(tag);
			return data;
		}

		public void read(CompoundTag nbt) {
		}

		@Override
		public CompoundTag save(CompoundTag nbt, HolderLookup.Provider provider) {
			return nbt;
		}

		public void markSyncDirty() {
			this.setDirty();
			this._syncDirty = true;
		}

		static WorldVariables clientSide = new WorldVariables();

		public static WorldVariables get(LevelAccessor world) {
			if (world instanceof ServerLevel level) {
				return level.getDataStorage().computeIfAbsent(FACTORY, DATA_NAME);
			} else {
				return clientSide;
			}
		}
	}

	public static class MapVariables extends SavedData {
		public static final String DATA_NAME = "tanshugetrees_mapvars";
		public static final SavedData.Factory<MapVariables> FACTORY = new SavedData.Factory<>(MapVariables::new, MapVariables::load, DataFixTypes.SAVED_DATA_MAP_DATA);
		boolean _syncDirty = false;
		public boolean shape_file_converter = false;
		public double shape_file_converter_count = 0;
		public String season = "Summer";

		public static MapVariables load(CompoundTag tag, HolderLookup.Provider provider) {
			MapVariables data = new MapVariables();
			data.read(tag);
			return data;
		}

		public void read(CompoundTag nbt) {
			shape_file_converter = nbt.getBoolean("shape_file_converter");
			shape_file_converter_count = nbt.getDouble("shape_file_converter_count");
			season = nbt.getString("season");
		}

		@Override
		public CompoundTag save(CompoundTag nbt, HolderLookup.Provider provider) {
			nbt.putBoolean("shape_file_converter", shape_file_converter);
			nbt.putDouble("shape_file_converter_count", shape_file_converter_count);
			nbt.putString("season", season);
			return nbt;
		}

		public void markSyncDirty() {
			this.setDirty();
			_syncDirty = true;
		}

		static MapVariables clientSide = new MapVariables();

		public static MapVariables get(LevelAccessor world) {
			if (world instanceof ServerLevelAccessor serverLevelAcc) {
				return serverLevelAcc.getLevel().getServer().getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(FACTORY, DATA_NAME);
			} else {
				return clientSide;
			}
		}
	}
}
