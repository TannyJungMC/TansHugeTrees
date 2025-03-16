package tannyjung.tanshugetrees_handcode.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_handcode.Handcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class TreeFunction {

	public static void run (LevelAccessor level, ServerLevel world, WorldGenLevel world_gen_level, String path, int posX, int posY, int posZ) {

		File file = new File(Handcode.directory_config + "/custom_packs/.organized/" + path);

		if (file.exists() == true && file.isDirectory() == false) {

			try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

				{

					if (read_all.startsWith("fill = ") == true) {

						{

							String[] get = read_all.replace("place_block = ", "").split(" \\| ");

							double chance = Double.parseDouble(get[0]);
							String[] offset_pos = get[1].split(" - ");

							String[] offset_pos_from = offset_pos[0].split("/");
							int offset_from_posX = Integer.parseInt(offset_pos_from[0]);
							int offset_from_posY = Integer.parseInt(offset_pos_from[1]);
							int offset_from_posZ = Integer.parseInt(offset_pos_from[2]);

							String[] offset_pos_to = offset_pos[1].split("/");
							int offset_to_posX = Integer.parseInt(offset_pos_to[0]);
							int offset_to_posY = Integer.parseInt(offset_pos_to[1]);
							int offset_to_posZ = Integer.parseInt(offset_pos_to[2]);

							BlockState block = Misc.textToBlock(get[2]);
							boolean keep = get[3].equals("true");

							// Test
							{

								if (Math.random() >= chance) {

									continue;

								}

								if (block == Blocks.AIR.defaultBlockState()) {

									continue;

								}

							}

							for (int offset_posX = offset_from_posX; offset_posX <= offset_to_posX; offset_posX++) {

								for (int offset_posY = offset_from_posY; offset_posY <= offset_to_posY; offset_posY++) {

									for (int offset_posZ = offset_from_posZ; offset_posZ <= offset_to_posZ; offset_posZ++) {

										BlockPos pos = new BlockPos(posX + offset_posX, posY + offset_posY, posZ + offset_posZ);

										// Keep
										{

											if (keep == true) {

												if (
														Misc.isBlockTaggedAs(level.getBlockState(pos), "tanshugetrees:passable_blocks") == false
																||
																Misc.isBlockTaggedAs(level.getBlockState(pos), "tanshugetrees:fluid_blocks") == true
												) {

													continue;

												}

											}

										}

										level.setBlock(new BlockPos(pos), block, 2);

									}

								}

							}

						}

					} else if (read_all.startsWith("feature = ") == true) {

						{

							String[] get = read_all.replace("feature = ", "").split(" \\| ");
							double chance = Double.parseDouble(get[0]);
							String[] offset_pos = get[1].split("/");
							int offset_posX = Integer.parseInt(offset_pos[0]);
							int offset_posY = Integer.parseInt(offset_pos[1]);
							int offset_posZ = Integer.parseInt(offset_pos[2]);
							String feature = get[2];

							if (Math.random() < chance) {

								level.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE).getHolderOrThrow(FeatureUtils.createKey(feature)).value().place(world_gen_level, world.getChunkSource().getGenerator(), level.getRandom(), BlockPos.containing(posX + offset_posX, posY + offset_posY, posZ + offset_posZ));

							}

						}

					} else {

						String read_all_final = read_all;

						TanshugetreesMod.queueServerWork(20, () -> {

							MCreatorLink.runCommand(world, posX, posY, posZ, read_all_final);

						});

					}

				}

			} buffered_reader.close(); } catch (Exception e) { e.printStackTrace(); }

		}

	}

}