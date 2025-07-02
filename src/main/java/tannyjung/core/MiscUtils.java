package tannyjung.core;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class MiscUtils {

	public static void exception (Exception exception) {

		Logger LOGGER = LogManager.getLogger("TannyJung's Mods");
		LOGGER.error("--------------------------------------------------");
		StackTraceElement[] list = exception.getStackTrace();

		for (StackTraceElement get : list) {

			LOGGER.error(get);

		}

	}

	public static boolean isConnectedToInternet () {

		try {

			URL test = new URI("https://www.google.com").toURL();
			HttpURLConnection connection = (HttpURLConnection) test.openConnection();
			connection.setRequestMethod("HEAD");
			connection.setConnectTimeout(3000);
			connection.setReadTimeout(3000);

			// int responseCode = connection.getResponseCode();
			// return (200 <= responseCode && responseCode < 400);

		} catch (Exception exception) {

			return false;

		}

		return true;

	}

	public static String quardtreeChunkToNode (int chunkX, int chunkZ) {

		StringBuilder return_text = new StringBuilder();

		{

			int localX = chunkX & 31;
			int localZ = chunkZ & 31;

			for (int level = 0; level < 2; level++) {

				int size = 32 >> (level + 1);
				int posX = (localX / size) % 2;
				int posZ = (localZ / size) % 2;

				if (posX == 0 && posZ == 0) return_text.append("-NW");
				else if (posX == 1 && posZ == 0) return_text.append("-NE");
				else if (posX == 0 && posZ == 1) return_text.append("-SW");
				else return_text.append("-SE");

			}

		}

		return return_text.substring(1);

	}

	public static boolean configTestBiome (Holder<Biome> biome_center, String config_value) {

		boolean return_logic = false;

		{

			String biome_centerID = GameUtils.biome.toID(biome_center);

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

							if (GameUtils.biome.isTaggedAs(biome_center, split_get) == false) {

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

							if (GameUtils.block.isTaggedAs(ground_block, split_get) == false) {

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

	public static int[] textPosConverter (String pos, int rotation, boolean mirrored) {

		int[] return_number = new int[3];

		{

			String[] get = pos.split("/");
			int posX = Integer.parseInt(get[0]);
			int posY = Integer.parseInt(get[1]);
			int posZ = Integer.parseInt(get[2]);

			// Rotation & Mirrored
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

			return_number[0] = posX;
			return_number[1] = posY;
			return_number[2] = posZ;

		}

		return return_number;

	}

}