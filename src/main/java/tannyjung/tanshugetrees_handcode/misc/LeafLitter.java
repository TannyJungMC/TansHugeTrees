package tannyjung.tanshugetrees_handcode.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.CatLieOnBedGoal;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.chunk.ChunkAccess;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.config.ConfigMain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class LeafLitter {

    public static void start (ChunkAccess chunk, int posX, int posY, int posZ, BlockState block) {

        File file = new File(Handcode.directory_config + "/custom_packs/.organized/leaf_litter/" + Misc.blockToTextID(block).replace(":", "-") + ".txt");

        if (ConfigMain.pre_leaves_litter_classic_only == false && (file.exists() == true && file.isDirectory() == false)) {

            // Get from custom pack for custom
            {

                String[] get = null;
                String[] get_from_to = null;
                String[] get_from = null;
                String[] get_to = null;
                boolean place = false;
                double chance = 0.0;
                BlockState block_current = Blocks.AIR.defaultBlockState();

                {

                    try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                        {

                            if (read_all.equals("") == false) {

                                get = read_all.split(" \\| ");
                                chance = Double.parseDouble(get[0]);

                                if (Math.random() < chance) {

                                    place = false;
                                    get_from_to = get[1].split(" = ");
                                    get_to = get_from_to[1].split("/");

                                    if (get_from_to[0].contains("/") == true) {

                                        get_from = get_from_to[0].split("/");
                                        block_current = chunk.getBlockState(new BlockPos(posX, posY + Integer.parseInt(get_from[0]), posZ));

                                    } else {

                                        get_from = new String[1];
                                        get_from[0] = get_from_to[0];

                                    }

                                    // Testing
                                    {

                                        if (get_from[0].equals("ground") == true) {

                                            {

                                                if (Misc.isBlockTaggedAs(block_current, "tanshugetrees:air_blocks") == true) {

                                                    BlockState ground_block = chunk.getBlockState(new BlockPos(posX, posY - 1, posZ));

                                                    if (Misc.isBlockTaggedAs(ground_block, "tanshugetrees:passable_blocks") == false) {

                                                        place = true;

                                                    }

                                                }

                                            }

                                        } else if (get_from[0].equals("water") == true) {

                                            {

                                                if (Misc.isBlockTaggedAs(block_current, "tanshugetrees:air_blocks") == true) {

                                                    BlockState ground_block = chunk.getBlockState(new BlockPos(posX, posY - 1, posZ));

                                                    if (Misc.isBlockTaggedAs(ground_block, "tanshugetrees:water_blocks") == true) {

                                                        place = true;

                                                    }

                                                }

                                            }

                                        } else if (block_current == Misc.textToBlock(get_from[1])) {

                                            place = true;

                                        }

                                    }

                                    if (place == true) {

                                        chunk.setBlockState(new BlockPos(posX, posY + Integer.parseInt(get_to[0]), posZ), Misc.textToBlock(get_to[1]), false);
                                        break;

                                    }

                                }

                            }

                        }

                    } buffered_reader.close(); } catch (Exception e) { e.printStackTrace(); }

                }

            }

        } else {

            if (ConfigMain.pre_leaves_litter_classic == true) {

                // Classic Style
                {

                    if (Misc.isBlockTaggedAs(chunk.getBlockState(new BlockPos(posX, posY, posZ)), "tanshugetrees:air_blocks") == true) {

                        // If Found Water
                        if (Misc.isBlockTaggedAs(chunk.getBlockState(new BlockPos(posX, posY - 1, posZ)), "tanshugetrees:water_blocks") == true) {

                            block = (block.getBlock().getStateDefinition().getProperty("waterlogged") instanceof BooleanProperty property ? block.setValue(property, true) : block);
                            posY = posY - 1;

                        }

                        chunk.setBlockState(new BlockPos(posX, posY, posZ), block, false);

                    }

                }

            }

        }

    }

}