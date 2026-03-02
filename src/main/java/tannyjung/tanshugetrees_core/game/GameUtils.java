package tannyjung.tanshugetrees_core.game;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.*;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import net.minecraftforge.event.server.ServerLifecycleEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;
import tannyjung.tanshugetrees.init.TanshugetreesModMenus;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.outside.FileManager;
import tannyjung.tanshugetrees_core.outside.OutsideUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

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
import net.minecraftforge.fml.ModList;
import net.minecraft.world.level.chunk.ChunkStatus;

public class GameUtils {

	public static class Misc {

        public static boolean isModLoaded (String id) {

            return ModList.get().isLoaded(id);

        }

        public static boolean testCustomBiome (Holder<Biome> biome, String config_value) {

			boolean result = false;

            if (config_value.equals("all") == true) {

                result = true;

            } else {

                String biome_centerID = Space.getBiomeID(biome);

                for (String split : config_value.split(" / ")) {

                    result = true;

                    for (String split2 : split.split(", ")) {

                        String split_get = split2.replaceAll("[#!]", "");

                        {

                            if (split2.startsWith("#") == true || split2.startsWith("!#") == true) {

                                if (Space.isBiomeTaggedAs(biome, split_get) == false) {

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

            return result;

        }

        public static boolean testCustomBlock (BlockState test_block, String config_value) {

            boolean return_logic = false;

            if (config_value.equals("all") == true) {

                return_logic = true;

            } else {

                String value = "";

                for (String split : config_value.split(" / ")) {

                    return_logic = true;

                    for (String split2 : split.split(", ")) {

                        value = split2.replaceAll("[#!]", "");

                        {

                            if (split2.startsWith("#") == true || split2.startsWith("!#") == true) {

                                if (Tile.isTaggedAs(test_block, value) == false) {

                                    return_logic = false;

                                }

                            } else {

                                if (test_block.equals(Tile.fromText(value)) == false) {

                                    return_logic = false;

                                }

                            }

                            if (split2.startsWith("!") == true) {

                                return_logic = !return_logic;

                            }

                        }

                        if (return_logic == false) {

                            break;

                        }

                    }

                    if (return_logic == true) {

                        break;

                    }

                }

            }

            return return_logic;

        }

		public static void sendChatMessage (ServerLevel level_server, String target, String data) {

			String[] split = data.split(" \\| ")[0].split(" / ");
			String prefix_color = "white";

			if (split.length > 1) {

				prefix_color = split[1];

			}

            Command.run(level_server, 0, 0, 0, "tellraw " + target + " [{\"text\":\"\"}," + Data.createTextData("[" + Core.mod_id_short + "] / " + prefix_color + " / This message was sent from " + Core.mod_name + " mod |   | " + data) + "]");

        }

		public static void spawnParticle (ServerLevel level_server, double posX, double posY, double posZ, double spreadX, double spreadY, double spreadZ, double speed, int count, String id) {

			ParticleType<?> particle = ForgeRegistries.PARTICLE_TYPES.getValue(ResourceLocation.parse(id));

			if (particle != null) {

				for (ServerPlayer player : level_server.players()) {

					level_server.sendParticles(player, (ParticleOptions) particle, true, posX, posY, posZ, count, spreadX, spreadY, spreadZ, speed);

				}

			}

		}

		public static void playSound (ServerLevel level_server, double posX, double posY, double posZ, float volume, float pitch, String id) {

			SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse(id));

			if (sound != null) {

				level_server.playSound(null, BlockPos.containing(posX, posY, posZ), sound, SoundSource.NEUTRAL, volume, pitch);

			}

		}

	}

	public static class Command {

		public static void run (ServerLevel level_server, double posX, double posY, double posZ, String command) {
			
			/*
			(1.20.1) (1.21.1)
			level_server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(posX, posY, posZ), Vec2.ZERO, level_server, 4, "", Component.literal(""), level_server.getServer(), null).withSuppressedOutput(), command);
			(1.21.8)
			level_server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(posX, posY, posZ), Vec2.ZERO, level_server, PermissionSet.ALL_PERMISSIONS, "", Component.literal(""), level_server.getServer(), null).withSuppressedOutput(), command);
			*/
			level_server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(posX, posY, posZ), Vec2.ZERO, level_server, 4, "", Component.literal(""), level_server.getServer(), null).withSuppressedOutput(), command);

		}

		public static void runEntity (Entity entity, String command) {

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

		public static boolean result (ServerLevel level_server, double posX, double posY, double posZ, String command) {

			StringBuilder result = new StringBuilder();

			CommandSource data_consumer = new CommandSource() {

				@Override
				public boolean acceptsSuccess() {
					result.append("pass");
					return true;
				}

				@Override
				public void sendSystemMessage(Component component) {
				}

				@Override
				public boolean acceptsFailure() {
					return false;
				}

				@Override
				public boolean shouldInformAdmins() {
					return false;
				}

			};

			/*
			(1.20.1) (1.21.1)
			level_server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(data_consumer, new Vec3(posX, posY, posZ), Vec2.ZERO, level_server, 4, "", Component.literal(""), level_server.getServer(), null), command);
			(1.21.8)
			level_server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(data_consumer, new Vec3(posX, posY, posZ), Vec2.ZERO, level_server, PermissionSet.ALL_PERMISSIONS, "", Component.literal(""), level_server.getServer(), null), command);
			*/
			level_server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(data_consumer, new Vec3(posX, posY, posZ), Vec2.ZERO, level_server, 4, "", Component.literal(""), level_server.getServer(), null), command);

			return result.toString().equals("pass");

		}

		public static boolean resultEntity (Entity entity, String command) {

			StringBuilder result = new StringBuilder();

			CommandSource data_consumer = new CommandSource() {

				@Override
				public boolean acceptsSuccess() {
					result.append("pass");
					return true;
				}

				@Override
				public void sendSystemMessage(Component component) {
				}

				@Override
				public boolean acceptsFailure() {
					return false;
				}

				@Override
				public boolean shouldInformAdmins() {
					return false;
				}

			};

			if (entity.level() instanceof ServerLevel level_server) {

				level_server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(data_consumer, entity.position(), entity.getRotationVector(), level_server, 4, entity.getName().getString(), entity.getDisplayName(), level_server.getServer(), entity), command);

			}

			return result.toString().equals("pass");

		}

	}

	public static class Tile {

		public static boolean isTaggedAs (BlockState block, String tag) {

			try {

				return block.is(BlockTags.create(ResourceLocation.parse(tag)));

			} catch (Exception exception) {

				OutsideUtils.exception(new Exception(), exception, "");

			}

			return false;

		}

		public static BlockState fromText (String data) {

			BlockState block = Blocks.AIR.defaultBlockState();

			try {

				String id = data;

				// Get Block ID
				{

					if (id.endsWith("}") == true) {

						id = id.substring(0, id.indexOf("{"));

					}

					if (id.endsWith("]") == true) {

						id = id.substring(0, id.indexOf("["));

					}

				}

				/*
				(1.20.1) (1.21.1)

				(1.21.8)

				*/
				Block block_test = ForgeRegistries.BLOCKS.getValue(ResourceLocation.parse(id));

				if (block_test != null) {

					block = block_test.defaultBlockState();

					if (data.endsWith("}") == true) {

						// To Do -> Put "{...}" from data into the block

						data = data.substring(0, data.indexOf("{"));

					}

					if (data.endsWith("]") == true) {

						String[] properties = data.substring(data.indexOf("[") + 1, data.length() - 1).split(",");

						for (String property_data : properties) {

							String[] get = property_data.split("=");
							Property<?> test = block.getBlock().getStateDefinition().getProperty(get[0]);

							if (test instanceof BooleanProperty) {

								block = setPropertyLogic(block, get[0], Boolean.parseBoolean(get[1]));

							} else if (test instanceof IntegerProperty) {

								block = setPropertyNumber(block, get[0], Integer.parseInt(get[1]));

							} else if (test instanceof EnumProperty<?>) {

								block = setPropertyCustom(block, get[0], get[1]);

							}

						}

					}

				}

			} catch (Exception exception) {

				OutsideUtils.exception(new Exception(), exception, "");

			}

			return block;

		}

		public static String toText (BlockState block) {

			return block.toString().replace("Block{", "").replace("}", "");

		}

		public static String toTextID (BlockState block) {

			String return_text = block.getBlock().toString();
			return return_text.substring("Block{".length(), return_text.length() - 1);

		}

		public static void setScheduleTick (ServerLevel level_server, int posX, int posY, int posZ, int value) {

			level_server.scheduleTick(new BlockPos(posX, posY, posZ), level_server.getBlockState(new BlockPos(posX, posY, posZ)).getBlock(), value);

		}

		public static boolean getPropertyLogic (BlockState block, String name) {

			Property<?> property = block.getBlock().getStateDefinition().getProperty(name);

			if (property instanceof BooleanProperty) {

				return Boolean.parseBoolean(block.getValue(property).toString());

			}

			return false;

		}

		public static int getPropertyNumber (BlockState block, String name) {

			Property<?> property = block.getBlock().getStateDefinition().getProperty(name);

			if (property instanceof IntegerProperty) {

				return Integer.parseInt(block.getValue(property).toString());

			}

			return 0;

		}

		public static String getPropertyCustom (BlockState block, String name) {

			Property<?> property = block.getBlock().getStateDefinition().getProperty(name);

			if (property instanceof EnumProperty<?>) {

				return block.getValue(property).toString();

			}

			return "";

		}

		public static BlockState setPropertyLogic (BlockState block, String name, boolean value) {

			Property<?> property = block.getBlock().getStateDefinition().getProperty(name);

			if (block.hasProperty(property) == true) {

				if (property instanceof BooleanProperty property_instance) {

					block = block.setValue(property_instance, value);

				}

			}

			return block;

		}

		public static BlockState setPropertyNumber (BlockState block, String name, int value) {

			Property<?> property = block.getBlock().getStateDefinition().getProperty(name);

			if (block.hasProperty(property) == true) {

				if (property instanceof IntegerProperty property_instance) {

					block = block.setValue(property_instance, value);

				}

			}

			return block;

		}

		public static BlockState setPropertyCustom (BlockState block, String name, String value) {

			if (name.equals("facing") == true) {

				if (block.hasProperty(DirectionalBlock.FACING) == true) {

					block = block.setValue(DirectionalBlock.FACING, Direction.valueOf(value.toUpperCase()));

				} else if (block.hasProperty(HorizontalDirectionalBlock.FACING) == true) {

					block = block.setValue(HorizontalDirectionalBlock.FACING, Direction.valueOf(value.toUpperCase()));

				}

			} else if (name.equals("type") == true) {

				if (block.hasProperty(SlabBlock.TYPE) == true) {

					block = block.setValue(SlabBlock.TYPE, SlabType.valueOf(value.toUpperCase()));

				}

			}

			return block;

		}

	}

	public static class Mob {

		public static List<Entity> getAtArea (ServerLevel level_server, double posX, double posY, double posZ, int distance, boolean is_box, int count, String id, String tag) {

			Vec3 center = new Vec3(posX, posY, posZ);
			int distance_power = distance * distance;
			List<String> tags = Arrays.stream(tag.split(" / ")).toList();

			List<Entity> entities = level_server.getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(distance), entity -> {

				boolean test = false;

				if (is_box == true || entity.distanceToSqr(center) <= distance_power) {

					if (id.isEmpty() == true || EntityType.getKey(entity.getType()).toString().equals(id) == true) {

						if (tag.isEmpty() == true || entity.getTags().containsAll(tags) == true) {

							test = true;

						}

					}

				}

				return test;

			});

			if (distance > 0) {

				entities = entities.stream().sorted(Comparator.comparingDouble(entity -> entity.distanceToSqr(center))).toList();

			}

			if (count > 0) {

				if (entities.size() > count) {

					entities = entities.subList(0, count);

				}

			}

			return entities;

		}

		public static List<Entity> getAtEverywhere (ServerLevel level_server, String id, String tag) {

			List<Entity> entities = new ArrayList<>();
			List<String> tags = Arrays.stream(tag.split(" / ")).toList();

			level_server.getAllEntities().forEach(entity -> {

				if (id.isEmpty() == true || EntityType.getKey(entity.getType()).toString().equals(id) == true) {

					if (tag.isEmpty() == true || entity.getTags().containsAll(tags) == true) {

						entities.add(entity);

					}

				}

			});

			return entities;

		}

		public static Entity getAtAreaOne (ServerLevel level_server, double posX, double posY, double posZ, int distance, boolean is_box, String id, String tag) {

			List<Entity> entities = GameUtils.Mob.getAtArea(level_server, posX, posY, posZ, distance, is_box, 1, id, tag);

			if (entities.isEmpty() == false) {

				return entities.get(0);

			} else {

				return null;

			}

		}

		public static Entity getAtEverywhereOne (ServerLevel level_server, String id, String tag) {

			List<Entity> entities = GameUtils.Mob.getAtEverywhere(level_server, id, tag);

			if (entities.isEmpty() == false) {

				return entities.get(0);

			} else {

				return null;

			}

		}

		public static Entity summon (ServerLevel level_server, double posX, double posY, double posZ, String id, String name, String tag, String custom) {

			Entity entity = BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(id)).create(level_server);

			if (entity != null) {

				if (custom.isEmpty() == false) {

					entity.load(Data.convertJSONToTag(custom));

				}

				entity.setCustomName(Data.convertJSONToComponent("[" + Data.createTextData(name) + "]"));

				if (name.contains(" / ") == true) {

					entity.setCustomNameVisible(true);

				}

				entity.addTag("TANNYJUNG");
				entity.addTag(Core.mod_id_big);

				for (String get : tag.split(" / ")) {

					entity.addTag(get);

				}

				entity.setPos(posX, posY, posZ);
				level_server.addFreshEntity(entity);

			}

			return entity;

		}

		public static void summonWorldGen (ServerLevel level_server, double posX, double posY, double posZ, String id, String name, String tag, String custom) {

			level_server.getServer().execute(() -> {

				GameUtils.Mob.summon(level_server, posX, posY, posZ, id, name, tag, custom);

			});

		}

		public static boolean isCreativeMode (Entity entity) {

			if (entity instanceof Player player) {

				return player.getAbilities().instabuild;

			}

			return false;

		}

		public static boolean canTickingAt (ServerLevel level_server, int posX, int posY, int posZ) {

			return level_server.isPositionEntityTicking(new BlockPos(posX, posY, posZ));

		}

	}

	public static class Item {

		public static ItemStack getSlot (Entity entity, EquipmentSlot equipment_slot) {

			if (entity instanceof LivingEntity living_entity) {

				return living_entity.getItemBySlot(equipment_slot);

			}

			return ItemStack.EMPTY;

		}

		public static void setCount (Entity entity, EquipmentSlot equipment_slot, int value) {

			if (entity instanceof LivingEntity living_entity) {

				ItemStack item = living_entity.getItemBySlot(equipment_slot);
				item.setCount(value);

			}

		}

		public static void addCount (Entity entity, EquipmentSlot equipment_slot, int value) {

			if (entity instanceof LivingEntity living_entity) {

				ItemStack item = living_entity.getItemBySlot(equipment_slot);
				item.setCount(item.getCount() + value);

			}

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

		public static int[] getWorldSpawnPos (LevelAccessor level_accessor) {

			/*
			(1.20.1)
			return new int[]{level_accessor.getLevelData().getXSpawn(), level_accessor.getLevelData().getZSpawn()};
			(1.21.1)
			return new int[]{level_accessor.getLevelData().getSpawnPos().getX(), level_accessor.getLevelData().getSpawnPos().getZ()};
			*/
			return new int[]{level_accessor.getLevelData().getXSpawn(), level_accessor.getLevelData().getZSpawn()};

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

		public static boolean testChunkStatus (LevelAccessor level_accessor, int chunkX, int chunkZ, ChunkStatus status) {

			return level_accessor.hasChunk(chunkX, chunkZ) == true && level_accessor.getChunk(chunkX, chunkZ).getHighestGeneratedStatus().isOrAfter(status) == true;

		}

		public static void placeFeature (LevelAccessor level_accessor, int posX, int posY, int posZ, String id) {

			WorldGenLevel level_world_gen = (WorldGenLevel) level_accessor;
			BlockPos pos = new BlockPos(posX, posY, posZ);

			/*
			(1.20.1)
			level_accessor.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE).getOrThrow(FeatureUtils.createKey(id)).value().place(level_world_gen, level_world_gen.getLevel().getChunkSource().getGenerator(), level_world_gen.getRandom(), pos);
			(1.21.1)
			level_world_gen.holderOrThrow(ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.parse(id))).value().place(level_world_gen, level_world_gen.getLevel().getChunkSource().getGenerator(), level_world_gen.getRandom(), pos);
			(1.21.8)
			level_accessor.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE).getValueOrThrow(FeatureUtils.createKey(id)).place(level_world_gen, level_world_gen.getLevel().getChunkSource().getGenerator(), level_world_gen.getRandom(), pos);
			*/
			level_accessor.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE).getOrThrow(FeatureUtils.createKey(id)).value().place(level_world_gen, level_world_gen.getLevel().getChunkSource().getGenerator(), level_world_gen.getRandom(), pos);

		}

		public static String getBiomeID (Holder<Biome> biome) {

			String return_text = biome.toString().replace("Reference{ResourceKey[minecraft:worldgen/biome / ", "");
			return return_text.substring(0, return_text.indexOf("]"));

		}

		public static boolean isBiomeTaggedAs (Holder<Biome> biome, String tag) {

			try {

				return biome.is(TagKey.create(Registries.BIOME, ResourceLocation.parse(tag)));

			} catch (Exception exception) {

				OutsideUtils.exception(new Exception(), exception, "");

			}

			return false;

		}

		public static Holder<Biome> getBiomeAt (ServerLevel level_server, int posX, int posY, int posZ) {

			int quartX = posX >> 2;
			int quartY = posY >> 2;
			int quartZ = posZ >> 2;
			return level_server.getChunkSource().getGenerator().getBiomeSource().getNoiseBiome(quartX, quartY, quartZ, level_server.getChunkSource().randomState().sampler());

		}

		public static Vec3 getPosLook (Entity entity, double offsetX, double offsetY, double offsetZ) {

			Vec3 vec3_forward = Vec3.directionFromRotation(entity.getXRot(), entity.getYRot());
			Vec3 vec3_vertical = null;

			if (Math.abs(vec3_forward.y) > 0.999) {

				vec3_vertical = new Vec3(0,0,1);

			} else {

				vec3_vertical = new Vec3(0,1,0);

			}

			Vec3 vec3_horizontal = vec3_forward.cross(vec3_vertical).normalize();
			Vec3 vec3_vertical_adjust = vec3_horizontal.cross(vec3_forward).normalize();
			return entity.position().add(vec3_horizontal.scale(offsetX)).add(vec3_vertical_adjust.scale(offsetY)).add(vec3_forward.scale(offsetZ));
		}

		public static Vec3 getPosRay (Entity entity, double distance) {

			return entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(distance)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getLocation();

		}

	}

	public static class Data {

		public static CompoundTag convertJSONToTag (String data) {

			CompoundTag tag = null;

			try {

				tag = TagParser.parseTag(data);

			} catch (Exception ignored) {



			}

			return tag;

		}

		public static MutableComponent convertJSONToComponent (String data) {

			MutableComponent component = null;

			try {

				/*
				(1.20.1)
				component = Component.Serializer.fromJson(data);
				(1.21.1)
				component = Component.Serializer.fromJson(data, RegistryAccess.EMPTY);
				*/
				component = Component.Serializer.fromJson(data);

			} catch (Exception ignored) {



			}

			return component;

		}

		public static String convertFileToForgeData (String path) {

			StringBuilder data = new StringBuilder();

			for (String read_all : FileManager.readTXT(path)) {

				if (read_all.isEmpty() == false && read_all.startsWith("---") == false) {

					data.append(read_all.replace(" = ", ":"));
					data.append(",");

				}

			}

			return "{ForgeData:{" + Core.mod_id + ":{" + data + "}}}";

		}

		public static String createTextData (String data) {

			StringBuilder convert = new StringBuilder();
			String[] split = new String[0];
			boolean first = false;

			for (String read_all : data.split(" \\| ")) {

				if (first == false) {

					first = true;

				} else {

					convert.append(",");

				}

				split = read_all.split(" / ");

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

							convert.append(",\"underlined\":true,\"clickEvent\":{\"action\":\"open_url\",\"value\":\"");
							convert.append(split[2]);
							convert.append("\"}");

						} else if (split[2].startsWith("/") == true) {

							convert.append(",\"underlined\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"");
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

		public static String createTextDataDoubleBackslash (String data) {

			return createTextData(data).replace("\"", "\\\"");

		}

		public static String createItem (String name, String lore, String custom_data, String forge_data) {

			/*
			(1.20.1)
			String part_name = "display:{Name:\"" + createTextDataDoubleBackslash(name) + "\"},";
			String part_lore = "Lore:[\"" + Data.createTextDataDoubleBackslash(lore) + "\"],";
			String part_custom_data = "tag:{" + Core.mod_id + ":{" + custom_data + "}},";
			String part_forge_data = "BlockEntityData:{ForgeData:{" + Core.mod_id + ":{" + forge_data + "}}},";
			(1.21.1)
			String part_name = "custom_name:\"" + createTextDataDoubleBackslash(name) + "\",";
			String part_lore = "lore:[\"" + Data.createTextDataDoubleBackslash(lore) + "\"],";
			String part_custom_data = "custom_data:{" + custom_data + "},";
			String part_forge_data = "block_entity_data:{id:\"\",ForgeData:{" + Core.mod_id + ":{" + forge_data + "}}},";
			*/
			String part_name = "display:{Name:\"" + createTextDataDoubleBackslash(name) + "\"},";
			String part_lore = "Lore:[\"" + Data.createTextDataDoubleBackslash(lore) + "\"],";
			String part_custom_data = "tag:{" + Core.mod_id + ":{" + custom_data + "}},";
			String part_forge_data = "BlockEntityData:{ForgeData:{" + Core.mod_id + ":{" + forge_data + "}}},";

			StringBuilder write = new StringBuilder();
			if (name.isEmpty() == false) write.append(part_name);
			if (lore.isEmpty() == false) write.append(part_lore);
			if (custom_data.isEmpty() == false) write.append(part_custom_data);
			if (forge_data.isEmpty() == false) write.append(part_forge_data);
			return write.toString();

		}

		public static String getEntityText (Entity entity, String name) {

			/*
			(1.20.1) (1.21.1)
			return entity.getPersistentData().getCompound(Core.mod_id).getString(name);
			(1.21.8)
			return entity.getPersistentData().getCompound(Core.mod_id).getString(name).get();
			*/
			return entity.getPersistentData().getCompound(Core.mod_id).getString(name);

		}

		public static Boolean getEntityLogic (Entity entity, String name) {

			/*
			(1.20.1) (1.21.1)
			return entity.getPersistentData().getCompound(Core.mod_id).getBoolean(name);
			(1.21.8)
			return entity.getPersistentData().getCompound(Core.mod_id).getBoolean(name).get();
			*/
			return entity.getPersistentData().getCompound(Core.mod_id).getBoolean(name);

		}

		public static double getEntityNumber (Entity entity, String name) {

			/*
			(1.20.1) (1.21.1)
			return entity.getPersistentData().getCompound(Core.mod_id).getDouble(name);
			(1.21.8)
			return entity.getPersistentData().getCompound(Core.mod_id).getDouble(name).get();
			*/
			return entity.getPersistentData().getCompound(Core.mod_id).getDouble(name);

		}

		public static double[] getEntityListNumber (Entity entity, String name) {

			/*
			(1.20.1) (1.21.1)
			ListTag list = entity.getPersistentData().getCompound(Core.mod_id).getList(name, Tag.TAG_DOUBLE);
			(1.21.8)
			ListTag list = entity.getPersistentData().getCompound(Core.mod_id).getList(name).get();
			*/
			ListTag list = entity.getPersistentData().getCompound(Core.mod_id).getList(name, Tag.TAG_DOUBLE);

			double[] convert = new double[list.size()];

			for (int count = 0; count <= list.size() - 1; count++) {

				/*
				(1.20.1) (1.21.1)
				convert[count] = list.getDouble(count);
				(1.21.8)
				convert[count] = list.getDouble(count).get();
				*/
				convert[count] = list.getDouble(count);

			}

			return convert;

		}

		public static double[] getEntityListNumberFloat (Entity entity, String name) {

			/*
			(1.20.1) (1.21.1)
			ListTag list = entity.getPersistentData().getCompound(Core.mod_id).getList(name, Tag.TAG_FLOAT);
			(1.21.8)
			ListTag list = entity.getPersistentData().getCompound(Core.mod_id).getList(name).get();
			*/
			ListTag list = entity.getPersistentData().getCompound(Core.mod_id).getList(name, Tag.TAG_FLOAT);

			double[] convert = new double[list.size()];

			for (int count = 0; count <= list.size() - 1; count++) {

				/*
				(1.20.1) (1.21.1)
				convert[count] = list.getFloat(count);
				(1.21.8)
				convert[count] = list.getFloat(count).get();
				*/
				convert[count] = list.getFloat(count);

			}

			return convert;

		}

		public static void setEntityText (Entity entity, String name, String value) {

			CompoundTag tag = new CompoundTag();
			CompoundTag tag_add = new CompoundTag();
			tag_add.putString(name, value);
			tag.put(Core.mod_id, tag_add);
			entity.getPersistentData().merge(tag);

		}

		public static void setEntityLogic (Entity entity, String name, boolean value) {

			CompoundTag tag = new CompoundTag();
			CompoundTag tag_add = new CompoundTag();
			tag_add.putBoolean(name, value);
			tag.put(Core.mod_id, tag_add);
			entity.getPersistentData().merge(tag);

		}

		public static void setEntityNumber (Entity entity, String name, double value) {

			CompoundTag tag = new CompoundTag();
			CompoundTag tag_add = new CompoundTag();
			tag_add.putDouble(name, value);
			tag.put(Core.mod_id, tag_add);
			entity.getPersistentData().merge(tag);

		}

		public static void addEntityNumber (Entity entity, String name, double value) {

			/*
			(1.20.1) (1.21.1)
			entity.getPersistentData().getCompound(Core.mod_id).putDouble(name, entity.getPersistentData().getCompound(Core.mod_id).getDouble(name) + value);
			(1.21.8)
			entity.getPersistentData().getCompound(Core.mod_id).putDouble(name, entity.getPersistentData().getCompound(Core.mod_id).getDouble(name).get() + value);
			*/
			entity.getPersistentData().getCompound(Core.mod_id).putDouble(name, entity.getPersistentData().getCompound(Core.mod_id).getDouble(name) + value);

		}

		public static String getBlockText (LevelAccessor level_accessor, int posX, int posY, int posZ, String name) {

			return new Object() {

				public String getValue (LevelAccessor level_accessor, int posX, int posY, int posZ, String name) {

					BlockEntity blockEntity = level_accessor.getBlockEntity(new BlockPos(posX, posY, posZ));

					if (blockEntity != null) {

                        /*
                        (1.20.1) (1.21.1)
                        return blockEntity.getPersistentData().getCompound(Core.mod_id).getString(name);
                        (1.21.8)
                        return blockEntity.getPersistentData().getCompound(Core.mod_id).getString(name).get();
                        */
						return blockEntity.getPersistentData().getCompound(Core.mod_id).getString(name);

					}

					return "";

				}

			}.getValue(level_accessor, posX, posY, posZ, name);

		}

		public static double getBlockNumber (LevelAccessor level_accessor, int posX, int posY, int posZ, String name) {

			return new Object() {

				public double getValue (LevelAccessor level_accessor, int posX, int posY, int posZ, String name) {

					BlockEntity blockEntity = level_accessor.getBlockEntity(new BlockPos(posX, posY, posZ));

					if (blockEntity != null) {

                        /*
                        (1.20.1) (1.21.1)
                        return blockEntity.getPersistentData().getCompound(Core.mod_id).getDouble(name);
                        (1.21.8)
                        return blockEntity.getPersistentData().getCompound(Core.mod_id).getDouble(name).get();
                        */
						return blockEntity.getPersistentData().getCompound(Core.mod_id).getDouble(name);

					}

					return 0.0;

				}

			}.getValue(level_accessor, posX, posY, posZ, name);

		}

		public static boolean getBlockLogic (LevelAccessor level_accessor, int posX, int posY, int posZ, String name) {

			return new Object() {

				public boolean getValue (LevelAccessor level_accessor, int posX, int posY, int posZ, String name) {

					BlockEntity blockEntity = level_accessor.getBlockEntity(new BlockPos(posX, posY, posZ));

					if (blockEntity != null) {

                        /*
                        (1.20.1) (1.21.1)
                        return blockEntity.getPersistentData().getCompound(Core.mod_id).getBoolean(name);
                        (1.21.8)
                        return blockEntity.getPersistentData().getCompound(Core.mod_id).getBoolean(name).get();
                        */
						return blockEntity.getPersistentData().getCompound(Core.mod_id).getBoolean(name);

					}

					return false;

				}

			}.getValue(level_accessor, posX, posY, posZ, name);

		}

		public static void setBlockText (LevelAccessor level_accessor, ServerLevel level_server, int posX, int posY, int posZ, String name, String value) {

			BlockEntity block_entity = level_accessor.getBlockEntity(new BlockPos(posX, posY, posZ));

			if (block_entity != null) {

				CompoundTag tag = new CompoundTag();
				CompoundTag tag_add = new CompoundTag();
				tag_add.putString(name, value);
				tag.put(Core.mod_id, tag_add);
				block_entity.getPersistentData().merge(tag);
				BlockState block = level_accessor.getBlockState(new BlockPos(posX, posY, posZ));
				level_server.sendBlockUpdated(new BlockPos(posX, posY, posZ), block, block, 2);

			}

		}

		public static void setBlockLogic (LevelAccessor level_accessor, ServerLevel level_server, int posX, int posY, int posZ, String name, boolean value) {

			BlockEntity block_entity = level_accessor.getBlockEntity(new BlockPos(posX, posY, posZ));

			if (block_entity != null) {

				CompoundTag tag = new CompoundTag();
				CompoundTag tag_add = new CompoundTag();
				tag_add.putBoolean(name, value);
				tag.put(Core.mod_id, tag_add);
				block_entity.getPersistentData().merge(tag);
				BlockState block = level_accessor.getBlockState(new BlockPos(posX, posY, posZ));
				level_server.sendBlockUpdated(new BlockPos(posX, posY, posZ), block, block, 2);

			}

		}

		public static void setBlockNumber (LevelAccessor level_accessor, ServerLevel level_server, int posX, int posY, int posZ, String name, double value) {

			BlockEntity block_entity = level_accessor.getBlockEntity(new BlockPos(posX, posY, posZ));

			if (block_entity != null) {

				CompoundTag tag = new CompoundTag();
				CompoundTag tag_add = new CompoundTag();
				tag_add.putDouble(name, value);
				tag.put(Core.mod_id, tag_add);
				block_entity.getPersistentData().merge(tag);
				BlockState block = level_accessor.getBlockState(new BlockPos(posX, posY, posZ));
				level_server.sendBlockUpdated(new BlockPos(posX, posY, posZ), block, block, 2);

			}

		}

		public static void addBlockNumber (LevelAccessor level_accessor, ServerLevel level_server, int posX, int posY, int posZ, String name, double value) {

			BlockEntity block_entity = level_accessor.getBlockEntity(new BlockPos(posX, posY, posZ));

			if (block_entity != null) {

				/*
				(1.20.1) (1.21.1)
				block_entity.getPersistentData().getCompound(Core.mod_id).putDouble(name, block_entity.getPersistentData().getCompound(Core.mod_id).getDouble(name) + value);
				(1.21.8)
				block_entity.getPersistentData().getCompound(Core.mod_id).putDouble(name, block_entity.getPersistentData().getCompound(Core.mod_id).getDouble(name).get() + value);
				*/
				block_entity.getPersistentData().getCompound(Core.mod_id).putDouble(name, block_entity.getPersistentData().getCompound(Core.mod_id).getDouble(name) + value);

				BlockState block = level_accessor.getBlockState(new BlockPos(posX, posY, posZ));
				level_server.sendBlockUpdated(new BlockPos(posX, posY, posZ), block, block, 2);

			}

		}

		public static String getItemText (Entity entity, EquipmentSlot slot, String name) {

			/*
			(1.20.1)
			return Item.getSlot(entity, slot).getOrCreateTag().getCompound(Core.mod_id).getString(name);
			(1.21.1)
			return Item.getSlot(entity, slot).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getCompound(Core.mod_id).getString(name);
			*/
			return Item.getSlot(entity, slot).getOrCreateTag().getCompound(Core.mod_id).getString(name);

		}

		public static void setItemText (Entity entity, EquipmentSlot slot, String name, String value) {

			CompoundTag tag = new CompoundTag();
			CompoundTag tag_add = new CompoundTag();
			tag_add.putString(name, value);
			tag.put(Core.mod_id, tag_add);

			/*
			(1.20.1)
			Item.getSlot(entity, slot).getOrCreateTag().merge(tag);
			(1.21.1)
			CustomData.update(DataComponents.CUSTOM_DATA, Item.getSlot(entity, slot), test -> test.merge(tag));
			*/
			Item.getSlot(entity, slot).getOrCreateTag().merge(tag);

		}

	}

	public static class Score {

		public static void create (ServerLevel level_server, String name) {

			Scoreboard scoreboard = level_server.getServer().getScoreboard();
			Objective objective = scoreboard.getObjective(name);

			if (objective == null) {

				/*
				(1.20.1)
				scoreboard.addObjective(name, ObjectiveCriteria.DUMMY, Component.literal(name), ObjectiveCriteria.RenderType.INTEGER);
				(1.21.1)
				scoreboard.addObjective(name, ObjectiveCriteria.DUMMY, Component.literal(name), ObjectiveCriteria.RenderType.INTEGER, true, null);
				*/
				scoreboard.addObjective(name, ObjectiveCriteria.DUMMY, Component.literal(name), ObjectiveCriteria.RenderType.INTEGER);

			}

		}

		public static int get (ServerLevel level_server, String objective, String player) {

			ServerScoreboard score = level_server.getServer().getScoreboard();
			Objective objective_test = score.getObjective(objective);

			if (objective_test != null) {

				/*
				(1.20.1)
				return score.getOrCreatePlayerScore(player, objective_test).getScore();
				(1.21.1)
				return score.getOrCreatePlayerScore(ScoreHolder.forNameOnly(player), objective_test, false).get();
				*/
				return score.getOrCreatePlayerScore(player, objective_test).getScore();

			}

			return 0;

		}

		public static void set (ServerLevel level_server, String objective, String player, int value) {

			ServerScoreboard score = level_server.getServer().getScoreboard();
			Objective objective_test = score.getObjective(objective);

			if (objective_test != null) {

				/*
				(1.20.1)
				score.getOrCreatePlayerScore(player, objective_test).setScore(value);
				(1.21.1)
				score.getOrCreatePlayerScore(ScoreHolder.forNameOnly(player), objective_test, false).set(value);
				*/
				score.getOrCreatePlayerScore(player, objective_test).setScore(value);

			}

		}

		public static void add (ServerLevel level_server, String objective, String player, int value) {

			ServerScoreboard score = level_server.getServer().getScoreboard();
			Objective objective_test = score.getObjective(objective);

			if (objective_test != null) {

				int old_value = get(level_server, objective, player);

				/*
				(1.20.1)
				score.getOrCreatePlayerScore(player, objective_test).setScore(old_value + value);
				(1.21.1)
				score.getOrCreatePlayerScore(ScoreHolder.forNameOnly(player), objective_test, false).set(old_value + value);
				*/
				score.getOrCreatePlayerScore(player, objective_test).setScore(old_value + value);

			}

		}

	}

	public static class GUI {

		public static String getTextBox (Entity entity, String name) {

			if (entity instanceof Player player) {

				if (player.containerMenu instanceof TanshugetreesModMenus.MenuAccessor menu) {

					return menu.getMenuState(0, name, "");

				}

			}

			return "";

		}

		public static void setTextBox (Entity entity, String name, String value) {

			if (entity instanceof Player player) {

				if (player.containerMenu instanceof TanshugetreesModMenus.MenuAccessor menu) {

					menu.sendMenuStateUpdate(player, 0, name, value, true);

				}

			}

		}

	}

}