package tannyjung.tanshugetrees_handcode.misc;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class Misc {

	public static boolean isBlockTaggedAs (BlockState block, String tag) {

		return block.is(BlockTags.create(new ResourceLocation(tag)));

	}

	public static BlockState textToBlock (String id) {

		BlockState return_block = Blocks.AIR.defaultBlockState();

		{
			try {

				return_block = BlockStateParser.parseForBlock(BuiltInRegistries.BLOCK.asLookup(), id, true).blockState();

			} catch (Exception e) {

				return_block = Blocks.AIR.defaultBlockState();

			}

		}

		return return_block;

	}

	public static String blockToText (BlockState block) {

		String return_text = "";

		{

			return_text = block.toString();
			return_text = return_text.replace("Block{", "").replace("}", "");

		}

		return return_text;

	}

	public static String blockToTextID (BlockState block) {

		String return_text = "";

		{

			return_text = blockToText(block);

			if (return_text.endsWith("]") == true) {

				return_text = return_text.substring(0, return_text.indexOf("["));

			}

		}

		return return_text;

	}

	public static String biomeToBiomeID (Holder<Biome> biome) {

		String return_text = "";

		{

			return_text = biome.toString().replace("Reference{ResourceKey[minecraft:worldgen/biome / ", "");
			return_text = return_text.substring(0, return_text.indexOf("]"));

		}

		return return_text;

	}

	public static boolean isBiomeTaggedAs (Holder<Biome> biome, String tag) {

		boolean return_logic = false;

		{

			try {

				return_logic = biome.is(TagKey.create(Registries.BIOME, new ResourceLocation(tag)));

			} catch (Exception ignored) {}

		}

		return return_logic;

	}

	public static void sendChatMessage (LevelAccessor level, String target, String color, String text) {

		if (level == null) {

			return;

		}

		level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, level.getServer().overworld(), 4, "", Component.literal(""), level.getServer(), null).withSuppressedOutput(), "tellraw " + target + " [{\"text\":\"" + text + "\",\"color\":\"" + color + "\"}]");

	}

	public static void runCommand (LevelAccessor level, double posX, double posY, double posZ, String command) {

		if (level == null) {

			return;

		}

		level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(posX, posY, posZ), Vec2.ZERO, level.getServer().overworld(), 4, "", Component.literal(""), level.getServer(), null).withSuppressedOutput(), command);

	}

}