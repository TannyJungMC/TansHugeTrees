package tannyjung.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
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
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Objective;
import net.minecraftforge.server.ServerLifecycleHooks;
import tannyjung.tanshugetrees.TanshugetreesMod;

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

			return_text.append("summon ")
					.append(id)
					.append(" ~ ~ ~ {Tags:[\"")
					.append(TanshugetreesMod.MODID.toUpperCase())
			;

			if (tag.equals("") == false) {

				return_text
						.append("\",\"")
						.append(TanshugetreesMod.MODID.toUpperCase())
						.append("-")
						.append(tag.replace(" / ", "\",\"" + TanshugetreesMod.MODID.toUpperCase() + "-"))
				;

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

		public static int get (LevelAccessor level, String player) {

			ServerScoreboard score = level.getServer().getScoreboard();
			Objective objective = score.getObjective(TanshugetreesMod.MODID.toUpperCase());

			return score.getOrCreatePlayerScore(player, objective).getScore();

		}

		public static void set (LevelAccessor level, String player, int value) {

			ServerScoreboard score = level.getServer().getScoreboard();
			Objective objective = score.getObjective(TanshugetreesMod.MODID.toUpperCase());

			score.getOrCreatePlayerScore(player, objective).setScore(value);

		}

		public static void add (LevelAccessor level, String player, int value) {

			ServerScoreboard score = level.getServer().getScoreboard();
			Objective objective = score.getObjective(TanshugetreesMod.MODID.toUpperCase());
			int old_value = get(level, player);

			score.getOrCreatePlayerScore(player, objective).setScore(old_value + value);

		}

	}

	public static class biome {

		public static String toID(Holder<net.minecraft.world.level.biome.Biome> biome) {

			String return_text = biome.toString().replace("Reference{ResourceKey[minecraft:worldgen/biome / ", "");
			return return_text.substring(0, return_text.indexOf("]"));

		}

		public static boolean isTaggedAs(Holder<net.minecraft.world.level.biome.Biome> biome, String tag) {

			try {

				return biome.is(TagKey.create(Registries.BIOME, new ResourceLocation(tag)));

			} catch (Exception ignored) {

			}

			return false;

		}

	}

	public static class block {

		public static boolean isTaggedAs (BlockState block, String tag) {

			try {

				return block.is(BlockTags.create(new ResourceLocation(tag)));

			} catch (Exception ignored) {

			}

			return false;

		}

		public static BlockState fromText (String id) {

			try {

				return BlockStateParser.parseForBlock(BuiltInRegistries.BLOCK.asLookup(), id, true).blockState();

			} catch (Exception ignored) {

			}

			return Blocks.AIR.defaultBlockState();

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

		public static boolean propertyBooleanGet (BlockState block, String property) {

			return block.getBlock().getStateDefinition().getProperty(property) instanceof BooleanProperty get && block.getValue(get);

		}

		public static BlockState propertyBooleanSet (BlockState block, String property, boolean value) {

			return block.getBlock().getStateDefinition().getProperty(property) instanceof BooleanProperty set
					? block.setValue(set, value)
					: block;

		}

	}

	public static class NBT {

		public static class entity {

			public static String getText (net.minecraft.world.entity.Entity entity, String name) {

				return entity.getPersistentData().getString(name);

			}

			public static Boolean getLogic (net.minecraft.world.entity.Entity entity, String name) {

				return entity.getPersistentData().getBoolean(name);

			}

			public static double getNumber (net.minecraft.world.entity.Entity entity, String name) {

				return entity.getPersistentData().getDouble(name);

			}

			public static ListTag getListNumber (net.minecraft.world.entity.Entity entity, String name) {

				return entity.getPersistentData().getList(name, Tag.TAG_DOUBLE);

			}

			public static void setText (net.minecraft.world.entity.Entity entity, String name, String value) {

				entity.getPersistentData().putString(name, value);

			}

			public static void setLogic (net.minecraft.world.entity.Entity entity, String name, boolean value) {

				entity.getPersistentData().putBoolean(name, value);

			}

			public static void setNumber (net.minecraft.world.entity.Entity entity, String name, double value) {

				entity.getPersistentData().putDouble(name, value);

			}

			public static void addNumber (net.minecraft.world.entity.Entity entity, String name, double value) {

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

		}

	}

}