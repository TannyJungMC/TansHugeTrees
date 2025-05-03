package tannyjung.tanshugetrees_handcode.systems.living_tree_mechanics;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import tannyjung.tanshugetrees.TanshugetreesMod;
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

		if (ConfigMain.developer_mode == true) {

			GameUtils.runCommandEntity(entity, "particle flash ~ ~ ~ 0 0 0 0 1 force");

		}

		// Get Settings
		{

			File file = new File(Handcode.directory_config + "/custom_packs/.organized/presets/" + GameUtils.NBTEntityTextGet(entity, "settings"));

			if (file.exists() == true && file.isDirectory() == false) {

				{

					try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

						{

							if (read_all.startsWith("Block ") == true) {

								GameUtils.NBTEntityTextSet(entity, read_all.substring(6, 9), read_all.substring(12));

							}

						}

					} buffered_reader.close(); } catch (Exception e) { TanshugetreesMod.LOGGER.error(e.getMessage()); }

				}

			} else {

				return;

			}

		}

		File file = new File(Handcode.directory_config + "/custom_packs/" + GameUtils.NBTEntityTextGet(entity, "file"));
		int process = 0;

		if (file.exists() == true && file.isDirectory() == false) {

			{

				try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

					{

						process = process + 1;

						// Skipping
						{

							if (process < GameUtils.NBTEntityNumberGet(entity, "process_save")) {

								continue;

							} else if (GameUtils.NBTEntityNumberGet(entity, "process_save") + ConfigMain.rt_dynamic_process_limit <= process) {

								GameUtils.NBTEntityNumberSet(entity, "process_save", process);
								return;

							}

						}

						if (read_all.startsWith("+b") == true) {

							if (read_all.endsWith("le1") == true || read_all.endsWith("le2") == true) {

								getData(entity, read_all);

							} else {

								GameUtils.NBTEntityTextSet(entity, "pre_block", read_all);

							}

						}

					}

				} buffered_reader.close(); } catch (Exception e) { TanshugetreesMod.LOGGER.error(e.getMessage()); }

			}

			GameUtils.NBTEntityNumberSet(entity, "process_save", 0);

		}

	}

	private static void getData (Entity entity, String read_all) {

		LevelAccessor level = entity.level();
		int rotation = (int) GameUtils.NBTEntityNumberGet(entity, "rotation");
		boolean mirrored = GameUtils.NBTEntityLogicGet(entity, "mirrored");

		BlockPos pre_pos = null;
		BlockState pre_block = Blocks.AIR.defaultBlockState();

		{

			String pre_block_data = GameUtils.NBTEntityTextGet(entity, "pre_block");

			if (pre_block_data.startsWith("+b") == true) {

				int[] pre_pos_get = FileManager.textPosConverter(pre_block_data.substring(2, pre_block_data.length() - 3), rotation, mirrored);
				int pre_posX = entity.getBlockX() + pre_pos_get[0];
				int pre_posY = entity.getBlockY() + pre_pos_get[1];
				int pre_posZ = entity.getBlockZ() + pre_pos_get[2];
				pre_pos = new BlockPos(pre_posX, pre_posY, pre_posZ);
				pre_block = GameUtils.textToBlock(GameUtils.NBTEntityTextGet(entity, pre_block_data.substring(pre_block_data.length() - 3)));

				if (level.hasChunk(pre_posX >> 4, pre_posZ >> 4) == false) {

					return;

				}

			}

		}

		BlockPos pos = null;
		BlockState block = Blocks.AIR.defaultBlockState();

		{

			int[] pos_get = FileManager.textPosConverter(read_all.substring(2, read_all.length() - 3), rotation, mirrored);
			int posX = entity.getBlockX() + pos_get[0];
			int posY = entity.getBlockY() + pos_get[1];
			int posZ = entity.getBlockZ() + pos_get[2];
			pos = new BlockPos(posX, posY, posZ);
			block = GameUtils.textToBlock(GameUtils.NBTEntityTextGet(entity, read_all.substring(read_all.length() - 3)));

			if (level.hasChunk(posX >> 4, posZ >> 4) == false) {

				return;

			}

		}

		run(level, pre_pos, pre_block, pos, block);

	}

	private static void run (LevelAccessor level, BlockPos pre_pos, BlockState pre_block, BlockPos pos, BlockState block) {

		if (level.getBlockState(pre_pos).getBlock().equals(pre_block.getBlock()) == false) {

			// If missing twigs
			{

				if (level.getBlockState(pos).equals(block) == true) {

					block = GameUtils.blockPropertyBooleanSet(block, "persistent", false);
					level.setBlock(pos, block, 2);

				}

			}

		} else {

			// Leaf Litter Remover
			{

				if (Math.random() < ConfigMain.leaves_litter_remover_chance) {

					// For test because the real use marker falling down and set block, or maybe use this for "Fast" mode.
					// Also don't forget to add count limit

					int height = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ());

					if (height < pos.getY()) {

						LeafLitter.start(level, pos.getX(), height, pos.getZ(), block, true);

					}

				}

			}

			if (level.getBlockState(pos).getBlock().equals(block.getBlock()) == true) {

				// Leaf Drop
				{

					level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);

					if (ConfigMain.leaves_drop_animation_chance > 0) {

						// Animation
						{

							if (Math.random() < ConfigMain.leaves_drop_animation_chance) {

								if (GameUtils.commandResult(level, 0,0, 0, "execute if score leaf_drop_count TANSHUGETREES matches .." + ConfigMain.leaves_drop_animation_count_limit) == true) {

									String command = "transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[1.0f,1.0f,1.0f]},block_state:{Name:\"" + GameUtils.blockToTextID(block) + "\"},ForgeData:{block:\"" + GameUtils.blockToText(block) + "\"}";
									command = GameUtils.summonEntity("minecraft:block_display", "", "leaf_drop", "white", command);
									GameUtils.runCommand(level, pos.getX(), pos.getY(), pos.getZ(), command);

									GameUtils.runCommand(level, 0, 0, 0, "scoreboard players add leaf_drop_count TANSHUGETREES 1");

								}

							}

						}

					} else {

						// No Animation
						{

							int height = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ());

							if (height < pos.getY()) {

								LeafLitter.start(level, pos.getX(), height, pos.getZ(), block, false);

							}

						}

					}

				}

			} else {

				// Leaf Regrow
				{

					level.setBlock(pos, block, 2);

				}

			}

		}

	}

}