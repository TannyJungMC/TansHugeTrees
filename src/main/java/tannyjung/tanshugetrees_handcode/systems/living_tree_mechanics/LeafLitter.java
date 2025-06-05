package tannyjung.tanshugetrees_handcode.systems.living_tree_mechanics;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.misc.GameUtils;
import tannyjung.tanshugetrees_handcode.config.ConfigMain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class LeafLitter {

    public static void start (LevelAccessor level, int posX, int posY, int posZ, BlockState block, boolean remove) {

        File file = new File(Handcode.directory_config + "/custom_packs/.organized/leaf_litter/" + GameUtils.block.toTextID(block).replace(":", "-") + ".txt");

        if (ConfigMain.leaf_litter_classic_only == false && (file.exists() == true && file.isDirectory() == false)) {

            // Get from custom pack for custom
            {

                boolean pass = false;
                double chance = 0.0;

                String[] get = null;
                String[] get_from_to = null;
                String[] get_from = null;
                String[] get_to = null;

                String block_from_text = "";
                BlockState block_from = Blocks.AIR.defaultBlockState();
                BlockState block_to = Blocks.AIR.defaultBlockState();
                BlockPos pos_from = null;
                BlockPos pos_to = null;

                {

                    try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                        {

                            if (read_all.equals("") == false) {

                                get = read_all.split(" \\| ");
                                chance = Double.parseDouble(get[0]);

                                if (Math.random() < chance || remove == true) {

                                    // Get Pos From-To
                                    {

                                        get_from_to = get[1].split(" = ");

                                        // From
                                        {

                                            if (get_from_to[0].equals("ground") == true || get_from_to[0].equals("water") == true) {

                                                pos_from = new BlockPos(posX, posY - 1, posZ);
                                                block_from_text = get_from_to[0];

                                            } else {

                                                get_from = get_from_to[0].split("/");
                                                pos_from = new BlockPos(posX, posY + Integer.parseInt(get_from[0]), posZ);
                                                block_from_text = get_from[1];

                                            }

                                            block_from = level.getBlockState(pos_from);

                                        }

                                        // To
                                        {

                                            get_to = get_from_to[1].split("/");
                                            pos_to = new BlockPos(posX, posY + Integer.parseInt(get_to[0]), posZ);
                                            block_to = GameUtils.block.fromText(get_to[1]);

                                        }

                                    }

                                    if (remove == false) {

                                        // Place
                                        {

                                            // Test Ground Block
                                            {

                                                if (block_from_text.equals("ground") == true) {

                                                    {

                                                        if (GameUtils.block.isTaggedAs(level.getBlockState(new BlockPos(posX, posY, posZ)), "tanshugetrees:air_blocks") == true) {

                                                            if (GameUtils.block.isTaggedAs(block_from, "tanshugetrees:passable_blocks") == false) {

                                                                pass = true;

                                                            }

                                                        }

                                                    }

                                                } else if (block_from_text.equals("water") == true) {

                                                    {

                                                        if (GameUtils.block.isTaggedAs(level.getBlockState(new BlockPos(posX, posY, posZ)), "tanshugetrees:air_blocks") == true) {

                                                            if (level.isWaterAt(pos_from) == true) {

                                                                pass = true;

                                                            }

                                                        }

                                                    }

                                                } else {

                                                    if (block_from_text.startsWith("#") == true) {

                                                        if (GameUtils.block.isTaggedAs(block_from, block_from_text.substring(1)) == true) {

                                                            pass = true;

                                                        }

                                                    } else {

                                                        if (GameUtils.block.fromText(block_from_text) == block_from) {

                                                            pass = true;

                                                        }

                                                    }

                                                }

                                            }

                                            if (pass == true) {

                                                level.setBlock(pos_to, block_to, 2);
                                                break;

                                            }

                                        }

                                    } else {

                                        // Remove
                                        {

                                            if (level.getBlockState(pos_to).equals(block_to) == true) {

                                                level.setBlock(pos_to, Blocks.AIR.defaultBlockState(), 2);
                                                break;

                                            }

                                        }

                                    }

                                }

                            }

                        }

                    } buffered_reader.close(); } catch (Exception e) { TanshugetreesMod.LOGGER.error(e.getMessage()); }

                }

            }

        } else {

            if (ConfigMain.leaf_litter_classic == true) {

                // Classic Style
                {

                    BlockPos pos = new BlockPos(posX, posY, posZ);

                    if (remove == false) {

                        // Place
                        {

                            if (GameUtils.block.isTaggedAs(level.getBlockState(pos), "tanshugetrees:air_blocks") == true) {

                                // If Found Water
                                if (level.isWaterAt(new BlockPos(posX, posY - 1, posZ)) == true) {

                                    block = GameUtils.block.propertyBooleanSet(block, "waterlogged", true);
                                    posY = posY - 1;

                                }

                                level.setBlock(new BlockPos(posX, posY, posZ), block, 2);

                            }

                        }

                    } else {

                        // Remove
                        {

                            if (level.getBlockState(pos).getBlock().equals(block.getBlock()) == true) {

                                if (GameUtils.block.propertyBooleanGet(level.getBlockState(pos), "waterlogged") == true) {

                                    level.setBlock(pos, Blocks.WATER.defaultBlockState(), 2);

                                } else {

                                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);

                                }

                            }

                        }

                    }

                }

            }

        }

    }

}