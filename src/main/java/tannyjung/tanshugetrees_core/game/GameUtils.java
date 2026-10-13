package tannyjung.tanshugetrees_core.game;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.*;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import tannyjung.tanshugetrees_core.Core;

import java.util.*;

/*
(1.20.1)
import net.minecraftforge.fml.ModList;
import net.minecraft.world.level.chunk.ChunkStatus;
(1.21.1)
import net.neoforged.fml.ModList;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.scores.ScoreHolder;
*/
import net.neoforged.fml.ModList;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import tannyjung.tanshugetrees_core.outside.CacheManager;
import tannyjung.tanshugetrees_core.outside.FileManager;
import tannyjung.tanshugetrees_core.outside.OutsideUtils;

public class GameUtils {

	public static boolean isModLoaded (String id) {

		return ModList.get().isLoaded(id);

	}

	public static String randomChooseVariant (String test) {

		if (test.isEmpty() == false) {

			String[] split = null;

			for (String variant : test.split(" \\| ")) {

				split = variant.split(" / ");

				if (Math.random() < Double.parseDouble(split[0])) {

					return split[1];

				}

			}

		}

		return "";

	}

	public static void sendChatMessage (ServerLevel level_server, String data) {

		String[] split = data.split(" \\| ")[0].split(" / ");
		String prefix_color = "white";

		if (split.length > 1) {

			prefix_color = split[1];

		}

		runCommand(level_server, Vec3.ZERO, "tellraw @a [{\"text\":\"\"}," + Data.createText("[" + Core.mod_id_short + "] / " + prefix_color + " / This message was sent from " + Core.mod_name + " mod (Global) |   | " + data) + "]");

	}

	public static void sendChatMessagePrivate (Player player, String data) {

		String[] split = data.split(" \\| ")[0].split(" / ");
		String prefix_color = "white";

		if (split.length > 1) {

			prefix_color = split[1];

		}

		runCommandEntity(player, "tellraw @s [{\"text\":\"\"}," + Data.createText("[" + Core.mod_id_short + "] / " + prefix_color + " / This message was sent from " + Core.mod_name + " mod (Private) |   | " + data) + "]");

	}

	public static void runCommand (ServerLevel level_server, Vec3 vec3, String command) {

		/*
		(1.20.1) (1.21.1)
		level_server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, vec3, Vec2.ZERO, level_server, 4, "", Component.literal(""), level_server.getServer(), null).withSuppressedOutput(), command);
		(1.21.8)
		level_server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, vec3, Vec2.ZERO, level_server, PermissionSet.ALL_PERMISSIONS, "", Component.literal(""), level_server.getServer(), null).withSuppressedOutput(), command);
		*/
		level_server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, vec3, Vec2.ZERO, level_server, 4, "", Component.literal(""), level_server.getServer(), null).withSuppressedOutput(), command);

	}

	public static void runCommandEntity (Entity entity, String command) {

		if (entity.level() instanceof ServerLevel level_server) {

			/*
			(1.20.1) (1.21.1)
			level_server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), level_server, 4, entity.getName().getString(), entity.getDisplayName(), level_server.getServer(), entity), command);
			(1.21.8)
			level_server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), level_server, PermissionSet.ALL_PERMISSIONS, entity.getName().getString(), entity.getDisplayName(), level_server.getServer(), entity), command);
			*/
			level_server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), level_server, 4, entity.getName().getString(), entity.getDisplayName(), level_server.getServer(), entity), command);

		}

	}

	public static void spawnParticle (ServerLevel level_server, Vec3 vec3, double spreadX, double spreadY, double spreadZ, double speed, int count, String id) {

		ParticleType<?> particle = level_server.registryAccess().registryOrThrow(Registries.PARTICLE_TYPE).get(ResourceLocation.parse(id));

		if (particle == null) {

			return;

		}

		for (ServerPlayer player : level_server.players()) {

			level_server.sendParticles(player, (ParticleOptions) particle, true, vec3.x, vec3.y, vec3.z, count, spreadX, spreadY, spreadZ, speed);

		}

	}

	public static void playSound (ServerLevel level_server, Vec3 vec3, double pitch, double max_distance, String id) {

		ResourceLocation location = ResourceLocation.parse(id);

		if (level_server.registryAccess().registryOrThrow(Registries.SOUND_EVENT).get(location) == null) {

			return;

		}

		double distance = 0;
		double percent = 0;
		double volume_min = 0;
		double volume_max = max_distance / 12.0;

		for (ServerPlayer player_server : level_server.players()) {

			distance = player_server.position().distanceTo(vec3);

			if (distance > max_distance) {

				continue;

			}

			SoundEvent sound = SoundEvent.createFixedRangeEvent(location, (float) max_distance);
			percent = distance / max_distance;
			volume_min = (percent * 0.75) + 0.25;
			double volume = volume_min * volume_max;

			Core.DelayedWork.createBasic((int) Math.round(distance / 17.0), () -> {

				player_server.connection.send(new ClientboundSoundPacket(Holder.direct(sound), SoundSource.AMBIENT, vec3.x, vec3.y, vec3.z, (float) volume, (float) pitch, level_server.getRandom().nextLong()));

			});

		}

	}

	public static class Space {

		public static String getDimensionID (ServerLevel level_server) {

			/*
			(1.20.1) (1.21.1)
			return level_server.dimension().location().toString();
			(1.21.8)
			return level_server.dimension().identifier().toString();
			*/
			return level_server.dimension().location().toString();

		}

		public static BlockPos getWorldSpawnPos (LevelAccessor level_accessor) {

			/*
			(1.20.1)
			return new BlockPos(level_accessor.getLevelData().getXSpawn(), level_accessor.getLevelData().getYSpawn(), level_accessor.getLevelData().getZSpawn());
			(1.21.1)
			return level_accessor.getLevelData().getSpawnPos();
			*/
			return level_accessor.getLevelData().getSpawnPos();

		}

		public static int getBuildHeight (LevelAccessor level_accessor, boolean highest) {

			if (highest == true) {

				/*
				(1.20.1) (1.21.1)
				return level_accessor.getMaxBuildHeight() - 1;
				(1.21.8)
				return level_accessor.getMaxY() - 1;
				*/
				return level_accessor.getMaxBuildHeight() - 1;

			} else {

				/*
				(1.20.1) (1.21.1)
				return level_accessor.getMinBuildHeight() + 1;
				(1.21.8)
				return level_accessor.getMinY() + 1;
				*/
				return level_accessor.getMinBuildHeight() + 1;

			}

		}

		public static boolean testChunkStatus (LevelAccessor level_accessor, ChunkPos chunk_pos, ChunkStatus status) {

			return level_accessor.hasChunk(chunk_pos.x, chunk_pos.z) == true && level_accessor.getChunk(chunk_pos.x, chunk_pos.z).getHighestGeneratedStatus().isOrAfter(status) == true;

		}

		public static void placeFeature (LevelAccessor level_accessor, BlockPos pos, String id) {

			WorldGenLevel level_world_gen = (WorldGenLevel) level_accessor;
			HolderLookup.RegistryLookup<ConfiguredFeature<?, ?>> lookup = level_world_gen.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE);
			ResourceKey<ConfiguredFeature<?, ?>> key = ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.parse(id));
			ChunkGenerator chunk_generator = level_world_gen.getLevel().getChunkSource().getGenerator();
			RandomSource random = level_world_gen.getRandom();

			/*
			(1.20.1) (1.21.1)
			lookup.getOrThrow(key).value().place(level_world_gen, chunk_generator, random, pos);
			(1.21.8)
			lookup.getValueOrThrow(key).place(level_world_gen, chunk_generator, random, pos);
			*/
			lookup.getOrThrow(key).value().place(level_world_gen, chunk_generator, random, pos);

		}

		public static int getHeightWorldGen (WorldGenLevel level_world_gen, ServerLevel level_server, ChunkGenerator chunk_generator, int posX, int posZ, Heightmap.Types type_normal, Heightmap.Types type_outside) {

			BlockPos pos = new BlockPos(posX, 0, posZ);
			ChunkPos chunk_pos = new ChunkPos(pos);

			if (level_world_gen.hasChunk(chunk_pos.x, chunk_pos.z) == true) {

				ChunkAccess chunk = level_world_gen.getChunk(pos);

				if (chunk instanceof ProtoChunk == true) {

					if (Space.testChunkStatus(level_world_gen, chunk_pos, ChunkStatus.CARVERS) == true) {

						return chunk.getHeight(type_normal, pos.getX(), pos.getZ()) + 1;

					}

				} else {

					return level_world_gen.getHeight(type_normal, posX, posZ);

				}

			}

			return chunk_generator.getBaseHeight(posX, posZ, type_outside, level_world_gen, level_server.getChunkSource().randomState());

		}

	}

	public static class Environment {

		public static boolean test (Holder<Biome> biome, String test) {

			if (test.isEmpty() == true) {

				return false;

			} else if (test.equals("all") == true) {

				return true;

			} else {

				String key = biome + " -> " + test;

				if (CacheManager.DataLogic.existNormal("test_biome", key) == false) {

					boolean result = false;

					{

						String biome_centerID = toID(biome);

						for (String split : test.split(" / ")) {

							result = true;

							for (String split2 : split.split(", ")) {

								String split_get = split2.replaceAll("[#!]", "");

								{

									if (split2.startsWith("#") == true || split2.startsWith("!#") == true) {

										try {

											if (biome.is(TagKey.create(Registries.BIOME, ResourceLocation.parse(split_get))) == false) {

												result = false;

											}

										} catch (Exception ignored) {

											result = false;

										}

									} else {

										if (biome_centerID.equals(split_get) == false) {

											result = false;

										}

									}

									if (split2.startsWith("!") == true) {

										result = !result;

									}

								}

								if (result == false) {

									break;

								}

							}

							if (result == true) {

								break;

							}

						}

					}

					CacheManager.DataLogic.setNormal("test_biome", key, result);

				}

				// TODO -> Remove this debug

				Map<String, Boolean> test_biome = CacheManager.DataLogic.getNormal("test_biome");

				if (test_biome == null) {

					Core.logger.error("No Main -----> " + key);

				}

				if (test_biome.get(key) == null) {

					Core.logger.error("No Key -----> " + key);

				}

				return test_biome.get(key);

			}

		}

		public static String toID (Holder<Biome> biome) {

			String return_text = biome.toString().replace("Reference{ResourceKey[minecraft:worldgen/biome / ", "");
			return return_text.substring(0, return_text.indexOf("]"));

		}

		public static Holder<Biome> getAt (LevelAccessor level_accessor, BlockPos pos) {

			ChunkPos chunk_pos = new ChunkPos(pos);

			if (Space.testChunkStatus(level_accessor, chunk_pos, ChunkStatus.BIOMES) == true) {

				return level_accessor.getChunk(chunk_pos.x, chunk_pos.z).getNoiseBiome(pos.getX() >> 2, pos.getY() >> 2, pos.getZ() >> 2);

			} else {

				return level_accessor.getUncachedNoiseBiome(pos.getX() >> 2, pos.getY() >> 2, pos.getZ() >> 2);

			}

		}

	}

	public static class Data {

		public static MutableComponent convertJSONToComponent (String data) {

			try {

				/*
				(1.20.1)
				return Component.Serializer.fromJson(data);
				(1.21.1)
				return Component.Serializer.fromJson(data, RegistryAccess.EMPTY);
				*/
				return Component.Serializer.fromJson(data, RegistryAccess.EMPTY);

			} catch (Exception exception) {

				OutsideUtils.exception(new Exception(), exception, "");

			}

			return null;

		}

		public static String convertFileToTag (String path) {

			Set<String> normal = new HashSet<>();

			// Add Normal
			{

				normal.add("-");
				normal.add("0");
				normal.add("1");
				normal.add("2");
				normal.add("3");
				normal.add("4");
				normal.add("5");
				normal.add("6");
				normal.add("7");
				normal.add("8");
				normal.add("9");

			}

			StringBuilder data = new StringBuilder();
			String[] split = null;

			for (String scan : FileManager.readTXT(path)) {

				if (scan.isEmpty() == false && scan.startsWith("---") == false) {

					split = scan.split(" = ");

					if (split[1].isEmpty() == true || split[1].equals("none") == true) {

						continue;

					} else if (normal.contains(split[1].substring(0, 1)) == true) {

						data.append(split[0]).append(":").append(split[1]);

					} else if (split[1].equals("true") == true || split[1].equals("false") == true) {

						data.append(split[0]).append(":").append(split[1]);

					} else {

						data.append(split[0]).append(":\"").append(split[1]).append("\"");

					}

					data.append(",");

				}

			}

			return "{" + data + "}";

		}

		public static String createText (String data) {

			StringBuilder convert = new StringBuilder();
			String[] split = null;
			boolean first = false;

			for (String scan : data.split(" \\| ")) {

				if (first == false) {

					first = true;

				} else {

					convert.append(",");

				}

				split = scan.split(" / ");

				if (split.length == 1) {

					convert.append("{\"text\":\"");
					convert.append(split[0]);
					convert.append("\",\"color\":\"white\"");

				} else {

					convert.append("{\"text\":\"");
					convert.append(split[0]);
					convert.append("\",\"color\":\"");
					convert.append(split[1]);
					convert.append("\"");

					if (split.length == 3) {

						if (split[2].startsWith("https") == true) {

							convert.append(",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"");
							convert.append(split[2]);
							convert.append("\"}");

						} else if (split[2].startsWith("/") == true) {

							convert.append(",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"");
							convert.append(split[2]);
							convert.append("\"}");

						}

						convert.append(",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"");
						convert.append(split[2]);
						convert.append("\"}");

					}

				}

				convert.append("}");

			}

			return convert.toString();

		}

		public static String createTextDoubleBackslash (String data) {

			return createText(data).replace("\"", "\\\"");

		}

		public static String createItem (String name, String lore, String custom_data, String forge_data) {

			/*
			(1.20.1)
			String part_name = "display:{Name:\"" + createTextDoubleBackslash(name) + "\"},";
			String part_lore = "Lore:[\"" + createTextDoubleBackslash(lore) + "\"],";
			String part_custom_data = "tag:{" + Core.mod_id + ":{" + custom_data + "}},";
			String part_forge_data = "BlockEntityData:{NeoForgeData:{" + Core.mod_id + ":{" + forge_data + "}}},";
			(1.21.1)
			String part_name = "custom_name:\"" + createTextDoubleBackslash(name) + "\",";
			String part_lore = "lore:[\"" + createTextDoubleBackslash(lore) + "\"],";
			String part_custom_data = "custom_data:{" + custom_data + "},";
			String part_forge_data = "entity_block_data:{id:\"\",NeoForgeData:{" + Core.mod_id + ":{" + forge_data + "}}},";
			*/
			String part_name = "custom_name:\"" + createTextDoubleBackslash(name) + "\",";
			String part_lore = "lore:[\"" + createTextDoubleBackslash(lore) + "\"],";
			String part_custom_data = "custom_data:{" + custom_data + "},";
			String part_forge_data = "entity_block_data:{id:\"\",NeoForgeData:{" + Core.mod_id + ":{" + forge_data + "}}},";

			StringBuilder write = new StringBuilder();
			if (name.isEmpty() == false) write.append(part_name);
			if (lore.isEmpty() == false) write.append(part_lore);
			if (custom_data.isEmpty() == false) write.append(part_custom_data);
			if (forge_data.isEmpty() == false) write.append(part_forge_data);
			return write.toString();

		}

	}

}