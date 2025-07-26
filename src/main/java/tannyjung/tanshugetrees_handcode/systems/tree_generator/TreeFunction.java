package tannyjung.tanshugetrees_handcode.systems.tree_generator;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import tannyjung.core.OutsideUtils;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.core.GameUtils;
;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class TreeFunction {

	public static void start (LevelAccessor level_accessor, ServerLevel level_server, int posX, int posY, int posZ, String path, boolean only_loaded_chunk) {

		File file = new File(Handcode.directory_config + "/custom_packs/.organized/functions/" + path);

		if (file.exists() == true && file.isDirectory() == false) {

			WorldGenLevel world_gen = (WorldGenLevel) level_accessor;
			BlockPos pos = null;
			boolean chunk_loaded = GameUtils.command.result(level_server, posX, posY, posZ, "execute if loaded ~ ~ ~");
			boolean function_in_loaded_chunk = false;

			String[] get = new String[0];
			double chance = 0.0;
			String[] offset_pos = new String[0];
			int offset_posX = 0;
			int offset_posY = 0;
			int offset_posZ = 0;
			String[] min_max = new String[0];
			int minX = 0;
			int minY = 0;
			int minZ = 0;
			int maxX = 0;
			int maxY = 0;
			int maxZ = 0;

			BlockState block = Blocks.AIR.defaultBlockState();
			boolean keep = false;
			String feature = "";
			String command = "";

			// Read File
			{

				try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file), 65536); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

					{

						if (read_all.equals("") == false) {

							if (read_all.startsWith("block = ") == true) {

								{

									if (only_loaded_chunk == false) {

										try {

											get = read_all.replace("block = ", "").split(" \\| ");
											chance = Double.parseDouble(get[0]);
											block = GameUtils.block.fromText(get[3]);
											keep = get[4].equals("true");

										} catch (Exception ignored) {

											return;

										}

										if (Math.random() < chance && block != Blocks.AIR.defaultBlockState()) {

											// Get Pos
											{

												try {

													offset_pos = get[1].split("/");
													offset_posX = Integer.parseInt(offset_pos[0]);
													offset_posY = Integer.parseInt(offset_pos[1]);
													offset_posZ = Integer.parseInt(offset_pos[2]);

													min_max = get[2].split("/");
													minX = Integer.parseInt(min_max[0]);
													minY = Integer.parseInt(min_max[1]);
													minZ = Integer.parseInt(min_max[2]);
													maxX = Integer.parseInt(min_max[3]);
													maxY = Integer.parseInt(min_max[4]);
													maxZ = Integer.parseInt(min_max[5]);

												} catch (Exception ignored) {

													return;

												}

											}

											for (int testX = minX; testX <= maxX; testX++) {

												for (int testY = minY; testY <= maxY; testY++) {

													for (int testZ = minZ; testZ <= maxZ; testZ++) {

														pos = new BlockPos(posX + offset_posX + testX, posY + offset_posY + testY, posZ + offset_posZ + testZ);

														if (level_accessor.hasChunk(pos.getX() >> 4, pos.getZ() >> 4) == true) {

															// Keep
															{

																if (keep == true) {

																	if (GameUtils.block.isTaggedAs(level_accessor.getBlockState(pos), "tanshugetrees:passable_blocks") == false || level_accessor.isWaterAt(pos) == true) {

																		continue;

																	}

																}

															}

															level_accessor.setBlock(new BlockPos(pos), block, 2);

														}

													}

												}

											}

										}

									}

								}

							} else if (read_all.startsWith("feature = ") == true) {

								{

									if (only_loaded_chunk == false) {

										try {

											get = read_all.replace("feature = ", "").split(" \\| ");
											chance = Double.parseDouble(get[0]);
											offset_pos = get[1].split("/");
											offset_posX = Integer.parseInt(offset_pos[0]);
											offset_posY = Integer.parseInt(offset_pos[1]);
											offset_posZ = Integer.parseInt(offset_pos[2]);
											feature = get[2];

										} catch (Exception ignored) {

											return;

										}

										if (Math.random() < chance) {

											pos = new BlockPos(posX + offset_posX, posY + offset_posY, posZ + offset_posZ);
											world_gen.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE).getHolderOrThrow(FeatureUtils.createKey(feature)).value().place(world_gen, world_gen.getLevel().getChunkSource().getGenerator(), world_gen.getRandom(), pos);

										}

									}

								}

							} else if (read_all.startsWith("command = ") == true) {

								{
									if (chunk_loaded == true) {

										try {

											get = read_all.replace("command = ", "").split(" \\| ");
											chance = Double.parseDouble(get[0]);
											command = get[1];

										} catch (Exception ignored) {

											return;

										}

										if (Math.random() < chance) {

											GameUtils.command.run(level_server, posX, posY, posZ, command);

										}

									} else {

										function_in_loaded_chunk = true;

									}

								}

							}

						}

					}

				} buffered_reader.close(); } catch (Exception exception) { OutsideUtils.exception(new Exception(), exception); }

			}

			if (function_in_loaded_chunk == true) {

				GameUtils.command.run(level_server, posX, posY, posZ, GameUtils.entity.summonCommand("marker", "TANSHUGETREES / TANSHUGETREES-tree_function_in_loaded_chunk", "Tree Function in Loaded Chunk", "ForgeData:{function:\"" + path +"\"}"));

			}

		}

	}

}