package tannyjung.tanshugetrees_handcode.systems.tree_generator;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkStatus;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.misc.GameUtils;
;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class TreeFunction {

	public static void start (LevelAccessor level, String path, int posX, int posY, int posZ) {

		File file = new File(Handcode.directory_config + "/custom_packs/.organized/functions/" + path);

		if (file.exists() == true && file.isDirectory() == false) {

			WorldGenLevel world_gen = (WorldGenLevel) level;
			BlockPos pos = null;

			String[] get = new String[0];
			double chance = 0.0;
			BlockState block = Blocks.AIR.defaultBlockState();
			boolean keep = false;

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
			int startX = 0;
			int startY = 0;
			int startZ = 0;
			int endX = 0;
			int endY = 0;
			int endZ = 0;

			String feature = "";

			{

				try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

					{

						if (read_all.equals("") == false) {

							if (read_all.startsWith("block = ") == true) {

								{

									try {

										get = read_all.replace("block = ", "").split(" \\| ");
										chance = Double.parseDouble(get[0]);
										block = GameUtils.textToBlock(get[3]);
										keep = get[4].equals("true");

									} catch (Exception e) {

										return;

									}

									// Cancellation Conditions
									{

										if (Math.random() >= chance) {

											continue;

										}

										if (block == Blocks.AIR.defaultBlockState()) {

											continue;

										}

									}

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

											startX = Math.min(0, Mth.nextInt(RandomSource.create(), minX, maxX));
											startY = Math.min(0, Mth.nextInt(RandomSource.create(), minY, maxY));
											startZ = Math.min(0, Mth.nextInt(RandomSource.create(), minZ, maxZ));
											endX = Math.max(0, Mth.nextInt(RandomSource.create(), minX, maxX));
											endY = Math.max(0, Mth.nextInt(RandomSource.create(), minY, maxY));
											endZ = Math.max(0, Mth.nextInt(RandomSource.create(), minZ, maxZ));

										} catch (Exception e) {

											return;

										}

									}

									for (int testX = startX; testX <= endX; testX++) {

										for (int testY = startY; testY <= endY; testY++) {

											for (int testZ = startZ; testZ <= endZ; testZ++) {

												pos = new BlockPos(posX + offset_posX + testX, posY + offset_posY + testY, posZ + offset_posZ + testZ);

												// Keep
												{

													if (keep == true) {

														if (GameUtils.isBlockTaggedAs(level.getBlockState(pos), "tanshugetrees:passable_blocks") == false || level.isWaterAt(pos) == true) {

															continue;

														}

													}

												}

												if (level.hasChunk(pos.getX() >> 4, pos.getZ() >> 4) == true && level.getChunk(pos).getStatus().isOrAfter(ChunkStatus.FULL) == false) {

													level.setBlock(new BlockPos(pos), block, 2);

												}

											}

										}

									}

								}

							} else if (read_all.startsWith("feature = ") == true) {

								{

									try {

										get = read_all.replace("feature = ", "").split(" \\| ");
										chance = Double.parseDouble(get[0]);
										offset_pos = get[1].split("/");
										offset_posX = Integer.parseInt(offset_pos[0]);
										offset_posY = Integer.parseInt(offset_pos[1]);
										offset_posZ = Integer.parseInt(offset_pos[2]);
										feature = get[2];

									} catch (Exception e) {

										return;

									}

									if (Math.random() < chance) {

										pos = new BlockPos(posX + offset_posX, posY + offset_posY, posZ + offset_posZ);
										world_gen.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE).getHolderOrThrow(FeatureUtils.createKey(feature)).value().place(world_gen, world_gen.getLevel().getChunkSource().getGenerator(), world_gen.getRandom(), pos);

									}

								}

							} else {

								GameUtils.runCommand(level, posX, posY, posZ, read_all);

							}

						}

					}

				} buffered_reader.close(); } catch (Exception e) { TanshugetreesMod.LOGGER.error(e.getMessage()); }

			}

		}

	}

}