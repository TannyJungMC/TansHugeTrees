package tannyjung.tanshugetrees_handcode.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkStatus;
import tannyjung.tanshugetrees_handcode.Handcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;

public class TXTFunction {

	public static void run (LevelAccessor level, ServerLevel world, String path, int posX, int posY, int posZ) {

		File file = new File(Handcode.directory_config + "/custom_packs/.organized/" + path);

		if (file.exists() == true && file.isDirectory() == false) {

			String[] array = new String[0];

			double chance = 0.0;
			String[] array_pos = new String[0];

			String[] array_pos_from = new String[0];
			int offset_from_posX = 0;
			int offset_from_posY = 0;
			int offset_from_posZ = 0;

			String[] array_pos_to = new String[0];
			int offset_to_posX = 0;
			int offset_to_posY = 0;
			int offset_to_posZ = 0;

			BlockState block = Blocks.AIR.defaultBlockState();
			boolean keep = false;
			BlockPos pos = null;

			try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

				{

					if (read_all.startsWith("place_block = ") == true) {

						// Get Values
						{

							array = read_all.replace("place_block = ", "").split(" \\| ");

							chance = Double.parseDouble(array[0]);
							array_pos = array[1].split(" - ");

							array_pos_from = array_pos[0].split("/");
							offset_from_posX = Integer.parseInt(array_pos_from[0]);
							offset_from_posY = Integer.parseInt(array_pos_from[1]);
							offset_from_posZ = Integer.parseInt(array_pos_from[2]);

							array_pos_to = array_pos[1].split("/");
							offset_to_posX = Integer.parseInt(array_pos_to[0]);
							offset_to_posY = Integer.parseInt(array_pos_to[1]);
							offset_to_posZ = Integer.parseInt(array_pos_to[2]);

							block = Misc.textToBlock(array[2]);
							keep = array[3].equals("true");

						}

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

									pos = new BlockPos(posX + offset_posX, posY + offset_posY, posZ + offset_posZ);

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

				}

			} buffered_reader.close(); } catch (Exception e) { e.printStackTrace(); }

		}

	}

}