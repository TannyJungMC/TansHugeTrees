package tannyjung.core;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Objective;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Comparator;
import java.util.List;

public class GameUtils {

    public static class GroupPosBlockCommand {

        public final BlockPos pos;
        public final BlockState block;
        public final String command;

        public GroupPosBlockCommand (BlockPos pos, BlockState block, String command) {

            this.pos = pos;
            this.block = block;
            this.command = command;

        }

    }

	public static class outside {

		public static String quardtreeChunkToNode (int chunkX, int chunkZ) {

			StringBuilder return_text = new StringBuilder();

			{

				int localX = chunkX & 31;
				int localZ = chunkZ & 31;

				for (int level = 1; level <= 2; level++) {

					int size = 32 >> level;
					int posX = (localX / size) % 2;
					int posZ = (localZ / size) % 2;

					if (posX == 0 && posZ == 0) return_text.append("-NW");
					else if (posX == 1 && posZ == 0) return_text.append("-NE");
					else if (posX == 0) return_text.append("-SW");
					else return_text.append("-SE");

				}

			}

			return return_text.substring(1);

		}

		public static int[] posRotationMirrored (int posX, int posZ, int rotation, boolean mirrored) {

            {

                if (mirrored == true) {

                    posX = posX * (-1);

                }

                if (rotation == 2) {

                    int posX_save = posX;
                    posX = posZ;
                    posZ = posX_save * (-1);

                } else if (rotation == 3) {

                    posX = posX * (-1);
                    posZ = posZ * (-1);

                } else if (rotation == 4) {

                    int posX_save = posX;
                    int posZ_save = posZ;
                    posX = posZ_save * (-1);
                    posZ = posX_save;

                }

            }

			return new int[]{posX, posZ};

		}

		public static boolean configTestBiome (Holder<Biome> biome_center, String config_value) {

			boolean return_logic = false;

			{

				String biome_centerID = biome.toID(biome_center);

				for (String split : config_value.split(" / ")) {

					return_logic = true;

					for (String split2 : split.split(", ")) {

						String split_get = split2.replaceAll("[#!]", "");

						{

							if (split2.contains("#") == false) {

								if (biome_centerID.equals(split_get) == false) {

									return_logic = false;

								}

							} else {

								if (biome.isTaggedAs(biome_center, split_get) == false) {

									return_logic = false;

								}

							}

							if (split2.contains("!") == true) {

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

		public static boolean configTestBlock (BlockState ground_block, String config_value) {

			boolean return_logic = false;

			{

				for (String split : config_value.split(" / ")) {

					return_logic = true;

					for (String split2 : split.split(", ")) {

						String split_get = split2.replaceAll("[#!]", "");

						{

							if (split2.contains("#") == false) {

								if (ForgeRegistries.BLOCKS.getKey(ground_block.getBlock()).toString().equals(split_get) == false) {

									return_logic = false;

								}

							} else {

								if (block.isTaggedAs(ground_block, split_get) == false) {

									return_logic = false;

								}

							}

							if (split2.contains("!") == true) {

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

		public static String getForgeDataFromGiveFile (String path) {

			String return_text = "";
			File file = new File(path);

			if (file.exists() == true && file.isDirectory() == false) {

				StringBuilder data = new StringBuilder();

				// Read File
				{

					try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file), 65536); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

						{

							data.append(read_all);

						}

					} buffered_reader.close(); } catch (Exception exception) { OutsideUtils.exception(new Exception(), exception); }

				}

				return_text = data.substring(data.indexOf("ForgeData"), data.length() - 2);

			}

			return return_text;

		}

	}

	public static class misc {

		public static void sendChatMessage (ServerLevel level_server, String target, String color, String text) {

			level_server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, level_server, 4, "", Component.literal(""), level_server.getServer(), null).withSuppressedOutput(), "tellraw " + target + " [{\"text\":\"" + text + "\",\"color\":\"" + color + "\"}]");

		}

		public static int playerCount () {

			return ServerLifecycleHooks.getCurrentServer().getPlayerCount();

		}

		public static String getCurrentDimensionID (Level level) {

			return level.dimension().location().toString();

		}

	}

	public static class command {

		public static void run (ServerLevel level_server, double posX, double posY, double posZ, String command) {

			level_server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(posX, posY, posZ), Vec2.ZERO, level_server, 4, "", Component.literal(""), level_server.getServer(), null).withSuppressedOutput(), command);

		}

		public static void runEntity (Entity entity, String command) {

			if (entity.level() instanceof ServerLevel level_server) {

				level_server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), level_server, 4, entity.getName().getString(), entity.getDisplayName(), level_server.getServer(), entity), command);

			}

		}

		public static boolean result (ServerLevel level_server, int posX, int posY, int posZ, String command) {

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

		public static String resultCustom (ServerLevel level_server, int posX, int posY, int posZ, String command) {

			StringBuilder result = new StringBuilder();

			CommandSource data_consumer = new CommandSource() {

				@Override
				public void sendSystemMessage(Component component) {

					result.append(component);

				}

				@Override
				public boolean acceptsSuccess() {
					return true;
				}

				@Override
				public boolean acceptsFailure() {
					return true;
				}

				@Override
				public boolean shouldInformAdmins() {
					return false;
				}

			};

			level_server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(data_consumer, new Vec3(posX, posY, posZ), Vec2.ZERO, level_server, 4, "", Component.literal(""), level_server.getServer(), null), command);
			return result.toString();

		}

	}

	public static class score {

		public static int get (ServerLevel level_server, String objective, String player) {

			ServerScoreboard score = level_server.getServer().getScoreboard();
			Objective objective_get = score.getObjective(objective);

			return score.getOrCreatePlayerScore(player, objective_get).getScore();

		}

		public static void set (ServerLevel level_server, String objective, String player, int value) {

			ServerScoreboard score = level_server.getServer().getScoreboard();
			Objective objective_get = score.getObjective(objective);

			score.getOrCreatePlayerScore(player, objective_get).setScore(value);

		}

		public static void add (ServerLevel level_server, String objective, String player, int value) {

			ServerScoreboard score = level_server.getServer().getScoreboard();
			Objective objective_get = score.getObjective(objective);
			int old_value = get(level_server, objective, player);

			score.getOrCreatePlayerScore(player, objective_get).setScore(old_value + value);

		}

	}

	public static class biome {

		public static String toID (Holder<Biome> biome) {

			String return_text = biome.toString().replace("Reference{ResourceKey[minecraft:worldgen/biome / ", "");
			return return_text.substring(0, return_text.indexOf("]"));

		}

		public static boolean isTaggedAs (Holder<Biome> biome, String tag) {

			try {

				return biome.is(TagKey.create(Registries.BIOME, ResourceLocation.parse(tag)));

			} catch (Exception exception) {

				OutsideUtils.exception(new Exception(), exception);

			}

			return false;

		}

	}

	public static class block {

		public static boolean isTaggedAs (BlockState block, String tag) {

			try {

				return block.is(BlockTags.create(ResourceLocation.parse(tag)));

			} catch (Exception exception) {

				OutsideUtils.exception(new Exception(), exception);

			}

			return false;

		}

		public static BlockState fromText (String data) {

			BlockState block = Blocks.AIR.defaultBlockState();

			try {

				String id = data;

				// Get Block ID
				{

					if (data.endsWith("}") == true) {

						id = data.substring(0, data.indexOf("{"));

					}

					if (data.endsWith("]") == true) {

						id = data.substring(0, data.indexOf("["));

					}

					block = ForgeRegistries.BLOCKS.getValue(ResourceLocation.parse(id)).defaultBlockState();

				}

				if (data.endsWith("}") == true) {

					data = data.substring(0, data.indexOf("{"));

				}

				if (data.endsWith("]") == true) {

					String[] properties = data.substring(data.indexOf("[") + 1, data.length() - 1).split(",");

					for (String property_data : properties) {

						String[] get = property_data.split("=");
						Property<?> property = block.getBlock().getStateDefinition().getProperty(get[0]);

						if (property instanceof BooleanProperty) {

							block = propertyBooleanSet(block, get[0], Boolean.parseBoolean(get[1]));

						} else if (property instanceof IntegerProperty) {

							block = propertyIntegerSet(block, get[0], Integer.parseInt(get[1]));

						} else if (property instanceof EnumProperty<?>) {

							block = propertyEnumSet(block, get[0], get[1]);

						}

					}

				}

			} catch (Exception exception) {

				OutsideUtils.exception(new Exception(), exception);

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

		public static String textFromItemText (String text) {

			String id = text.substring(0, text.indexOf("{"));
			String convert = text.substring(text.indexOf("ForgeData"), text.length() - 1);
			return id + "{" + convert;

		}

		public static boolean propertyBooleanGet (BlockState block, String name) {

			Property<?> property = block.getBlock().getStateDefinition().getProperty(name);

			if (property instanceof BooleanProperty) {

				return Boolean.parseBoolean(block.getValue(property).toString());

			}

			return false;

		}

		public static int propertyIntegerGet (BlockState block, String name) {

			Property<?> property = block.getBlock().getStateDefinition().getProperty(name);

			if (property instanceof IntegerProperty) {

				return Integer.parseInt(block.getValue(property).toString());

			}

			return 0;

		}

		public static String propertyEnumGet (BlockState block, String name) {

			Property<?> property = block.getBlock().getStateDefinition().getProperty(name);

			if (property instanceof EnumProperty<?>) {

				return block.getValue(property).toString();

			}

			return "";

		}

		public static BlockState propertyBooleanSet (BlockState block, String name, boolean value) {

			Property<?> property = block.getBlock().getStateDefinition().getProperty(name);

            if (block.hasProperty(property) == true) {

                if (property instanceof BooleanProperty property_instance) {

                    block = block.setValue(property_instance, value);

                }

            }

			return block;

		}

		public static BlockState propertyIntegerSet (BlockState block, String name, int value) {

			Property<?> property = block.getBlock().getStateDefinition().getProperty(name);

            if (block.hasProperty(property) == true) {

                if (property instanceof IntegerProperty property_instance) {

                    block = block.setValue(property_instance, value);

                }

            }

			return block;

		}

		public static BlockState propertyEnumSet (BlockState block, String name, String value) {

			if (name.equals("facing") == true) {

				if (block.hasProperty(DirectionalBlock.FACING) == true) {

					block = block.setValue(DirectionalBlock.FACING, Direction.valueOf(value.toUpperCase()));

				} else if (block.hasProperty(HorizontalDirectionalBlock.FACING) == true) {

					block = block.setValue(HorizontalDirectionalBlock.FACING, Direction.valueOf(value.toUpperCase()));

				}

			}

			return block;

		}


	}

	public static class entity {

		public static String summonCommand (String id, String tag, String name, String custom) {

			StringBuilder return_text = new StringBuilder();

			return_text
					.append("summon ")
					.append(id)
					.append(" ~ ~ ~ {Tags:[\"")
			;

			if (tag.equals("") == false) {

				return_text.append(tag.replace(" / ", "\",\""));

			}

			return_text.append("\"]");

			if (name.equals("") == false) {

				return_text
						.append(",CustomName:'{\"text\":\"")
						.append(name)
						.append("\"}'")
				;

			}

			if (custom.equals("") == false) {

				return_text
						.append(",")
						.append(custom)
				;

			}

			return return_text + "}";

		}

		public static List<Entity> getAllAt (Level level, int posX, int posY, int posZ) {

			Vec3 center = new Vec3((posX + 0.5), (posY + 0.5), (posZ + 0.5));
			return level.getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(3 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(center))).toList();

		}

		public static boolean isCreativeMode (Entity entity) {

			if (entity instanceof Player player) {

				return player.getAbilities().instabuild;

			}

			return false;

		}

		public static ItemStack itemGet (Entity entity, EquipmentSlot equipment_slot) {

			if (entity instanceof LivingEntity living_entity) {

				return living_entity.getItemBySlot(equipment_slot);

			}

			return ItemStack.EMPTY;

		}

		public static void itemCountSet (Entity entity, EquipmentSlot equipment_slot, int value) {

			if (entity instanceof LivingEntity living_entity) {

				ItemStack item = living_entity.getItemBySlot(equipment_slot);
				item.setCount(value);

			}

		}

		public static void itemCountAdd (Entity entity, EquipmentSlot equipment_slot, int value) {

			if (entity instanceof LivingEntity living_entity) {

				ItemStack item = living_entity.getItemBySlot(equipment_slot);
				item.setCount(item.getCount() + value);

			}

		}

	}

	public static class nbt {

		public static class entity {

			public static String getText (Entity entity, String name) {

				return entity.getPersistentData().getString(name);

			}

			public static Boolean getLogic (Entity entity, String name) {

				return entity.getPersistentData().getBoolean(name);

			}

			public static double getNumber (Entity entity, String name) {

				return entity.getPersistentData().getDouble(name);

			}

			public static double[] getListNumber (Entity entity, String name) {

				ListTag list = entity.getPersistentData().getList(name, Tag.TAG_DOUBLE);
				double[] convert = new double[list.size()];

				for (int count = 0; count <= list.size() - 1; count++) {

					convert[count] = list.getDouble(count);

				}

				return convert;

			}

			public static double[] getListNumberFloat (Entity entity, String name) {

				ListTag list = entity.getPersistentData().getList(name, Tag.TAG_FLOAT);
				double[] convert = new double[list.size()];

				for (int count = 0; count <= list.size() - 1; count++) {

					convert[count] = list.getFloat(count);

				}

				return convert;

			}

			public static void setText (Entity entity, String name, String value) {

				entity.getPersistentData().putString(name, value);

			}

			public static void setLogic (Entity entity, String name, boolean value) {

				entity.getPersistentData().putBoolean(name, value);

			}

			public static void setNumber (Entity entity, String name, double value) {

				entity.getPersistentData().putDouble(name, value);

			}

			public static void addNumber (Entity entity, String name, double value) {

				entity.getPersistentData().putDouble(name, entity.getPersistentData().getDouble(name) + value);

			}

		}

		public static class block {

			public static String getText (LevelAccessor level_accessor, BlockPos pos, String name) {

				return new Object() {

					public String getValue (LevelAccessor level_accessor, BlockPos pos, String name) {

						BlockEntity blockEntity = level_accessor.getBlockEntity(pos);

						if (blockEntity != null) {

							return blockEntity.getPersistentData().getString(name);

						}

						return "";

					}

				}.getValue(level_accessor, pos, name);

			}

			public static double getNumber (LevelAccessor level_accessor, BlockPos pos, String name) {

				return new Object() {

					public double getValue (LevelAccessor level_accessor, BlockPos pos, String name) {

						BlockEntity blockEntity = level_accessor.getBlockEntity(pos);

						if (blockEntity != null) {

							return blockEntity.getPersistentData().getDouble(name);

						}

						return 0.0;

					}

				}.getValue(level_accessor, pos, name);

			}

			public static boolean getLogic (LevelAccessor level_accessor, BlockPos pos, String name) {

				return new Object() {

					public boolean getValue (LevelAccessor level_accessor, BlockPos pos, String name) {

						BlockEntity blockEntity = level_accessor.getBlockEntity(pos);

						if (blockEntity != null) {

							return blockEntity.getPersistentData().getBoolean(name);

						}

						return false;

					}

				}.getValue(level_accessor, pos, name);

			}

			public static void setText (LevelAccessor level_accessor, BlockPos pos, String name, String value) {

				BlockEntity block_entity = level_accessor.getBlockEntity(pos);

				if (block_entity != null) {

					block_entity.getPersistentData().putString(name, value);
					BlockState block = level_accessor.getBlockState(pos);

					if (level_accessor instanceof ServerLevel level_server) {

						level_server.sendBlockUpdated(pos, block, block, 2);

					}

				}

			}

			public static void setNumber (LevelAccessor level_accessor, BlockPos pos, String name, double value) {

				BlockEntity block_entity = level_accessor.getBlockEntity(pos);

				if (block_entity != null) {

					block_entity.getPersistentData().putDouble(name, value);
					BlockState block = level_accessor.getBlockState(pos);

					if (level_accessor instanceof ServerLevel level_server) {

						level_server.sendBlockUpdated(pos, block, block, 2);

					}

				}

			}

			public static void setLogic (LevelAccessor level_accessor, BlockPos pos, String name, boolean value) {

				BlockEntity block_entity = level_accessor.getBlockEntity(pos);

				if (block_entity != null) {

					block_entity.getPersistentData().putBoolean(name, value);
					BlockState block = level_accessor.getBlockState(pos);

					if (level_accessor instanceof ServerLevel level_server) {

						level_server.sendBlockUpdated(pos, block, block, 2);

					}

				}

			}

			public static void addNumber (LevelAccessor level_accessor, BlockPos pos, String name, double value) {

				BlockEntity block_entity = level_accessor.getBlockEntity(pos);

				if (block_entity != null) {

					block_entity.getPersistentData().putDouble(name, block_entity.getPersistentData().getDouble(name) + value);
					BlockState block = level_accessor.getBlockState(pos);

					if (level_accessor instanceof ServerLevel level_server) {

						level_server.sendBlockUpdated(pos, block, block, 2);

					}

				}

			}

		}

		public static CompoundTag textToCompoundTag (String id) {

			CompoundTag return_NBT = new CompoundTag();

			{

				try {

					return_NBT = TagParser.parseTag(id.substring(id.indexOf("{")));

				} catch (Exception ignored) {



				}

			}

			return return_NBT;

		}

	}

}