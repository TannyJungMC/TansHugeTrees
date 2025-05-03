package tannyjung.tanshugetrees_handcode.systems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.misc.GameUtils;
;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class TreeFunction {

	public static void run (WorldGenLevel world_gen, String path, int posX, int posY, int posZ) {

		File file = new File(Handcode.directory_config + "/custom_packs/.organized/functions/" + path);

		if (file.exists() == true && file.isDirectory() == false) {

			try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

				{

					if (read_all.equals("") == false) {

						if (read_all.startsWith("block = ") == true) {

							{

								String[] get = read_all.replace("block = ", "").split(" \\| ");

								double chance = Double.parseDouble(get[0]);
								BlockState block = GameUtils.textToBlock(get[3]);
								boolean keep = get[4].equals("true");

								// Cancellation Conditions
								{

									if (Math.random() >= chance) {

										continue;

									}

									if (block == Blocks.AIR.defaultBlockState()) {

										continue;

									}

								}

								String[] offset_pos = get[1].split("/");
								int offset_posX = Integer.parseInt(offset_pos[0]);
								int offset_posY = Integer.parseInt(offset_pos[1]);
								int offset_posZ = Integer.parseInt(offset_pos[2]);

								String[] min_max = get[2].split("/");
								int minX = Integer.parseInt(min_max[0]);
								int minY = Integer.parseInt(min_max[1]);
								int minZ = Integer.parseInt(min_max[2]);
								int maxX = Integer.parseInt(min_max[3]);
								int maxY = Integer.parseInt(min_max[4]);
								int maxZ = Integer.parseInt(min_max[5]);

								int startX = Math.min(0, Mth.nextInt(RandomSource.create(), minX, maxX));
								int startY = Math.min(0, Mth.nextInt(RandomSource.create(), minY, maxY));
								int startZ = Math.min(0, Mth.nextInt(RandomSource.create(), minZ, maxZ));
								int endX = Math.max(0, Mth.nextInt(RandomSource.create(), minX, maxX));
								int endY = Math.max(0, Mth.nextInt(RandomSource.create(), minY, maxY));
								int endZ = Math.max(0, Mth.nextInt(RandomSource.create(), minZ, maxZ));

								for (int testX = startX; testX <= endX; testX++) {

									for (int testY = startY; testY <= endY; testY++) {

										for (int testZ = startZ; testZ <= endZ; testZ++) {

											BlockPos pos = new BlockPos(posX + offset_posX + testX, posY + offset_posY + testY, posZ + offset_posZ + testZ);

											// Keep
											{

												if (keep == true) {

													if (GameUtils.isBlockTaggedAs(world_gen.getBlockState(pos), "tanshugetrees:passable_blocks") == false || world_gen.isWaterAt(pos) == true) {

														continue;

													}

												}

											}

											world_gen.setBlock(new BlockPos(pos), block, 2);

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

									world_gen.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE).getHolderOrThrow(FeatureUtils.createKey(feature)).value().place((WorldGenLevel) world_gen, world_gen.getLevel().getChunkSource().getGenerator(), world_gen.getRandom(), BlockPos.containing(posX + offset_posX, posY + offset_posY, posZ + offset_posZ));

								}

							}

						} else {

							GameUtils.runCommand(world_gen.getLevel(), posX, posY, posZ, read_all);

						}

					}

				}

			} buffered_reader.close(); } catch (Exception e) { TanshugetreesMod.LOGGER.error(e.getMessage()); }

		}

	}

}