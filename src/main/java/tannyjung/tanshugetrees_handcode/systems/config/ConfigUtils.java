package tannyjung.tanshugetrees_handcode.systems.config;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import tannyjung.tanshugetrees_handcode.misc.GameUtils;

public class ConfigUtils {

	public static boolean testBiome (Holder<Biome> biome_center, String config_value) {

		boolean return_logic = false;

		{

			String biome_centerID = GameUtils.biomeToBiomeID(biome_center);

			for (String split : config_value.split(" / ")) {

				return_logic = true;

				for (String split2 : split.split(" & ")) {

					String split_get = split2.replaceAll("[#!]", "");

					{

						if (split2.contains("#") == false) {

							if (biome_centerID.equals(split_get) == false) {

								return_logic = false;

							}

						} else {

							if (GameUtils.isBiomeTaggedAs(biome_center, split_get) == false) {

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

	public static boolean testGroundBlock (BlockState ground_block, String config_value) {

			boolean return_logic = false;

			{

				for (String split : config_value.split(" / ")) {

					return_logic = true;

					for (String split2 : split.split(" & ")) {

						String split_get = split2.replaceAll("[#!]", "");

						{

							if (split2.contains("#") == false) {

								if (ForgeRegistries.BLOCKS.getKey(ground_block.getBlock()).toString().equals(split_get) == false) {

									return_logic = false;

								}

							} else {

								if (GameUtils.isBlockTaggedAs(ground_block, split_get) == false) {

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

}