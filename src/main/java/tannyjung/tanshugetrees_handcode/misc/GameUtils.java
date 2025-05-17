package tannyjung.tanshugetrees_handcode.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Objective;
import net.minecraftforge.server.ServerLifecycleHooks;
import tannyjung.tanshugetrees.TanshugetreesMod;

public class GameUtils {

	// --------------------------------------------------
	// Misc
	// --------------------------------------------------

	public static int playerCount (LevelAccessor level) {

		if (level.isClientSide() == true) {

			return Minecraft.getInstance().getConnection().getOnlinePlayers().size();

		} else {

			return ServerLifecycleHooks.getCurrentServer().getPlayerCount();

		}

    }

	public static String summonEntity (String id, String tag, String name, String name_color, String custom) {

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
					.append(tag.replace(" / ", "\",\""))
			;

		}

		return_text.append("\"]");

		if (name.equals("") == false) {

			return_text
					.append(",CustomName:'{\"text\":\"")
					.append(TanshugetreesMod.MODID.toUpperCase())
					.append("-")
					.append(name)
					.append("\",\"color\":\"")
					.append(name_color)
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

	// --------------------------------------------------
	// Command
	// --------------------------------------------------

	public static void sendChatMessage (LevelAccessor level, String target, String color, String text) {

		if (level == null) {

			return;

		}

		level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, level instanceof ServerLevel ? (ServerLevel) level : null, 4, "", Component.literal(""), level.getServer(), null).withSuppressedOutput(), "tellraw " + target + " [{\"text\":\"" + text + "\",\"color\":\"" + color + "\"}]");

	}

	public static void runCommand (LevelAccessor level, double posX, double posY, double posZ, String command) {

		if (level instanceof ServerLevel world) {

			world.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(posX, posY, posZ), Vec2.ZERO, world, 4, "", Component.literal(""), world.getServer(), null).withSuppressedOutput(), command);

		}

	}

	public static void runCommandEntity (Entity entity, String command) {

		if (entity.level() instanceof ServerLevel world) {

			entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), world, 4, entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), command);

		}

	}

	public static boolean commandResult (LevelAccessor level, int posX, int posY, int posZ, String command) {

		StringBuilder result = new StringBuilder();

		CommandSource data_consumer = new CommandSource() {

			@Override
			public boolean acceptsSuccess() {
				result.append("pass");
				return true;
			}

			@Override public void sendSystemMessage(Component component) {}
			@Override public boolean acceptsFailure() { return false; }
			@Override public boolean shouldInformAdmins() { return false; }

		};

		if (level instanceof ServerLevel world) world.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(data_consumer, new Vec3(posX, posY, posZ), Vec2.ZERO, world, 4, "", Component.literal(""), world.getServer(), null), command);

		return result.toString().equals("pass");

	}

	public static boolean commandResultEntity (Entity entity, String command) {

		StringBuilder result = new StringBuilder();

		CommandSource data_consumer = new CommandSource() {

			@Override
			public boolean acceptsSuccess() {
				result.append("pass");
				return true;
			}

			@Override public void sendSystemMessage(Component component) {}
			@Override public boolean acceptsFailure() { return false; }
			@Override public boolean shouldInformAdmins() { return false; }

		};

		entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(data_consumer, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4, entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), command);
		return result.toString().equals("pass");

	}

	public static String commandResultCustom (LevelAccessor level, int posX, int posY, int posZ, String command) {

		StringBuilder result = new StringBuilder();

		CommandSource data_consumer = new CommandSource() {

			@Override public void sendSystemMessage(Component component) {

				result.append(component);

			}

			@Override public boolean acceptsSuccess() {return true;}
			@Override public boolean acceptsFailure() { return true; }
			@Override public boolean shouldInformAdmins() { return false; }

		};

		if (level instanceof ServerLevel world) world.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(data_consumer, new Vec3(posX, posY, posZ), Vec2.ZERO, world, 4, "", Component.literal(""), world.getServer(), null), command);
		return result.toString();

	}


	// --------------------------------------------------
	// Scoreboard
	// --------------------------------------------------

	public static int scoreGet (LevelAccessor level, String player) {

		ServerScoreboard score = level.getServer().getScoreboard();
		Objective objective = score.getObjective(TanshugetreesMod.MODID.toUpperCase());

		return score.getOrCreatePlayerScore(player, objective).getScore();

	}

	public static void scoreSet (LevelAccessor level, String player, int value) {

		ServerScoreboard score = level.getServer().getScoreboard();
		Objective objective = score.getObjective(TanshugetreesMod.MODID.toUpperCase());

		score.getOrCreatePlayerScore(player, objective).setScore(value);

	}

	public static void scoreAddRemove (LevelAccessor level, String player, int value) {

		ServerScoreboard score = level.getServer().getScoreboard();
		Objective objective = score.getObjective(TanshugetreesMod.MODID.toUpperCase());
		int old_value = scoreGet(level, player);

		score.getOrCreatePlayerScore(player, objective).setScore(old_value + value);

	}

	// --------------------------------------------------
	// Dimension
	// --------------------------------------------------

	public static String getCurrentDimensionID(LevelAccessor level) {

		if (level instanceof ServerLevel world) {

			return world.dimension().location().toString();

		}

		return "";

	}

	// --------------------------------------------------
	// Biome
	// --------------------------------------------------

	public static String biomeToBiomeID (Holder<Biome> biome) {

		String return_text = biome.toString().replace("Reference{ResourceKey[minecraft:worldgen/biome / ", "");
		return return_text.substring(0, return_text.indexOf("]"));

	}

	public static boolean isBiomeTaggedAs (Holder<Biome> biome, String tag) {

		try {

			return biome.is(TagKey.create(Registries.BIOME, new ResourceLocation(tag)));

		} catch (Exception ignored) {}

		return false;

	}

	// --------------------------------------------------
	// Block
	// --------------------------------------------------

	public static boolean isBlockTaggedAs (BlockState block, String tag) {

		try {

			return block.is(BlockTags.create(new ResourceLocation(tag)));

		} catch (Exception ignored) {}

		return false;

	}

	public static BlockState textToBlock (String id) {

		try {

			return BlockStateParser.parseForBlock(BuiltInRegistries.BLOCK.asLookup(), id, true).blockState();

		} catch (Exception ignored) {}

		return Blocks.AIR.defaultBlockState();

	}

	public static String blockToText (BlockState block) {

		return block.toString().replace("Block{", "").replace("}", "");

	}

	public static String blockToTextID (BlockState block) {

		String return_text = blockToText(block);

		if (return_text.endsWith("]") == true) {

			return_text = return_text.substring(0, return_text.indexOf("["));

		}

		return return_text;

	}

	public static boolean blockPropertyBooleanGet (BlockState block, String property) {

		return block.getBlock().getStateDefinition().getProperty(property) instanceof BooleanProperty get && block.getValue(get);

	}

	public static BlockState blockPropertyBooleanSet (BlockState block, String property, boolean value) {

		return block.getBlock().getStateDefinition().getProperty(property) instanceof BooleanProperty set
				? block.setValue(set, value)
				: block;

	}

	// --------------------------------------------------
	// NBT
	// --------------------------------------------------

	public static String NBTTextGet(Entity entity, String name) {

		return entity.getPersistentData().getString(name);

	}

	public static Boolean NBTLogicGet(Entity entity, String name) {

		return entity.getPersistentData().getBoolean(name);

	}

	public static double NBTNumberGet(Entity entity, String name) {

		return entity.getPersistentData().getDouble(name);

	}

	public static void NBTTextSet(Entity entity, String name, String value) {

		entity.getPersistentData().putString(name, value);

	}

	public static void NBTLogicSet(Entity entity, String name, boolean value) {

		entity.getPersistentData().putBoolean(name, value);

	}

	public static void NBTNumberSet(Entity entity, String name, double value) {

		entity.getPersistentData().putDouble(name, value);

	}

	// --------------------------------------------------

}