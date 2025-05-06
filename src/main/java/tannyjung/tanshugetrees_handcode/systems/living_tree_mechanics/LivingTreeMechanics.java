package tannyjung.tanshugetrees_handcode.systems.living_tree_mechanics;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees.network.TanshugetreesModVariables;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.misc.FileManager;
import tannyjung.tanshugetrees_handcode.misc.GameUtils;
import tannyjung.tanshugetrees_handcode.systems.LeafLitter;
import tannyjung.tanshugetrees_handcode.systems.config.ConfigMain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class LivingTreeMechanics {

	public static void start (Entity entity) {

		LevelAccessor level = entity.level();

		// If Area Loaded
		{

			if (GameUtils.commandResult(level, entity.getBlockX(), entity.getBlockY(), entity.getBlockZ(), "execute if loaded ~ ~ ~") == false) {

				return;

			}

		}

		if (ConfigMain.developer_mode == true) {

			GameUtils.runCommandEntity(entity, "particle flash ~ ~ ~ 0 0 0 0 1 force");

		}

		int[] leaves_type = new int[2];

		// Get Settings
		{

			File file = new File(Handcode.directory_config + "/custom_packs/.organized/presets/" + GameUtils.NBTEntityTextGet(entity, "settings"));
			String id = "";
			int leaves_type_test = 0;

			if (file.exists() == true && file.isDirectory() == false) {

				{

					try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

						{

							if (read_all.startsWith("Block ") == true) {

								GameUtils.NBTEntityTextSet(entity, read_all.substring(6, 9), read_all.substring(12));

								// Test Leaves Type
								{

									id = read_all.substring(6, 9);

									if (id.startsWith("le") == true) {

										id = GameUtils.NBTEntityTextGet(entity, "id");

										if (id.endsWith("]") == true) {

											id = id.substring(0, id.indexOf("["));

										}

										if (ConfigMain.deciduous_leaves_list.contains(id) == true) {

											leaves_type_test = 1;

										} else if (ConfigMain.coniferous_leaves_list.contains(id) == true) {

											leaves_type_test = 2;

										} else {

											leaves_type_test = 0;

										}

										if (id.endsWith("1") == true) {

											leaves_type[0] = leaves_type_test;

										} else {

											leaves_type[1] = leaves_type_test;

										}

									}

								}

							}

						}

					} buffered_reader.close(); } catch (Exception e) { TanshugetreesMod.LOGGER.error(e.getMessage()); }

				}

			} else {

				return;

			}

		}

		File file = new File(Handcode.directory_config + "/custom_packs/" + GameUtils.NBTEntityTextGet(entity, "file"));

		if (file.exists() == true && file.isDirectory() == false) {

			int process = 0;
			int rotation = (int) GameUtils.NBTEntityNumberGet(entity, "rotation");
			boolean mirrored = GameUtils.NBTEntityLogicGet(entity, "mirrored");

			// Read Data
			{

				try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

					{

						process = process + 1;

						// Skipping
						{

							if (process < GameUtils.NBTEntityNumberGet(entity, "process_save")) {

								continue;

							} else if (GameUtils.NBTEntityNumberGet(entity, "process_save") + ConfigMain.living_tree_mechanics_process_limit <= process) {

								GameUtils.NBTEntityNumberSet(entity, "process_save", process);
								return;

							}

						}

						if (read_all.startsWith("+b") == true) {

							if (read_all.endsWith("le1") == true || read_all.endsWith("le2") == true) {

								getData(level, entity, read_all, rotation, mirrored, leaves_type);

							} else {

								GameUtils.NBTEntityTextSet(entity, "pre_block", read_all);

							}

						}

					}

				} buffered_reader.close(); } catch (Exception e) { TanshugetreesMod.LOGGER.error(e.getMessage()); }

			}

			// At the end of the file
			{

				if (GameUtils.NBTEntityLogicGet(entity, "dead_tree") == true) {

					GameUtils.commandResultEntity(entity, "kill @s");

				} else {

					boolean dead_tree = false;

					if (level.getBlockState(new BlockPos(entity.getBlockX(), entity.getBlockY(), entity.getBlockZ())).isAir() == true) {

						dead_tree = true;

					} else if (GameUtils.NBTEntityLogicGet(entity, "have_leaves") == false) {

						String season = TanshugetreesModVariables.MapVariables.get(level).season;

						if (season.equals("autumn") == false && season.equals("winter") == false) {

							dead_tree = true;

						}

					}

					if (dead_tree == true) {

						GameUtils.NBTEntityLogicSet(entity, "dead_tree", true);

					}

					GameUtils.NBTEntityNumberSet(entity, "process_save", 0);
					GameUtils.NBTEntityLogicSet(entity, "have_leaves", false);

				}

			}

		}

	}

	private static void getData (LevelAccessor level, Entity entity, String read_all, int rotation, boolean mirrored, int[] leaves_type) {

		BlockPos pre_pos = null;
		BlockState pre_block = Blocks.AIR.defaultBlockState();

		{

			String pre_block_data = GameUtils.NBTEntityTextGet(entity, "pre_block");

			if (pre_block_data.startsWith("+b") == true) {

				int[] get = FileManager.textPosConverter(pre_block_data.substring(2, pre_block_data.length() - 3), rotation, mirrored);
				int posX = entity.getBlockX() + get[0];
				int posY = entity.getBlockY() + get[1];
				int posZ = entity.getBlockZ() + get[2];

				pre_pos = new BlockPos(posX, posY, posZ);
				pre_block = GameUtils.textToBlock(GameUtils.NBTEntityTextGet(entity, pre_block_data.substring(pre_block_data.length() - 3)));

				if (GameUtils.commandResult(level, posX, posY, posZ, "execute if loaded ~ ~ ~") == false) {

					return;

				}

			}

		}

		BlockPos pos = null;
		BlockState block = Blocks.AIR.defaultBlockState();
		int leaves_type_get = 0;

		{

			int[] get = FileManager.textPosConverter(read_all.substring(2, read_all.length() - 3), rotation, mirrored);
			int posX = entity.getBlockX() + get[0];
			int posY = entity.getBlockY() + get[1];
			int posZ = entity.getBlockZ() + get[2];

			pos = new BlockPos(posX, posY, posZ);

			String id = read_all.substring(read_all.length() - 3);
			block = GameUtils.textToBlock(GameUtils.NBTEntityTextGet(entity, id));
			leaves_type_get = leaves_type[Integer.parseInt(id.substring(2))];

			if (GameUtils.commandResult(level, posX, posY, posZ, "execute if loaded ~ ~ ~") == false) {

				return;

			}

		}

		run(level, entity, pre_pos, pre_block, pos, block, leaves_type_get);

	}

	private static void run (LevelAccessor level, Entity entity, BlockPos pre_pos, BlockState pre_block, BlockPos pos, BlockState block, int leaves_type) {

		if (level.getBlockState(pre_pos).getBlock().equals(pre_block.getBlock()) == false) {

			// Missing Twig
			{

				if (level.getBlockState(pos).equals(block) == true) {

					block = GameUtils.blockPropertyBooleanSet(block, "persistent", false);
					level.setBlock(pos, block, 2);

				}

			}

		} else {

			// Leaf Litter Remover
			{

				if (Math.random() < ConfigMain.leaf_litter_remover_chance) {

					if (GameUtils.scoreGet(level, "leaf_litter_remover") < ConfigMain.leaf_drop_animation_count_limit) {

						GameUtils.scoreAddRemove(level, "leaf_litter_remover", 1);

						GameUtils.runCommand(level, pos.getX(), pos.getY(), pos.getZ(), GameUtils.summonEntity("marker", "", "leaf_litter_remover", "white", "ForgeData:{block:\"" + GameUtils.blockToText(block) + "\"}"));

					}

				}

			}

			if (level.getBlockState(pos).getBlock().equals(block.getBlock()) == true) {

				if (GameUtils.NBTEntityLogicGet(entity, "have_leaves") == false) {

					GameUtils.NBTEntityLogicSet(entity, "have_leaves", true);

				}

				// Leaf Drop
				{

					double chance = 0.0;

					if (ConfigMain.leaf_light_level_detection < 15 && ConfigMain.leaf_light_level_detection > level.getBrightness(LightLayer.SKY, pos)) {

						// By Light Level
						{

							chance = ConfigMain.leaves_drop_chance_summer;

						}

					} else {

						if (leaves_type == 1) {

							// By Seasons
							{

								if (TanshugetreesModVariables.MapVariables.get(level).season.equals("summer")) {

									chance = ConfigMain.leaves_drop_chance_summer;

								} else if (TanshugetreesModVariables.MapVariables.get(level).season.equals("autumn")) {

									chance = ConfigMain.leaves_drop_chance_autumn;

								} else if (TanshugetreesModVariables.MapVariables.get(level).season.equals("winter")) {

									chance = ConfigMain.leaves_drop_chance_winter;

								} else if (TanshugetreesModVariables.MapVariables.get(level).season.equals("spring")) {

									chance = ConfigMain.leaves_drop_chance_spring;

								}

							}

						} else if (leaves_type == 2) {

							chance = ConfigMain.leaves_drop_chance_coniferous;

						} else {

							chance = ConfigMain.leaves_drop_chance_summer;

						}

					}

					if (Math.random() < chance) {

						level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);

						if (ConfigMain.leaf_drop_animation_chance > 0 && Math.random() < ConfigMain.leaf_drop_animation_chance) {

							// Animation
							{

								if (GameUtils.scoreGet(level, "leaf_drop") < ConfigMain.leaf_drop_animation_count_limit) {

									// Don't create animation, if there's a block below.
									if (GameUtils.isBlockTaggedAs(level.getBlockState(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ())), "tanshugetrees:passable_blocks") == true) {

										GameUtils.scoreAddRemove(level, "leaf_drop", 1);

										String command = "transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[1.0f,1.0f,1.0f]},block_state:{Name:\"" + GameUtils.blockToTextID(block) + "\"},ForgeData:{block:\"" + GameUtils.blockToText(block) + "\"}";
										command = GameUtils.summonEntity("block_display", "", "leaf_drop", "white", command);
										GameUtils.runCommand(level, pos.getX(), pos.getY(), pos.getZ(), command);


									}
								}

							}

						} else {

							// No Animation
							{

								int height_motion = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ());

								if (height_motion < pos.getY()) {

									LeafLitter.start(level, pos.getX(), height_motion, pos.getZ(), block, false);

								}

							}

						}

					}

				}

			} else {

				// Cancel By Light Level
				{

					if (ConfigMain.leaf_light_level_detection < 15 && ConfigMain.leaf_light_level_detection > level.getBrightness(LightLayer.SKY, pos)) {

						return;

					}

				}

				// Leaf Regrow
				{

					double chance = 0.0;

					if (leaves_type == 1) {

						// By Seasons
						{

							if (TanshugetreesModVariables.MapVariables.get(level).season.equals("summer")) {

								chance = ConfigMain.leaves_regrow_chance_summer;

							} else if (TanshugetreesModVariables.MapVariables.get(level).season.equals("autumn")) {

								chance = ConfigMain.leaves_regrow_chance_autumn;

							} else if (TanshugetreesModVariables.MapVariables.get(level).season.equals("winter")) {

								chance = ConfigMain.leaves_regrow_chance_winter;

							} else if (TanshugetreesModVariables.MapVariables.get(level).season.equals("spring")) {

								chance = ConfigMain.leaves_regrow_chance_spring;

							}

						}

					} else if (leaves_type == 2) {

						chance = ConfigMain.leaves_regrow_chance_coniferous;

					} else {

						chance = ConfigMain.leaves_regrow_chance_summer;

					}

					if (Math.random() < chance) {

						level.setBlock(pos, block, 2);

					}

				}

			}

		}

	}

}