package tannyjung.core;

import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Objective;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.Comparator;
import java.util.List;

public class GameUtils {

	public static class misc {

		public static void sendChatMessage (LevelAccessor level, String target, String color, String text) {

			if (level == null) {

				return;

			}

			level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, level instanceof ServerLevel ? (ServerLevel) level : null, 4, "", Component.literal(""), level.getServer(), null).withSuppressedOutput(), "tellraw " + target + " [{\"text\":\"" + text + "\",\"color\":\"" + color + "\"}]");

		}

		public static int playerCount (LevelAccessor level) {

			if (level.isClientSide() == true) {

				return Minecraft.getInstance().getConnection().getOnlinePlayers().size();

			} else {

				return ServerLifecycleHooks.getCurrentServer().getPlayerCount();

			}

		}

		public static String summonEntity (String id, String tag, String name, String custom) {

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

		public static String getCurrentDimensionID (ServerLevel world) {

			return world.dimension().location().toString();

		}

		public static List<Entity> getEntitiesAt (LevelAccessor level, int posX, int posY, int posZ) {

			Vec3 center = new Vec3((posX + 0.5), (posY + 0.5), (posZ + 0.5));
			return level.getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(3 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(center))).toList();

		}

	}

	public static class command {

		public static void run (LevelAccessor level, double posX, double posY, double posZ, String command) {

			if (level instanceof ServerLevel world) {

				world.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(posX, posY, posZ), Vec2.ZERO, world, 4, "", Component.literal(""), world.getServer(), null).withSuppressedOutput(), command);

			}

		}

		public static void runEntity (Entity entity, String command) {

			if (entity.level() instanceof ServerLevel world) {

				entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), world, 4, entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), command);

			}

		}

		public static boolean result (LevelAccessor level, int posX, int posY, int posZ, String command) {

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

			if (level instanceof ServerLevel world)
				world.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(data_consumer, new Vec3(posX, posY, posZ), Vec2.ZERO, world, 4, "", Component.literal(""), world.getServer(), null), command);

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

			entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(data_consumer, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4, entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), command);
			return result.toString().equals("pass");

		}

		public static String resultCustom (LevelAccessor level, int posX, int posY, int posZ, String command) {

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

			if (level instanceof ServerLevel world)
				world.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(data_consumer, new Vec3(posX, posY, posZ), Vec2.ZERO, world, 4, "", Component.literal(""), world.getServer(), null), command);
			return result.toString();

		}

	}

	public static class score {

		public static int get (LevelAccessor level, String objective, String player) {

			ServerScoreboard score = level.getServer().getScoreboard();
			Objective objective_get = score.getObjective(objective);

			return score.getOrCreatePlayerScore(player, objective_get).getScore();

		}

		public static void set (LevelAccessor level, String objective, String player, int value) {

			ServerScoreboard score = level.getServer().getScoreboard();
			Objective objective_get = score.getObjective(objective);

			score.getOrCreatePlayerScore(player, objective_get).setScore(value);

		}

		public static void add (LevelAccessor level, String objective, String player, int value) {

			ServerScoreboard score = level.getServer().getScoreboard();
			Objective objective_get = score.getObjective(objective);
			int old_value = get(level, objective, player);

			score.getOrCreatePlayerScore(player, objective_get).setScore(old_value + value);

		}

	}

	public static class biome {

		public static String toID(Holder<net.minecraft.world.level.biome.Biome> biome) {

			String return_text = biome.toString().replace("Reference{ResourceKey[minecraft:worldgen/biome / ", "");
			return return_text.substring(0, return_text.indexOf("]"));

		}

		public static boolean isTaggedAs(Holder<net.minecraft.world.level.biome.Biome> biome, String tag) {

			try {

				return biome.is(TagKey.create(Registries.BIOME, ResourceLocation.parse(tag)));

			} catch (Exception exception) {

				MiscUtils.exception(new Exception(), exception);

			}

			return false;

		}

	}

	public static class block {

		public static boolean isTaggedAs (BlockState block, String tag) {

			try {

				return block.is(BlockTags.create(ResourceLocation.parse(tag)));

			} catch (Exception exception) {

				MiscUtils.exception(new Exception(), exception);

			}

			return false;

		}

		public static BlockState fromText (String data) {

			BlockState block = Blocks.AIR.defaultBlockState();

			if (data.endsWith("]") == true) {

				block = ForgeRegistries.BLOCKS.getValue(ResourceLocation.parse(data.substring(0, data.indexOf("[")))).defaultBlockState();
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

			} else {

				block = ForgeRegistries.BLOCKS.getValue(ResourceLocation.parse(data)).defaultBlockState();

			}

			return block;

		}

		public static String toText (BlockState block) {

			return block.toString().replace("Block{", "").replace("}", "");

		}

		public static String toTextID (BlockState block) {

			String return_text = toText(block);

			if (return_text.endsWith("]") == true) {

				return_text = return_text.substring(0, return_text.indexOf("["));

			}

			return return_text;

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

			if (property instanceof BooleanProperty property_instance) {

				block = block.setValue(property_instance, value);

			}

			return block;

		}

		public static BlockState propertyIntegerSet (BlockState block, String name, int value) {

			Property<?> property = block.getBlock().getStateDefinition().getProperty(name);

			if (property instanceof IntegerProperty property_instance) {

				block = block.setValue(property_instance, value);

			}

			return block;

		}

		public static BlockState propertyEnumSet (BlockState block, String name, String value) {

			Property<?> property = block.getBlock().getStateDefinition().getProperty(name);

			if (property instanceof EnumProperty property_instance) {

				block = block.setValue(property_instance, value);

			}

			return block;

		}


	}

	public static class NBT {

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

			public static String getText (LevelAccessor level, BlockPos pos, String name) {

				return new Object() {

					public String getValue (LevelAccessor level, BlockPos pos, String name) {

						BlockEntity blockEntity = level.getBlockEntity(pos);

						if (blockEntity != null) {

							return blockEntity.getPersistentData().getString(name);

						}

						return "";

					}

				}.getValue(level, pos, name);

			}

			public static double getNumber (LevelAccessor level, BlockPos pos, String name) {

				return new Object() {

					public double getValue (LevelAccessor level, BlockPos pos, String name) {

						BlockEntity blockEntity = level.getBlockEntity(pos);

						if (blockEntity != null) {

							return blockEntity.getPersistentData().getDouble(name);

						}

						return 0.0;

					}

				}.getValue(level, pos, name);

			}

			public static void setText (LevelAccessor level, BlockPos pos, String name, String value) {

				BlockEntity block_entity = level.getBlockEntity(pos);

				if (block_entity != null) {

					block_entity.getPersistentData().putString(name, value);

					if (level instanceof Level level_fix) {

						BlockState block = level.getBlockState(pos);
						level_fix.sendBlockUpdated(pos, block, block, 2);

					}

				}

			}

			public static void setNumber (LevelAccessor level, BlockPos pos, String name, double value) {

				BlockEntity block_entity = level.getBlockEntity(pos);

				if (block_entity != null) {

					block_entity.getPersistentData().putDouble(name, value);

					if (level instanceof Level level_fix) {

						BlockState block = level.getBlockState(pos);
						level_fix.sendBlockUpdated(pos, block, block, 2);

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

}