package tannyjung.tanshugetrees_handcode.systems.living_tree_mechanics;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import tannyjung.tanshugetrees_core.game.GameUtils;
import tannyjung.tanshugetrees_core.game.TXTFunction;
import tannyjung.tanshugetrees_handcode.data.FileConfig;
import tannyjung.tanshugetrees_handcode.systems.Cache;

public class LeafLitter {

    public static void create (LevelAccessor level_accessor, ServerLevel level_server, int posX, int posY, int posZ, BlockState block, boolean remove) {

        String function_id = "leaf_litter/" + GameUtils.block.toTextID(block).replace(":", "-");

        if (FileConfig.leaf_litter_classic_only == false && Cache.getFunction(function_id).length > 0) {

            TXTFunction.run(level_accessor, level_server, posX, posY, posZ, function_id, true);

        } else {

            if (FileConfig.leaf_litter_classic == true) {

                // Classic Style
                {

                    BlockPos pos = new BlockPos(posX, posY, posZ);

                    if (remove == false) {

                        // Place
                        {

                            if (GameUtils.block.isTaggedAs(level_accessor.getBlockState(pos), "tanshugetrees:passable_blocks") == true) {

                                BlockPos pos_ground = new BlockPos(posX, posY - 1, posZ);

                                if (level_accessor.isWaterAt(pos_ground) == true) {

                                    block = GameUtils.block.propertyBooleanSet(block, "waterlogged", true);
                                    posY = posY - 1;

                                } else if (GameUtils.block.isTaggedAs(level_accessor.getBlockState(pos_ground), "tanshugetrees:passable_blocks") == true) {

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

                                if (GameUtils.block.propertyBooleanGet(level_accessor.getBlockState(pos), "waterlogged") == true) {

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