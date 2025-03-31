package tannyjung.tanshugetrees_handcode.misc;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
import tannyjung.tanshugetrees.procedures.SendChatMessageProcedure;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class MiscOutside {

	public static boolean isConnectedToInternet() {

		boolean return_logic = true;

		try {

			URL test = new URI("https://www.google.com/").toURL();
			HttpURLConnection connection = (HttpURLConnection) test.openConnection();
			connection.setRequestMethod("HEAD");
			connection.setConnectTimeout(3000);
			connection.setReadTimeout(3000);
			int responseCode = connection.getResponseCode();
			// return (200 <= responseCode && responseCode < 400);

		} catch (Exception e) {

			return_logic = false;

		}

		return return_logic;

	}

}