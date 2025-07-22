package tannyjung.tanshugetrees_handcode.systems.living_tree_mechanics;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import tannyjung.core.MiscUtils;
import tannyjung.tanshugetrees.network.TanshugetreesModVariables;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.core.GameUtils;
import tannyjung.tanshugetrees_handcode.config.ConfigMain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class LivingTreeMechanics {

	public static void start (Entity entity) {

		LevelAccessor level_accessor = entity.level();
		ServerLevel level_server = (ServerLevel) entity.level();

		// If Area Loaded
		{

			if (GameUtils.command.result(level_server, entity.getBlockX(), entity.getBlockY(), entity.getBlockZ(), "execute if loaded ~ ~ ~") == false) {

				return;

			}

		}

		if (ConfigMain.developer_mode == true) {

			GameUtils.command.runEntity(entity, "particle flash ~ ~ ~ 0 0 0 0 1 force");

		}

		Map<String, BlockState> map_block = new HashMap<>();
		int[] leaves_type = new int[2];
		boolean can_leaves_decay = false;
		boolean can_leaves_drop = false;
		boolean can_leaves_regrow = false;

		// Read Settings
		{

			File file = new File(Handcode.directory_config + "/custom_packs/.organized/presets/" + GameUtils.nbt.entity.getText(entity, "settings"));
			String get_short = "";
			String get = "";

			if (file.exists() == true && file.isDirectory() == false) {

				{

					try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file), 65536); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

						{

							if (read_all.startsWith("can_leaves_decay = ") == true) {

								can_leaves_decay = Boolean.parseBoolean(read_all.replace("can_leaves_decay = ", ""));

							} else if (read_all.startsWith("can_leaves_drop = ") == true) {

								can_leaves_drop = Boolean.parseBoolean(read_all.replace("can_leaves_drop = ", ""));

							} else if (read_all.startsWith("can_leaves_regrow = ") == true) {

								can_leaves_regrow = Boolean.parseBoolean(read_all.replace("can_leaves_regrow = ", ""));

							} else if (read_all.startsWith("Block ") == true) {

								get_short = read_all.substring(("Block ").length(), ("Block ###").length());
								get = read_all.substring(("Block ### = ").length());

								// Get ID
								{

									if (get.endsWith(" keep") == true) {

										get = get.substring(0, get.length() - (" keep").length());

									}

								}

								map_block.put(get_short, GameUtils.block.fromText(get));

								if (get_short.startsWith("le") == true) {

									// Leaves Types
									{

										if (get.endsWith("]") == true) {

											get = get.substring(0, get.indexOf("["));

										}

										int number = Integer.parseInt(get_short.substring(2)) - 1;

										if (ConfigMain.deciduous_leaves_list.contains(get) == true) {

											leaves_type[number] = 1;

										} else if (ConfigMain.coniferous_leaves_list.contains(get) == true) {

											leaves_type[number] = 2;

										}

									}

								}

							}

						}

					} buffered_reader.close(); } catch (Exception exception) { MiscUtils.exception(new Exception(), exception); }

				}

			} else {

				return;

			}

		}

		File file = new File(Handcode.directory_config + "/custom_packs/" + GameUtils.nbt.entity.getText(entity, "file"));

		if (file.exists() == true && file.isDirectory() == false) {

			BlockPos center_pos = new BlockPos(entity.getBlockX(), entity.getBlockY(), entity.getBlockZ());
			String current_season = TanshugetreesModVariables.MapVariables.get(level_accessor).season;
			int biome_type = 0;

			// Biome Type Test
			{

				if (GameUtils.biome.isTaggedAs(level_accessor.getBiome(center_pos), "forge:is_snowy")) {

					biome_type = 1;

				} else if (GameUtils.biome.isTaggedAs(level_accessor.getBiome(center_pos), "tanshugetrees:tropical_biomes")) {

					biome_type = 2;

				}

			}

			// Read Tree Shape
			{

				int process = 0;
				int rotation = (int) GameUtils.nbt.entity.getNumber(entity, "rotation");
				boolean mirrored = GameUtils.nbt.entity.getLogic(entity, "mirrored");

				String id = "";
				int[] get = null;
				int posX = 0;
				int posY = 0;
				int posZ = 0;
				BlockPos pre_pos = new BlockPos(0, 0, 0);
				BlockState pre_block = Blocks.AIR.defaultBlockState();
				String pre_block_data = "";
				BlockPos pos = new BlockPos(0, 0, 0);
				BlockState block = Blocks.AIR.defaultBlockState();

				try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file), 65536); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

					{

						process = process + 1;

						// Skipping Conditions
						{

							// Out of Save
							{

								if (process < GameUtils.nbt.entity.getNumber(entity, "process_save")) {

									continue;

								}

							}

							// Out of Process Limit
							{

								if (GameUtils.nbt.entity.getNumber(entity, "process_save") + ConfigMain.living_tree_mechanics_process_limit <= process) {

									GameUtils.nbt.entity.setNumber(entity, "process_save", process);
									return;

								}

							}

						}

						if (read_all.startsWith("+b") == true) {

							if (read_all.endsWith("le1") == false && read_all.endsWith("le2") == false) {

								GameUtils.nbt.entity.setText(entity, "pre_block", read_all);

							} else {

								// Get Leave Data
								{

									get = MiscUtils.textPosConverter(read_all.substring(2, read_all.length() - 3), rotation, mirrored);
									posX = entity.getBlockX() + get[0];
									posY = entity.getBlockY() + get[1];
									posZ = entity.getBlockZ() + get[2];

									if (GameUtils.command.result(level_server, posX, posY, posZ, "execute if loaded ~ ~ ~") == false) {

										return;

									}

									pos = new BlockPos(posX, posY, posZ);
									id = read_all.substring(read_all.length() - 3);
									block = map_block.get(id);

								}

								// Get Previous Block Data
								{

									pre_block_data = GameUtils.nbt.entity.getText(entity, "pre_block");
									get = MiscUtils.textPosConverter(pre_block_data.substring(2, pre_block_data.length() - 3), rotation, mirrored);
									posX = entity.getBlockX() + get[0];
									posY = entity.getBlockY() + get[1];
									posZ = entity.getBlockZ() + get[2];

									if (GameUtils.command.result(level_server, posX, posY, posZ, "execute if loaded ~ ~ ~") == false) {

										return;

									}

									pre_pos = new BlockPos(posX, posY, posZ);
									pre_block = map_block.get(pre_block_data.substring(pre_block_data.length() - 3));

								}

								if (level_accessor.getBlockState(pre_pos).getBlock() == pre_block.getBlock()) {

									if (can_leaves_drop == true || can_leaves_regrow == true) {

										run(level_accessor, level_server, entity, pos, map_block, block, leaves_type[Integer.parseInt(id.substring(2)) - 1], biome_type, current_season, can_leaves_drop, can_leaves_regrow);

									}

								} else {

									// Missing Twig
									{

										if (can_leaves_decay == true) {

											if (level_accessor.getBlockState(pos).getBlock() == block.getBlock()) {

												block = GameUtils.block.propertyBooleanSet(block, "persistent", false);
												level_accessor.setBlock(pos, block, 2);

											}

										}

									}

								}

							}

						}

					}

				} buffered_reader.close(); } catch (Exception exception) { MiscUtils.exception(new Exception(), exception); }

			}

			// At the end of the file
			{

				if (GameUtils.nbt.entity.getLogic(entity, "dead_tree") == true) {

					GameUtils.command.runEntity(entity, "kill @s");

				} else {

					GameUtils.nbt.entity.setNumber(entity, "process_save", 0);

					if (level_accessor.getBlockState(center_pos).isAir() == false) {

						return;

					}

					if (GameUtils.nbt.entity.getLogic(entity, "still_alive") == true) {

						GameUtils.nbt.entity.setLogic(entity, "still_alive", false);
						return;

					}

					GameUtils.nbt.entity.setLogic(entity, "dead_tree", true);

				}

			}

		}

	}

	private static void run (LevelAccessor level_accessor, ServerLevel level_server, Entity entity, BlockPos pos, Map<String, BlockState> map_block, BlockState block, int leaves_type, int biome_type, String current_season, boolean can_leaves_drop, boolean can_leaves_regrow) {

		boolean straighten = false;
		boolean can_pos_photosynthesis = false;

		// Leaves Straighten Test
		{

			if ((GameUtils.nbt.entity.getNumber(entity, "straighten_highestX") != pos.getX() || GameUtils.nbt.entity.getNumber(entity, "straighten_highestY") < pos.getY() || GameUtils.nbt.entity.getNumber(entity, "straighten_highestZ") != pos.getZ())) {

				GameUtils.nbt.entity.setNumber(entity, "straighten_highestX", pos.getX());
				GameUtils.nbt.entity.setNumber(entity, "straighten_highestY", pos.getY());
				GameUtils.nbt.entity.setNumber(entity, "straighten_highestZ", pos.getZ());

			} else {

				straighten = true;

			}

		}

		// Light Level Detection
		{

			if (ConfigMain.leaf_light_level_detection <= level_accessor.getBrightness(LightLayer.SKY, pos) + 1) {

				can_pos_photosynthesis = true;
				GameUtils.nbt.entity.setLogic(entity, "still_alive", true);

			}

		}

		// Leaf Litter Remover
		{

			if (Math.random() < ConfigMain.leaf_litter_remover_chance) {

				if (GameUtils.score.get(level_server, "TANSHUGETREES", "leaf_litter_remover") < ConfigMain.leaf_drop_animation_count_limit) {

					GameUtils.score.add(level_server, "TANSHUGETREES", "leaf_litter_remover", 1);

					GameUtils.command.run(level_server, pos.getX(), pos.getY(), pos.getZ(), GameUtils.misc.summonEntity("marker", "TANSHUGETREES / TANSHUGETREES-leaf_litter_remover", "Leaf Litter Remover", "ForgeData:{block:\"" + GameUtils.block.toText(block) + "\"}"));

				}

			}

		}

		if (level_accessor.getBlockState(pos).getBlock() == block.getBlock()) {

			// Leaf Drop
			{

				if (can_leaves_drop == true) {

					double chance = 0.0;

					if (straighten == true) {

						BlockState test = level_accessor.getBlockState(new BlockPos(pos.getX(), (int) GameUtils.nbt.entity.getNumber(entity, "straighten_highestY"), pos.getZ()));

						if (map_block.get("le1").getBlock() != test.getBlock() && map_block.get("le2").getBlock() != test.getBlock()) {

							chance = 1.0;

						}

					} else if (can_pos_photosynthesis == false) {

						chance = ConfigMain.leaf_light_level_detection_drop_chance;

					} else {

						if (leaves_type == 1) {

							if (biome_type == 0) {

								// By Seasons
								{

									chance = switch (current_season) {
										case "Spring" -> ConfigMain.leaf_drop_chance_spring;
										case "Summer" -> ConfigMain.leaf_drop_chance_summer;
										case "Autumn" -> ConfigMain.leaf_drop_chance_autumn;
										case "Winter" -> ConfigMain.leaf_drop_chance_winter;
										default -> chance;
									};

								}

							} else if (biome_type == 1) {

								chance = ConfigMain.leaf_drop_chance_winter;

							} else if (biome_type == 2) {

								chance = ConfigMain.leaf_drop_chance_summer;

							}

						} else if (leaves_type == 2) {

							// Only drop coniferous leaves in summer
							{

								if (current_season.equals("Summer")) {

									chance = ConfigMain.leaf_drop_chance_coniferous;

								}

							}

						} else {

							chance = ConfigMain.leaf_drop_chance_summer;

						}

					}

					if (Math.random() < chance) {

						level_accessor.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);

						if (ConfigMain.leaf_drop_animation_chance > 0 && Math.random() < ConfigMain.leaf_drop_animation_chance) {

							// Animation
							{

								if (GameUtils.score.get(level_server, "TANSHUGETREES", "leaf_drop") < ConfigMain.leaf_drop_animation_count_limit) {

									// Don't create animation, if there's a block below.
									if (GameUtils.block.isTaggedAs(level_accessor.getBlockState(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ())), "tanshugetrees:passable_blocks") == true) {

										GameUtils.score.add(level_server, "TANSHUGETREES", "leaf_drop", 1);

										String command = "transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[1.0f,1.0f,1.0f]},block_state:{Name:\"" + GameUtils.block.toTextID(block) + "\"},ForgeData:{block:\"" + GameUtils.block.toText(block) + "\"}";
										command = GameUtils.misc.summonEntity("block_display", "TANSHUGETREES / TANSHUGETREES-leaf_drop", "Falling Leaf", command);
										GameUtils.command.run(level_server, pos.getX(), pos.getY(), pos.getZ(), command);

									}

								}

							}

						} else {

							// No Animation
							{

								int height_motion = level_accessor.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ());

								if (height_motion < pos.getY()) {

									LeafLitter.start(level_server, pos.getX(), height_motion, pos.getZ(), block, false);

								}

							}

						}

					}

				}

			}

		} else if (level_accessor.getBlockState(pos).isAir() == true) {

			// Leaf Regrowth
			{

				if (can_leaves_regrow == true) {

					double chance = 0.0;

					if (straighten == true) {

						// Cancel By Straighten (if no block above)
						{

							BlockState test = level_accessor.getBlockState(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ()));

							if (map_block.get("le1").getBlock() != test.getBlock() && map_block.get("le2").getBlock() != test.getBlock()) {

								return;

							} else {

								chance = 1.0;

							}

						}

					} else if (can_pos_photosynthesis == true) {

						if (leaves_type == 1) {

							if (biome_type == 0) {

								// By Seasons
								{

									chance = switch (current_season) {
										case "Spring" -> ConfigMain.leaf_regrowth_chance_spring;
										case "Summer" -> ConfigMain.leaf_regrowth_chance_summer;
										case "Autumn" -> ConfigMain.leaf_regrowth_chance_autumn;
										case "Winter" -> ConfigMain.leaf_regrowth_chance_winter;
										default -> chance;
									};

								}

							} else if (biome_type == 1) {

								chance = ConfigMain.leaf_regrowth_chance_winter;

							} else if (biome_type == 2) {

								chance = ConfigMain.leaf_regrowth_chance_summer;

							}

						} else if (leaves_type == 2) {

							chance = ConfigMain.leaf_regrowth_chance_coniferous;

						} else {

							chance = ConfigMain.leaf_regrowth_chance_summer;

						}

					}

					if (Math.random() < chance) {

						level_accessor.setBlock(pos, block, 2);

					}

				}

			}

		}

	}

}