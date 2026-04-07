package tannyjung.tanshugetrees_core.game;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.*;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import tannyjung.tanshugetrees.init.TanshugetreesModMenus.MenuAccessor;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.outside.CacheManager;
import tannyjung.tanshugetrees_core.outside.FileManager;
import tannyjung.tanshugetrees_core.outside.OutsideUtils;

import java.util.*;

/*
(1.20.1)
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.data.worldgen.features.FeatureUtils;
(1.21.1)
import net.neoforged.fml.ModList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.scores.ScoreHolder;
*/
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.data.worldgen.features.FeatureUtils;

public class GameUtils {

	public static class Misc {

        public static boolean isModLoaded (String id) {

            return ModList.get().isLoaded(id);

        }

		public static String testVariant (String test) {

			if (test.equals("none") == false) {

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

            Command.run(level_server, Vec3.ZERO, "tellraw @a [{\"text\":\"\"}," + Data.createText("[" + Core.mod_id_short + "] / " + prefix_color + " / This message was sent from " + Core.mod_name + " mod (Global) |   | " + data) + "]");

        }

		public static void sendChatMessagePrivate (Player player, String data) {

			String[] split = data.split(" \\| ")[0].split(" / ");
			String prefix_color = "white";

			if (split.length > 1) {

				prefix_color = split[1];

			}

			Command.runEntity(player, "tellraw @s [{\"text\":\"\"}," + Data.createText("[" + Core.mod_id_short + "] / " + prefix_color + " / This message was sent from " + Core.mod_name + " mod (Private) |   | " + data) + "]");

		}

		public static void spawnParticle (ServerLevel level_server, Vec3 vec3, double spreadX, double spreadY, double spreadZ, double speed, int count, String id) {

			/*
			(1.20.1)
			ParticleType<?> particle = ForgeRegistries.PARTICLE_TYPES.getValue(ResourceLocation.parse(id));
			(1.21.1)
			ParticleType<?> particle = BuiltInRegistries.PARTICLE_TYPE.get(ResourceLocation.parse(id));
			*/
			ParticleType<?> particle = ForgeRegistries.PARTICLE_TYPES.getValue(ResourceLocation.parse(id));

			if (particle == null) {

				return;

			}

			for (ServerPlayer player : level_server.players()) {

				level_server.sendParticles(player, (ParticleOptions) particle, true, vec3.x, vec3.y, vec3.z, count, spreadX, spreadY, spreadZ, speed);

			}

		}

		public static void playSound (ServerLevel level_server, BlockPos pos, double volume, double pitch, String id) {

			/*
			(1.20.1)
			SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse(id));
			(1.21.1)
			SoundEvent sound = BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse(id));
			*/
			SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse(id));

			if (sound == null) {

				return;

			}

			level_server.playSound(null, pos, sound, SoundSource.NEUTRAL, (float) volume, (float) pitch);

		}

		public static Entity summonText (ServerLevel level_server, Vec3 vec3, double size, String data, boolean temporary) {

			Entity entity = Mob.summon(level_server, vec3, "minecraft:text_display", "Display Text", Core.mod_id_big + "-display_text", "{see_through:1b,alignment:\"left\",brightness:{block:15, sky:15},line_width:1000,transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[" + size + "f," + size + "f," + size + "f]},billboard:vertical,text:'" + Data.createText(data) + "'}");

			if (temporary == true) {

				Core.DelayedWork.create(false, 200, () -> {

					for (Entity scan : Mob.getAtArea(level_server, vec3, 1, true, 0, "minecraft:text_display", Core.mod_id_big + "-display_text")) {

						scan.discard();

					}

				});

			}

			return entity;

		}

		public static Entity summonBlock (ServerLevel level_server, Vec3 vec3, String name, String tag, double offsetX, double offsetY, double offsetZ, double sizeX, double sizeY, double sizeZ, int rotate_horizontal, int rotate_vertical, String id) {

			offsetX = offsetX - (sizeX / 2);
			offsetZ = offsetZ - (sizeZ / 2);
			offsetY = offsetY - 0.5;
			return Mob.summon(level_server, vec3, "minecraft:block_display", name, tag, "{transformation:{left_rotation:[0.0f,0.0f,0.0f,1.0f],right_rotation:[0.0f,0.0f,0.0f,1.0f],translation:[" + offsetX + "f," + offsetY + "f," + offsetZ + "f],scale:[" + sizeX + "f," + sizeY + "f," + sizeZ + "f]},Rotation:[" + rotate_horizontal + "f," + rotate_vertical + "f],block_state:{Name:\"" + id + "\"}}");

		}

	}

	public static class Command {

		public static void run (ServerLevel level_server, Vec3 vec3, String command) {
			
			/*
			(1.20.1) (1.21.1)
			level_server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, vec3, Vec2.ZERO, level_server, 4, "", Component.literal(""), level_server.getServer(), null).withSuppressedOutput(), command);
			(1.21.8)
			level_server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, vec3, Vec2.ZERO, level_server, PermissionSet.ALL_PERMISSIONS, "", Component.literal(""), level_server.getServer(), null).withSuppressedOutput(), command);
			*/
			level_server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, vec3, Vec2.ZERO, level_server, 4, "", Component.literal(""), level_server.getServer(), null).withSuppressedOutput(), command);

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

		public static boolean result (ServerLevel level_server, Vec3 vec3, String command) {

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
			level_server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(data_consumer, vec3, Vec2.ZERO, level_server, 4, "", Component.literal(""), level_server.getServer(), null), command);
			(1.21.8)
			level_server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(data_consumer, vec3, Vec2.ZERO, level_server, PermissionSet.ALL_PERMISSIONS, "", Component.literal(""), level_server.getServer(), null), command);
			*/
			level_server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(data_consumer, vec3, Vec2.ZERO, level_server, 4, "", Component.literal(""), level_server.getServer(), null), command);

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

		public static boolean test (BlockState block, String test) {

			if (test.equals("none") == true) {

				return false;

			} else if (test.equals("all") == true) {

				return true;

			} else {

				if (CacheManager.DataLogic.existNormal("test_block", block + " -> " + test) == false) {

					boolean result = false;

					{

						String[] data = Tile.toText(block);
						String block_id = data[0];
						List<String> properties = new ArrayList<>();

						if (data[1].isEmpty() == false) {

							properties = Arrays.stream(data[1].substring(1, data[1].length() - 1).split(",")).toList();

						}

						String value = "";
						int index = 0;
						String block_test = "";

						for (String split : test.split(" / ")) {

							result = true;

							for (String split2 : split.split(", ")) {

								value = split2.replaceAll("[#!]", "");

								{

									if (split2.startsWith("#") == true || split2.startsWith("!#") == true) {

										try {

											if (block.is(BlockTags.create(ResourceLocation.parse(value))) == false) {

												result = false;

											}

										} catch (Exception ignored) {

											result = false;

										}

									} else {

										index = value.indexOf("[");

										if (index == -1) {

											block_test = value;

											if (block_id.equals(block_test) == false) {

												result = false;

											}

										} else {

											block_test = value.substring(0, index);

											if (block_id.equals(block_test) == false) {

												result = false;

											} else {

												for (String property : value.substring(index + 1, value.length() - 1).split(",")) {

													if (properties.contains(property) == false) {

														result = false;
														break;

													}

												}

											}

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

					CacheManager.DataLogic.setNormal("test_block", block + " -> " + test, result);

				}

				return CacheManager.DataLogic.getNormal("test_block").get(block + " -> " + test);

			}

		}

		public static void set (LevelAccessor level_accessor, BlockPos pos, BlockState block, boolean is_world_gen) {

			// World Height Limit
			{

				if (Space.getBuildHeight(level_accessor, false) > pos.getY()) {

					return;

				} else if (Space.getBuildHeight(level_accessor, true) < pos.getY()) {

					return;

				}

			}

			// Waterlogged
			{

				if (level_accessor.isWaterAt(pos) == true) {

					block = Tile.setPropertyLogic(block, "waterlogged", true);

				}

			}

			int type = 0;

			if (is_world_gen == false) {

				type = 2;

			}

			level_accessor.setBlock(pos, block, type);

		}

		public static void remove (LevelAccessor level_accessor, ServerLevel level_server, BlockPos pos, boolean is_world_gen) {

			// World Height Limit
			{

				if (Space.getBuildHeight(level_accessor, false) > pos.getY()) {

					return;

				} else if (Space.getBuildHeight(level_accessor, true) < pos.getY()) {

					return;

				}

			}

			BlockState block = null;

			if (level_accessor.isWaterAt(pos) == true) {

				block = Blocks.WATER.defaultBlockState();

			} else {

				block = Blocks.AIR.defaultBlockState();

			}

			set(level_accessor, pos, block, is_world_gen);

			if (is_world_gen == false) {

				level_server.neighborChanged(pos.above(), level_server.getBlockState(pos.above()).getBlock(), pos);

			}

		}

		public static void removeDrop (LevelAccessor level_accessor, ServerLevel level_server, BlockPos pos) {

			Item.spawn(level_server, pos.getCenter(), level_accessor.getBlockState(pos).getBlock().asItem().getDefaultInstance());
			remove(level_accessor, level_server, pos, false);

		}

		public static BlockState fromText (String data) {

			BlockState block = null;

			// Get Block
			{

				Block get = null;
				String id = data;

				if (id.endsWith("}") == true) {

					id = id.substring(0, id.indexOf("{"));

				}

				if (id.endsWith("]") == true) {

					id = id.substring(0, id.indexOf("["));

				}

				/*
				(1.20.1)
				get = ForgeRegistries.BLOCKS.getValue(ResourceLocation.parse(id));
				(1.21.1)
				get = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(id));
				*/
				get = ForgeRegistries.BLOCKS.getValue(ResourceLocation.parse(id));

				if (get == null) {

					return Blocks.AIR.defaultBlockState();

				}

				block = get.defaultBlockState();

			}

			if (data.endsWith("}") == true) {

				data = data.substring(0, data.indexOf("{"));
				// TODO -> Put "{...}" from data into the block

			}

			if (data.endsWith("]") == true) {

				{

					String[] properties = data.substring(data.indexOf("[") + 1, data.length() - 1).split(",");

					for (String scan : properties) {

						String[] get = scan.split("=");
						Property<?> test = block.getBlock().getStateDefinition().getProperty(get[0]);

						if (test instanceof BooleanProperty == true) {

							block = setPropertyLogic(block, get[0], Boolean.parseBoolean(get[1]));

						} else if (test instanceof IntegerProperty == true) {

							block = setPropertyNumber(block, get[0], Integer.parseInt(get[1]));

						} else if (test instanceof EnumProperty<?> == true) {

							block = setPropertyCustom(block, get[0], get[1]);

						}

					}

				}

			}

			return block;

		}

		public static String[] toText (BlockState block) {

			String[] split = block.toString().substring("Block{".length()).split("}");

			if (split.length == 1) {

				split = new String[]{split[0], ""};

			}

			return split;

		}

		public static BlockState randomRotation (BlockState block) {

			if (Math.random() < 0.25) {

				return Tile.setPropertyCustom(block, "facing", "north");

			} else if (Math.random() < 0.25) {

				return Tile.setPropertyCustom(block, "facing", "west");

			} else if (Math.random() < 0.25) {

				return Tile.setPropertyCustom(block, "facing", "east");

			} else {

				return Tile.setPropertyCustom(block, "facing", "south");

			}

		}

		public static void setScheduleTick (ServerLevel level_server, BlockPos pos, int value) {

			level_server.scheduleTick(pos, level_server.getBlockState(pos).getBlock(), value);

		}

		public static boolean isPassable (LevelAccessor level_accessor, BlockPos pos) {

			return level_accessor.getBlockState(pos).getCollisionShape(level_accessor, pos).isEmpty() == true;

		}

		public static boolean getPropertyLogic (BlockState block, String name) {

			Property<?> property = block.getBlock().getStateDefinition().getProperty(name);

			if (property instanceof BooleanProperty == true) {

				return Boolean.parseBoolean(block.getValue(property).toString());

			}

			return false;

		}

		public static int getPropertyNumber (BlockState block, String name) {

			Property<?> property = block.getBlock().getStateDefinition().getProperty(name);

			if (property instanceof IntegerProperty == true) {

				return Integer.parseInt(block.getValue(property).toString());

			}

			return 0;

		}

		public static String getPropertyCustom (BlockState block, String name) {

			Property<?> property = block.getBlock().getStateDefinition().getProperty(name);

			if (property instanceof EnumProperty<?> == true) {

				return block.getValue(property).toString();

			}

			return "";

		}

		public static BlockState setPropertyLogic (BlockState block, String name, boolean value) {

			Property<?> property = block.getBlock().getStateDefinition().getProperty(name);

			if (block.hasProperty(property) == true) {

				if (property instanceof BooleanProperty property_instance) {

					if (property_instance.getValue(String.valueOf(value)).isPresent() == true) {

						block = block.setValue(property_instance, value);

					}

				}

			}

			return block;

		}

		public static BlockState setPropertyNumber (BlockState block, String name, int value) {

			Property<?> property = block.getBlock().getStateDefinition().getProperty(name);

			if (block.hasProperty(property) == true) {

				if (property instanceof IntegerProperty property_instance) {

					if (property_instance.getValue(String.valueOf(value)).isPresent() == true) {

						block = block.setValue(property_instance, value);

					}

				}

			}

			return block;

		}

		public static BlockState setPropertyCustom (BlockState block, String name, String value) {

			Property<?> property = block.getBlock().getStateDefinition().getProperty(name);

			if (property instanceof EnumProperty property_instance) {

				if (property_instance.getValue(value).isPresent() == true) {

					block = block.setValue(property_instance, (Enum) property_instance.getValue(value).get());

				}

			}

			return block;

		}

	}

	public static class Mob {

		public static List<Entity> getAtArea (ServerLevel level_server, Vec3 vec3, int distance, boolean is_box, int count, String id, String tag) {

			List<String> tags = Arrays.stream(tag.split(" / ")).toList();

			List<Entity> entities = level_server.getEntitiesOfClass(Entity.class, new AABB(vec3, vec3).inflate(distance), entity -> {

				boolean test = false;

				if (is_box == true || entity.position().distanceTo(vec3) <= distance) {

					if (id.isEmpty() == true || EntityType.getKey(entity.getType()).toString().equals(id) == true) {

						if (tag.isEmpty() == true || entity.getTags().containsAll(tags) == true) {

							test = true;

						}

					}

				}

				return test;

			});

			if (distance > 0) {

				entities = entities.stream().sorted(Comparator.comparingDouble(entity -> entity.position().distanceTo(vec3))).toList();

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

		public static Entity getAtAreaOne (ServerLevel level_server, Vec3 vec3, int distance, boolean is_box, String id, String tag) {

			List<Entity> entities = Mob.getAtArea(level_server, vec3, distance, is_box, 1, id, tag);

			if (entities.isEmpty() == false) {

				return entities.get(0);

			} else {

				return null;

			}

		}

		public static Entity getAtEverywhereOne (ServerLevel level_server, String id, String tag) {

			List<Entity> entities = Mob.getAtEverywhere(level_server, id, tag);

			if (entities.isEmpty() == false) {

				return entities.get(0);

			} else {

				return null;

			}

		}

		public static Entity summon (ServerLevel level_server, Vec3 vec3, String id, String name, String tag, String custom) {

			/*
			(1.20.1)
			Entity entity = ForgeRegistries.ENTITY_TYPES.getValue(ResourceLocation.parse(id)).create(level_server);
			(1.21.1)
			Entity entity = BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(id)).create(level_server);
			*/
			EntityType<?> type = ForgeRegistries.ENTITY_TYPES.getValue(ResourceLocation.parse(id));

			if (type == null) {

				return null;

			}

			Entity entity = type.create(level_server);

			if (entity == null) {

				return null;

			}

			if (custom.isEmpty() == false) {

				entity.load(Data.convertJSONToTag(custom));

			}

			entity.setCustomName(Data.convertJSONToComponent("[" + Data.createText(name) + "]"));

			if (name.contains(" / ") == true) {

				entity.setCustomNameVisible(true);

			}

			entity.addTag("TANNYJUNG");
			entity.addTag(Core.mod_id_big);

			for (String get : tag.split(" / ")) {

				entity.addTag(get);

			}

			entity.setPos(vec3);
			level_server.addFreshEntity(entity);

			return entity;

		}

		public static void summonWorldGen (ServerLevel level_server, Vec3 vec3, String id, String name, String tag, String custom) {

			level_server.getServer().execute(() -> {

				Mob.summon(level_server, vec3, id, name, tag, custom);

			});

		}

		public static boolean isCreativeMode (Entity entity) {

			if (entity instanceof Player player) {

				return player.getAbilities().instabuild;

			}

			return false;

		}

		public static boolean isSneaking (Entity entity) {

			if (entity instanceof Player player) {

				return player.isShiftKeyDown();

			}

			return false;

		}

	}

	public static class Item {

		public static ItemStack getSlot (Entity entity, EquipmentSlot equipment_slot) {

			if (entity instanceof LivingEntity living_entity) {

				return living_entity.getItemBySlot(equipment_slot);

			}

			return ItemStack.EMPTY;

		}

		public static boolean isTaggedAs (ItemStack item, String tag) {

			return item.is(ItemTags.create(ResourceLocation.parse(tag))) == true;

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

		public static void setCooldown (Entity entity, ItemStack item, int tick) {

			if (entity instanceof Player player) {

				player.getCooldowns().addCooldown(item.getItem(), tick);

			}

		}

		public static void addDamage (ItemStack item, int value) {

			item.setDamageValue(item.getDamageValue() + value);

			if (item.getMaxDamage() < item.getDamageValue()) {

				item.shrink(1);

			}

		}

		public static void spawn (ServerLevel level_server, Vec3 vec3, ItemStack item) {

			ItemEntity entityToSpawn = new ItemEntity(level_server, vec3.x, vec3.y, vec3.z, item);
			level_server.addFreshEntity(entityToSpawn);

		}

		public static String toID (ItemStack item) {

			String id = item.getDescriptionId();
			return id.substring(id.indexOf(".") + 1).replace(".", ":");

		}

		public static ItemStack fromID (String id) {

			/*
			(1.20.1)
			return ForgeRegistries.ITEMS.getValue(ResourceLocation.parse(id)).getDefaultInstance();
			(1.21.1)
			return BuiltInRegistries.ITEM.get(ResourceLocation.parse(id)).getDefaultInstance();
			*/
			return ForgeRegistries.ITEMS.getValue(ResourceLocation.parse(id)).getDefaultInstance();

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
			return new BlockPos(level_accessor.getLevelData().getXSpawn(), level_accessor.getLevelData().getYSpawn(), level_accessor.getLevelData().getZSpawn());

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

		public static boolean testChunkStatus (LevelAccessor level_accessor, ChunkPos chunk_pos, String status) {

			return level_accessor.hasChunk(chunk_pos.x, chunk_pos.z) == true && level_accessor.getChunk(chunk_pos.x, chunk_pos.z).getHighestGeneratedStatus().isOrAfter(ChunkStatus.byName(status)) == true;

		}

		public static void placeFeature (LevelAccessor level_accessor, BlockPos pos, String id) {

			WorldGenLevel level_world_gen = (WorldGenLevel) level_accessor;

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

		public static int getHeight (LevelAccessor level_accessor, int posX, int posZ, String type) {

			return level_accessor.getHeight(Heightmap.Types.valueOf(type), posX, posZ);

		}

		public static int getHeightWorldGen (LevelAccessor level_accessor, ServerLevel level_server, ChunkGenerator chunk_generator, int posX, int posZ, String type_normal, String type_world_gen) {

			BlockPos pos = new BlockPos(posX, 0, posZ);
			ChunkPos chunk_pos = new ChunkPos(pos);
			Heightmap.Types type = Heightmap.Types.valueOf(type_world_gen);

			if (level_accessor.hasChunk(chunk_pos.x, chunk_pos.z) == true) {

				if (level_accessor.getChunk(pos) instanceof ProtoChunk == true) {

					if (Space.testChunkStatus(level_accessor, chunk_pos, "carvers") == true) {

						return level_accessor.getChunk(chunk_pos.x, chunk_pos.z).getHeight(type, pos.getX(), pos.getZ()) + 1;

					}

				} else {

					return getHeight(level_accessor, posX, posZ, type_normal);

				}

			}

			return chunk_generator.getBaseHeight(posX, posZ, type, level_accessor, level_server.getChunkSource().randomState());

		}

	}

	public static class Environment {

		public static boolean test (Holder<Biome> biome, String test) {

			if (test.equals("none") == true) {

				return false;

			} else if (test.equals("all") == true) {

				return true;

			} else {

				String biome_centerID = toID(biome);

				if (CacheManager.DataLogic.existNormal("biome_test", biome + " -> " + test) == false) {

					boolean result = false;

					{

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

					CacheManager.DataLogic.setNormal("biome_test", biome + " -> " + test, result);

				}

				return CacheManager.DataLogic.getNormal("biome_test").get(biome + " -> " + test);

			}

		}

		public static String toID (Holder<Biome> biome) {

			String return_text = biome.toString().replace("Reference{ResourceKey[minecraft:worldgen/biome / ", "");
			return return_text.substring(0, return_text.indexOf("]"));

		}

		public static Holder<Biome> getAt (LevelAccessor level_accessor, BlockPos pos) {

			ChunkPos chunk_pos = new ChunkPos(pos);

			if (level_accessor.hasChunk(chunk_pos.x, chunk_pos.z) == true) {

				if (level_accessor.getChunk(pos) instanceof ProtoChunk == true) {

					if (Space.testChunkStatus(level_accessor, chunk_pos, "biomes") == true) {

						return level_accessor.getChunk(chunk_pos.x, chunk_pos.z).getNoiseBiome(pos.getX() >> 2, pos.getY() >> 2, pos.getZ() >> 2);

					}

				} else {

					return level_accessor.getBiome(pos);

				}

			}

			return level_accessor.getUncachedNoiseBiome(pos.getX() >> 2, pos.getY() >> 2, pos.getZ() >> 2);

		}

	}

	public static class GUI {

		public static String getTextBox (Player player, String name) {

			if (player.containerMenu instanceof MenuAccessor menu) {

				return menu.getMenuState(0, name, "");

			}

			return "";

		}

		public static void setTextBox (Player player, String name, String value) {

			if (player.containerMenu instanceof MenuAccessor menu) {

				menu.sendMenuStateUpdate(player, 0, name, value, true);

			}

		}

	}

	public static class Score {

		public static void create (ServerLevel level_server, String name) {

			Scoreboard scoreboard = level_server.getServer().getScoreboard();
			Objective objective = scoreboard.getObjective(name);

			if (objective != null) {

				return;

			}

			/*
            (1.20.1)
            scoreboard.addObjective(name, ObjectiveCriteria.DUMMY, Component.literal(name), ObjectiveCriteria.RenderType.INTEGER);
            (1.21.1)
            scoreboard.addObjective(name, ObjectiveCriteria.DUMMY, Component.literal(name), ObjectiveCriteria.RenderType.INTEGER, true, null);
            */
			scoreboard.addObjective(name, ObjectiveCriteria.DUMMY, Component.literal(name), ObjectiveCriteria.RenderType.INTEGER);

		}

		public static int get (ServerLevel level_server, String objective, String player) {

			ServerScoreboard score = level_server.getServer().getScoreboard();
			Objective objective_test = score.getObjective(objective);

			if (objective_test == null) {

				return 0;

			}

			/*
            (1.20.1)
            return score.getOrCreatePlayerScore(player, objective_test).getScore();
            (1.21.1)
            return score.getOrCreatePlayerScore(ScoreHolder.forNameOnly(player), objective_test, false).get();
            */
			return score.getOrCreatePlayerScore(player, objective_test).getScore();

		}

		public static void set (ServerLevel level_server, String objective, String player, int value) {

			ServerScoreboard score = level_server.getServer().getScoreboard();
			Objective objective_test = score.getObjective(objective);

			if (objective_test == null) {

				return;

			}

			/*
            (1.20.1)
            score.getOrCreatePlayerScore(player, objective_test).setScore(value);
            (1.21.1)
            score.getOrCreatePlayerScore(ScoreHolder.forNameOnly(player), objective_test, false).set(value);
            */
			score.getOrCreatePlayerScore(player, objective_test).setScore(value);

		}

		public static void add (ServerLevel level_server, String objective, String player, int value) {

			ServerScoreboard score = level_server.getServer().getScoreboard();
			Objective objective_test = score.getObjective(objective);

			if (objective_test == null) {

				return;

			}

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

	public static class Data {

		public static CompoundTag convertJSONToTag (String data) {

			try {

				return TagParser.parseTag(data);

			} catch (Exception ignored) {

				return new CompoundTag();

			}

		}

		public static MutableComponent convertJSONToComponent (String data) {

			try {

				/*
				(1.20.1)
				return Component.Serializer.fromJson(data);
				(1.21.1)
				return Component.Serializer.fromJson(data, RegistryAccess.EMPTY);
				*/
				return Component.Serializer.fromJson(data);

			} catch (Exception exception) {

				OutsideUtils.exception(new Exception(), exception, "");
				return MutableComponent.create(ComponentContents.EMPTY);

			}

		}

		public static String convertFileToForgeData (String path) {

			StringBuilder data = new StringBuilder();
			String[] split = null;
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

			for (String scan : FileManager.readTXT(path)) {

				if (scan.isEmpty() == false && scan.startsWith("---") == false) {

					split = scan.split(" = ");

					if (split[1].equals("none") == true) {

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

			return "{ForgeData:{" + Core.mod_id + ":{" + data + "}}}";

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
			String part_forge_data = "BlockEntityData:{ForgeData:{" + Core.mod_id + ":{" + forge_data + "}}},";
			(1.21.1)
			String part_name = "custom_name:\"" + createTextDoubleBackslash(name) + "\",";
			String part_lore = "lore:[\"" + createTextDoubleBackslash(lore) + "\"],";
			String part_custom_data = "custom_data:{" + custom_data + "},";
			String part_forge_data = "block_entity_data:{id:\"\",ForgeData:{" + Core.mod_id + ":{" + forge_data + "}}},";
			*/
			String part_name = "display:{Name:\"" + createTextDoubleBackslash(name) + "\"},";
			String part_lore = "Lore:[\"" + createTextDoubleBackslash(lore) + "\"],";
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
			String data = entity.getPersistentData().getCompound(Core.mod_id).getString(name);
			(1.21.8)
			String data = entity.getPersistentData().getCompound(Core.mod_id).getString(name).get();
			*/
			String data = entity.getPersistentData().getCompound(Core.mod_id).getString(name);

			if (data.isEmpty() == true) {

				return "none";

			}

			return data;

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

		public static String getBlockText (LevelAccessor level_accessor, BlockPos pos, String name) {

			return new Object() {

				public String getValue (LevelAccessor level_accessor, BlockPos pos, String name) {

					BlockEntity block_entity = level_accessor.getBlockEntity(pos);

					if (block_entity == null) {

                        return "";

					}

					/*
                    (1.20.1) (1.21.1)
                    return block_entity.getPersistentData().getCompound(Core.mod_id).getString(name);
                    (1.21.8)
                    return block_entity.getPersistentData().getCompound(Core.mod_id).getString(name).get();
                    */
					return block_entity.getPersistentData().getCompound(Core.mod_id).getString(name);

				}

			}.getValue(level_accessor, pos, name);

		}

		public static double getBlockNumber (LevelAccessor level_accessor, BlockPos pos, String name) {

			return new Object() {

				public double getValue (LevelAccessor level_accessor, BlockPos pos, String name) {

					BlockEntity block_entity = level_accessor.getBlockEntity(pos);

					if (block_entity == null) {

                        return 0.0;

					}

					/*
                    (1.20.1) (1.21.1)
                    return block_entity.getPersistentData().getCompound(Core.mod_id).getDouble(name);
                    (1.21.8)
                    return block_entity.getPersistentData().getCompound(Core.mod_id).getDouble(name).get();
                    */
					return block_entity.getPersistentData().getCompound(Core.mod_id).getDouble(name);

				}

			}.getValue(level_accessor, pos, name);

		}

		public static boolean getBlockLogic (LevelAccessor level_accessor, BlockPos pos, String name) {

			return new Object() {

				public boolean getValue (LevelAccessor level_accessor, BlockPos pos, String name) {

					BlockEntity blockEntity = level_accessor.getBlockEntity(pos);

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

			}.getValue(level_accessor, pos, name);

		}

		public static void setBlockText (LevelAccessor level_accessor, ServerLevel level_server, BlockPos pos, String name, String value) {

			BlockEntity block_entity = level_accessor.getBlockEntity(pos);

			if (block_entity != null) {

				CompoundTag tag = new CompoundTag();
				CompoundTag tag_add = new CompoundTag();
				tag_add.putString(name, value);
				tag.put(Core.mod_id, tag_add);
				block_entity.getPersistentData().merge(tag);
				BlockState block = level_accessor.getBlockState(pos);
				level_server.sendBlockUpdated(pos, block, block, 2);

			}

		}

		public static void setBlockLogic (LevelAccessor level_accessor, ServerLevel level_server, BlockPos pos, String name, boolean value) {

			BlockEntity block_entity = level_accessor.getBlockEntity(pos);

			if (block_entity != null) {

				CompoundTag tag = new CompoundTag();
				CompoundTag tag_add = new CompoundTag();
				tag_add.putBoolean(name, value);
				tag.put(Core.mod_id, tag_add);
				block_entity.getPersistentData().merge(tag);
				BlockState block = level_accessor.getBlockState(pos);
				level_server.sendBlockUpdated(pos, block, block, 2);

			}

		}

		public static void setBlockNumber (LevelAccessor level_accessor, ServerLevel level_server, BlockPos pos, String name, double value) {

			BlockEntity block_entity = level_accessor.getBlockEntity(pos);

			if (block_entity != null) {

				CompoundTag tag = new CompoundTag();
				CompoundTag tag_add = new CompoundTag();
				tag_add.putDouble(name, value);
				tag.put(Core.mod_id, tag_add);
				block_entity.getPersistentData().merge(tag);
				BlockState block = level_accessor.getBlockState(pos);
				level_server.sendBlockUpdated(pos, block, block, 2);

			}

		}

		public static void addBlockNumber (LevelAccessor level_accessor, ServerLevel level_server, BlockPos pos, String name, double value) {

			BlockEntity block_entity = level_accessor.getBlockEntity(pos);

			if (block_entity != null) {

				/*
				(1.20.1) (1.21.1)
				block_entity.getPersistentData().getCompound(Core.mod_id).putDouble(name, block_entity.getPersistentData().getCompound(Core.mod_id).getDouble(name) + value);
				(1.21.8)
				block_entity.getPersistentData().getCompound(Core.mod_id).putDouble(name, block_entity.getPersistentData().getCompound(Core.mod_id).getDouble(name).get() + value);
				*/
				block_entity.getPersistentData().getCompound(Core.mod_id).putDouble(name, block_entity.getPersistentData().getCompound(Core.mod_id).getDouble(name) + value);

				BlockState block = level_accessor.getBlockState(pos);
				level_server.sendBlockUpdated(pos, block, block, 2);

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
			CustomData.update(DataComponents.CUSTOM_DATA, Item.getSlot(entity, slot), create -> test.merge(tag));
			*/
			Item.getSlot(entity, slot).getOrCreateTag().merge(tag);

		}

	}

}