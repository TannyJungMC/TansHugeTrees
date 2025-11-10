package tannyjung.tanshugetrees_handcode.systems.living_tree_mechanics;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import tannyjung.core.Utils;
import tannyjung.tanshugetrees_handcode.config.ConfigMain;
import tannyjung.tanshugetrees_handcode.systems.Cache;

public class LeafLitter {

    public static void start (LevelAccessor level_accessor, int posX, int posY, int posZ, BlockState block, boolean remove) {

        String[] leaf_litter = Cache.leaf_litter(Utils.block.toTextID(block).replace(":", "-"));

        if (ConfigMain.leaf_litter_classic_only == false && leaf_litter.length > 0) {

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

                for (String read_all : leaf_litter) {

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

                                        block_from = level_accessor.getBlockState(pos_from);

                                    }

                                    // To
                                    {

                                        get_to = get_from_to[1].split("/");
                                        pos_to = new BlockPos(posX, posY + Integer.parseInt(get_to[0]), posZ);
                                        block_to = Utils.block.fromText(get_to[1]);

                                    }

                                }

                                if (remove == false) {

                                    // Place
                                    {

                                        // Test Ground Block
                                        {

                                            if (block_from_text.equals("ground") == true) {

                                                {

                                                    if (Utils.block.isTaggedAs(level_accessor.getBlockState(new BlockPos(posX, posY, posZ)), "tanshugetrees:passable_blocks") == true) {

                                                        if (Utils.block.isTaggedAs(block_from, "tanshugetrees:passable_blocks") == false) {

                                                            pass = true;

                                                        }

                                                    }

                                                }

                                            } else if (block_from_text.equals("water") == true) {

                                                {

                                                    if (Utils.block.isTaggedAs(level_accessor.getBlockState(new BlockPos(posX, posY, posZ)), "tanshugetrees:passable_blocks") == true) {

                                                        if (level_accessor.isWaterAt(pos_from) == true) {

                                                            pass = true;

                                                        }

                                                    }

                                                }

                                            } else {

                                                if (block_from_text.startsWith("#") == true) {

                                                    if (Utils.block.isTaggedAs(block_from, block_from_text.substring(1)) == true) {

                                                        pass = true;

                                                    }

                                                } else {

                                                    if (Utils.block.fromText(block_from_text) == block_from) {

                                                        pass = true;

                                                    }

                                                }

                                            }

                                        }

                                        if (pass == true) {

                                            level_accessor.setBlock(pos_to, block_to, 2);
                                            break;

                                        }

                                    }

                                } else {

                                    // Remove
                                    {

                                        if (level_accessor.getBlockState(pos_to).equals(block_to) == true) {

                                            level_accessor.setBlock(pos_to, Blocks.AIR.defaultBlockState(), 2);
                                            break;

                                        }

                                    }

                                }

                            }

                        }

                    }

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

                            if (Utils.block.isTaggedAs(level_accessor.getBlockState(pos), "tanshugetrees:passable_blocks") == true) {

                                BlockPos pos_ground = new BlockPos(posX, posY - 1, posZ);

                                if (level_accessor.isWaterAt(pos_ground) == true) {

                                    block = Utils.block.propertyBooleanSet(block, "waterlogged", true);
                                    posY = posY - 1;

                                } else if (Utils.block.isTaggedAs(level_accessor.getBlockState(pos_ground), "tanshugetrees:passable_blocks") == true) {

                                    return;

                                }

                                level_accessor.setBlock(new BlockPos(posX, posY, posZ), block, 2);

                            }

                        }

                    } else {

                        // Remove
                        {

                            if (level_accessor.getBlockState(pos).getBlock().equals(block.getBlock()) == true) {

                                BlockState block_to = Blocks.AIR.defaultBlockState();

                                if (Utils.block.propertyBooleanGet(level_accessor.getBlockState(pos), "waterlogged") == true) {

                                    block_to = Blocks.WATER.defaultBlockState();

                                } else {

                                    block_to = Blocks.AIR.defaultBlockState();

                                }

                                level_accessor.setBlock(pos, block_to, 2);

                            }

                        }

                    }

                }

            }

        }

    }

}