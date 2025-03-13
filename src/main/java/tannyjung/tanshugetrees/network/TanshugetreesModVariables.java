package tannyjung.tanshugetrees.network;

import tannyjung.tanshugetrees.TanshugetreesMod;

import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.nbt.CompoundTag;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class TanshugetreesModVariables {
	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		TanshugetreesMod.addNetworkMessage(SavedDataSyncMessage.class, SavedDataSyncMessage::buffer, SavedDataSyncMessage::new, SavedDataSyncMessage::handler);
	}

	@Mod.EventBusSubscriber
	public static class EventBusVariableHandlers {
		@SubscribeEvent
		public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
			if (!event.getEntity().level().isClientSide()) {
				SavedData mapdata = MapVariables.get(event.getEntity().level());
				SavedData worlddata = WorldVariables.get(event.getEntity().level());
				if (mapdata != null)
					TanshugetreesMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()), new SavedDataSyncMessage(0, mapdata));
				if (worlddata != null)
					TanshugetreesMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()), new SavedDataSyncMessage(1, worlddata));
			}
		}

		@SubscribeEvent
		public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
			if (!event.getEntity().level().isClientSide()) {
				SavedData worlddata = WorldVariables.get(event.getEntity().level());
				if (worlddata != null)
					TanshugetreesMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()), new SavedDataSyncMessage(1, worlddata));
			}
		}
	}

	public static class WorldVariables extends SavedData {
		public static final String DATA_NAME = "tanshugetrees_worldvars";

		public static WorldVariables load(CompoundTag tag) {
			WorldVariables data = new WorldVariables();
			data.read(tag);
			return data;
		}

		public void read(CompoundTag nbt) {
		}

		@Override
		public CompoundTag save(CompoundTag nbt) {
			return nbt;
		}

		public void syncData(LevelAccessor world) {
			this.setDirty();
			if (world instanceof Level level && !level.isClientSide())
				TanshugetreesMod.PACKET_HANDLER.send(PacketDistributor.DIMENSION.with(level::dimension), new SavedDataSyncMessage(1, this));
		}

		static WorldVariables clientSide = new WorldVariables();

		public static WorldVariables get(LevelAccessor world) {
			if (world instanceof ServerLevel level) {
				return level.getDataStorage().computeIfAbsent(e -> WorldVariables.load(e), WorldVariables::new, DATA_NAME);
			} else {
				return clientSide;
			}
		}
	}

	public static class MapVariables extends SavedData {
		public static final String DATA_NAME = "tanshugetrees_mapvars";
		public boolean auto_gen = false;
		public boolean auto_gen_chat_messages = false;
		public double auto_gen_cooldown = 0;
		public double auto_gen_count = 0;
		public String auto_gen_teleport_player_back = "";
		public boolean detect_exist = false;
		public double generation_distance_max = 0;
		public double loop_second = 0;
		public String mod_version = "";
		public double rt_dynamic_tick = 0;
		public String season = "";
		public double season_detector_tick = 0;
		public String tanny_pack_version = "";
		public boolean version_1192 = false;

		public static MapVariables load(CompoundTag tag) {
			MapVariables data = new MapVariables();
			data.read(tag);
			return data;
		}

		public void read(CompoundTag nbt) {
			auto_gen = nbt.getBoolean("auto_gen");
			auto_gen_chat_messages = nbt.getBoolean("auto_gen_chat_messages");
			auto_gen_cooldown = nbt.getDouble("auto_gen_cooldown");
			auto_gen_count = nbt.getDouble("auto_gen_count");
			auto_gen_teleport_player_back = nbt.getString("auto_gen_teleport_player_back");
			detect_exist = nbt.getBoolean("detect_exist");
			generation_distance_max = nbt.getDouble("generation_distance_max");
			loop_second = nbt.getDouble("loop_second");
			mod_version = nbt.getString("mod_version");
			rt_dynamic_tick = nbt.getDouble("rt_dynamic_tick");
			season = nbt.getString("season");
			season_detector_tick = nbt.getDouble("season_detector_tick");
			tanny_pack_version = nbt.getString("tanny_pack_version");
			version_1192 = nbt.getBoolean("version_1192");
		}

		@Override
		public CompoundTag save(CompoundTag nbt) {
			nbt.putBoolean("auto_gen", auto_gen);
			nbt.putBoolean("auto_gen_chat_messages", auto_gen_chat_messages);
			nbt.putDouble("auto_gen_cooldown", auto_gen_cooldown);
			nbt.putDouble("auto_gen_count", auto_gen_count);
			nbt.putString("auto_gen_teleport_player_back", auto_gen_teleport_player_back);
			nbt.putBoolean("detect_exist", detect_exist);
			nbt.putDouble("generation_distance_max", generation_distance_max);
			nbt.putDouble("loop_second", loop_second);
			nbt.putString("mod_version", mod_version);
			nbt.putDouble("rt_dynamic_tick", rt_dynamic_tick);
			nbt.putString("season", season);
			nbt.putDouble("season_detector_tick", season_detector_tick);
			nbt.putString("tanny_pack_version", tanny_pack_version);
			nbt.putBoolean("version_1192", version_1192);
			return nbt;
		}

		public void syncData(LevelAccessor world) {
			this.setDirty();
			if (world instanceof Level && !world.isClientSide())
				TanshugetreesMod.PACKET_HANDLER.send(PacketDistributor.ALL.noArg(), new SavedDataSyncMessage(0, this));
		}

		static MapVariables clientSide = new MapVariables();

		public static MapVariables get(LevelAccessor world) {
			if (world instanceof ServerLevelAccessor serverLevelAcc) {
				return serverLevelAcc.getLevel().getServer().getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(e -> MapVariables.load(e), MapVariables::new, DATA_NAME);
			} else {
				return clientSide;
			}
		}
	}

	public static class SavedDataSyncMessage {
		private final int type;
		private SavedData data;

		public SavedDataSyncMessage(FriendlyByteBuf buffer) {
			this.type = buffer.readInt();
			CompoundTag nbt = buffer.readNbt();
			if (nbt != null) {
				this.data = this.type == 0 ? new MapVariables() : new WorldVariables();
				if (this.data instanceof MapVariables mapVariables)
					mapVariables.read(nbt);
				else if (this.data instanceof WorldVariables worldVariables)
					worldVariables.read(nbt);
			}
		}

		public SavedDataSyncMessage(int type, SavedData data) {
			this.type = type;
			this.data = data;
		}

		public static void buffer(SavedDataSyncMessage message, FriendlyByteBuf buffer) {
			buffer.writeInt(message.type);
			if (message.data != null)
				buffer.writeNbt(message.data.save(new CompoundTag()));
		}

		public static void handler(SavedDataSyncMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
			NetworkEvent.Context context = contextSupplier.get();
			context.enqueueWork(() -> {
				if (!context.getDirection().getReceptionSide().isServer() && message.data != null) {
					if (message.type == 0)
						MapVariables.clientSide = (MapVariables) message.data;
					else
						WorldVariables.clientSide = (WorldVariables) message.data;
				}
			});
			context.setPacketHandled(true);
		}
	}
}
