package tannyjung.tanshugetrees_handcode.misc;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class Misc {

	public static boolean isBlockTaggedAs (BlockState block, String tag) {

		return block.is(BlockTags.create(new ResourceLocation(tag)));

	}

	public static BlockState textToBlock (String id) {

		BlockState return_block = Blocks.AIR.defaultBlockState();

		{

			String extra_settings = "";

			if (id.endsWith("]") == true) {

				extra_settings = id.substring(id.indexOf("["));
				id = id.substring(0, id.indexOf("["));

			}

			// Convert ID into Block
			return_block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation((id).toLowerCase(java.util.Locale.ENGLISH))).defaultBlockState();

			// Set Extra Settings
			if (extra_settings.equals("") == false) {

				if (extra_settings.contains("persistent=true") == true) {

					return_block = return_block.getBlock().getStateDefinition().getProperty("persistent") instanceof BooleanProperty property ? return_block.setValue(property, true) : return_block;

				}

			}

		}

		return return_block;

	}

	public static boolean isBiomeTaggedAs (Holder<Biome> biome_get, String tag) {

		return biome_get.is(TagKey.create(Registries.BIOME, new ResourceLocation(tag)));

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